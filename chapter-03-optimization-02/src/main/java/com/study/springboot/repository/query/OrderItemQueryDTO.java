package com.study.springboot.repository.query;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderItemQueryDTO {
    private Long orderId;
    private String itemNames;
    private int orderPrice;
    private int orderCount;
}
