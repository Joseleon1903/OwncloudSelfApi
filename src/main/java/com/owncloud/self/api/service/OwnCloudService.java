package com.owncloud.self.api.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;


public interface OwnCloudService {

    void uploadCloud(MultipartFile archivo, String carpetaDestino) throws Exception;

    List<String> getFilesCloud(String carpetaDestino) throws Exception;

    void deleteFileCloud(String carpetaDestino) throws Exception;

    Resource getFile(String root) throws Exception;
}
