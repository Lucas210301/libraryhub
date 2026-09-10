package com.ufape.libraryhub.communication.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public record FineDTOResponse(

        Long id,

        LoanDTOResponse loan,

        String type,

        String reason,

        BigDecimal amount,

        LocalDate issuedAt,

        boolean paid,

        LocalDate paymentDate) {
}
