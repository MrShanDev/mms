package com.sxpcwlkj.framework.handler;


import cn.hutool.core.io.IoUtil;
import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * 使HttpServletRequest中getReader()和getInputStream()可重复使用
 * 请求是post，但我们又要对参数签名，这个时候我们需要获取到body的信息，
 * 但是当我们使用*HttpServletRequest的getReader()和getInputStream()获取参数后，
 * 后面不管是框架还是自己想再次获取body已经没办法获取,
 * HttpServletRequest 的 getInputStream() 和 getReader() 都只能读取一次。
 *
 * @author sxpcwlkj
 * @RequestBody 也是流的形式读取，流读了一次就没有了
 */
public class HttpServletRequestWrapperHandler extends HttpServletRequestWrapper {
    private final byte[] body;

    private final Map<String, String[]> parameterMap;

    public HttpServletRequestWrapperHandler(HttpServletRequest request) throws IOException {
        super(request);
        request.setCharacterEncoding("UTF-8");
        parameterMap = new HashMap<>(request.getParameterMap());

        body = IoUtil.readBytes(request.getInputStream(), false);
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    /**
     * 获取流程和子流程的输入流
     *
     * @return 流程和子流程的输入流
     * @throws IOException 流程和子流程的输入流
     */
    @Override
    public ServletInputStream getInputStream() throws IOException {
        final ByteArrayInputStream bais = new ByteArrayInputStream(body);
        return new ServletInputStream() {
            @Override
            public int read() throws IOException {
                return bais.read();
            }

            @Override
            public int available() throws IOException {
                return body.length;
            }

            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }
        };
    }

    /**
     * 获取请求参数
     *
     * @param key 参数名
     * @return 参数值
     */
    @Override
    public String[] getParameterValues(String key) {
        String[] values = parameterMap.get(key);
        if (values == null) {
            values = super.getParameterValues(key);
        }
        return values;
    }

    /**
     * 设置请求参数
     *
     * @param key   参数名
     * @param value 参数值
     */
    public void setParameter(String key, String... value) {
        parameterMap.put(key, value);
    }

}

