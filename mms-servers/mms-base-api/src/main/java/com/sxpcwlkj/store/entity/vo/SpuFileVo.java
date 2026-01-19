package com.sxpcwlkj.store.entity.vo;

import com.sxpcwlkj.store.enums.FileType;
import lombok.Data;

@Data
public class SpuFileVo {

    private String skuId;

    private String fileUrl;

    private FileType fileType;

    private String videoImg;
}
