package com.study.springboot.api;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.study.springboot.api.MemberApiController.MemberDTO;
import com.study.springboot.domain.Member;
import com.study.springboot.service.MemberService;

import lombok.AllArgsConstructor;
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
    
    @GetMapping("/api/v1/members")
    public List<Member> findMembersV1() {
        return memberService.findMembers();
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
    
    @GetMapping("/api/v2/members")
    public Result<List<MemberDTO>> findMembersV2() {
        List<Member> findMembers = memberService.findMembers();
        List<MemberDTO> collect = findMembers.stream()
                                                .map(member -> new MemberDTO(member.getUserName()))
                                                .collect(Collectors.toList());
        return new Result<List<MemberDTO>>(collect);
    }
    
    @PutMapping("/api/v2/members/{id}")
    public UpdateMemberResponse modifyMemberV2(@PathVariable("id") Long id, 
                                               @RequestBody @Valid UpdateMemberRequest request) {
        memberService.update(id, request.getUserName());
        Member findMember = memberService.findOne(id);
        return new UpdateMemberResponse(findMember.getId(), findMember.getUserName());
    }
    
    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }
    
    @Data
    @AllArgsConstructor
    static class MemberDTO {
        private String name;
    }
    
    @Data
    static class UpdateMemberRequest {
        @NotEmpty
        private String userName;
    }
    
    @Data
    static class UpdateMemberResponse {
        @NonNull
        private Long id;
        @NonNull
        private String name;
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
