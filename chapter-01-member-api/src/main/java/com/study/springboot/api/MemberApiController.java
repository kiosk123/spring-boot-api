package com.study.springboot.api;

import javax.validation.Valid;

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
    
    @PostMapping("/api/v1/members")
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member) {
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }
    
    @Data
    @RequiredArgsConstructor
    static class CreateMemberResponse {
        @NonNull
        private Long id;
    }
}
