package com.droveda.azure_labs.application.core.usecase;

import com.droveda.azure_labs.application.ports.in.UploadFileInputPort;
import com.droveda.azure_labs.application.ports.out.StorageOutputPort;

public class UploadFileInputPortUseCase implements UploadFileInputPort {

    private final StorageOutputPort storageOutputPort;

    public UploadFileInputPortUseCase(StorageOutputPort storageOutputPort) {
        this.storageOutputPort = storageOutputPort;
    }

    @Override
    public void uploadFile(String fileName, String path) {
        storageOutputPort.uploadFile(fileName, path);
    }

    @Override
    public void uploadFileManagedIdentity(String fileName, String path) {
        storageOutputPort.uploadFileManagedIdentityExample(fileName, path);
    }
}
