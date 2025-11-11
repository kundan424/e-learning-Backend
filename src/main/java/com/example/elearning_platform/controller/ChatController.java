package com.example.elearning_platform.controller;

import com.example.elearning_platform.dto.ChatMessage;
import com.example.elearning_platform.dto.SignalMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessageSendingOperations messagingTemplate;

    @MessageMapping("/chat.sendMessage/{courseId}")
    @SendTo("/topic/course/{courseId}")
    public ChatMessage sendMessage(
            @DestinationVariable String courseId,
            @Payload ChatMessage chatMessage
    ) {
        return chatMessage;
    }

    //    1:1 chat
    @MessageMapping("/chat.sendPrivateMessage")
    public void sendPrivateMessage(@Payload ChatMessage chatMessage) {

        System.out.println("---- PRIVATE MESSAGE RECEIVED ----");
        System.out.println("Recipient: " + chatMessage.getRecipient());

        String recipientUsername = chatMessage.getRecipient();
        messagingTemplate.convertAndSendToUser(
                recipientUsername,
                "/queue/private",
                chatMessage
        );
    }

    @MessageMapping("/signal.forward")
    public void forwardSignal(@Payload SignalMessage signalMessage) {
        String recipientUsername = signalMessage.getRecipient();

        // Forward the *entire* signal message to the recipient's private queue
        messagingTemplate.convertAndSendToUser(
                recipientUsername,
                "/queue/signal",
                signalMessage
        );
    }
}
