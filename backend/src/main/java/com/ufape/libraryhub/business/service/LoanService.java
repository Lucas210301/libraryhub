package com.ufape.libraryhub.business.service;

import com.ufape.libraryhub.business.model.Loan;
import com.ufape.libraryhub.data.ILoanRepository;
import com.ufape.libraryhub.exception.LoanNotFoundException;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class LoanService implements ILoanService {

    private final ILoanRepository repository;

    public LoanService(ILoanRepository repository) {
        this.repository = repository;
    }

    @Override
    public Loan create(Loan loan) {
        return repository.save(loan);
    }

    @Override
    public Loan update(Loan loan) {
        return repository.save(loan);
    }

    @Override
    public Loan findById(Long id) throws LoanNotFoundException {
        return repository.findById(id).orElseThrow(() -> new LoanNotFoundException(id));
    }

    @Override
    public List<Loan> findAll() {
        return repository.findAll();
    }

    @Override
    public long countActiveLoansByMember(Long memberId) {
        return repository.countByMemberIdAndReturnDateIsNull(memberId);
    }

    @Override
    public boolean hasActiveLoansByMember(Long memberId) {
        return repository.existsByMemberIdAndReturnDateIsNull(memberId);
    }

    @Override
    public void deleteByMember(Long memberId) {
        repository.deleteByMemberId(memberId);
    }

    @Override
    public void deleteByItem(Long itemId) {
        repository.deleteByItemId(itemId);
    }
}
