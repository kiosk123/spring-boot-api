package com.study.springboot.repository.query;

import java.time.LocalDateTime;

import com.study.springboot.domain.Address;
import com.study.springboot.domain.OrderStatus;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderFlatDTO {
    private Long orderId;
    private String name;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private Address address;
    
    // OrderItemr과 Item 관련 필드
    private String itemName;
    private int orderPrice;
    private int orderCount;
    
    
}
