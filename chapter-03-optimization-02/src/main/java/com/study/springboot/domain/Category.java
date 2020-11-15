package com.study.springboot.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {
    @Id @GeneratedValue
    @Column(name = "CATEGORY_ID")
    private Long id;
    private String name;
    
    @ManyToMany
    @JoinTable(name = "CATEGORY_ITEM",
               joinColumns = {@JoinColumn(name = "CATEGORY_ID")},
               inverseJoinColumns = {@JoinColumn(name = "ITEM_ID")})
    private List<Item> items = new ArrayList<>();
    
    @ManyToOne
    @JoinColumn(name = "PARENT_ID")
    private Category parent;
    
    @OneToMany(mappedBy = "parent")
    private List<Category> children = new ArrayList<>();
}
