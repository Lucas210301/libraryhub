package com.ufape.libraryhub.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ufape.libraryhub.business.facade.IFacade;
import com.ufape.libraryhub.business.model.Book;
import com.ufape.libraryhub.business.model.Item;
import com.ufape.libraryhub.business.model.Loan;
import com.ufape.libraryhub.business.model.Magazine;
import com.ufape.libraryhub.business.model.Member;
import com.ufape.libraryhub.exception.LoanLimitExceededException;
import com.ufape.libraryhub.exception.MemberAlreadyRegisteredException;
import com.ufape.libraryhub.exception.MemberWithActiveLoansException;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class FacadeIntegrationTest {

    @Autowired
    private IFacade facade;

    @Test
    void registerLoanStoresDueDateFromItemPolicy() throws Exception {
        Member member = facade.registerMember(new Member("Ana Souza", "ana.souza@ufape.edu.br"));
        Item book = facade.registerItem(new Book("Clean Code", "Robert Martin", "9780132350884"));
        Item magazine = facade.registerItem(new Magazine("Superinteressante", "Abril", 450));

        Loan bookLoan = facade.registerLoan(member.getId(), book.getId());
        Loan magazineLoan = facade.registerLoan(member.getId(), magazine.getId());

        assertEquals(LocalDate.now().plusDays(15), bookLoan.getDueDate());
        assertEquals(LocalDate.now().plusDays(7), magazineLoan.getDueDate());
        assertFalse(facade.findItem(book.getId()).isAvailable());
    }

    @Test
    void registerLoanFailsWhenMemberReachesActiveLoanLimit() throws Exception {
        Member member = facade.registerMember(new Member("Bruno Lima", "bruno.lima@ufape.edu.br"));
        for (int index = 1; index <= 3; index++) {
            Item item = facade.registerItem(new Book("Book " + index, "Author " + index, "ISBN" + index));
            facade.registerLoan(member.getId(), item.getId());
        }
        Item extraItem = facade.registerItem(new Book("Book 4", "Author 4", "ISBN4"));

        assertThrows(LoanLimitExceededException.class,
                () -> facade.registerLoan(member.getId(), extraItem.getId()));
    }

    @Test
    void returnLoanReleasesItemForANewLoan() throws Exception {
        Member member = facade.registerMember(new Member("Carla Dias", "carla.dias@ufape.edu.br"));
        Item book = facade.registerItem(new Book("Refactoring", "Martin Fowler", "9780134757599"));
        Loan loan = facade.registerLoan(member.getId(), book.getId());

        Loan returned = facade.returnLoan(loan.getId());

        assertFalse(returned.isActive());
        assertEquals(LocalDate.now(), returned.getReturnDate());
        assertTrue(facade.findItem(book.getId()).isAvailable());
    }

    @Test
    void removeMemberFailsWhileThereAreActiveLoans() throws Exception {
        Member member = facade.registerMember(new Member("Diego Alves", "diego.alves@ufape.edu.br"));
        Item book = facade.registerItem(new Book("Domain Driven Design", "Eric Evans", "9780321125217"));
        facade.registerLoan(member.getId(), book.getId());

        assertThrows(MemberWithActiveLoansException.class, () -> facade.removeMember(member.getId()));
    }

    @Test
    void registerMemberFailsWithDuplicatedEmail() throws Exception {
        facade.registerMember(new Member("Elisa Rocha", "elisa.rocha@ufape.edu.br"));

        assertThrows(MemberAlreadyRegisteredException.class,
                () -> facade.registerMember(new Member("Elisa R.", "elisa.rocha@ufape.edu.br")));
    }
}
