package com.droveda.azure_labs.adapters.out.storageaccount;

import com.azure.identity.ClientSecretCredentialBuilder;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.droveda.azure_labs.application.ports.out.StorageOutputPort;
import org.springframework.stereotype.Component;

@Component
public class StorageAccountAdapter implements StorageOutputPort {

    @Override
    public void uploadFile(String fileName, String path) {
//        String connectStr = System.getenv("AZURE_STORAGE_CONNECTION_STRING");

        // GET these values from the Azure Portal, Inside your App Registration in Microsoft Entra ID
        String tenantId = System.getenv("MY_AZ_TENANT_ID");
        String clientId = System.getenv("MY_AZ_CLIENT_ID");
        String clientSecret = System.getenv("MY_AZ_CLIENT_SECRET");

        var clientSecretCredential = new ClientSecretCredentialBuilder()
                .clientId(clientId)
                .clientSecret(clientSecret)
                .tenantId(tenantId)
                .build();

        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
//                .connectionString(connectStr)
                .endpoint("https://drovedastorage.blob.core.windows.net/")
                .credential(clientSecretCredential)
                .buildClient();

        String containerName = "data";

        BlobContainerClient blobContainerClient = blobServiceClient.getBlobContainerClient(containerName);
        BlobClient blobClient = blobContainerClient.getBlobClient(fileName);
        blobClient.uploadFromFile(path + fileName);
    }
}
