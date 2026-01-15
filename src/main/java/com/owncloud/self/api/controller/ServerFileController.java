package com.owncloud.self.api.controller;

import com.owncloud.self.api.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/server/file")
public class ServerFileController {

    private static final String UPLOAD_DIR = "/opt/uploads";


    private final SystemService systemService;

    @Autowired
    public ServerFileController(SystemService systemService) {
        this.systemService = systemService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file,
                                             @RequestParam(value = "folder", defaultValue = "") String folder) {
        System.out.println("FolderName: "+ folder);
        try {
            systemService.uploadSystem(file, folder);
            return ResponseEntity.ok("Archivo subido correctamente");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error al subir archivo: " + e.getMessage());
        }
    }
}
