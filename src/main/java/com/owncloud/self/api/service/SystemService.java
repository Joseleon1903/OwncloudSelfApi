package com.owncloud.self.api.service;

import org.springframework.web.multipart.MultipartFile;

public interface SystemService {

    void uploadSystem(MultipartFile archivo, String carpetaDestino) throws Exception;


}
