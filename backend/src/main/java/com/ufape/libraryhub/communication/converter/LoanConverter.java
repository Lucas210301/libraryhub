package com.ufape.libraryhub.communication.converter;

import com.ufape.libraryhub.business.model.Loan;
import com.ufape.libraryhub.communication.dto.response.LoanDTOResponse;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class LoanConverter {

    private final MemberConverter memberConverter;

    private final ItemConverter itemConverter;

    public LoanConverter(MemberConverter memberConverter, ItemConverter itemConverter) {
        this.memberConverter = memberConverter;
        this.itemConverter = itemConverter;
    }

    public LoanDTOResponse entityToResponse(Loan loan) {
        return new LoanDTOResponse(
                loan.getId(),
                memberConverter.entityToResponse(loan.getMember()),
                itemConverter.entityToResponse(loan.getItem()),
                loan.getLoanDate(),
                loan.getDueDate(),
                loan.getReturnDate(),
                loan.isActive(),
                loan.isOverdue(LocalDate.now()));
    }

    public List<LoanDTOResponse> entityToResponseList(List<Loan> loans) {
        return loans.stream().map(this::entityToResponse).toList();
    }
}
