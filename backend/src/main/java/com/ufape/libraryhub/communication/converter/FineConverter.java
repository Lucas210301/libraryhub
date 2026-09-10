package com.ufape.libraryhub.communication.converter;

import com.ufape.libraryhub.business.model.Fine;
import com.ufape.libraryhub.communication.dto.response.FineDTOResponse;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class FineConverter {

    private final LoanConverter loanConverter;

    public FineConverter(LoanConverter loanConverter) {
        this.loanConverter = loanConverter;
    }

    public FineDTOResponse entityToResponse(Fine fine) {
        return new FineDTOResponse(
                fine.getId(),
                loanConverter.entityToResponse(fine.getLoan()),
                fine.getType().name(),
                fine.getReason(),
                fine.getAmount(),
                fine.getIssuedAt(),
                fine.isPaid(),
                fine.getPaymentDate());
    }

    public List<FineDTOResponse> entityToResponseList(List<Fine> fines) {
        return fines.stream().map(this::entityToResponse).toList();
    }
}
