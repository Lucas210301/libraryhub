package com.ufape.libraryhub.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ufape.libraryhub.business.facade.IFacade;
import com.ufape.libraryhub.business.model.Book;
import com.ufape.libraryhub.business.model.DamageSeverity;
import com.ufape.libraryhub.business.model.Fine;
import com.ufape.libraryhub.business.model.Item;
import com.ufape.libraryhub.business.model.Loan;
import com.ufape.libraryhub.business.model.Member;
import com.ufape.libraryhub.business.model.Reservation;
import com.ufape.libraryhub.business.model.ReservationStatus;
import com.ufape.libraryhub.exception.DuplicatedFineException;
import com.ufape.libraryhub.exception.DuplicatedReservationException;
import com.ufape.libraryhub.exception.ItemAvailableException;
import com.ufape.libraryhub.exception.ItemReservedByAnotherMemberException;
import com.ufape.libraryhub.exception.MemberWithUnpaidFinesException;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ReservationFineIntegrationTest {

    @Autowired
    private IFacade facade;

    @Test
    void reserveItemFailsWhenTheItemIsAvailable() throws Exception {
        Member member = member("Ana Souza", "ana.souza@ufape.edu.br");
        Item book = book("Clean Code", "9780132350884");

        assertThrows(ItemAvailableException.class, () -> facade.reserveItem(member.getId(), book.getId()));
    }

    @Test
    void reserveItemFailsWhenTheMemberAlreadyReservedTheSameItem() throws Exception {
        Member borrower = member("Bruno Lima", "bruno.lima@ufape.edu.br");
        Member reader = member("Carla Dias", "carla.dias@ufape.edu.br");
        Item book = book("Refactoring", "9780134757599");
        facade.registerLoan(borrower.getId(), book.getId());
        facade.reserveItem(reader.getId(), book.getId());

        assertThrows(DuplicatedReservationException.class,
                () -> facade.reserveItem(reader.getId(), book.getId()));
    }

    @Test
    void reservationQueueBlocksAnotherMemberAfterTheReturn() throws Exception {
        Member borrower = member("Diego Alves", "diego.alves@ufape.edu.br");
        Member reader = member("Elisa Rocha", "elisa.rocha@ufape.edu.br");
        Member outsider = member("Felipe Nunes", "felipe.nunes@ufape.edu.br");
        Item book = book("Domain Driven Design", "9780321125217");
        Loan loan = facade.registerLoan(borrower.getId(), book.getId());
        facade.reserveItem(reader.getId(), book.getId());
        facade.returnLoan(loan.getId());

        assertThrows(ItemReservedByAnotherMemberException.class,
                () -> facade.registerLoan(outsider.getId(), book.getId()));
    }

    @Test
    void loanFromTheMemberInTheQueueFulfillsTheReservation() throws Exception {
        Member borrower = member("Gabriel Silva", "gabriel.silva@ufape.edu.br");
        Member reader = member("Helena Costa", "helena.costa@ufape.edu.br");
        Item book = book("Working Effectively with Legacy Code", "9780131177055");
        Loan loan = facade.registerLoan(borrower.getId(), book.getId());
        Reservation reservation = facade.reserveItem(reader.getId(), book.getId());
        facade.returnLoan(loan.getId());

        facade.registerLoan(reader.getId(), book.getId());

        assertEquals(ReservationStatus.FULFILLED, facade.findReservation(reservation.getId()).getStatus());
    }

    @Test
    void cancelledReservationDoesNotBlockOtherMembers() throws Exception {
        Member borrower = member("Igor Ramos", "igor.ramos@ufape.edu.br");
        Member reader = member("Julia Prado", "julia.prado@ufape.edu.br");
        Member outsider = member("Karla Melo", "karla.melo@ufape.edu.br");
        Item book = book("The Pragmatic Programmer", "9780135957059");
        Loan loan = facade.registerLoan(borrower.getId(), book.getId());
        Reservation reservation = facade.reserveItem(reader.getId(), book.getId());
        facade.returnLoan(loan.getId());
        facade.cancelReservation(reservation.getId());

        Loan secondLoan = facade.registerLoan(outsider.getId(), book.getId());

        assertTrue(secondLoan.isActive());
    }

    @Test
    void unpaidFineBlocksNewLoansAndMemberRemoval() throws Exception {
        Member member = member("Lucas Freitas", "lucas.freitas@ufape.edu.br");
        Item book = book("Test Driven Development", "9780321146533");
        Item other = book("Extreme Programming Explained", "9780321278654");
        Loan loan = facade.registerLoan(member.getId(), book.getId());
        facade.returnLoan(loan.getId());
        facade.registerDamageFine(loan.getId(), DamageSeverity.SEVERE);

        assertThrows(MemberWithUnpaidFinesException.class,
                () -> facade.registerLoan(member.getId(), other.getId()));
        assertThrows(MemberWithUnpaidFinesException.class, () -> facade.removeMember(member.getId()));
    }

    @Test
    void payingTheFineReleasesTheMember() throws Exception {
        Member member = member("Marina Torres", "marina.torres@ufape.edu.br");
        Item book = book("Design Patterns", "9780201633610");
        Item other = book("Object Thinking", "9780735619654");
        Loan loan = facade.registerLoan(member.getId(), book.getId());
        facade.returnLoan(loan.getId());
        Fine fine = facade.registerDamageFine(loan.getId(), DamageSeverity.LIGHT);

        facade.payFine(fine.getId());
        Loan newLoan = facade.registerLoan(member.getId(), other.getId());

        assertTrue(facade.findFine(fine.getId()).isPaid());
        assertTrue(newLoan.isActive());
        assertEquals(BigDecimal.ZERO, facade.totalUnpaidFines());
    }

    @Test
    void damageFineCannotBeRegisteredTwiceForTheSameLoan() throws Exception {
        Member member = member("Nina Barros", "nina.barros@ufape.edu.br");
        Item book = book("Implementation Patterns", "9780321413093");
        Loan loan = facade.registerLoan(member.getId(), book.getId());
        facade.returnLoan(loan.getId());
        facade.registerDamageFine(loan.getId(), DamageSeverity.LIGHT);

        assertThrows(DuplicatedFineException.class,
                () -> facade.registerDamageFine(loan.getId(), DamageSeverity.SEVERE));
    }

    private Member member(String name, String email) throws Exception {
        return facade.registerMember(new Member(name, email));
    }

    private Item book(String title, String isbn) {
        return facade.registerItem(new Book(title, "Author", isbn));
    }
}
