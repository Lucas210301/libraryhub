package com.ufape.libraryhub.communication.dto.response;

import java.time.LocalDate;

public record ReservationDTOResponse(

        Long id,

        MemberDTOResponse member,

        ItemDTOResponse item,

        LocalDate createdAt,

        String status,

        boolean active) {
}
