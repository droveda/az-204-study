package com.droveda.azure_labs.config;

import com.droveda.azure_labs.adapters.out.storageaccount.StorageAccountAdapter;
import com.droveda.azure_labs.application.core.usecase.UploadFileInputPortUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyBeansConfiguration {

    @Bean
    public UploadFileInputPortUseCase uploadFileUseCase(StorageAccountAdapter storageAccountAdapter) {
        return new UploadFileInputPortUseCase(storageAccountAdapter);
    }

}
