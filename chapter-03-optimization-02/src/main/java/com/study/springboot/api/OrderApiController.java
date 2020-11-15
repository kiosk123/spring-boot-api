package com.study.springboot.api;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.study.springboot.domain.Address;
import com.study.springboot.domain.Order;
import com.study.springboot.domain.OrderItem;
import com.study.springboot.domain.OrderStatus;
import com.study.springboot.repository.OrderRepository;
import com.study.springboot.repository.OrderSearch;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * OneTox(OneToMany)
 * Order -> OrderItem
 * Item -> OrderItem
 */
@RestController
@RequiredArgsConstructor
public class OrderApiController {
    
    private final OrderRepository orderRepository;
    
    /**
     * 엔티티 직접 노출
     * lazy 모드 설정된 필드를 루프돌면서 강제 초기화함
     * 여기서는 제대로 동작안함 양방향 참조로 인한 무한루프 @JsonIgnore 설정 필요...
     */
    @GetMapping("/api/v1/orders")
    public List<Order> ordersV1() {
        List<Order> all = null;
//        all = orderRepository.findAll(new OrderSearch());
//        all.forEach(o -> {
//                    o.getMember().getUserName();
//                    o.getDelivery().getAddress();
//                    o.getOrderItems().forEach(i -> {
//                        i.getItem().getName();
//                        });
//                    });
        return all;
    }
    
    /**
     * v1에 비해 개선되었지만 lazy 로딩으로 인한 성능이슈가 있음!!!
     */
    @GetMapping("/api/v2/orders")
    public List<OrderDTO> orderV2() {
        List<Order> orders = orderRepository.findAll(new OrderSearch());
        return orders.stream().map(o -> new OrderDTO(o))
                     .collect(Collectors.toList());
        
    }
    
    @GetMapping("/api/v3/orders")
    public List<OrderDTO> orderV3() {
        List<Order> orders = orderRepository.findAllWithItem(); 
        return null;
    }
    

    @Getter
    static class OrderDTO {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;
        private List<OrderItemDTO> orderItems;
//        private List<OrderItem> orderItems;
        
        public OrderDTO(Order order) {
            orderId = order.getId();
            name = order.getMember().getUserName();
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress();
            orderItems = order.getOrderItems()
                              .stream()
                              .map(OrderItemDTO::new)
                              .collect(Collectors.toList());
            
//            // 엔티티를 DTO로 랩핑하는 것도 허용하면 안됨 !!!!
//            // 이것은 잘못된 예제 !!!!
//            //orderItem 프록시 초기화
//            order.getOrderItems().forEach(i -> i.getItem()); 
//            orderItems = order.getOrderItems();
        }
    }
    // OrderItem -> OrderItemDTO
    @Getter
    static class OrderItemDTO {
        private String itemName;
        private int orderPrice;
        private int count;
        
        public OrderItemDTO(OrderItem orderItem) {
            itemName = orderItem.getItem().getName();
            orderPrice = orderItem.getOrderPrice();
            count = orderItem.getOrderCount();
        }
    }
}

