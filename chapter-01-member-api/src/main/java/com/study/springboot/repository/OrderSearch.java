package com.study.springboot.repository;

import com.study.springboot.domain.OrderStatus;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OrderSearch {
    
    private String memberName;
    private OrderStatus orderStatus;
}
