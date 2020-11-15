package com.study.springboot.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "ORDER_ITEM")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {
    @Id @GeneratedValue
    @Column(name = "ORDER_ITEM_ID")
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ITEM_ID")
    private Item item;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORDER_ID")
    private Order order;
    
    private Integer orderPrice;
    private Integer orderCount;
    
    //==생성 메서드==/
    public static OrderItem createOrderItem(Item item, int orderPrice, int orderCount) {
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setOrderCount(orderCount);
        
        item.removeStock(orderCount); //넘어온 만큼 아이템의 재고를 제거
        return orderItem;
    }
    
    //==비즈니스 로직==/
    public void cancel() {
       getItem().addStock(orderCount); //재고 수량을 원복
    }
    
    /**
     * 주문 상품 전체 가격 조회
     * @return
     */
    public Integer getTotalPrice() {
        return orderCount * orderPrice;
    }
}
