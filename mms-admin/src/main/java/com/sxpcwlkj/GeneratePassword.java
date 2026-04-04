package com.sxpcwlkj;

import com.baomidou.dynamic.datasource.toolkit.CryptoUtils;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.Scanner;

/**
 * 交互式生成/更新 mms-admin/config 下 application-*-secret.yml（数据源 ENC、公钥），
 * 并将 RSA 密钥对写入同目录 {@link #KEYS_FILE_NAME}（勿提交仓库）。
 * <p>
 * 运行：在 {@code mms} 目录 {@code mvn -pl mms-admin exec:java -Dexec.mainClass=com.sxpcwlkj.GeneratePassword}
 * 或在 IDE 中直接运行本类 main（工作目录选 {@code mms} 或 {@code mms/mms-admin}）。
 */
public final class GeneratePassword {

    private static final String KEYS_FILE_NAME = "dynamic-datasource-rsa.keys";
    private static final int RSA_BITS = 512;

    private static final String JDBC_SUFFIX =
            "?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true"
                    + "&serverTimezone=GMT%2B8&autoReconnect=true&rewriteBatchedStatements=true"
                    + "&allowPublicKeyRetrieval=true";

    private GeneratePassword() {}

    public static void main(String[] args) throws Exception {
        try (Scanner sc = new Scanner(System.in, StandardCharsets.UTF_8)) {
            Path configDir = resolveConfigDir();
            System.out.println("配置文件会写到目录：\n  " + configDir.toAbsolutePath());
            Files.createDirectories(configDir);
            while (true) {
                System.out.println();
                System.out.println("========== 数据源密码工具（GeneratePassword）==========");
                System.out.println("  1  写开发环境  → application-dev-secret.yml");
                System.out.println("  2  写生产环境  → application-prod-secret.yml");
                System.out.println("  3  把一段 ENC 密文还原成明文（需要公钥）");
                System.out.println("  4  退出");
                System.out.print("请输入数字 1～4：");
                String c = sc.nextLine().trim();
                switch (c) {
                    case "1" -> runProfile(sc, configDir, Profile.DEV);
                    case "2" -> runProfile(sc, configDir, Profile.PROD);
                    case "3" -> runDecrypt(sc, configDir);
                    case "4" -> {
                        System.out.println("已退出。");
                        return;
                    }
                    default -> System.out.println("输入不对，请只输入 1、2、3 或 4。");
                }
            }
        }
    }

    private enum Profile {
        DEV("application-dev-secret.yml", "application-dev-secret.example.yml"),
        PROD("application-prod-secret.yml", "application-prod-secret.example.yml");

        private final String secretFile;
        private final String exampleFile;

        Profile(String secretFile, String exampleFile) {
            this.secretFile = secretFile;
            this.exampleFile = exampleFile;
        }
    }

    private static Path resolveConfigDir() {
        Path a = Paths.get("mms-admin", "config");
        if (Files.isDirectory(a)) {
            return a.toAbsolutePath().normalize();
        }
        Path b = Paths.get("mms", "mms-admin", "config");
        if (Files.isDirectory(b)) {
            return b.toAbsolutePath().normalize();
        }
        return Paths.get(System.getProperty("user.dir", "."), "mms-admin", "config")
                .toAbsolutePath()
                .normalize();
    }

    private static void runProfile(Scanner sc, Path configDir, Profile profile) throws Exception {
        Path secretPath = configDir.resolve(profile.secretFile);
        Path examplePath = configDir.resolve(profile.exampleFile);
        if (!Files.exists(secretPath)) {
            if (Files.exists(examplePath)) {
                Files.copy(examplePath, secretPath);
                System.out.println("已按示例新建文件：" + secretPath.getFileName());
            } else {
                System.out.println("找不到示例文件：" + examplePath + "\n请先保证仓库里有 application-*-secret.example.yml。");
                return;
            }
        }

        String envLabel = profile == Profile.DEV ? "【开发】" : "【生产】";
        System.out.println();
        System.out.println("—— " + envLabel + " 数据库连接 ——（直接回车 = 用括号里的默认值）");

        String defHost = profile == Profile.DEV ? "localhost" : "127.0.0.1";
        String defPort = "3306";
        String defDb = "mms";
        String defUser = "root";
        String defPass = profile == Profile.DEV ? "123456" : "";

        String host = readLine(sc, "数据库地址（IP 或域名）", defHost);
        String port = readLine(sc, "端口", defPort);
        String database = readLine(sc, "库名", defDb);
        String username = readLine(sc, "账号", defUser);
        String passwordPlain = readLine(sc, "密码", defPass);
        String bootAdminPwd =
                readLine(
                        sc,
                        "监控后台密码（Spring Boot Admin 客户端密码）",
                        profile == Profile.DEV ? "123456" : "请替换");

        System.out.println();
        System.out.println("—— RSA 密钥 ——");
        System.out.println("  1  重新生成一对新密钥（会覆盖本地 " + KEYS_FILE_NAME + "）");
        System.out.println("  2  沿用已有密钥文件（与上次生成配套，不换密文密钥）");
        System.out.print("请输入 1 或 2：");
        String km = sc.nextLine().trim();
        String privateKey;
        String publicKey;
        Path keysPath = configDir.resolve(KEYS_FILE_NAME);
        if ("2".equals(km)) {
            if (!Files.exists(keysPath)) {
                System.out.println("没有密钥文件，改为生成新密钥。");
                km = "1";
            }
        }
        if ("2".equals(km)) {
            Properties p = loadKeys(keysPath);
            privateKey = Objects.requireNonNull(p.getProperty("privateKey"), "privateKey").trim();
            publicKey = Objects.requireNonNull(p.getProperty("publicKey"), "publicKey").trim();
            System.out.println("已读取本地密钥。");
        } else {
            String[] pair = CryptoUtils.genKeyPair(RSA_BITS);
            privateKey = pair[0];
            publicKey = pair[1];
            saveKeys(keysPath, privateKey, publicKey);
            System.out.println("新密钥已保存到：\n  " + keysPath.toAbsolutePath());
        }

        String jdbcUrl = "jdbc:mysql://" + host + ":" + port + "/" + database + JDBC_SUFFIX;
        String urlEnc = enc(privateKey, jdbcUrl);
        String userEnc = enc(privateKey, username);
        String passEnc = enc(privateKey, passwordPlain);

        Map<String, Object> root = loadYaml(secretPath);
        Map<String, Object> spring = map(root, "spring");
        Map<String, Object> boot = map(spring, "boot");
        Map<String, Object> adminClient = map(map(boot, "admin"), "client");
        adminClient.put("password", bootAdminPwd);

        Map<String, Object> ds = map(spring, "datasource");
        Map<String, Object> dynamic = map(ds, "dynamic");
        dynamic.put("public-key", publicKey);
        Map<String, Object> dsDs = map(dynamic, "datasource");
        Map<String, Object> master = map(dsDs, "master");
        master.put("type", "com.zaxxer.hikari.HikariDataSource");
        master.put("driverClassName", "com.mysql.cj.jdbc.Driver");
        master.put("url", urlEnc);
        master.put("username", userEnc);
        master.put("password", passEnc);

        writeYaml(secretPath, root);
        System.out.println("完成。已更新文件：\n  " + secretPath.toAbsolutePath());
    }

    private static void runDecrypt(Scanner sc, Path configDir) throws Exception {
        Path keysPath = configDir.resolve(KEYS_FILE_NAME);
        String defPub = "";
        if (Files.exists(keysPath)) {
            defPub = loadKeys(keysPath).getProperty("publicKey", "").trim();
        }
        System.out.println();
        System.out.println("—— 解密 ENC ——（公钥要和 yml 里 public-key 那一段一致）");
        String pub = readLine(sc, "公钥（一长串 Base64）", defPub);
        if (pub.isEmpty()) {
            System.out.println("公钥不能为空。");
            return;
        }
        System.out.print("密文（可粘贴 ENC(……) 或中间那一段 Base64）：");
        String cipher = sc.nextLine().trim();
        cipher = stripEnc(cipher);
        try {
            String plain = CryptoUtils.decrypt(pub, cipher);
            System.out.println("解密结果：" + plain);
        } catch (Exception e) {
            System.out.println("解密失败：请检查公钥是否与生成密文时一致，密文是否完整。");
        }
    }

    private static String stripEnc(String s) {
        String t = s.trim();
        if (t.startsWith("ENC(") && t.endsWith(")")) {
            return t.substring(4, t.length() - 1).trim();
        }
        return t;
    }

    private static String enc(String privateKey, String plain) throws Exception {
        return "ENC(" + CryptoUtils.encrypt(privateKey, plain) + ")";
    }

    private static String readLine(Scanner sc, String label, String defaultValue) {
        System.out.print(label + "（默认 " + defaultValue + "）: ");
        String s = sc.nextLine();
        if (s == null || s.isBlank()) {
            return defaultValue;
        }
        return s.trim();
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Object> map(Map<String, Object> parent, String key) {
        Object o = parent.get(key);
        if (o instanceof Map) {
            return (Map<String, Object>) o;
        }
        Map<String, Object> m = new LinkedHashMap<>();
        parent.put(key, m);
        return m;
    }

    private static Map<String, Object> loadYaml(Path path) throws IOException {
        String text = Files.readString(path, StandardCharsets.UTF_8);
        Yaml yaml = new Yaml();
        Object loaded = yaml.load(text);
        if (loaded instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> m = (Map<String, Object>) loaded;
            return m;
        }
        return new LinkedHashMap<>();
    }

    private static void writeYaml(Path path, Map<String, Object> root) throws IOException {
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setPrettyFlow(true);
        options.setIndent(2);
        options.setIndicatorIndent(0);
        Yaml yaml = new Yaml(options);
        String body = yaml.dump(root);
        String profileHint = path.getFileName().toString().contains("prod") ? "prod" : "dev";
        String header =
                """
                # 本文件由 GeneratePassword 生成，含真实密码，勿提交 Git（见 .gitignore）
                # 对应主配置里的 spring.config.import，profile=%s

                """
                        .formatted(profileHint);
        Files.writeString(path, header + body, StandardCharsets.UTF_8);
    }

    private static void saveKeys(Path path, String privateKey, String publicKey) throws IOException {
        Properties p = new Properties();
        p.setProperty("privateKey", privateKey);
        p.setProperty("publicKey", publicKey);
        try (OutputStream os = Files.newOutputStream(path)) {
            p.store(os, "RSA keys by GeneratePassword; do not commit");
        }
    }

    private static Properties loadKeys(Path path) throws IOException {
        Properties p = new Properties();
        try (InputStream in = Files.newInputStream(path)) {
            p.load(in);
        }
        return p;
    }
}
