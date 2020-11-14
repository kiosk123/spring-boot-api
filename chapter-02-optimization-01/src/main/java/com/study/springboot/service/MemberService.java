package com.study.springboot.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.study.springboot.domain.Member;
import com.study.springboot.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true) //읽기만 처리시 readOnly = true시 성능 최적화
@RequiredArgsConstructor //final이 있는 필드만 가지고 생성자를 만든다.
public class MemberService {
    
    //final로 설정할 것을 추천
    private final MemberRepository memberRepository;
    
//    @Autowired //stub을 이용해서 테스트 할 수도 있기 때문에...
//    public MemberService (MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }
    
    //회원가입
    @Transactional
    public Long join(Member member) {
        validateDuplicationMember(member);
        return memberRepository.save(member);
    }
    
    //회원 전체조회
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }
    
    public Member findOne(Long id) {
        return memberRepository.findOne(id);
    }
    
    @Transactional //Member를 반환할 수도 있지만 Command와 Query를 철저하게 분리하자
    public void update(Long id, String userName) {
        Member member = memberRepository.findOne(id);
        member.setUserName(userName);
    }
    
    //중복회원 검증
    private void validateDuplicationMember(Member member) {
        //멀티스레드 환경에서 문제 될 수 읶기때문에 데이터베이스 userName에 unique 제약조건을 권장함
        List<Member> findMembers = memberRepository.findByUserName(member.getUserName());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다");
        }
    }
}
