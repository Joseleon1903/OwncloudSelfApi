package com.owncloud.self.api.controller;

import com.owncloud.self.api.service.OwnCloudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/owncloud/file")
public class OwnCloudUploaderController {

    private final OwnCloudService ownCloudService;

    @Autowired
    public OwnCloudUploaderController(OwnCloudService ownCloudService) {
        this.ownCloudService = ownCloudService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> subirArchivo(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "folder", defaultValue = "") String folder) {

        System.out.println("FolderName: "+ folder);
        try {
            ownCloudService.uploadCloud(file, folder);
            return ResponseEntity.ok("Archivo subido correctamente");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error al subir archivo: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> eliminarArchivo(@RequestParam(value = "root", defaultValue = "") String root) {
        System.out.println("FolderName: "+ root);
        try {
            ownCloudService.deleteFileCloud(root);
            return ResponseEntity.ok("Archivo eliminado correctamente");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error al eliminar archivo: " + e.getMessage());
        }
    }

}
