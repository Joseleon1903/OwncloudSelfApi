package com.owncloud.self.api.service;

import com.github.sardine.DavResource;
import com.github.sardine.Sardine;
import com.github.sardine.SardineFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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

        String fileName = archivo.getOriginalFilename();

        String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8);

        String urlArchivo = urlCarpeta +encodedFileName;
        System.out.println("urlArchivo: "+urlArchivo);
        try (InputStream is = archivo.getInputStream()) {
            sardine.put(urlArchivo, is);
        }
        // Verificar que el archivo se subió
        if (!sardine.exists(urlArchivo)) {
            throw new Exception("El archivo no se subió correctamente");
        }
    }

    @Override
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
        System.out.println("urlCarpeta: "+urlCarpeta);
        System.out.println("la ruta existe procediendo a buscar el listado de contenido");
        List<DavResource> listFile=  sardine.list(urlCarpeta);
        listFile.forEach( fl ->{
            rootlist.add(fl.getName()) ;
        });
        System.out.println("list size: "+ rootlist.size());
        return rootlist;
    }

    @Override
    public void deleteFileCloud(String urlFile) throws Exception {
        System.out.println("Entering deleteFileCloud");
        Sardine sardine = SardineFactory.begin(username, password);

        //validar si existe la ruta
        System.out.println("validar si existe la ruta");
        if (!sardine.exists(urlFile)) {
            System.out.println("carperta no existe..");
            System.out.println("rurlCarpeta: "+urlFile);
            throw new Exception("La ruta no existe");
        }
        System.out.println("intentando elimiar file : "+ urlFile);
        try{
            sardine.delete(urlFile);
        } catch (Exception e) {
            throw new Exception("Error al subir archivo: " + e.getMessage());
        }
        System.out.println("Existing deleteFileCloud..");
    }

    @Override
    public Resource getFile(String root) throws Exception {
        Sardine sardine = SardineFactory.begin(username, password);
        //validar si existe la ruta
        String urlarchivo = ownCloudUrl + root;
        System.out.println("validar si existe la ruta que se envia" );
//        if (!sardine.exists(root)) {
//            System.out.println("carperta no existe...");
//            System.out.println("rurlCarpeta : "+root);
//            throw new Exception("La ruta no existe.");
//        }
        System.out.println("urlarchivo: "+urlarchivo);
        InputStream inputStream =sardine.get(urlarchivo);
        return new InputStreamResource(inputStream);
    }

}
