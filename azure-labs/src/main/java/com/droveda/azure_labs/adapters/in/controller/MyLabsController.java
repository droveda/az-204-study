package com.droveda.azure_labs.adapters.in.controller;

import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/lab")
public class MyLabsController {

    @GetMapping("/managed-identity")
    public ResponseEntity<String> firstExample() {

        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                .endpoint("https://drovedastorage.blob.core.windows.net/")
                .credential(new DefaultAzureCredentialBuilder().build())
                .buildClient();

        String containerName = "data";

        BlobContainerClient blobContainerClient = blobServiceClient.getBlobContainerClient(containerName);
        BlobClient blobClient = blobContainerClient.getBlobClient("script01.txt");
        blobClient.uploadFromFile("./some-path/" + "script01.txt");

        return ResponseEntity.ok("OK");
    }

}
