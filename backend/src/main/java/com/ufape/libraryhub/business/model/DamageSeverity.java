package com.ufape.libraryhub.business.model;

import java.math.BigDecimal;

public enum DamageSeverity {

    LIGHT(new BigDecimal("25.00")),
    SEVERE(new BigDecimal("60.00"));

    private final BigDecimal amount;

    DamageSeverity(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
