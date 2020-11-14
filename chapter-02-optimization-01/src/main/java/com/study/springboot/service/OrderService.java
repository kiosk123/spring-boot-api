package com.study.springboot.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.study.springboot.domain.Delivery;
import com.study.springboot.domain.Item;
import com.study.springboot.domain.Member;
import com.study.springboot.domain.Order;
import com.study.springboot.domain.OrderItem;
import com.study.springboot.repository.ItemRepository;
import com.study.springboot.repository.MemberRepository;
import com.study.springboot.repository.OrderRepository;
import com.study.springboot.repository.OrderSearch;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {
    
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    
    //주문
    @Transactional
    public Long order(Long memberId, Long itemId, int count) {
        //엔티티 조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);
        
        //배송정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());
        
        //주문상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);
        
        //주문 생성
        Order order = Order.createOrder(member, delivery, orderItem);
        orderRepository.save(order);
        
        return order.getId();
    }
    
    //취소
    @Transactional
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findOne(orderId);
        order.cancel();
    }
    
    //검색
    public List<Order> findOrders(OrderSearch orderSearch) {
        return orderRepository.findAll(orderSearch);
    }
}
