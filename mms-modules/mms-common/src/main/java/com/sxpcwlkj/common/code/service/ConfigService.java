package com.sxpcwlkj.common.code.service;

/**
 * @ClassName ConfigService
 * @Description TODO
 * @Author mmsAdmin
 * @Date 2023/1/23 21:02
 */
public interface ConfigService {
    /**
     * 根据参数 key 获取参数值
     *
     * @param configKey 参数 key
     * @return 参数值
     */
    String getConfigValue(String configKey);
}
