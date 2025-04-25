package com.sxpcwlkj.framework.entity;


import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @ClassName RsaKeyEntity
 * @Description 保存公钥 私钥额一个基础类
 * @Author 西决
 * @Date 2023/6/11 22:31
 */
@Data
public class RsaKeyEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 公钥加密
     */
    private String publicKey;

    /**
     * 私钥解密
     */
    private String privateKey;

    /**
     * 公钥RAS+MD5生成32位
     */
    private String uuid;


    public RsaKeyEntity(String publicKey, String privateKey) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    public RsaKeyEntity(String publicKey, String privateKey, String uuid) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
        this.uuid = uuid;
    }


}
