package com.study.springboot.repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.study.springboot.domain.Member;
import com.study.springboot.domain.Order;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em;

    public Long save(Order order) {
        em.persist(order);
        return order.getId();
    }

    public Order findOne(Long id) {
        return em.find(Order.class, id);
    }

    /**
     * 여기서는 Criteria로 처리했지만 안씀 -> QueryDSL을 쓴다
     */
    public List<Order> findAll(OrderSearch orderSearch) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Order> cq = cb.createQuery(Order.class);
        Root<Order> o = cq.from(Order.class);
        Join<Order, Member> m = o.join("member", JoinType.INNER); // 회원과 조인
        List<Predicate> criteria = new ArrayList<>();
        
        // 주문 상태 검색
        if (orderSearch.getOrderStatus() != null) {
            Predicate status = cb.equal(o.get("status"), orderSearch.getOrderStatus());
            criteria.add(status);
        }
        // 회원 이름 검색
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            Predicate name = cb.like(m.<String>get("name"), "%" + orderSearch.getMemberName() + "%");
            criteria.add(name);
        }
        cq.where(cb.and(criteria.toArray(new Predicate[criteria.size()])));
        TypedQuery<Order> query = em.createQuery(cq).setMaxResults(1000); // 최대 1000건

        return query.getResultList();
    }

    public List<Order> findAllWithMemberDelivery() {
        return em.createQuery("select o from Order o join fetch o.member join fetch o.delivery d", Order.class)
                 .getResultList();
    }

    /**
     * 
     * DTO 생성자 파라미터로 엔티티(입베디드 값타입은 제외)를 넘기는 것은 안된다.
     * 왜냐하면 엔티티 파라미터를 식별자를 인식하기 때문
     * 이렇게 사용할꺼면 쿼리 리포지토리 따로 만들어서 분리해서 사용할 것을 권장
     * 왜냐하면 리포지토리는 순수 엔티티 조회 용도로 기능하는 것이 가장 좋음..
     */
    public List<OrderSimpleQueryDTO> findOrderDTOs() {
        return em.createQuery("select new com.study.springboot.repository.OrderSimpleQueryDTO(o.id, m.userName, o.orderDate, o.status, d.address) "
                            + "from Order o join o.member m join o.delivery d",
                            OrderSimpleQueryDTO.class)
                 .getResultList();
    }

    /**
     * 페치 조인을 적용한 Order 정보 가져오기
     * 컬렉션(orderItems)과 조인되기 때문에 Order가 orderItem의 갯수만큼 생성 - 일대다 페치조인에서 발생하는 문제
     * 그래서 distict 키워드로 해결한다.
     * 일대다 페치조인은 페이징 불가능!!!.
     * 컬렉션 페치조인은 1개만 사용하자 -> 2개 이상이면 데이터 뻥튀기 및 정합성 문제 발생!!!
     * 한꺼번에 조회되는 데이터량이 엄청 많음 !!!
     */
	public List<Order> findAllWithItem() {
        return em.createQuery("select distinct o from Order o " +
                              "join fetch o.member " +
                              "join fetch o.delivery d "+
                              "join fetch o.orderItems oi " +
                              "join fetch oi.item i", Order.class)
                 .getResultList();
	}
	
	/**
	 * 컬렉션 데이터를 페이징 하기 위한 방법
	 * XToOne 관계는 모두 페치 조인한다.
	 * 컬렉션은 지연 로딩으로 조회한다.
	 * 지연 로딩 성능 최적화를 위해 @BatchSize를 적용한다. 이옵션을 적용하면 컬렉션이나 프록시 객체를 한번에 설정한 
	 * size만큼 In 쿼리로 조회한다.
	 */
	public List<Order> findAllWithItemUsingPaging() {
        return em.createQuery("select distinct o from Order o " +
                              "join fetch o.member " +    //member와 delivery까지만 페치조인
                              "join fetch o.delivery d ", //item은 ToOne관계지만 orderItems를 통해서 나오기 때문에 일반조인
                               Order.class)
                 .setFirstResult(0)
                 .setMaxResults(100)
                 .getResultList();
    }
}
