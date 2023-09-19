package com.yourink.member.service;

import com.yourink.domain.member.Member;
import com.yourink.dto.api.ErrorCode;
import com.yourink.dto.member.MemberResponse;
import com.yourink.exception.DuplicatedResourceException;
import com.yourink.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberResponse createMember(String email) {
        if (memberRepository.existsByEmail(email)) {
            throw new DuplicatedResourceException(ErrorCode.DUPLICATED_RESOURCE.getMessage(), ErrorCode.DUPLICATED_RESOURCE.getCode(), ErrorCode.DUPLICATED_RESOURCE.getStatus());
        }

        var savedMember = memberRepository.save(Member.create(email));

        return new MemberResponse(savedMember.getId(), savedMember.getEmail());
    }
}
