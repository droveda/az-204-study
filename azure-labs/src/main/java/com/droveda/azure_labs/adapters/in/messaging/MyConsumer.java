package com.droveda.azure_labs.adapters.in.messaging;

import com.azure.messaging.servicebus.ServiceBusErrorContext;
import com.azure.messaging.servicebus.ServiceBusReceivedMessage;
import com.azure.messaging.servicebus.ServiceBusReceivedMessageContext;
import com.droveda.azure_labs.adapters.out.message.dto.MyMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class MyConsumer extends BaseConsumer<MyMessage> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyConsumer.class);

    public MyConsumer(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    public void onMessage(ServiceBusReceivedMessageContext context) {
        LOGGER.info("Receiving message Thread - {}", Thread.currentThread().getName());
        ServiceBusReceivedMessage message = context.getMessage();
        LOGGER.info("Processing message. Session: {}, Sequence #: {}. Contents: {}", message.getMessageId(),
                message.getSequenceNumber(), message.getBody());

        var myMessage = convertObject(message.getBody().toString(), MyMessage.class);
        LOGGER.info("MyMessage: {}", myMessage);

    }

    @Override
    public void onError(ServiceBusErrorContext context) {
        LOGGER.error(context.getException().getLocalizedMessage(), context.getException());
    }
}
