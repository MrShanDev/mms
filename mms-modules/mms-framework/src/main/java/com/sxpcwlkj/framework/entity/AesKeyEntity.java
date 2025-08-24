package com.sxpcwlkj.framework.entity;


import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @ClassName RsaKeyEntity
 * @Description 保存公钥 私钥额一个基础类
 * @Author mmsAdmin
 * @Date 2023/6/11 22:31
 */
@Data
public class AesKeyEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * appId
     */
    private String appId;
    /**
     * 秘钥
     */
    private String secretKey;

    public AesKeyEntity(String appId, String secretKey) {
        this.appId = appId;
        this.secretKey = secretKey;
    }


}
