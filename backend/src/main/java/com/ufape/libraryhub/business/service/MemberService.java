package com.ufape.libraryhub.business.service;

import com.ufape.libraryhub.business.model.Member;
import com.ufape.libraryhub.data.IMemberRepository;
import com.ufape.libraryhub.exception.MemberAlreadyRegisteredException;
import com.ufape.libraryhub.exception.MemberNotFoundException;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class MemberService implements IMemberService {

    private final IMemberRepository repository;

    public MemberService(IMemberRepository repository) {
        this.repository = repository;
    }

    @Override
    public Member create(Member member) throws MemberAlreadyRegisteredException {
        if (repository.existsByEmail(member.getEmail())) {
            throw new MemberAlreadyRegisteredException(member.getEmail());
        }
        return repository.save(member);
    }

    @Override
    public Member update(Long id, Member member) throws MemberNotFoundException, MemberAlreadyRegisteredException {
        Member stored = findById(id);
        if (repository.existsByEmailAndIdNot(member.getEmail(), id)) {
            throw new MemberAlreadyRegisteredException(member.getEmail());
        }
        stored.setName(member.getName());
        stored.setEmail(member.getEmail());
        return repository.save(stored);
    }

    @Override
    public Member findById(Long id) throws MemberNotFoundException {
        return repository.findById(id).orElseThrow(() -> new MemberNotFoundException(id));
    }

    @Override
    public List<Member> findAll() {
        return repository.findAll();
    }

    @Override
    public void delete(Long id) throws MemberNotFoundException {
        repository.delete(findById(id));
    }
}
