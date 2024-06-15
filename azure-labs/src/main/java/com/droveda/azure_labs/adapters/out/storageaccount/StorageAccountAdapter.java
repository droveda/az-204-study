package com.droveda.azure_labs.adapters.out.storageaccount;

import com.azure.identity.ClientSecretCredentialBuilder;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.identity.IntelliJCredential;
import com.azure.identity.IntelliJCredentialBuilder;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.droveda.azure_labs.application.ports.out.StorageOutputPort;
import org.springframework.stereotype.Component;

@Component
public class StorageAccountAdapter implements StorageOutputPort {

    private static final String STORAGE_ACCOUNT_URL = "https://drovedastorage.blob.core.windows.net/";
    private static final String CONTAINER_NAME = "data";

    @Override
    public void uploadFile(String fileName, String path) {
//        String connectStr = System.getenv("AZURE_STORAGE_CONNECTION_STRING");

        // GET these values from the Azure Portal, Inside your App Registration in Microsoft Entra ID
        String tenantId = System.getenv("AZURE_TENANT_ID");
        String clientId = System.getenv("AZURE_CLIENT_ID");
        String clientSecret = System.getenv("AZURE_CLIENT_SECRET");

        var clientSecretCredential = new ClientSecretCredentialBuilder()
                .clientId(clientId)
                .clientSecret(clientSecret)
                .tenantId(tenantId)
                .build();

        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
//                .connectionString(connectStr)
                .endpoint(STORAGE_ACCOUNT_URL)
                .credential(clientSecretCredential)
                .buildClient();

        BlobContainerClient blobContainerClient = blobServiceClient.getBlobContainerClient(CONTAINER_NAME);
        BlobClient blobClient = blobContainerClient.getBlobClient(fileName);
        blobClient.uploadFromFile(path + fileName);
    }

    @Override
    public void uploadFileManagedIdentityExample(String fileName, String path) {

        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                .endpoint(STORAGE_ACCOUNT_URL)
                .credential(new DefaultAzureCredentialBuilder()
                        .build())
                .buildClient();

        BlobContainerClient blobContainerClient = blobServiceClient.getBlobContainerClient(CONTAINER_NAME);
        BlobClient blobClient = blobContainerClient.getBlobClient(fileName);
        blobClient.uploadFromFile(path + fileName);
    }
}
