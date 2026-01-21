package com.owncloud.self.api.mapper;

import com.owncloud.self.api.domain.Item;
import com.owncloud.self.api.utils.LimitedInputStream;
import org.springframework.web.multipart.MultipartFile;

public class ItemMapper {

    public static Item parceitem(MultipartFile archivo, String carpetaDestino, String url){
        Item item= new Item();

        item.setName(archivo.getOriginalFilename());
        item.setRoot(carpetaDestino);
        item.setType(archivo.getContentType());
        item.setSize(LimitedInputStream.longToSizeString(archivo.getSize()));
        item.setUrl(url);
        return item;
    }


}
