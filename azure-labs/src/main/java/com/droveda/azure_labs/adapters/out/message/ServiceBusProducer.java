package com.droveda.azure_labs.adapters.out.message;

import com.azure.messaging.servicebus.ServiceBusMessage;
import com.azure.messaging.servicebus.ServiceBusSenderClient;
import com.droveda.azure_labs.adapters.out.message.dto.MyMessage;
import com.droveda.azure_labs.application.ports.out.MessageOutputPort;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ServiceBusProducer implements MessageOutputPort<MyMessage> {

    private final ServiceBusSenderClient producer;
    private final ObjectMapper objectMapper;

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceBusProducer.class);

    public ServiceBusProducer(ServiceBusSenderClient producer, ObjectMapper objectMapper) {
        this.producer = producer;
        this.objectMapper = objectMapper;
    }

    @Override
    public void sendMessage(MyMessage message) {
        try {
            producer.sendMessage(new ServiceBusMessage(objectMapper.writeValueAsString(message)));
        } catch (JsonProcessingException ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
    }
}
