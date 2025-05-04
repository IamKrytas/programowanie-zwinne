package com.project.controller;

import com.project.model.ChatMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Controller
public class ChatController {
    @MessageMapping("/send")
    @SendTo("/topic/messages")
    public ChatMessage send(ChatMessage message) {
        message.setTimestamp(LocalDateTime.now().toString());
        return message;
    }
}
