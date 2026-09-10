package com.ufape.libraryhub.communication.dto.request;

import com.ufape.libraryhub.business.model.DamageSeverity;
import jakarta.validation.constraints.NotNull;

public record DamageFineDTORequest(

        @NotNull(message = "Loan is required")
        Long loanId,

        @NotNull(message = "Severity is required")
        DamageSeverity severity) {
}
