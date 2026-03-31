package com.sxpcwlkj.plugin;

/**
 * 插件描述读取、校验或生命周期中的不可恢复错误。
 */
public class PluginException extends RuntimeException {

    public PluginException(String message) {
        super(message);
    }

    public PluginException(String message, Throwable cause) {
        super(message, cause);
    }
}
