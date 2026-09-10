package com.ufape.libraryhub.business.service;

import com.ufape.libraryhub.business.model.Member;
import com.ufape.libraryhub.exception.MemberAlreadyRegisteredException;
import com.ufape.libraryhub.exception.MemberNotFoundException;
import java.util.List;

public interface IMemberService {

    Member create(Member member) throws MemberAlreadyRegisteredException;

    Member update(Long id, Member member) throws MemberNotFoundException, MemberAlreadyRegisteredException;

    Member findById(Long id) throws MemberNotFoundException;

    List<Member> findAll();

    void delete(Long id) throws MemberNotFoundException;
}
