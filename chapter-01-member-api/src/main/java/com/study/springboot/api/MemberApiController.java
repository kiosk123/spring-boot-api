package com.study.springboot.api;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.study.springboot.domain.Member;
import com.study.springboot.service.MemberService;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MemberApiController {
    
    private final MemberService memberService;
    
    /**
     * v1은 api처리에 엔티티 사용
     * 엔티티가 변경되면 API스펙이 바뀌므로 아주 좋지 않은 방식!!! 사용하지 않을 것을 권장!!!!
     */
    @PostMapping("/api/v1/members")
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member) {
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }
    
    /**
     * v2는 엔티티가 변경되고 api스펙이 변경되지 않음 - 권장하는 방식!
     */
    @PostMapping("/api/v2/members")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request) {
        Member member = new Member(request.getUserName());
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }
    
    @Data
    static class CreateMemberRequest {
        @NotEmpty
        private String userName;
    }
    
    @Data
    static class CreateMemberResponse {
        @NonNull
        private Long id;
    }
}
