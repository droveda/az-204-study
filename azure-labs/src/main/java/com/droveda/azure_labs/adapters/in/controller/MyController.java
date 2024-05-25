package com.droveda.azure_labs.adapters.in.controller;

import com.droveda.azure_labs.application.ports.in.UploadFileInputPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class MyController {

    @Autowired
    private UploadFileInputPort uploadFileInputPort;

    @GetMapping("/test")
    public ResponseEntity<String> firstExample() {

        uploadFileInputPort.uploadFile("script01.txt", "./some-path/");

        return ResponseEntity.ok("OK");
    }

}
