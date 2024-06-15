package com.droveda.azure_labs.application.ports.in;

public interface UploadFileInputPort {

    void uploadFile(String fileName, String path);

    void uploadFileManagedIdentity(String fileName, String path);

}
