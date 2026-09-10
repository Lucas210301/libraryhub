package com.ufape.libraryhub.business.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@DiscriminatorValue("DAMAGE")
public class DamagedItemFine extends Fine {

    @Enumerated(EnumType.STRING)
    @Column(name = "severity")
    private DamageSeverity severity;

    protected DamagedItemFine() {
        super();
    }

    public DamagedItemFine(Loan loan, LocalDate issuedAt, DamageSeverity severity) {
        super(loan, issuedAt);
        this.severity = severity;
    }

    @Override
    public FineType getType() {
        return FineType.DAMAGE;
    }

    @Override
    public BigDecimal getAmount() {
        return severity.getAmount();
    }

    @Override
    public String getReason() {
        return "Item returned with " + severity.name().toLowerCase() + " damage";
    }

    public DamageSeverity getSeverity() {
        return severity;
    }
}
