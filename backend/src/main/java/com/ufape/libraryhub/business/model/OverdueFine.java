package com.ufape.libraryhub.business.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@DiscriminatorValue("OVERDUE")
public class OverdueFine extends Fine {

    private static final BigDecimal DAILY_RATE = new BigDecimal("1.50");

    @Column(name = "days_late")
    private Integer daysLate;

    protected OverdueFine() {
        super();
    }

    public OverdueFine(Loan loan, LocalDate issuedAt, Integer daysLate) {
        super(loan, issuedAt);
        this.daysLate = daysLate;
    }

    @Override
    public FineType getType() {
        return FineType.OVERDUE;
    }

    @Override
    public BigDecimal getAmount() {
        return DAILY_RATE.multiply(BigDecimal.valueOf(daysLate));
    }

    @Override
    public String getReason() {
        return "Returned " + daysLate + " days after the due date";
    }

    public Integer getDaysLate() {
        return daysLate;
    }
}
