package com.sxpcwlkj.demo.controller;

import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xijue
 * @ClassName WebSocketController
 * @description: socket测试
 * @date 2024年10月23日
 * @version: 1.0
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/ws")
public class WebSocketController {

    @GetMapping("sendMsg")
    public void sendMsg(@NotBlank(message = "msg：不能为空") String msg  ){
        //WebSocketService.sendMessage("服务端消息  :"  + LocalDateTime.now().toString()+" "+msg);
    }
}
