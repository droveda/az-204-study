package com.droveda.azure_labs.adapters.in.controller;

import com.droveda.azure_labs.application.core.model.AccountMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/jms-test")
public class JMSController {

    private final JmsTemplate jmsTemplate;

    public JMSController(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    @GetMapping
    public ResponseEntity<String> hello() {
        jmsTemplate.convertAndSend("my-queue", new AccountMessage(
                "John Locke",
                "123456",
                75
        ));
        return ResponseEntity.ok("OK");
    }

}
