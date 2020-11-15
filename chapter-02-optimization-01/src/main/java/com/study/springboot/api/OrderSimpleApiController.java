package com.study.springboot.api;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.study.springboot.domain.Address;
import com.study.springboot.domain.Order;
import com.study.springboot.domain.OrderStatus;
import com.study.springboot.repository.OrderRepository;
import com.study.springboot.repository.OrderSearch;
import com.study.springboot.repository.OrderSimpleQueryDTO;

import lombok.Data;
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
    
    /**
     * Order엔티티를 OrderDTO로 변환하여 반환하는 api
     * v1보다 개선되었지만 리스트로 반환함 실제로는 리스트로 반환하지 말 것을 추천함!!
     * 그리고 레이지 로딩으로 인하여 엔티티 필드의 메소드 호출될때마다 호출됨
     * 
     * 참고로 orderRepository에 직접 접근이고 findAll 메서드에는 @Transactional
     * 안붙어 있음 즉 호출하는 즉시 영속성 컨텍스트 시작임...
     */
    @GetMapping("/api/v2/simple-orders")
    public List<SimpleOrderDTO> orderV2() {
        List<Order> orders = orderRepository.findAll(new OrderSearch());
        List<SimpleOrderDTO> result = orders.stream()
                                            .map(SimpleOrderDTO::new)
                                            .collect(Collectors.toList());
        return result;
    }
    
    /**
     * v2에 페치조인으로 최적화해서 성능을 높임
     */
    @GetMapping("/api/v3/simple-orders")
    public List<SimpleOrderDTO> orderV3() {
        List<Order> orders = orderRepository.findAllWithMemberDelivery();
        return orders.stream()
                     .map(SimpleOrderDTO::new)
                     .collect(Collectors.toList());
    }
    /**
     * v3에서 페치조인 대신에 일반조인 사용하고 여기서 필요한 값만 바로 DTO에 넣어서
     * 데이터 추출
     */
    @GetMapping("/api/v4/simple-orders")
    public List<OrderSimpleQueryDTO> orderV4() {
        return orderRepository.findOrderDTOs();
    }
    
    @Data
    static class SimpleOrderDTO {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;
        
        public SimpleOrderDTO(Order order) {
            orderId = order.getId();
            name = order.getMember().getUserName();
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress();
        }
    }
}
