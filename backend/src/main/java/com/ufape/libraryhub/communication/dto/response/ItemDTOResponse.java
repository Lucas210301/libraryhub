package com.ufape.libraryhub.communication.dto.response;

public record ItemDTOResponse(

        Long id,

        String title,

        String type,

        String description,

        boolean available,

        int loanDurationInDays) {
}
