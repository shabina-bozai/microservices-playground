package com.demo.websocket.controller;

import com.demo.websocket.model.ChatMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Controller
public class ChatController {

    // Client sends to /app/chat.send → broadcast to all /topic/public subscribers
    @MessageMapping("/chat.send")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(ChatMessage message) {
        message.setTimestamp(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        return message;
    }

    // Client sends to /app/chat.join → broadcast join notification
    @MessageMapping("/chat.join")
    @SendTo("/topic/public")
    public ChatMessage addUser(ChatMessage message) {
        message.setContent(message.getSender() + " joined the chat");
        message.setTimestamp(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        return message;
    }
}
