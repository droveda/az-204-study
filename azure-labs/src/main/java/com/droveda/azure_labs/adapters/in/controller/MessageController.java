package com.droveda.azure_labs.adapters.in.controller;

import com.droveda.azure_labs.adapters.out.message.dto.MyMessage;
import com.droveda.azure_labs.application.ports.out.MessageOutputPort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/message")
public class MessageController {

    private final MessageOutputPort<MyMessage> messageOutputPort;

    public MessageController(MessageOutputPort<MyMessage> messageOutputPort) {
        this.messageOutputPort = messageOutputPort;
    }

    @GetMapping
    public ResponseEntity<String> sendMessage() {
        messageOutputPort.sendMessage(new MyMessage(1011, "Hello World!"));
        return ResponseEntity.ok("Sent");
    }

}
