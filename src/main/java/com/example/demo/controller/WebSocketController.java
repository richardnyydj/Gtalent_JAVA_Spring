package com.example.demo.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    @MessageMapping("/sendMessage") // 接收來自 /app/sendMessage 的訊息
    @SendTo("/topic/messages") // 轉發訊息到 /topic/messages
    public String sendMessage(String message) {
        return "Server received: " + message;
    }
}
