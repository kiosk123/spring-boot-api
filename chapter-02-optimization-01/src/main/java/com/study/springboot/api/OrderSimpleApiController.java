package com.study.springboot.api;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.study.springboot.domain.Order;
import com.study.springboot.repository.OrderRepository;
import com.study.springboot.repository.OrderSearch;

import lombok.RequiredArgsConstructor;

/**
 * xToOne(ManyToOne, OneToOne)
 * OrderToMember
 * Order -> Member
 * Order -> Delivery
 */
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {
    
    private final OrderRepository orderRepository;
    
    /**
     * Order 엔티티 직접 반환 v1
     * xToOne으로 양방향 연관관계 매핑되어 있는 엔티티 자체를 JSON으로 만들어서 보낼경우 
     * JSON 만드는 과정에서 무한루프 발생
     * 양방향 연관관계 무한루프 방지를 위해 한쪽필드는 @JsonIgnore를 필드에 붙여야하는 문제 발생
     * 또한 Lazy로딩 설정이 된 경우 로딩이 되어 있지 않으면 하이버네이트 프록시객체가 대신하기 때문에 문제 발생
     */
    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1() {
        List<Order> all = orderRepository.findAll(new OrderSearch());
        return all;
    }
}
