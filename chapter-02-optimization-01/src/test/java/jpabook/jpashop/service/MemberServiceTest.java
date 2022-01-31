package jpabook.jpashop.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.transaction.annotation.Transactional;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;

@ActiveProfiles(value = {"test"})
@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
public class MemberServiceTest {

    @Autowired
    MemberRepository memberRepository;
    MemberService memberService;
    
    @BeforeTransaction
    public void setUpMemberService() {
        memberService = new MemberService(memberRepository);
    }
    
    @Test
    public void memberJoinTest() {
        //given
        Member member = new Member();
        member.setName("kim");
        
        //when
        Long saveId = memberService.join(member);
        
        //then
        assertEquals(member, memberService.findOne(saveId));
    }

    @Test
    public void memberDuplicationJoinTest() {
        //given
        Member member = new Member();
        member.setName("kim");
        Member duplicatedMember = new Member();
        duplicatedMember.setName("kim");
                
        //when
        memberService.join(member);
        
        //then
        assertThrows(IllegalStateException.class, () -> {
            memberService.join(duplicatedMember);
        });
       
    }
}
