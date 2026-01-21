package com.owncloud.self.api.service;

import com.owncloud.self.api.domain.Item;
import com.owncloud.self.api.repository.ItemRepository;
import com.owncloud.self.api.utils.ListCasting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemServiceImpl implements ItemService{

    private final ItemRepository itemRepository;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public List<Item> getAll() {
        Iterable<Item> iterable = itemRepository.findAll();
        return ListCasting.iterableToList(iterable);
    }

    @Override
    public Item getById(Long id) {
        return itemRepository.findById(id).get();
    }

    @Override
    public boolean save(List<Item> items) {
        return false;
    }

    @Override
    public Item save(Item item) {

        return itemRepository.save(item);
    }

    @Override
    public boolean deleteById(Long id) {
        itemRepository.deleteById(id);
        return true;
    }
}
