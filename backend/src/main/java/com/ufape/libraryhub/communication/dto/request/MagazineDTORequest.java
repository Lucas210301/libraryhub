package com.ufape.libraryhub.communication.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record MagazineDTORequest(

        @NotBlank(message = "Title is required")
        String title,

        @NotBlank(message = "Publisher is required")
        String publisher,

        @NotNull(message = "Edition is required")
        @Positive(message = "Edition must be greater than zero")
        Integer edition) {
}
