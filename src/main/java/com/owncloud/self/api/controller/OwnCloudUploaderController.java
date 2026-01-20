package com.owncloud.self.api.controller;

import com.owncloud.self.api.service.OwnCloudService;
import com.owncloud.self.api.utils.LimitedInputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;
import java.io.InputStream;
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

    @GetMapping(value = "/view/jpg/image", produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public ResponseEntity<InputStreamResource> viewJpgImage(@RequestParam(value = "root", defaultValue = "") String root) throws Exception {
        System.out.println("root: "+root);
        Resource in = ownCloudService.getFile(root);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType( MediaType.IMAGE_JPEG_VALUE))
                .body(new InputStreamResource(in.getInputStream()));
    }

    @GetMapping(value = "/view/png/image", produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public ResponseEntity<InputStreamResource> viewPngImage(@RequestParam(value = "root", defaultValue = "") String root) throws Exception {
        System.out.println("root: "+root);
        Resource in = ownCloudService.getFile(root);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType( MediaType.IMAGE_PNG_VALUE))
                .body(new InputStreamResource(in.getInputStream()));
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> download(@RequestParam(value = "root", defaultValue = "") String root) throws Exception {
        System.out.println("root: "+root);
        Resource resource = ownCloudService.getFile(root);
        System.out.println("filename: "+resource.getFilename());

        StreamingResponseBody stream = outputStream -> {
            try (InputStream in = resource.getInputStream()) {
                in.transferTo(outputStream);
            }
        };

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename="+resource.getFilename())
                .build();
    }

    @GetMapping("/video")
    public ResponseEntity<Resource> streamVideo(@RequestParam(value = "root", defaultValue = "") String root,
                                                @RequestHeader HttpHeaders headers) throws Exception {

        Resource video = ownCloudService.getFile(root);
        long contentLength = video.contentLength();

        List<HttpRange> ranges = headers.getRange();

        if (ranges.isEmpty()) {
            return ResponseEntity.ok()
                    .contentType(MediaType.valueOf("video/mp4"))
                    .contentLength(contentLength)
                    .body(video);
        }

        HttpRange range = ranges.get(0);
        long start = range.getRangeStart(contentLength);
        long end = range.getRangeEnd(contentLength);
        long rangeLength = end - start + 1;

        InputStream inputStream = video.getInputStream();
        inputStream.skip(start);

        Resource region = new InputStreamResource(
                new LimitedInputStream(inputStream, rangeLength)
        );

        return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                .contentType(MediaType.valueOf("video/mp4"))
                .header(HttpHeaders.CONTENT_RANGE,
                        "bytes " + start + "-" + end + "/" + contentLength)
                .contentLength(rangeLength)
                .body(region);
    }


}