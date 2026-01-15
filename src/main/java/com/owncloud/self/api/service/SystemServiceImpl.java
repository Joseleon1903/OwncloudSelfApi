package com.owncloud.self.api.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class SystemServiceImpl implements SystemService {

    @Value("${upload.server.dir}")
    private String serverDir;

    @Override
    public void uploadSystem(MultipartFile file, String carpetaDestino) throws Exception {

        try {
            if (file.isEmpty()) {
                throw new Exception("Archivo vacío");
            }

            // Crear directorio si no existe
            Path uploadPath = Paths.get(serverDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Ruta final del archivo
            Path filePath = uploadPath.resolve(file.getOriginalFilename());

            // Guardar archivo
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            throw new Exception("Archivo subido correctamente");

        } catch (IOException e) {
            throw new Exception("Error al guardar archivo: " + e.getMessage());
        }
    }
}
