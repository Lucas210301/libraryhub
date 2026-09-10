package com.ufape.libraryhub.communication.dto.request;

import jakarta.validation.constraints.NotNull;

public record ReservationDTORequest(

        @NotNull(message = "Member is required")
        Long memberId,

        @NotNull(message = "Item is required")
        Long itemId) {
}
