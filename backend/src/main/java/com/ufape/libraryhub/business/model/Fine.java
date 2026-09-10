package com.ufape.libraryhub.business.model;

import com.ufape.libraryhub.exception.FineAlreadyPaidException;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "fine")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "fine_type")
public abstract class Fine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "loan_id", nullable = false)
    private Loan loan;

    @Column(nullable = false)
    private LocalDate issuedAt;

    @Column(nullable = false)
    private boolean paid = false;

    private LocalDate paymentDate;

    protected Fine() {
    }

    protected Fine(Loan loan, LocalDate issuedAt) {
        this.loan = loan;
        this.issuedAt = issuedAt;
        this.paid = false;
    }

    public abstract FineType getType();

    public abstract BigDecimal getAmount();

    public abstract String getReason();

    public void pay(LocalDate date) throws FineAlreadyPaidException {
        if (paid) {
            throw new FineAlreadyPaidException(id);
        }
        paid = true;
        paymentDate = date;
    }

    public Long getId() {
        return id;
    }

    public Loan getLoan() {
        return loan;
    }

    public LocalDate getIssuedAt() {
        return issuedAt;
    }

    public boolean isPaid() {
        return paid;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }
}
