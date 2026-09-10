package com.ufape.libraryhub.communication.converter;

import com.ufape.libraryhub.business.model.Member;
import com.ufape.libraryhub.communication.dto.request.MemberDTORequest;
import com.ufape.libraryhub.communication.dto.response.MemberDTOResponse;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class MemberConverter {

    public Member requestToEntity(MemberDTORequest request) {
        return new Member(request.name(), request.email());
    }

    public MemberDTOResponse entityToResponse(Member member) {
        return new MemberDTOResponse(member.getId(), member.getName(), member.getEmail());
    }

    public List<MemberDTOResponse> entityToResponseList(List<Member> members) {
        return members.stream().map(this::entityToResponse).toList();
    }
}
