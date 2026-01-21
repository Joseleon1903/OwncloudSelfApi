package com.owncloud.self.api.controller;

import com.owncloud.self.api.domain.Item;
import com.owncloud.self.api.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/items")
public class ItemsController {

    private final ItemService itemService;

    @Autowired
    public ItemsController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping(value = "/list")
    public ResponseEntity<List<Item>> getItemList() {

        System.out.println("entering getItemList: ");
            List<Item> lista = itemService.getAll();
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(lista);
    }

    @PostMapping
    public ResponseEntity<Item> saveItem(@RequestBody Item item) {
        System.out.println("entering saveItem: ");
        System.out.println("item: "+item);
        Item itemEntity = itemService.save(item);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(itemEntity);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Item> getItemById(@RequestParam("id") Long id) {

        System.out.println("entering getItemById: ");
        Item lista = itemService.getById(id);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(lista);
    }
}
