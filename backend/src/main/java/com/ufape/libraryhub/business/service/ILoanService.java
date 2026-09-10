package com.ufape.libraryhub.business.service;

import com.ufape.libraryhub.business.model.Loan;
import com.ufape.libraryhub.exception.LoanNotFoundException;
import java.util.List;

public interface ILoanService {

    Loan create(Loan loan);

    Loan update(Loan loan);

    Loan findById(Long id) throws LoanNotFoundException;

    List<Loan> findAll();

    long countActiveLoansByMember(Long memberId);

    boolean hasActiveLoansByMember(Long memberId);

    void deleteByMember(Long memberId);

    void deleteByItem(Long itemId);
}
