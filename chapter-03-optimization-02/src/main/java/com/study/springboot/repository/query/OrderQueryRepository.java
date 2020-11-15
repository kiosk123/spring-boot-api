package com.study.springboot.repository.query;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {
    
    private final EntityManager em;
    
    /**
     * DTO를 이용한 컬렉션 조회 Order -> OrderItems
     * N + 1문제 발생 - 주문 아이디로 루프 돌면서 가져오기 때문
     */
    public List<OrderQueryDTO> findOrderQueryDTOs() {
        List<OrderQueryDTO> result = findOrders();
        result.forEach(o -> o.setOrderItems(findOrderItems(o.getOrderId())));
        return result;
    }
    
    private List<OrderQueryDTO> findOrders() {
        return em.createQuery("select new com.study.springboot.repository.query.OrderQueryDTO(o.id, m.userName, o.orderDate, o.status, d.address) from Order o " +
                               "join o.member m " +
                               "join o.delivery d", OrderQueryDTO.class)
                 .getResultList();
    }
    
    private List<OrderItemQueryDTO> findOrderItems(Long orderId) {
        return em.createQuery("select new com.study.springboot.repository.query.OrderItemQueryDTO(oi.order.id, i.name, oi.orderPrice, oi.orderCount) from OrderItem oi " + 
                               "join oi.item i " + 
                               "where oi.order.id = :orderId", 
                               OrderItemQueryDTO.class)
                 .setParameter("orderId", orderId)
                 .getResultList();
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * N + 1문제 해결
     * batchsize 옵션이 in절을 이용한 쿼리최적화이기 때문에 
     * order id값만 따로 추출하여 in절에 세팅해서 값을 한꺼번에 가져온다
     * 쿼리 두번으로 데이터 조회 !!! 성능 최적화!!!
     */
    public List<OrderQueryDTO> findAllOrderQueryDTOsByOptimization() {
        List<OrderQueryDTO> result = findOrders();
        List<Long> orderIds = result.stream().map(o -> o.getOrderId()).collect(Collectors.toList());
        
        Map<Long, List<OrderItemQueryDTO>> orderIteMap = findAllOrderItemQueryDTOsByOptimization(orderIds);
        result.forEach(o -> o.setOrderItems(orderIteMap.get(o.getOrderId())));
        return result;
    }
    
    private Map<Long, List<OrderItemQueryDTO>> findAllOrderItemQueryDTOsByOptimization(List<Long> orderIds) {
        List<OrderItemQueryDTO> orderItems = em.createQuery("select new com.study.springboot.repository.query.OrderItemQueryDTO(oi.order.id, i.name, oi.orderPrice, oi.orderCount) from OrderItem oi " + 
                                                            "join oi.item i " + 
                                                            "where oi.order.id in :orderIds", 
                                                            OrderItemQueryDTO.class)
                                                .setParameter("orderIds", orderIds)
                                                .getResultList();
        //List<OrderItemQueryDTO>를 주문 아이디로 그룹화 하여 Map으로 반환 - key는 OrderId 값은 주문아이디에 대한 OrderItemQueryDTO
        Map<Long, List<OrderItemQueryDTO>> orderIteMap = orderItems.stream()
                                                                   .collect(Collectors.groupingBy(oi -> {
                                                                                return oi.getOrderId();
                                                                           }));
        return orderIteMap;
    }
    
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // 쿼리 1번으로 최적화
    // 반면에 데이터베이스의 로우 한줄을 DTO와 매핑하여 뿌리는 것이기 때문에 Order정보는 중복된다.

    public List<OrderFlatDTO> findOrderQueryDTOsInFlat() {
        return em.createQuery("select new com.study.springboot.repository.query.OrderFlatDTO(o.id, m.userName, o.orderDate, o.status, d.address, i.name, oi.orderPrice, oi.orderCount)" +
                                "from Order o join " +
                                "o.member m join " +
                                "o.delivery d join " +
                                "o.orderItems oi join " +
                                "oi.item i",
                                OrderFlatDTO.class)
                 .getResultList();
    }
}
