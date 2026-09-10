package com.ufape.libraryhub.business.facade;

import com.ufape.libraryhub.business.model.DamageSeverity;
import com.ufape.libraryhub.business.model.Fine;
import com.ufape.libraryhub.business.model.Item;
import com.ufape.libraryhub.business.model.Loan;
import com.ufape.libraryhub.business.model.Member;
import com.ufape.libraryhub.business.model.Reservation;
import com.ufape.libraryhub.exception.DuplicatedFineException;
import com.ufape.libraryhub.exception.DuplicatedReservationException;
import com.ufape.libraryhub.exception.FineAlreadyPaidException;
import com.ufape.libraryhub.exception.FineNotFoundException;
import com.ufape.libraryhub.exception.ItemAvailableException;
import com.ufape.libraryhub.exception.ItemNotAvailableException;
import com.ufape.libraryhub.exception.ItemNotFoundException;
import com.ufape.libraryhub.exception.ItemOnLoanException;
import com.ufape.libraryhub.exception.ItemReservedByAnotherMemberException;
import com.ufape.libraryhub.exception.LoanAlreadyReturnedException;
import com.ufape.libraryhub.exception.LoanLimitExceededException;
import com.ufape.libraryhub.exception.LoanNotFoundException;
import com.ufape.libraryhub.exception.MemberAlreadyRegisteredException;
import com.ufape.libraryhub.exception.MemberNotFoundException;
import com.ufape.libraryhub.exception.MemberWithActiveLoansException;
import com.ufape.libraryhub.exception.MemberWithUnpaidFinesException;
import com.ufape.libraryhub.exception.ReservationNotActiveException;
import com.ufape.libraryhub.exception.ReservationNotFoundException;
import java.math.BigDecimal;
import java.util.List;

public interface IFacade {

    Member registerMember(Member member) throws MemberAlreadyRegisteredException;

    Member updateMember(Long id, Member member) throws MemberNotFoundException, MemberAlreadyRegisteredException;

    Member findMember(Long id) throws MemberNotFoundException;

    List<Member> listMembers();

    void removeMember(Long id) throws MemberNotFoundException, MemberWithActiveLoansException,
            MemberWithUnpaidFinesException;

    Item registerItem(Item item);

    Item findItem(Long id) throws ItemNotFoundException;

    List<Item> listItems();

    List<Item> listAvailableItems();

    void removeItem(Long id) throws ItemNotFoundException, ItemOnLoanException;

    Loan registerLoan(Long memberId, Long itemId) throws MemberNotFoundException, ItemNotFoundException,
            ItemNotAvailableException, LoanLimitExceededException, MemberWithUnpaidFinesException,
            ItemReservedByAnotherMemberException, ReservationNotActiveException;

    Loan returnLoan(Long loanId) throws LoanNotFoundException, LoanAlreadyReturnedException;

    Loan findLoan(Long id) throws LoanNotFoundException;

    List<Loan> listLoans();

    Reservation reserveItem(Long memberId, Long itemId) throws MemberNotFoundException, ItemNotFoundException,
            ItemAvailableException, DuplicatedReservationException;

    Reservation cancelReservation(Long id) throws ReservationNotFoundException, ReservationNotActiveException;

    Reservation findReservation(Long id) throws ReservationNotFoundException;

    List<Reservation> listReservations();

    Fine registerDamageFine(Long loanId, DamageSeverity severity) throws LoanNotFoundException,
            DuplicatedFineException;

    Fine payFine(Long id) throws FineNotFoundException, FineAlreadyPaidException;

    Fine findFine(Long id) throws FineNotFoundException;

    List<Fine> listFines();

    BigDecimal totalUnpaidFines();
}
