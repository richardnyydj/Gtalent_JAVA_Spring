package com.example.demo.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.example.demo.config.RabbitMQConfig;

@Service
public class MessageListener { //雷同於Consumer

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void handleMessage(String message) {
        System.out.println("Received message from RabbitMQ: " + message);
        // 廣播到前端 WebSocket
        messagingTemplate.convertAndSend("/topic/messages", "From RabbitMQ: " + message);
    }
}