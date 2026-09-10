package com.ufape.libraryhub.business.facade;

import com.ufape.libraryhub.business.model.DamageSeverity;
import com.ufape.libraryhub.business.model.DamagedItemFine;
import com.ufape.libraryhub.business.model.Fine;
import com.ufape.libraryhub.business.model.Item;
import com.ufape.libraryhub.business.model.Loan;
import com.ufape.libraryhub.business.model.Member;
import com.ufape.libraryhub.business.model.OverdueFine;
import com.ufape.libraryhub.business.model.Reservation;
import com.ufape.libraryhub.business.service.IFineService;
import com.ufape.libraryhub.business.service.IItemService;
import com.ufape.libraryhub.business.service.ILoanService;
import com.ufape.libraryhub.business.service.IMemberService;
import com.ufape.libraryhub.business.service.IReservationService;
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
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class Facade implements IFacade {

    private static final int MAX_ACTIVE_LOANS_PER_MEMBER = 3;

    private final IMemberService memberService;

    private final IItemService itemService;

    private final ILoanService loanService;

    private final IReservationService reservationService;

    private final IFineService fineService;

    public Facade(IMemberService memberService, IItemService itemService, ILoanService loanService,
            IReservationService reservationService, IFineService fineService) {
        this.memberService = memberService;
        this.itemService = itemService;
        this.loanService = loanService;
        this.reservationService = reservationService;
        this.fineService = fineService;
    }

    @Override
    public Member registerMember(Member member) throws MemberAlreadyRegisteredException {
        return memberService.create(member);
    }

    @Override
    public Member updateMember(Long id, Member member) throws MemberNotFoundException, MemberAlreadyRegisteredException {
        return memberService.update(id, member);
    }

    @Override
    public Member findMember(Long id) throws MemberNotFoundException {
        return memberService.findById(id);
    }

    @Override
    public List<Member> listMembers() {
        return memberService.findAll();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeMember(Long id) throws MemberNotFoundException, MemberWithActiveLoansException,
            MemberWithUnpaidFinesException {
        Member member = memberService.findById(id);
        if (loanService.hasActiveLoansByMember(id)) {
            throw new MemberWithActiveLoansException(member.getName());
        }
        if (fineService.hasUnpaidFinesByMember(id)) {
            throw new MemberWithUnpaidFinesException(member.getName());
        }
        reservationService.deleteByMember(id);
        fineService.deleteByMember(id);
        loanService.deleteByMember(id);
        memberService.delete(id);
    }

    @Override
    public Item registerItem(Item item) {
        return itemService.create(item);
    }

    @Override
    public Item findItem(Long id) throws ItemNotFoundException {
        return itemService.findById(id);
    }

    @Override
    public List<Item> listItems() {
        return itemService.findAll();
    }

    @Override
    public List<Item> listAvailableItems() {
        return itemService.findAvailable();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeItem(Long id) throws ItemNotFoundException, ItemOnLoanException {
        reservationService.deleteByItem(id);
        fineService.deleteByItem(id);
        loanService.deleteByItem(id);
        itemService.delete(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Loan registerLoan(Long memberId, Long itemId) throws MemberNotFoundException, ItemNotFoundException,
            ItemNotAvailableException, LoanLimitExceededException, MemberWithUnpaidFinesException,
            ItemReservedByAnotherMemberException, ReservationNotActiveException {
        Member member = memberService.findById(memberId);
        Item item = itemService.findById(itemId);
        if (fineService.hasUnpaidFinesByMember(memberId)) {
            throw new MemberWithUnpaidFinesException(member.getName());
        }
        if (loanService.countActiveLoansByMember(memberId) >= MAX_ACTIVE_LOANS_PER_MEMBER) {
            throw new LoanLimitExceededException(member.getName(), MAX_ACTIVE_LOANS_PER_MEMBER);
        }
        fulfillReservationQueue(member, item);
        item.lend();
        itemService.update(item);
        return loanService.create(new Loan(member, item, LocalDate.now()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Loan returnLoan(Long loanId) throws LoanNotFoundException, LoanAlreadyReturnedException {
        Loan loan = loanService.findById(loanId);
        LocalDate today = LocalDate.now();
        long daysLate = loan.countDaysLate(today);
        loan.finish(today);
        itemService.update(loan.getItem());
        Loan returned = loanService.update(loan);
        if (daysLate > 0) {
            fineService.registerOverdue(new OverdueFine(returned, today, (int) daysLate));
        }
        return returned;
    }

    @Override
    public Loan findLoan(Long id) throws LoanNotFoundException {
        return loanService.findById(id);
    }

    @Override
    public List<Loan> listLoans() {
        return loanService.findAll();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Reservation reserveItem(Long memberId, Long itemId) throws MemberNotFoundException, ItemNotFoundException,
            ItemAvailableException, DuplicatedReservationException {
        Member member = memberService.findById(memberId);
        Item item = itemService.findById(itemId);
        if (item.isAvailable()) {
            throw new ItemAvailableException(item.getTitle());
        }
        return reservationService.create(new Reservation(member, item, LocalDate.now()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Reservation cancelReservation(Long id) throws ReservationNotFoundException, ReservationNotActiveException {
        Reservation reservation = reservationService.findById(id);
        reservation.cancel();
        return reservationService.update(reservation);
    }

    @Override
    public Reservation findReservation(Long id) throws ReservationNotFoundException {
        return reservationService.findById(id);
    }

    @Override
    public List<Reservation> listReservations() {
        return reservationService.findAll();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Fine registerDamageFine(Long loanId, DamageSeverity severity) throws LoanNotFoundException,
            DuplicatedFineException {
        Loan loan = loanService.findById(loanId);
        return fineService.registerDamage(new DamagedItemFine(loan, LocalDate.now(), severity));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Fine payFine(Long id) throws FineNotFoundException, FineAlreadyPaidException {
        Fine fine = fineService.findById(id);
        fine.pay(LocalDate.now());
        return fineService.update(fine);
    }

    @Override
    public Fine findFine(Long id) throws FineNotFoundException {
        return fineService.findById(id);
    }

    @Override
    public List<Fine> listFines() {
        return fineService.findAll();
    }

    @Override
    public BigDecimal totalUnpaidFines() {
        return fineService.totalUnpaid();
    }

    private void fulfillReservationQueue(Member member, Item item) throws ItemReservedByAnotherMemberException,
            ReservationNotActiveException {
        Optional<Reservation> next = reservationService.findNextActiveForItem(item.getId());
        if (next.isEmpty()) {
            return;
        }
        Reservation reservation = next.get();
        if (!reservation.belongsTo(member.getId())) {
            throw new ItemReservedByAnotherMemberException(item.getTitle());
        }
        reservation.fulfill();
        reservationService.update(reservation);
    }
}
