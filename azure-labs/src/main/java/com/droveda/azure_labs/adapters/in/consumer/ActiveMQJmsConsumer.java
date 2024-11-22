package com.droveda.azure_labs.adapters.in.consumer;

import com.droveda.azure_labs.application.core.model.AccountMessage;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@Profile("default")
public class ActiveMQJmsConsumer {

    @JmsListener(destination = "my-queue", containerFactory = "defaultJmsListenerContainerFactory")
    public void receive(AccountMessage message) {
        System.out.println("Received from activemq: " + message);
    }

}
