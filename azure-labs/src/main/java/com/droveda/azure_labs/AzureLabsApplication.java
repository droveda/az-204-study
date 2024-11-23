package com.droveda.azure_labs;

import com.azure.messaging.servicebus.ServiceBusProcessorClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class AzureLabsApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(AzureLabsApplication.class);

    @Autowired
    private ServiceBusProcessorClient processorClient;

    public static void main(String[] args) {
        SpringApplication.run(AzureLabsApplication.class, args);
    }

    @EventListener
    public void onApplicationReady(ApplicationReadyEvent event) {
        LOGGER.info("Initializing ServiceBus Consumer...");
        processorClient.start();
    }

    @EventListener
    public void onContextClosed(ContextClosedEvent event) {
        LOGGER.info("Destroying ServiceBus Consumer...");
        processorClient.close();
    }


}
