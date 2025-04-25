package com.sxpcwlkj.gen.config.template;

import lombok.Data;

import java.util.List;

/**
 * 代码生成信息
 *
 * @author xijue
 * @Doc mmsadmin.cn
 */
@Data
public class GeneratorInfo {
    private ProjectInfo project;
    private DeveloperInfo developer;
    private List<TemplateInfo> templates;
}
