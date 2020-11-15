package com.study.springboot.repository.query;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {
    
    private final EntityManager em;
    
    /**
     * DTO를 이용한 컬렉션 조회
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
}
