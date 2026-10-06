package com.sxpcwlkj.gen.config.template;

import cn.hutool.core.util.StrUtil;
import com.sxpcwlkj.common.exception.MmsException;
import com.sxpcwlkj.common.utils.JsonUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * 代码生成配置内容
 *
 * @author mmsAdmin
 * @Doc mmsadmin.cn
 */
@Configuration
public class GeneratorConfig {
    private String template="/template/gen";


    public GeneratorInfo getGeneratorConfig(int type)  {

        // 模板路径，如果不是以/结尾，则添加/
        if (!StrUtil.endWith(template, '/')) {
            template = template + "/";
        }

        // 模板配置文件
        InputStream isConfig = this.getClass().getResourceAsStream(template + "config.json");
        if (isConfig == null) {
            throw new MmsException("模板配置文件，config.json不存在");
        }
        try {
            // 读取模板配置文件
            String configContent = StreamUtils.copyToString(isConfig, StandardCharsets.UTF_8);
            GeneratorInfo generator = JsonUtil.parseObject(configContent, GeneratorInfo.class);
            assert generator != null;
            if (type == 3) {
                generator.getTemplates().removeIf(item -> "vue/dialog.vue.ftl".equals(item.getTemplateName()));
            }
            for (TemplateInfo templateInfo : generator.getTemplates()) {
                    // 模板文件
                    InputStream isTemplate = this.getClass().getResourceAsStream(template + templateInfo.getTemplateName());
                    if (isTemplate == null) {
                        throw new MmsException("模板文件 " + templateInfo.getTemplateName() + " 不存在");
                    }
                    // 读取模板内容
                    String templateContent = StreamUtils.copyToString(isTemplate, StandardCharsets.UTF_8);
                    templateInfo.setTemplateContent(templateContent);
            }
            return generator;
        } catch (IOException e) {
            throw new MmsException("读取config.json配置文件失败");
        }
    }
}
