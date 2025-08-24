package com.sxpcwlkj.websocket.entity;

import lombok.Data;

/**
 * @author mmsAdmin
 * @ClassName SendMsg
 * @description: 客服端报文对象
 * @date 2024年10月25日
 * @version: 1.0
 */

@Data
public class DataMsgVo {

    /**
     * 通信命令
     */
    private Integer cmd;

    /**
     * 消息内容
     */
    private String data;

}
