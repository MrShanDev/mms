package com.sxpcwlkj.bbs.entity.bo;

import com.sxpcwlkj.framework.config.ValidatedGroupConfig;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BbsTopicPcBo {

    /**
     * 分类ID
     */
    @NotBlank(message = "分类ID不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
    private String cateId;
    /**
     * 标题
     */
    @NotBlank(message = "标题不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
    private String title;
    /**
     * 内容
     */
    @NotBlank(message = "内容不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
    private String contentHtml;
}
