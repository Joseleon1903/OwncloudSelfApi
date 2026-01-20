package com.owncloud.self.api.controller;

import com.owncloud.self.api.service.OwnCloudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;

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

    @GetMapping(value = "/list")
    public ResponseEntity<List<String>> getRootArchivo(@RequestParam(value = "dir", defaultValue = "") String root) {

        System.out.println("root: "+ root);
        try {
            List<String> lista = ownCloudService.getFilesCloud(root);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(lista);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Collections.singletonList("{\"error\":\"Error al generar el JSON\"} - " + e.getMessage()));

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

    @GetMapping(value = "/view/image", produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public ResponseEntity<InputStreamResource> viewImage(@RequestParam(value = "root", defaultValue = "") String root) throws Exception {
        System.out.println("root: "+root);
        Resource in = ownCloudService.getFile(root);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType( MediaType.IMAGE_JPEG_VALUE))
                .body(new InputStreamResource(in.getInputStream()));
    }

}