package com.study.springboot.repository;

import java.time.LocalDateTime;

import com.study.springboot.domain.Address;
import com.study.springboot.domain.Order;
import com.study.springboot.domain.OrderStatus;

import lombok.Data;

@Data
public class OrderSimpleQueryDTO {
    private Long orderId;
    private String name;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private Address address;
    
    public OrderSimpleQueryDTO(Long orderId, 
                               String name, 
                               LocalDateTime orderDate, 
                               OrderStatus orderStatus, 
                               Address address) {
        this.orderId = orderId;
        this.name = name;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.address = address;
    }       
}
