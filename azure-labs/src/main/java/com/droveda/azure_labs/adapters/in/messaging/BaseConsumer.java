package com.droveda.azure_labs.adapters.in.messaging;

import com.azure.messaging.servicebus.ServiceBusErrorContext;
import com.azure.messaging.servicebus.ServiceBusReceivedMessageContext;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class BaseConsumer<T> {

    protected final ObjectMapper objectMapper;

    public BaseConsumer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public T convertObject(String messageBody, Class<T> targetClass) {
        try {
            return objectMapper.readValue(messageBody, targetClass);
        } catch (JacksonException ex) {
            throw new RuntimeException(ex);
        }
    }

    public abstract void onMessage(ServiceBusReceivedMessageContext context);

    public abstract void onError(ServiceBusErrorContext context);
}
