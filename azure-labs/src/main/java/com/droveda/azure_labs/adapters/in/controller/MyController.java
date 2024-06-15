package com.droveda.azure_labs.adapters.in.controller;

import com.azure.identity.ClientSecretCredentialBuilder;
import com.azure.security.keyvault.keys.KeyClient;
import com.azure.security.keyvault.keys.KeyClientBuilder;
import com.azure.security.keyvault.keys.cryptography.CryptographyClient;
import com.azure.security.keyvault.keys.cryptography.models.DecryptResult;
import com.azure.security.keyvault.keys.cryptography.models.EncryptResult;
import com.azure.security.keyvault.keys.cryptography.models.EncryptionAlgorithm;
import com.azure.security.keyvault.secrets.SecretClient;
import com.azure.security.keyvault.secrets.SecretClientBuilder;
import com.azure.security.keyvault.secrets.models.KeyVaultSecret;
import com.droveda.azure_labs.application.ports.in.UploadFileInputPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class MyController {

    private final static Logger LOGGER = LoggerFactory.getLogger(MyController.class);

    @Autowired
    private UploadFileInputPort uploadFileInputPort;

    @GetMapping("/test_upload")
    public ResponseEntity<String> exampleUploadFile() {
        uploadFileInputPort.uploadFile("script01.txt", "./some-path/");
        return ResponseEntity.ok("OK");
    }

    @GetMapping("/test_upload_managed_identity")
    public ResponseEntity<String> exampleUploadFileManagedIdentity() {
        uploadFileInputPort.uploadFileManagedIdentity("script01.txt", "./some-path/");
        return ResponseEntity.ok("OK");
    }

    @GetMapping("/kv")
    public ResponseEntity<String> keyVaultExample() {
        String tenantId = System.getenv("AZURE_TENANT_ID");
        String clientId = System.getenv("AZURE_CLIENT_ID");
        String clientSecret = System.getenv("AZURE_CLIENT_SECRET");

        var clientSecretCredential = new ClientSecretCredentialBuilder()
                .clientId(clientId)
                .clientSecret(clientSecret)
                .tenantId(tenantId)
                .build();

        KeyClient keyClient = new KeyClientBuilder()
                .vaultUrl("https://drovedakv.vault.azure.net/")
                .credential(clientSecretCredential)
                .buildClient();

        var key = keyClient.getKey("appkey");

        LOGGER.info(key.getName());

        var plainText = "This is a text that I want to encrypt";

        //Encrypt
        CryptographyClient cryptoClient = keyClient.getCryptographyClient(key.getName());
        EncryptResult encryptResult = cryptoClient.encrypt(EncryptionAlgorithm.RSA_OAEP, plainText.getBytes());
        LOGGER.info("My encrypted value: {}", new String(encryptResult.getCipherText()));

        //Decrypt
        DecryptResult decryptionResult = cryptoClient.decrypt(EncryptionAlgorithm.RSA_OAEP, encryptResult.getCipherText());
        LOGGER.info("My decrypted value: {}", new String(decryptionResult.getPlainText()));

        //Example of getting a secret
        String secretName = "dbpassword";

        SecretClient secretClient = new SecretClientBuilder()
                .vaultUrl("https://drovedakv.vault.azure.net/")
                .credential(clientSecretCredential)
                .buildClient();

        KeyVaultSecret secret = secretClient.getSecret(secretName);
        LOGGER.info(secret.getValue());

        return ResponseEntity.ok("OK");
    }


}
