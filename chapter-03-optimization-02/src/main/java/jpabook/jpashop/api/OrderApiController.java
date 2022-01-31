package com.study.springboot.api;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.study.springboot.domain.Address;
import com.study.springboot.domain.Order;
import com.study.springboot.domain.OrderItem;
import com.study.springboot.domain.OrderStatus;
import com.study.springboot.repository.OrderRepository;
import com.study.springboot.repository.OrderSearch;
import com.study.springboot.repository.query.OrderFlatDTO;
import com.study.springboot.repository.query.OrderItemQueryDTO;
import com.study.springboot.repository.query.OrderQueryDTO;
import com.study.springboot.repository.query.OrderQueryRepository;

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
    private final OrderQueryRepository orderQueryRepository;
    
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
    /**
     * 페치조인을 이용한 데이터 가져오기
     */
    @GetMapping("/api/v3/orders")
    public List<OrderDTO> orderV3() {
        return orderRepository.findAllWithItem()
                              .stream()
                              .map(OrderDTO::new)
                              .collect(Collectors.toList());
    }
    
    /**
     * default_batch_fetch_size 값 설정을 통한 쿼리 최적화 후 실행
     */
    @GetMapping("/api/v3.1/orders")
    public List<OrderDTO> orderV3_1() {
        return orderRepository.findAllWithItemUsingPaging()
                              .stream()
                              .map(OrderDTO::new)
                              .collect(Collectors.toList());
    }
    
    /**
     * DTO를 이용한 컬렉션 조회 - N + 1문제 발생
     */
    @GetMapping("/api/v4/orders")
    public List<OrderQueryDTO> ordersV4() {
        return orderQueryRepository.findOrderQueryDTOs();
    }
    
    /**
     * DTO를 이용한 컬렉션 조회 - 최적화 쿼리 두번으로 처리
     */
    @GetMapping("/api/v5/orders")
    public List<OrderQueryDTO> ordersV5() {
        return orderQueryRepository.findAllOrderQueryDTOsByOptimization();
    }
    
    /**
     * DTO를 이용한 컬렉션 조회 - 쿼리 한번으로 처리
     */
    @GetMapping("/api/v6/orders")
    public List<OrderFlatDTO> ordersV6() {
        return orderQueryRepository.findOrderQueryDTOsInFlat();
    }
    
    /**
     * v6버전을 v5 api 스펙에 맞게 변환
     */
    @GetMapping("/api/v6.1/orders")
    public List<OrderQueryDTO> ordersV6_1() {
        return orderQueryRepository.findOrderQueryDTOsInFlat()
                                   .stream() //orderId, name, orderDate, orderStatus, address
                                   .collect(Collectors.groupingBy(o -> new OrderQueryDTO(o.getOrderId(), 
                                                                                         o.getName(), 
                                                                                         o.getOrderDate(), 
                                                                                         o.getOrderStatus(), 
                                                                                         o.getAddress()),
                                            
                                            Collectors.mapping(o -> new OrderItemQueryDTO(o.getOrderId(),
                                                                                          o.getItemName(),
                                                                                          o.getOrderPrice(),
                                                                                          o.getOrderPrice()), 
                                            Collectors.toList())))
                                   .entrySet()
                                   .stream()
                                   .map(e -> { 
                                       OrderQueryDTO o = e.getKey();
                                       o.setOrderItems(e.getValue());
                                       return o;
                                   })
                                   .collect(Collectors.toList());
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

