package com.owncloud.self.api.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;


public interface OwnCloudService {

    void uploadCloud(MultipartFile archivo, String carpetaDestino) throws Exception;

    List<String> getFilesCloud(String carpetaDestino) throws Exception;

    void deleteFileCloud(String carpetaDestino) throws Exception;

    InputStream getFile(String root) throws Exception;
}
