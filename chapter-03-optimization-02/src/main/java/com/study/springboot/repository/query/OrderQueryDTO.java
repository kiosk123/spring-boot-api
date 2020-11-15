package com.study.springboot.repository.query;

import java.time.LocalDateTime;
import java.util.List;

import com.study.springboot.domain.Address;
import com.study.springboot.domain.OrderStatus;

import lombok.Data;

@Data
public class OrderQueryDTO {
    private Long orderId;
    private String name;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private Address address;
    private List<OrderItemQueryDTO> orderItems;
    
    public OrderQueryDTO(Long orderId, String name, LocalDateTime orderDate, OrderStatus orderStatus, Address address) {
        this.orderId = orderId;
        this.name = name;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.address = address;
    }
    
}
