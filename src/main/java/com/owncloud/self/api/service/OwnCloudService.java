package com.owncloud.self.api.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface OwnCloudService {

    void uploadCloud(MultipartFile archivo, String carpetaDestino) throws Exception;

    List<String> getFilesCloud(String carpetaDestino) throws Exception;
}
