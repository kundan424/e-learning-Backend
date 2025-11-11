package com.example.elearning_platform.dto;

import lombok.Data;

@Data
public class SignalMessage {

    private String sender;
    private String  recipient;
    private String type;
    private Object payload; // The actual WebRTC signal (can be JSON)
}
