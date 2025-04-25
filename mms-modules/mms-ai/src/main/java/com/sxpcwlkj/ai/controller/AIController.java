package com.sxpcwlkj.ai.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import com.sxpcwlkj.ai.service.OllamaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * AI问答
 * @author xijue
 */
@RestController
@CrossOrigin(origins = "*") // 允许跨域
@RequiredArgsConstructor
@Slf4j
public class AIController {
    private final OllamaService ollamaService;
    private final ExecutorService sseThreadPool;

    /**
     * 流式聊天接口
     * 支持上下文/接收一个附件集合
     * @param sessionId 会话ID
     * @param message 用户消息
     * @param files 附件集合
     * @return 消息流
     */
    @SaIgnore
    @PostMapping(value = "/api/chat", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public SseEmitter handleChat(
        @RequestParam String sessionId,
        @RequestParam String message,
        @RequestParam(required = false) List<MultipartFile> files) {
        // 实现文件处理和流式响应逻辑
        // 设置 SSE 超时为 5 分钟
        // 初始化时设置超时（单位：毫秒）// 1小时超时
        SseEmitter emitter = new SseEmitter(60 * 60 * 1000L);
        emitter.onTimeout(() -> {
            log.error("SSE超时: {}", sessionId);
            emitter.complete();
        });

        emitter.onError((ex) -> {
            log.error("SSE错误: {}", ex.getMessage());
        });
        sseThreadPool.execute(() -> {
            try {
                ollamaService.streamGenerate(sessionId, message, 0.7, 8000, emitter,files);
                emitter.complete();
            } catch (Exception e) {
                emitter.completeWithError(e);
            }
        });
        return emitter;
    }


    /**
     * 流式聊天接口
     * 支持上下文/接收一个附件集合
     * @return 消息流
     */
    @SaIgnore
    @PostMapping(value = "/api/chat/json", consumes = MediaType.APPLICATION_JSON_VALUE)
    public SseEmitter handleChatJson(@RequestBody ChatRequest request) {
        // 实现文件处理和流式响应逻辑
        // 设置 SSE 超时为 5 分钟
        // 初始化时设置超时（单位：毫秒）// 1小时超时
        SseEmitter emitter = new SseEmitter(60 * 60 * 1000L);
        emitter.onTimeout(() -> {
            log.error("SSE超时: {}", request.getSessionId());
            emitter.complete();
        });

        emitter.onError((ex) -> {
            log.error("SSE错误: {}", ex.getMessage());
        });
        sseThreadPool.execute(() -> {
            try {
                ollamaService.streamGenerate(request.getSessionId(), request.getMessage(), 0.7, 8000, emitter,null);
                emitter.complete();
            } catch (Exception e) {
                emitter.completeWithError(e);
            }
        });
        return emitter;
    }

}
