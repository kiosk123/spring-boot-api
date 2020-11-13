package com.study.springboot.repository;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import com.study.springboot.domain.Item;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ItemRepository {
    
    private final EntityManager em;
    
    public Long save(Item item) {
        if (item.getId() == null) {
            em.persist(item);
        }
        else {
            //merge에 사용된 파라미터는 1차캐시에 들어가지 않음 사용하려면 반환된 값을 사용해야함
            em.merge(item);  //이것은 사용하지 말자...
        }
        return item.getId();
    }
    
    public Item findOne(Long id) {
        return em.find(Item.class, id);
    }
    
    public List<Item> findAll() {
        return em.createQuery("select i from Item i", Item.class)
                 .getResultList();
    }
}
