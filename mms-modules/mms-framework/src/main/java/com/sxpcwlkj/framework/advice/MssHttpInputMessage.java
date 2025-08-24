package com.sxpcwlkj.framework.advice;

import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;

import java.io.IOException;
import java.io.InputStream;

/**
 * 新购 HttpInputMessage 实现
 *
 * @author mmsAdmin
 */
public class MssHttpInputMessage implements HttpInputMessage {
    HttpHeaders headers;
    InputStream body;

    public MssHttpInputMessage(HttpHeaders headers, InputStream body) {
        this.headers = headers;
        this.body = body;
    }

    @Override
    public @NotNull InputStream getBody() throws IOException {
        return this.body;
    }

    @Override
    public @NotNull HttpHeaders getHeaders() {
        return this.headers;
    }

}
