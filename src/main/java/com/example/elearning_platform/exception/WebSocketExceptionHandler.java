package com.example.elearning_platform.exception;

import org.springframework.messaging.converter.MessageConversionException;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class WebSocketExceptionHandler {

    @MessageExceptionHandler
    public void handleMessageConversionException(MessageConversionException ex) {
        System.err.println("--- WEBSOCKET DESERIALIZATION ERROR ---");
        ex.printStackTrace();
    }
}
