package com.study.springboot.repository;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import com.study.springboot.domain.Member;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor //final 필드를 기준으로 생성자를 만들어줌
public class MemberRepository {
    
    private final EntityManager em;
//    @PersistenceContext //spring-data-jpa사용하면 @Autowired 사용가능
//    private EntityManager em;

    /**
     * 저장된 엔티티를 반환할 수도 있지만 side effect 최소화를 위해
     * 저장된 엔티티의 PK값만 반환하여 다시 재조회하는 용도로 만든다
     */
    public Long save(Member member) {
        em.persist(member);
        return member.getId();
    }
    
    public Member findOne(Long id) {
        return em.find(Member.class, id);
    }
    
    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                 .getResultList();
    }
    
    public List<Member> findByUserName(String userName) {
        return em.createQuery("select m from Member m where m.userName = :userName", Member.class)
                 .setParameter("userName", userName)
                 .getResultList();
    }
}
