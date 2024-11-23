package com.droveda.azure_labs.config;

import com.azure.messaging.servicebus.ServiceBusClientBuilder;
import com.azure.messaging.servicebus.ServiceBusProcessorClient;
import com.azure.messaging.servicebus.ServiceBusSenderClient;
import com.droveda.azure_labs.adapters.in.messaging.MyConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessagingConfig {

    private final Logger LOGGER = LoggerFactory.getLogger(MessagingConfig.class);

    @Value("${servicebus.connectionString}")
    private String connectionString;

    @Value("${servicebus.queueName}")
    private String queueName;

    private final MyConsumer myConsumer;

    public MessagingConfig(MyConsumer myConsumer) {
        this.myConsumer = myConsumer;
    }

    @Bean
    public ServiceBusClientBuilder serviceBusClientBuilder() {
        return new ServiceBusClientBuilder()
                .connectionString(connectionString);
    }

    @Bean
    public ServiceBusSenderClient sender(ServiceBusClientBuilder builder) {
        return builder.connectionString(connectionString)
                .sender()
                .queueName(queueName)
                .buildClient();
    }

    @Bean
    public ServiceBusProcessorClient processorClient(ServiceBusClientBuilder builder) {
        return builder.connectionString(connectionString)
                .processor()
                .queueName(queueName)
                .processMessage(myConsumer::onMessage)
                .processError(myConsumer::onError)
                .buildProcessorClient();
    }

}
