package com.droveda.azure_labs.application.ports.out;

public interface StorageOutputPort {

    void uploadFile(String fileName, String path);

}
