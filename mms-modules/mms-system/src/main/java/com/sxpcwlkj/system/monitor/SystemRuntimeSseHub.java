package com.sxpcwlkj.system.monitor;

import com.sxpcwlkj.authority.LoginObject;
import com.sxpcwlkj.common.enums.ErrorCodeEnum;
import com.sxpcwlkj.common.exception.MmsException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * SSE 客户端管理（用于系统运行状态实时推送）
 */
@Component
@Slf4j
public class SystemRuntimeSseHub {

    private final List<Client> clients = new CopyOnWriteArrayList<>();

    public SseEmitter register(String token) {
        // token 无效直接拒绝
        if (LoginObject.getLoginIdByToken(token) == null) {
            throw new MmsException(ErrorCodeEnum.USER_NOT_LOGIN.getValue(), ErrorCodeEnum.USER_NOT_LOGIN.getKey());
        }

        // 不设置超时（由网关/浏览器决定），断开后自动移除
        SseEmitter emitter = new SseEmitter(0L);
        Client client = new Client(token, emitter);
        clients.add(client);

        emitter.onCompletion(() -> clients.remove(client));
        emitter.onTimeout(() -> clients.remove(client));
        emitter.onError((e) -> clients.remove(client));

        return emitter;
    }

    public void broadcast(Object payload) {
        for (Client c : clients) {
            // token 过期则移除
            if (LoginObject.getLoginIdByToken(c.token) == null) {
                clients.remove(c);
                continue;
            }
            try {
                c.emitter.send(SseEmitter.event().name("runtime").data(payload));
            } catch (IOException e) {
                clients.remove(c);
            } catch (Exception e) {
                clients.remove(c);
            }
        }
    }

    @Data
    @AllArgsConstructor
    private static class Client {
        private String token;
        private SseEmitter emitter;
    }
}
