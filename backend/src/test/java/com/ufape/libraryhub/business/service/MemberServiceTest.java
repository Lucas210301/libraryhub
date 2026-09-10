package com.ufape.libraryhub.business.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ufape.libraryhub.business.model.Member;
import com.ufape.libraryhub.data.IMemberRepository;
import com.ufape.libraryhub.exception.MemberAlreadyRegisteredException;
import com.ufape.libraryhub.exception.MemberNotFoundException;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    private static final String EMAIL = "ana.souza@ufape.edu.br";

    @Mock
    private IMemberRepository repository;

    @InjectMocks
    private MemberService service;

    @Test
    void createSavesMemberWhenEmailIsFree() throws MemberAlreadyRegisteredException {
        Member member = new Member("Ana Souza", EMAIL);
        when(repository.existsByEmail(EMAIL)).thenReturn(false);
        when(repository.save(member)).thenReturn(member);

        Member created = service.create(member);

        assertEquals("Ana Souza", created.getName());
        verify(repository).save(member);
    }

    @Test
    void createThrowsWhenEmailIsAlreadyRegistered() {
        Member member = new Member("Ana Souza", EMAIL);
        when(repository.existsByEmail(EMAIL)).thenReturn(true);

        assertThrows(MemberAlreadyRegisteredException.class, () -> service.create(member));
        verify(repository, never()).save(any(Member.class));
    }

    @Test
    void findByIdThrowsWhenMemberDoesNotExist() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(MemberNotFoundException.class, () -> service.findById(99L));
    }
}
