package com.sxpcwlkj.framework.utils;


import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.crypto.digest.MD5;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sxpcwlkj.common.annotation.IgnoreSign;
import com.sxpcwlkj.common.exception.MmsException;
import com.sxpcwlkj.common.utils.ByteStringUtil;
import com.sxpcwlkj.common.utils.DataUtil;
import com.sxpcwlkj.common.utils.JsonUtil;
import com.sxpcwlkj.common.utils.StringUtil;
import com.sxpcwlkj.framework.advice.ApiSecurityParam;
import com.sxpcwlkj.framework.entity.AesKeyEntity;
import com.sxpcwlkj.framework.entity.BaseEntityVo;
import com.sxpcwlkj.framework.entity.RsaKeyEntity;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.codec.binary.Base64;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ReflectionUtils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;

/**
 * 对参数进行签名
 * @author mmsAdmin
 * @Doc mmsadmin.cn
 */
public class SignUtil {
    private static final Logger log = LoggerFactory.getLogger(SignUtil.class);

    //=======================  测试 ========================
    public static void main(String[] args) throws Exception {
        log.info("=====================MD5==================");
        //MD5
        log.info("MD5:{}",pressWord("123456", "mms"));

        //秘钥对象
        AesKeyEntity aesKeyEntity = getAesKey();
        assert aesKeyEntity != null;
        //加密对象，支出注解@IgnoreSign，可以忽略加签属性
        BaseEntityVo baseEntityVo = new BaseEntityVo();
        baseEntityVo.setSort(1);
        baseEntityVo.setCreatedTime(new Date());
        baseEntityVo.setRemark("加密测试其中的‘sort’添加 @IgnoreSign注解，将被过滤不进行加签！");

        log.info("=====================对称加密==================");
        ApiSecurityParam param1 = new ApiSecurityParam()
            .setAppid(aesKeyEntity.getAppId())
            .addData(getAllParams(baseEntityVo),BaseEntityVo.class);
        // 加密
        log.info("加密前:{}",param1.getData());
        ApiSecurityParam  apiSecurityParam1 = encryptAesCryptoJs(param1,aesKeyEntity);
        log.info("加密后:{}",apiSecurityParam1.getSign());
        // 验签
        log.info("验签:{}",verifyAesCryptoJs(apiSecurityParam1,aesKeyEntity)+"");
        // 解密
        log.info("解密:{}",decryptAesCryptoJs(apiSecurityParam1,aesKeyEntity));

        log.info("=====================非对称加密==================");

        RsaKeyEntity rsaKeyEntity = getRsaKey();
        ApiSecurityParam param2 = new ApiSecurityParam()
            .setAppid(aesKeyEntity.getAppId())
            .addData(getAllParams(baseEntityVo),BaseEntityVo.class);
        log.info("加密前:{}",param2.getData());
        // 加密
        ApiSecurityParam  apiSecurityParam2 = encryptRsa(param2,rsaKeyEntity);
        assert apiSecurityParam2 != null;
        log.info("加密后:{}",apiSecurityParam2.getSign());
        // 验签
        log.info("验签:{}",verifyRsa(apiSecurityParam2,rsaKeyEntity)+"");
        // 解密
        log.info("解密:{}",decryptRsa(apiSecurityParam2,rsaKeyEntity));
    }


    //====================== 常量 ==========================

    // 加密类型
    public static final   String  ENCRYPTION_TYPE_AES = "AES";
    public static final   String  ENCRYPTION_TYPE_RSA = "RSA";

    /**
     * MD5加密
     * @param content 内容
     * @param salt 盐
     * @return 16进制密文字符串
     */
    public static String pressWord(String content,String salt) {
        int index = 0;
        int count = 2;
        MD5 md5 = new MD5(salt.getBytes(StandardCharsets.UTF_8), index, count);
        // 返回16进制格式
        return md5.digestHex(content);
    }

    //============================ 对称加密 =============================

    /**
     * 获取秘钥
     *
     * @return 秘钥
     */
    public static AesKeyEntity getAesKey() {
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            kgen.init(128, new SecureRandom());
            SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
            return new AesKeyEntity(RandomUtil.randomString(16), ByteStringUtil.byte2str(key.getEncoded()));
        } catch (NoSuchAlgorithmException e) {
            log.error("获取秘钥失败{}", e.getMessage());
        }
        return null;
    }

    /**
     * AES加密
     * php,java,.net三种语言同步实现加密、解密
     * 其他语言文档: <a href="https://blog.csdn.net/cctcc/article/details/54926947">...</a>
     * 通过String类型的密钥加密String
     * @return 16进制密文字符串
     */
    public static String encryptAes(String content,String key) {
        try {
            //构建
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            // 字符串装Byte
            byte[] byteKey = new SecretKeySpec(ByteStringUtil.hex2byte(key), "AES").getEncoded();
            //初始化
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(byteKey, "AES"));
            // 字符串转byte加密，返回byte字节
            byte[] data = cipher.doFinal(content.getBytes(StandardCharsets.UTF_8));
            return ByteStringUtil.byte2str(data);
        } catch (Exception e) {
            log.error("加密失败{}", e.getMessage());
            throw new RuntimeException("加密算法异常 ，异常信息：" + e.getMessage());
        }
    }

    /**
     * AES解密
     * php,java,.net三种语言同步实现加密、解密
     * 其他语言文档: <a href="https://blog.csdn.net/cctcc/article/details/54926947">...</a>
     * 通过String类型的密钥 解密String类型的密文
     * @return 明文字符串
     */
    public static String decryptAes(String content,String key) {
        byte[] data = null;
        try {
            //构建
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            //初始化
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(ByteStringUtil.hex2byte(key), "AES"));
            //解密
            data = cipher.doFinal(ByteStringUtil.hex2byte(content));
        } catch (Exception e) {
            log.error("解密失败{}", e.getMessage());
            throw new RuntimeException("解密算法异常 ，异常信息：" + e.getMessage());
        }
        if (data == null) {
            return null;
        }
        return new String(data, StandardCharsets.UTF_8);
    }

    /**
     * 加密方法
     * 支持：crypto-js通用加密方法
     * @param param 报文对象
     * @param aesKeyEntity     秘钥对象
     * @return 加密后的内容
     */
    public static  String encryptAesCryptoJs(String param, AesKeyEntity aesKeyEntity) {
        try {
            // "算法/模式/补码方式"NoPadding PkcsPadding
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            int blockSize = cipher.getBlockSize();
            byte[] dataBytes = param.getBytes(StandardCharsets.UTF_8);
            int plaintextLength = dataBytes.length;
            if (plaintextLength % blockSize != 0) {
                plaintextLength = plaintextLength + (blockSize - (plaintextLength % blockSize));
            }
            byte[] plaintext = new byte[plaintextLength];
            System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);
            SecretKeySpec keySpec = new SecretKeySpec(aesKeyEntity.getSecretKey().getBytes(StandardCharsets.UTF_8), "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(aesKeyEntity.getAppId().getBytes(StandardCharsets.UTF_8));
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
            byte[] encrypted = cipher.doFinal(plaintext);
            return new Base64().encodeToString(encrypted);
        } catch (Exception e) {
            throw new RuntimeException("加密算法异常 CryptoUtil encrypt()加密方法，异常信息：" + e.getMessage());
        }
    }
    /**
     * 加密方法
     * 支持：crypto-js通用加密方法
     * @param apiSecurityParam 报文对象
     * @param aesKeyEntity     秘钥对象
     * @return 加密后的内容
     */
    public static  ApiSecurityParam encryptAesCryptoJs(ApiSecurityParam apiSecurityParam, AesKeyEntity aesKeyEntity) {
        try {
            // "算法/模式/补码方式"NoPadding PkcsPadding
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            int blockSize = cipher.getBlockSize();
            byte[] dataBytes = apiSecurityParam.getData().toString().getBytes(StandardCharsets.UTF_8);
            int plaintextLength = dataBytes.length;
            if (plaintextLength % blockSize != 0) {
                plaintextLength = plaintextLength + (blockSize - (plaintextLength % blockSize));
            }
            byte[] plaintext = new byte[plaintextLength];
            System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);
            SecretKeySpec keySpec = new SecretKeySpec(aesKeyEntity.getSecretKey().getBytes(StandardCharsets.UTF_8), "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(aesKeyEntity.getAppId().getBytes(StandardCharsets.UTF_8));
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
            byte[] encrypted = cipher.doFinal(plaintext);
            apiSecurityParam.setSign(new Base64().encodeToString(encrypted));
            return apiSecurityParam;
        } catch (Exception e) {
            throw new RuntimeException("加密算法异常 CryptoUtil encrypt()加密方法，异常信息：" + e.getMessage());
        }
    }
    /**
     * 解密方法
     * 支持：crypto-js通用解密方法
     * @param param 报文对象
     * @param aesKeyEntity     秘钥对象
     * @return 解密后的内容
     */
    public static  String decryptAesCryptoJs(String param, AesKeyEntity aesKeyEntity) {
        try {
            byte[] encrypted1 = new Base64().decode(param);
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            SecretKeySpec keySpec = new SecretKeySpec(aesKeyEntity.getSecretKey().getBytes(StandardCharsets.UTF_8), "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(aesKeyEntity.getAppId().getBytes(StandardCharsets.UTF_8));
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
            byte[] original = cipher.doFinal(encrypted1);
            return URLDecoder.decode(new String(original).trim(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("加密算法异常 ，异常信息：" + e.getMessage());
        }
    }

    /**
     * 解密方法
     * 支持：crypto-js通用解密方法
     * @param apiSecurityParam 报文对象
     * @param aesKeyEntity     秘钥对象
     * @return 解密后的内容
     */
    public static  String decryptAesCryptoJs(ApiSecurityParam apiSecurityParam, AesKeyEntity aesKeyEntity) {
        try {
            byte[] encrypted1 = new Base64().decode(apiSecurityParam.getSign());
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            SecretKeySpec keySpec = new SecretKeySpec(aesKeyEntity.getSecretKey().getBytes(StandardCharsets.UTF_8), "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(aesKeyEntity.getAppId().getBytes(StandardCharsets.UTF_8));
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
            byte[] original = cipher.doFinal(encrypted1);
            return URLDecoder.decode(new String(original).trim(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("加密算法异常 ，异常信息：" + e.getMessage());
        }
    }

    /**
     * 验签
     * 解密 sign 和 data 对比是否是同一个 JSONObject
     * @param apiSecurityParam 报文对象
     * @param aesKeyEntity     秘钥对象
     * @return true: 通过 false：不通过
     */
    public static boolean verifyAesCryptoJs(ApiSecurityParam apiSecurityParam, AesKeyEntity aesKeyEntity) {
        // 如果请求参数是加密传输的，先从ApiSecurityParam获取签名和时间戳放到headers里面这里读取。
        // 如果请求参数是非加密即明文传输的，那签名参数只能放到header中
        try {
            // 解密
            String singJson = decryptAesCryptoJs(apiSecurityParam, aesKeyEntity);
            // 原串
            JSONObject dataJson = JsonUtil.parseObject(apiSecurityParam.getData(), JSONObject.class);
            Map<String,Object>  map = Convert.toMap(String.class,Object.class,dataJson);
            String dataStr = tansParams(map,null);
            // 比较 value
            boolean flag = singJson.equals(dataStr);
            if (!flag) {
                throw new MmsException("签名验证不通过");
            }
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error("签名验证不通过");
            throw new MmsException("签名验证不通过");
        }

    }


    // =============================== 非对称加密 ==============================

    /**
     * 获取秘钥对
     * @return 秘钥对象
     */
    public static RsaKeyEntity getRsaKey() {
        RSA rsa = new RSA();
        //获得私钥
        rsa.getPrivateKey();
        rsa.getPrivateKeyBase64();
        //获得公钥
        rsa.getPublicKey();
        rsa.getPublicKeyBase64();
        return new RsaKeyEntity(rsa.getPublicKeyBase64(),rsa.getPrivateKeyBase64());
    }

    /**
     * 非对称加密
     * 签名处理
     * 公钥加密
     * @param apiSecurityParam 报文对象
     * @param rsaKeyEntity     秘钥对象
     * @return 密文
     */
    public static ApiSecurityParam encryptRsa(ApiSecurityParam apiSecurityParam,RsaKeyEntity rsaKeyEntity) {
        try {
            // 顺序按照首字母ASCII码进行升序排列
            String content = apiSecurityParam.getData();
            RSA rsa = new RSA(null, rsaKeyEntity.getPublicKey());
            byte[] encrypt = rsa.encrypt(StrUtil.bytes(content, CharsetUtil.CHARSET_UTF_8), KeyType.PublicKey);
            apiSecurityParam.setSign(ByteStringUtil.byte2str(encrypt));
            return apiSecurityParam;
        } catch (RuntimeException e) {
            log.error("签名失败{}", e.getMessage());
        }
        return null;
    }

    /**
     * 非对称解密
     * 签名处理
     * 私钥解密
     * @param apiSecurityParam 报文对象
     * @param rsaKeyEntity     秘钥对象
     * @return 解密后的对象
     */
    public static String decryptRsa(ApiSecurityParam apiSecurityParam,RsaKeyEntity rsaKeyEntity) throws RuntimeException {
        try {
            if (StringUtil.isEmpty(rsaKeyEntity.getPrivateKey())) {
                throw new RuntimeException("私钥不能为空!");
            }
            RSA rsa = new RSA(rsaKeyEntity.getPrivateKey(), null);
            if (StringUtil.isEmpty(apiSecurityParam.getSign())) {
                log.info("sign,不能为空！");
                throw new RuntimeException("验签失败!");
            }
            byte[] aByte = HexUtil.decodeHex(apiSecurityParam.getSign());
            byte[] decrypt = rsa.decrypt(aByte, KeyType.PrivateKey);
            String stored = StrUtil.str(decrypt, CharsetUtil.CHARSET_UTF_8);
            stored=URLDecoder.decode(stored.trim(), StandardCharsets.UTF_8);
            String oidStr = apiSecurityParam.getData();
            if(oidStr.equals(stored)){
                return stored;
            }
            return "";
        } catch (Exception e) {
            log.error("解密异常{}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 非对称
     * 签名验证
     * @param apiSecurityParam 报文对象
     * @param rsaKeyEntity     秘钥对象
     * @return true: 通过 false：不通过
     */
    public static Boolean verifyRsa(ApiSecurityParam apiSecurityParam,RsaKeyEntity rsaKeyEntity) throws RuntimeException {
        try {
            // 解密
            String singJson = decryptRsa(apiSecurityParam, rsaKeyEntity);
            singJson= URLDecoder.decode(singJson, StandardCharsets.UTF_8);
            // 原串
            String dataJson = apiSecurityParam.getData();
            // 比较 value
            assert singJson != null;
            boolean flag = singJson.equals(dataJson);
            if (!flag) {
                throw new MmsException("签名验证不通过");
            }
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error("签名验证异常{}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    //============================ 工具类 =============================

    /**
     * 参数排序
     *
     * @param params 参数
     * @return 排序后的参数
     */
    public static String tansParams(Map<String, Object> params, List<String> ignoreParamNames) throws RuntimeException {
        if (params == null || params.isEmpty()) {
            return null;
        }
        params.remove("sign");
        // 排除不需要签名的参数
        if (ignoreParamNames != null && !ignoreParamNames.isEmpty()) {
            for (String ignoreParamName : ignoreParamNames) {
                params.remove(ignoreParamName);
            }
        }
        String[] sortedKeys = params.keySet().toArray(new String[]{});
        // 排序请求参数
        Arrays.sort(sortedKeys);
        StringBuilder s2 = new StringBuilder();
        for (String key : sortedKeys) {
            log.error(params.get(key).toString());
            if (DataUtil.isObjectEmpty(params.get(key))) {
                continue;
            }
            s2.append(key).append("=").append(params.get(key)).append("&");
        }
        s2.deleteCharAt(s2.length() - 1);
        return s2.toString();
    }

    /**
     * 类，获取所有参数
     *
     * @param object 对象
     * @return 参数
     */
    public static Map<String, Object> getAllParams(Object object) {
        Map<String, Object> params = new HashMap<>();
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field f : fields) {
            if (!"serialVersionUID".equals(f.getName())) {
                if(f.getType().isAssignableFrom(Date.class)){
                    params.put(f.getName(), DateUtil.format((Date) getFieldValueByName(f.getName(),object),"yyyy-MM-dd HH:mm:ss"));
                    continue;
                }
                params.put(f.getName(), String.valueOf(getFieldValueByName(f.getName(), object)));
            }
        }
        return params;
    }

    /**
     * 类，根据属性名获取属性值
     *
     * @param fieldName 属性名
     * @param object    对象
     */
    public static Object getFieldValueByName(String fieldName, Object object) {
        try {
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getter = "get" + firstLetter + fieldName.substring(1);
            Method method = object.getClass().getMethod(getter, new Class[]{});
            return method.invoke(object, new Object[]{});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 类，获取需要忽略签名的参数名
     *
     * @param classType 类
     * @return
     * @IgnoreSign 自定义注解，可以获取到需要忽略签名的参数名
     */
    public static List<String> getIgnoreSignParmas(Class classType) {
        final ArrayList<String> igoreSignFieldNames = new ArrayList<String>();
        if (classType != null) {
            ReflectionUtils.doWithFields(classType, new ReflectionUtils.FieldCallback() {
                @Override
                public void doWith(@NotNull Field field) throws IllegalArgumentException, IllegalAccessException {
                    igoreSignFieldNames.add(field.getName());
                }
            }, new ReflectionUtils.FieldFilter() {
                @Override
                public boolean matches(@NotNull Field field) {
                    //属性类标注了@IgnoreSign
                    IgnoreSign typeIgnore = AnnotationUtils.findAnnotation(field.getType(), IgnoreSign.class);
                    //属性定义处标注了@IgnoreSign
                    IgnoreSign varIgnoreSign = field.getAnnotation(IgnoreSign.class);
                    return typeIgnore != null || varIgnoreSign != null;
                }
            });
            if (igoreSignFieldNames.size() > 1) {
                System.out.println(classType.getCanonicalName() + "不需要签名的属性:" + igoreSignFieldNames.toString());
            }
        }
        return igoreSignFieldNames;
    }

    /**
     * 允许客户端请求时间误差为10分钟
     */
    public static boolean isExceedTheTimeLimit(String timeStamp) {
        if (timeStamp == null || timeStamp.isEmpty() || timeStamp.equals("null")) {
            throw new RuntimeException("缺少请求时间戳[timestamp].");
        }
        return System.currentTimeMillis() - Long.parseLong(timeStamp) > 1000 * 60 * 3;
    }

    public static Map<String, Object> requestToMap(HttpServletRequest request) {
        Map<String, Object> params = new HashMap<String, Object>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (String name : requestParams.keySet()) {
            String[] values = requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            params.put(name, valueStr);
        }
        return params;
    }
    /**
     * 是否是一个Json格式的字符串
     * @param jsonString 字符串
     * @return true：是 false：不是
     */
    private static boolean isJson(String jsonString) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.readTree(jsonString);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 判断Map中是否存在这个属性
     *
     * @param map
     * @param key
     * @return
     */
    public static Boolean mapIsEmpty(Map<String, Object> map, String key) {
        if (!map.containsKey(key) || StringUtil.isEmpty(map.get(key).toString())) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    /**
     * &格式对象 转换成json
     * @param input
     * @return
     * @throws Exception
     */
    public static JSONObject strToJsonObject(String input)  {
        JSONObject jsonObject=new JSONObject();
        String[] keyValues = input.split("&");
        for (String keyValue : keyValues) {
            String[] parts = keyValue.split("=");
            String key = parts[0];
            String value = parts.length > 1 ? parts[1] : "";
            jsonObject.put(key,value);
        }
        return jsonObject;
    }
}
