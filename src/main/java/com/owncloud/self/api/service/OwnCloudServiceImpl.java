package com.owncloud.self.api.service;

import com.github.sardine.DavResource;
import com.github.sardine.Sardine;
import com.github.sardine.SardineFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class OwnCloudServiceImpl implements OwnCloudService {

    @Value("${owncloud.url}")
    private String ownCloudUrl;

    @Value("${owncloud.username}")
    private String username;

    @Value("${owncloud.password}")
    private String password;

    public void uploadCloud(MultipartFile archivo, String carpetaDestino) throws Exception {
        Sardine sardine = SardineFactory.begin(username, password);
        System.out.println("validando ruta..");
        // Crear carpeta si no existe
        String urlCarpeta = ownCloudUrl + carpetaDestino + "/";
        if (!sardine.exists(urlCarpeta)) {
            System.out.println("carperta no existe..");
            System.out.println("rurlCarpeta: "+urlCarpeta);
            try{
                sardine.createDirectory(urlCarpeta);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println( "Error al subir archivo: " + e.getMessage());
            }
        }
        System.out.println("ruta valida creando archivo..");
        // URL completa del archivo
        String urlArchivo = urlCarpeta + archivo.getOriginalFilename();
        System.out.println("urlArchivo: "+urlArchivo);
        try (InputStream is = archivo.getInputStream()) {
            sardine.put(urlArchivo, is);
        }
        // Verificar que el archivo se subió
        if (!sardine.exists(urlArchivo)) {
            throw new Exception("El archivo no se subió correctamente");
        }
    }

    public List<String> getFilesCloud(String carpetaDestino) throws Exception {

        Sardine sardine = SardineFactory.begin(username, password);
        List<String> rootlist = new ArrayList<>();
        //validar si existe la ruta
        System.out.println("validar si existe la ruta");
        String urlCarpeta = ownCloudUrl + carpetaDestino + "/";
        if (!sardine.exists(urlCarpeta)) {
            System.out.println("carperta no existe..");
            System.out.println("rurlCarpeta: "+urlCarpeta);
            throw new Exception("La ruta no existe");
        }

        System.out.println("la ruta existe procediendo a buscar el listado de contenido");
        List<DavResource> listFile=  sardine.list(carpetaDestino);
        listFile.forEach( fl ->{
            rootlist.add(fl.getName()) ;
        });
        System.out.println("list size: "+ rootlist.size());
        return rootlist;
    }

}
