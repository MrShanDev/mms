package com.sxpcwlkj.gen;

import freemarker.template.Template;
import org.junit.jupiter.api.Test;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.net.URI;
import javax.tools.*;
import com.sun.source.util.JavacTask;

/** 无数据库模板回归：覆盖三种布局的路由、保存接口和生成组件引用。 */
class GeneratorLayoutTemplateTest {
    @Test
    void rendersAllLayouts() throws Exception {
        verify(Path.of("src/main/resources/template/gen"), null);
    }

    public static void main(String[] args) throws Exception {
        verify(Path.of(args[0]), Path.of(args[1]));
    }

    private static void verify(Path templates, Path samples) throws Exception {
        for (int layout = 1; layout <= 3; layout++) {
            Map<String, Object> model = model(layout);
            Map<String, String> rendered = new HashMap<>();
            try (var files = Files.walk(templates)) {
                for (Path file : files.filter(p -> p.toString().endsWith(".ftl")).toList()) {
                    String name = templates.relativize(file).toString();
                    String output = render(name, Files.readString(file), model);
                    rendered.put(name, output);
                    if (name.endsWith(".java.ftl")) checkJavaSyntax(name, output);
                    check(!output.contains("<#"), "Unrendered template: " + name);
                    if (samples != null) {
                        Path target = samples.resolve("layout-" + layout).resolve(name.replace(".ftl", ""));
                        Files.createDirectories(target.getParent());
                        Files.writeString(target, output);
                    }
                }
            }
            String page = rendered.get("vue/index.vue.ftl");
            String controller = rendered.get("java/controller/Controller.java.ftl");
            String api = rendered.get("vue/api.ts.ftl");
            check(page.contains("<SystemPage"), "Missing shared page shell");
            check(rendered.get("vue/form.vue.ftl").contains("required: true"), "Required validation missing");
            check(rendered.get("vue/dialog.vue.ftl").contains("dialogFormRef.value.validate()"), "Dialog validation missing");
            if (layout == 1) {
                check(page.contains("<el-pagination"), "List pagination missing");
                check(page.contains("@click=\"onSearch\""), "Search reset handler missing");
                check(page.contains("/components/SampleDialog.vue"), "Dialog path missing");
                check(controller.contains("/list"), "List API missing");
            } else if (layout == 2) {
                check(page.contains("row-key=\"id\""), "Tree row key missing");
                check(page.contains("全部折叠"), "Tree toggle missing");
                check(page.contains("type: curdEnum.INSERT })"), "Tree root insert uses wrong mode");
            } else {
                check(page.contains("<el-tabs") && page.contains("@click=\"save\""), "Settings form missing");
                check(!page.contains("<el-table") && !page.contains("<el-pagination"), "Singleton still renders list");
                check(controller.contains("@GetMapping(\"/singleton\")") && controller.contains("@PutMapping(\"/singleton\")"), "Singleton endpoints missing");
                check(!controller.contains("@PostMapping(\"/list\")"), "Singleton has list endpoint");
                check(api.contains("saveSingleton") && !api.contains("/list"), "Singleton API mismatch");
                check(!rendered.get("sql/menu.sql.ftl").contains("demo:sample:list"), "Singleton registers list permission");
            }
            System.out.println("Layout " + layout + ": all templates rendered and contracts verified");
        }
    }

    private static void checkJavaSyntax(String name, String content) throws Exception {
        var diagnostics = new DiagnosticCollector<JavaFileObject>();
        JavaFileObject source = new SimpleJavaFileObject(URI.create("string:///" + name.replace(".ftl", "")), JavaFileObject.Kind.SOURCE) {
            @Override public CharSequence getCharContent(boolean ignoreEncodingErrors) { return content; }
        };
        JavacTask task = (JavacTask) ToolProvider.getSystemJavaCompiler().getTask(null, null, diagnostics,
                List.of("-proc:none"), null, List.of(source));
        task.parse();
        check(diagnostics.getDiagnostics().stream().noneMatch(d -> d.getKind() == Diagnostic.Kind.ERROR),
                "Generated Java syntax error: " + name + diagnostics.getDiagnostics());
    }

    private static String render(String name, String content, Map<String, Object> model) throws Exception {
        StringWriter output = new StringWriter();
        new Template(name, new StringReader(content), null, "utf-8").process(model, output);
        return output.toString();
    }

    private static Map<String, Object> model(int layout) {
        Map<String, Object> data = new HashMap<>();
        data.put("package", "com.example"); data.put("packagePath", "com/example");
        data.put("moduleName", "demo"); data.put("ModuleName", "Demo");
        data.put("functionName", "sample"); data.put("FunctionName", "Sample");
        data.put("ClassName", "Sample"); data.put("tableName", "demo_sample");
        data.put("tableComment", "示例管理"); data.put("tableId", "id"); data.put("TableId", "Id");
        data.put("tableParentId", "parentId"); data.put("TableParentId", "ParentId");
        data.put("tableLabel", "name"); data.put("formLayout", layout); data.put("span", 24);
        data.put("author", "MMS"); data.put("email", "dev@example.com"); data.put("website", "MMS");
        data.put("version", "1.0.0"); data.put("date", "2026-10-06"); data.put("datetime", "2026-10-06 12:00:00");
        data.put("menuId", "3"); data.put("dbType", "MySQL");
        data.put("backendPath", "backend"); data.put("frontendPath", "frontend");
        List<Map<String, Object>> fields = new ArrayList<>();
        fields.add(field("id", "主键", "String", true, false));
        fields.add(field("name", "名称", "String", false, true));
        fields.add(field("parentId", "上级", "String", false, false));
        fields.add(field("sort", "排序", "Integer", false, false));
        fields.add(field("status", "状态", "Integer", false, false));
        data.put("fieldList", fields); data.put("formList", fields); data.put("listList", fields); data.put("gridList", fields);
        data.put("queryList", List.of(fields.get(1))); data.put("importList", List.of());
        data.put("fastList", List.of()); data.put("baseClass", new HashMap<>());
        return data;
    }

    private static Map<String, Object> field(String name, String comment, String type, boolean pk, boolean required) {
        Map<String, Object> field = new HashMap<>();
        field.put("attrName", name); field.put("fieldName", name); field.put("fieldComment", comment);
        field.put("attrType", type); field.put("fieldType", type.equals("Integer") ? "int" : "varchar");
        field.put("baseField", false); field.put("primaryPk", pk); field.put("formRequired", required);
        field.put("formType", "text"); field.put("queryFormType", "text"); field.put("queryType", "eq");
        field.put("autoFill", ""); field.put("formValidator", "@NotBlank");
        return field;
    }

    private static void check(boolean value, String message) {
        if (!value) throw new AssertionError(message);
    }
}
