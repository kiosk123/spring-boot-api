package com.study.springboot.service;



import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.study.springboot.domain.Book;
import com.study.springboot.domain.Item;
import com.study.springboot.repository.ItemRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {
    
    private final ItemRepository itemRepository;
    
    @Transactional
    public Long saveItem(Item item) {
        return itemRepository.save(item);
    }
    
    @Transactional
    public void updateItem(Long itemId, Book param) {
        Item findItem = itemRepository.findOne(itemId);
        findItem.setPrice(param.getPrice());
        findItem.setName(param.getName());
        findItem.setStockQuantity(param.getStockQuantity());
    }
    
    public Item findOne(Long id) {
        return itemRepository.findOne(id);
    }
    
    public List<Item> findItems() {
        return itemRepository.findAll();
    }
    
}
