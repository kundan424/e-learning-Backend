package com.example.elearning_platform.dto;

import lombok.Data;

@Data
public class ChatMessage {

    public enum MessageType {
        CHAT,
        ANNOUNCEMENT,
        PRIVATE
    }
    private MessageType type;
    private String content;
    private String sender;
    private String recipient;
}
