package com.study.springboot.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import com.study.springboot.repository.MemberRepository;

//JUnit5에서는 @RunWith가 @ExtendWith로 변경
@ActiveProfiles(value = {"test"})
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;
    
    @Test
    @Transactional //JUnit 테스트 후 트랜잭션 후 록백
//    @Rollback(false) //이거 붙이면 롤백 안됨
    void testMember() {
        Member member = new Member();
        member.setUserName("memberA");
        
        Long saveId = memberRepository.save(member);
        Member findMember = memberRepository.findOne(saveId);
        
        assertEquals(saveId, findMember.getId());
        assertEquals(member.getId(), findMember.getId());
        assertEquals(member.getUserName(), findMember.getUserName());
        assertEquals(member, findMember);
    }

}
