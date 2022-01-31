package jpabook.jpashop.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;

@ActiveProfiles(value = {"test"})
@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
public class OrderServiceTest {

    @Autowired
    OrderService orderService;
    
    @PersistenceContext
    EntityManager em;
    
    @Autowired
    OrderRepository orderRepository;
    
    private Member member;
    private Book book;
    
    @BeforeEach
    private void 데이터_세팅() {
        member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울", "경기", "123-123"));
        em.persist(member);
        
        book = new Book();
        book.setName("새 책");
        book.setStockQuantity(10);
        book.setPrice(10000);
        em.persist(book);
    }
    
    @Test
    public void 상품주문() {
       
       int orderCount = 2;
       
       //when
       Long orderId = orderService.order(member.getId(), book.getId(), orderCount);
       
       //then
       Order getOrder = orderRepository.findOne(orderId);
       
       //상품 주문시 상태는 ORDER
       assertEquals(OrderStatus.ORDER, getOrder.getStatus());
       
       //주문한 상품 종류 수가 정확해야한다.
       assertEquals(1, getOrder.getOrderItems().size());
       
       //주문 가격은 가격 * 수량
       assertEquals(10000 * orderCount, getOrder.getTotalPrice());
       
       //주문 수량 만큼 재고가 줄어야 한다.
       assertEquals(8, book.getStockQuantity());
    }

    @Test 
    public void 상품주문_재고수량초과() {
        //given
        int orderCount = 11;
        
        //then
        //재고 수량 부족으로 인한 예외 발생
        assertThrows(NotEnoughStockException.class, () -> {
            orderService.order(member.getId(), book.getId(), orderCount);
        });
    }
    
    @Test
    public void 주문취소() {
        //given
        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);
        
        //when
        orderService.cancelOrder(orderId);
        
        //then
        //주문 취소시 상태는 CANCEL이다.
        Order getOrder = orderRepository.findOne(orderId);
        
        //주문 취소시 상태는 CANCEL이다.
        assertEquals(OrderStatus.CANCEL, getOrder.getStatus());
        
        //"주문이 취소된 상품은 그만큼 재고가 증가해야한다."
        assertEquals(10, book.getStockQuantity());
    }

}
