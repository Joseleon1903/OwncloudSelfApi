package com.owncloud.self.api.service;

import com.owncloud.self.api.domain.Item;

import java.util.List;

public interface ItemService {


    List<Item> getAll();

    Item getById(Long id);

    boolean save(List<Item> items);

    Item save(Item item);

    boolean deleteById(Long id);

}
