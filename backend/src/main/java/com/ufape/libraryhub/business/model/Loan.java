package com.ufape.libraryhub.business.model;

import com.ufape.libraryhub.exception.LoanAlreadyReturnedException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "loan")
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(optional = false)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @Column(nullable = false)
    private LocalDate loanDate;

    @Column(nullable = false)
    private LocalDate dueDate;

    private LocalDate returnDate;

    protected Loan() {
    }

    public Loan(Member member, Item item, LocalDate loanDate) {
        this.member = member;
        this.item = item;
        this.loanDate = loanDate;
        this.dueDate = loanDate.plusDays(item.getLoanDurationInDays());
    }

    public void finish(LocalDate date) throws LoanAlreadyReturnedException {
        if (returnDate != null) {
            throw new LoanAlreadyReturnedException(id);
        }
        returnDate = date;
        item.giveBack();
    }

    public boolean isActive() {
        return returnDate == null;
    }

    public boolean isOverdue(LocalDate reference) {
        return isActive() && countDaysLate(reference) > 0;
    }

    public long countDaysLate(LocalDate reference) {
        return reference.isAfter(dueDate) ? ChronoUnit.DAYS.between(dueDate, reference) : 0;
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public Item getItem() {
        return item;
    }

    public LocalDate getLoanDate() {
        return loanDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }
}
