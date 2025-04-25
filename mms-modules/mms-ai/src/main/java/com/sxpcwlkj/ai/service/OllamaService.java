package com.sxpcwlkj.ai.service;

import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sxpcwlkj.ai.type.AIProvider;
import com.sxpcwlkj.ai.utils.DequeUtil;
import com.sxpcwlkj.ai.utils.FileParserUtil;
import com.sxpcwlkj.common.utils.DataUtil;
import com.sxpcwlkj.common.utils.IPUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import okhttp3.sse.EventSources;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Ollama Service
 * @author xijue
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class OllamaService {

    @Value("${mms.ai.type}")
    private String type;

    @Value("${mms.ai.url}")
    private String apiUrl;

    @Value("${mms.ai.model}")
    private String model;

    @Value("${mms.ai.key}")
    private String apiKey;

    private final DequeUtil dequeUtil;

    private final OkHttpClient client = new OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(90, TimeUnit.SECONDS)
        .build();


    /**
     * AI对话
     * @param sessionId 会话ID
     * @param userMessage 用户消息
     * @param temperature 温度
     * @param maxTokens 最大token数
     * @param emitter 响应体
     * @throws IOException 异常
     */
    public void streamGenerate(String sessionId,
                               String userMessage,
                               double temperature,
                               int maxTokens,
                               SseEmitter emitter,
                               List<MultipartFile>  files
                               ) throws IOException {

        // 1. 获取或创建上下文
        Deque<OllamaService.ChatMessage> context = dequeUtil.getContext(sessionId);

        // 2. 处理附件内容
        StringBuilder processedMessage = new StringBuilder(userMessage);

        if (files != null && !files.isEmpty()) {
            processedMessage.append("\n\n[附件内容]");
            for (MultipartFile file : files) {
                try {
                    String fileContent = FileParserUtil.parseFile(file);
                    processedMessage.append("\n\n文件：")
                        .append(file.getOriginalFilename())
                        .append("\n内容：")
                        .append(fileContent.trim());
                } catch (IOException e) {
                    sendError(emitter, "文件解析失败: " + e.getMessage());
                    return;
                }
            }
        }

        // 3. 添加处理后的消息到上下文
        dequeUtil.addMessageToContext(context, new ChatMessage("user", processedMessage.toString()));

        Request request =null;
        if(AIProvider.OLLAMA.getType().equals(type)){
            JsonObject body = new JsonObject();
            body.addProperty("model", model);
            body.addProperty("stream", true);
            // 3. 构建包含上下文的prompt
            body.addProperty("prompt", dequeUtil.buildContextPrompt(context));
            JsonObject options = new JsonObject();
            options.addProperty("temperature", temperature);
            options.addProperty("num_predict", maxTokens);
            body.add("options", options);
            request = new Request.Builder()
                .url(apiUrl)
                .post(RequestBody.create(body.toString(), MediaType.get("application/json")))
                .build();
        }else
        if(AIProvider.DEEPSEEK.getType().equals(type)){
            JsonObject body = new JsonObject();
            body.addProperty("model", model);
            body.addProperty("stream", true);
            body.addProperty("temperature", temperature);
            body.addProperty("max_tokens", maxTokens);
            // 转换上下文为messages数组
            JsonArray messages = new JsonArray();
            for (ChatMessage msg : context) {
                JsonObject message = new JsonObject();
                message.addProperty("role", msg.role());
                message.addProperty("content", msg.content());
                messages.add(message);
            }
            body.add("messages", messages);
            request = new Request.Builder()
                .url(apiUrl)
                .header("Authorization", "Bearer " + apiKey)
                .post(RequestBody.create(body.toString(), MediaType.get("application/json")))
                .build();
        }
        if(AIProvider.DOCUMENT.getType().equals(type)){
            // 创建 JSON 请求体
            MediaType mediaType = MediaType.get("application/json");
            JSONObject reqBody = new JSONObject();
            try {
                reqBody.put("content", userMessage);
                reqBody.put("bot_app_key", apiKey);
                reqBody.put("visitor_biz_id", sessionId);
                reqBody.put("session_id", IdUtil.fastSimpleUUID());
            } catch (Exception e) {
                e.printStackTrace();
            }
            request = new Request.Builder()
                .url(apiUrl)
                .post(RequestBody.create(reqBody.toString(),mediaType))
                .build();
        }
        else {
            sendError(emitter, "AI模块未匹配！");
            return;
        }

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                sendError(emitter, "API请求失败: " + response.code());
                return;
            }
            //数据处理
            processStreamResponse(response, emitter,context);
        } catch (IOException e) {
            sendError(emitter, "流式传输中断: " + e.getMessage());
        }

    }

    /**
     * Ollama 流式响应处理（带上下文收集）
     * @param response 响应体
     * @param emitter 响应体
     * @param context 上下文
     * @throws IOException 异常
     */
    private void processStreamResponse(Response response,
                                       SseEmitter emitter,
                                       Deque<ChatMessage> context) throws IOException {
        try (BufferedReader reader = new BufferedReader(
            new InputStreamReader(response.body().byteStream()))) {

            StringBuilder aiResponse = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                if (checkEmitterStatus(emitter)) {
                    System.out.println("连接已关闭，终止流处理");
                    break;
                }

                if(AIProvider.OLLAMA.getType().equals(type)){
                    JsonObject json = JsonParser.parseString(line).getAsJsonObject();
                    String content="";
                    boolean done=true;
                    content = json.get("response").getAsString();
                    done = json.get("done").getAsBoolean();
                    aiResponse.append(content);
                    try {
                        emitter.send(SseEmitter.event()
                            .name("message")
                            .data(new StreamResponse(content, done)));
                    } catch (IOException e) {
                        System.out.println("客户端已断开连接，停止发送数据");
                        break;
                    }

                    if (done) {
                        // 将AI回复加入上下文
                        dequeUtil.addMessageToContext(context, new ChatMessage("assistant", aiResponse.toString()));
                        emitter.complete();
                        break;
                    }
                }

                if(AIProvider.DEEPSEEK.getType().equals(type)){
                    // 跳过空行或非JSON数据
                    if (line.trim().isEmpty() || !line.startsWith("data: ")) {
                        // 处理官方API结束标记
                        if ("[DONE]".equals(line)) {
                            handleStreamEnd(emitter, context, aiResponse);
                            break;
                        }
                        continue;
                    }
                    // 安全移除前缀并修剪
                    line = line.substring(6).trim();
                    System.err.println("line:"+line);
                    // 修改后
                    if ("data: [DONE]".equals(line.trim())) {
                        handleStreamEnd(emitter, context, aiResponse);
                        break;
                    }
                    // 安全JSON解析
                    JsonObject json;
                    try {
                        json = JsonParser.parseString(line).getAsJsonObject();
                    } catch (Exception e) {
                        System.err.println("JSON解析失败: " + line);
                        continue;
                    }

                    // 增强错误处理
                    if (json.has("error")) {
                        handleApiError(emitter, json);
                        break;
                    }

                    // 验证核心字段
                    if (!json.has("choices") || json.get("choices").getAsJsonArray().isEmpty()) {
                        continue;
                    }

                    JsonObject choice = json.getAsJsonArray("choices").get(0).getAsJsonObject();

                    // 安全获取字段
                    JsonObject delta = choice.has("delta")
                        ? choice.getAsJsonObject("delta")
                        : new JsonObject();
                    String finishReason = (choice.get("finish_reason")==null|| "null".equals(choice.get("finish_reason").toString())) ? null : choice.get("finish_reason").getAsString() ;

                    // 内容提取
                    String content = delta.has("content")
                        ? delta.get("content").getAsString()
                        : "";

                    // 结束条件判断（处理"null"字符串情况）
                    System.err.println(finishReason);
                    boolean done = finishReason != null && !"null".equalsIgnoreCase(finishReason) && !finishReason.isEmpty();
                    if("length".equals(finishReason)){
                        sendError(emitter, "DeepSeek API错误: token限制结束");
                    }
                    /* 关键过滤逻辑：跳过无意义数据块 */
                    if (content.isEmpty() && !done) {
                        System.out.println("[INFO] 跳过空内容数据块");
                        continue;
                    }
                    aiResponse.append(content);
                    try {
                        emitter.send(SseEmitter.event()
                            .name("message")
                            .data(new StreamResponse(content, done)));
                    } catch (IOException e) {
                        System.out.println("客户端断开连接，停止发送");
                        break;
                    }
                }

                if(AIProvider.DOCUMENT.getType().equals(type)){
                    // 跳过空行或非JSON数据
                    // https://cloud.tencent.com/document/product/1759/105561#5a37acbc-b247-4325-a121-509d7c317fe5
                    if (line.trim().isEmpty() || line.startsWith("event:reply")||line.startsWith("event:token_stat")) {
                        continue;
                    }
                    // 安全移除前缀并修剪
                    line = line.substring(5).trim();
                    JSONObject dataObj = JSONObject.parseObject(line);
                    JSONObject payload = dataObj.getJSONObject("payload");
                    if (!payload.isEmpty()) {
                        String content = payload.getString("content");
                        if(null==content|| content.isEmpty()){
                            continue;
                        }
                        log.info(content);
                        try {
                            emitter.send(SseEmitter.event()
                                .name("message")
                                .data(new StreamResponse(content, true)));
                        } catch (IOException e) {
                            System.out.println("客户端断开连接，停止发送");
                            break;
                        }
                    }
                }

            }
        } catch (Exception e) {
            if (!checkEmitterStatus(emitter)) {
                emitter.completeWithError(e);
            }
        }
    }


    /**
     * 发送错误信息
     * @param emitter 响应体
     * @param message 错误信息
     */
    private void sendError(SseEmitter emitter, String message) {
        try {
            if (!checkEmitterStatus(emitter)) {
                emitter.send(SseEmitter.event().name("error").data(message));
            }
        } catch (IOException e) {
            System.err.println("发送错误信息失败: " + e.getMessage());
        } finally {
            emitter.complete();
        }
    }

    /**
     * 实现连接状态检查
     * @param emitter 响应体
     * @return true: 连接已断开，false: 连接正常
     */
    private boolean checkEmitterStatus(SseEmitter emitter) {
        try {
            emitter.send(SseEmitter.event().comment("ping"));
            // 连接正常
            return false;
        } catch (IOException e) {
            // 连接已断开
            return true;
        }
    }

    /**
     * 自定义一个StreamResponse对象
     * @param content 内容
     * @param isEnd 是否结束
     */
    public record StreamResponse(String content, boolean isEnd) {}

    /**
     * 自定义一个ChatMessage对象
     * @param role 角色
     * @param content 内容
     */
    public record ChatMessage(String role, String content) {}



    // 新增辅助方法
    private void handleStreamEnd(SseEmitter emitter,
                                 Deque<ChatMessage> context,
                                 StringBuilder aiResponse) {
        if (aiResponse.length() > 0) {
            dequeUtil.addMessageToContext(context,
                new ChatMessage("assistant", aiResponse.toString()));
        }
        emitter.complete();
    }

    private void handleApiError(SseEmitter emitter, JsonObject json) {
        JsonObject error = json.getAsJsonObject("error");
        String errorMsg = error.has("message")
            ? error.get("message").getAsString()
            : "未知API错误";
        sendError(emitter, "DeepSeek API错误: " + errorMsg);
    }

    private void handleProcessingError(SseEmitter emitter, Exception e) {
        if (!checkEmitterStatus(emitter)) {
            sendError(emitter, "流处理异常: " + e.getMessage());
            emitter.completeWithError(e);
        }
    }

}
