package com.droveda.azure_labs.application.ports.out;

public interface MessageOutputPort<T> {

    void sendMessage(T message);

}
