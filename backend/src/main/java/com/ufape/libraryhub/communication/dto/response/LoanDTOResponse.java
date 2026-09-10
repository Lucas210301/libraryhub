package com.ufape.libraryhub.communication.dto.response;

import java.time.LocalDate;

public record LoanDTOResponse(

        Long id,

        MemberDTOResponse member,

        ItemDTOResponse item,

        LocalDate loanDate,

        LocalDate dueDate,

        LocalDate returnDate,

        boolean active,

        boolean overdue) {
}
