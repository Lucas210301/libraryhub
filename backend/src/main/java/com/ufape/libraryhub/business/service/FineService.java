package com.ufape.libraryhub.business.service;

import com.ufape.libraryhub.business.model.DamagedItemFine;
import com.ufape.libraryhub.business.model.Fine;
import com.ufape.libraryhub.business.model.OverdueFine;
import com.ufape.libraryhub.data.IFineRepository;
import com.ufape.libraryhub.exception.DuplicatedFineException;
import com.ufape.libraryhub.exception.FineNotFoundException;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class FineService implements IFineService {

    private final IFineRepository repository;

    public FineService(IFineRepository repository) {
        this.repository = repository;
    }

    @Override
    public OverdueFine registerOverdue(OverdueFine fine) {
        return repository.save(fine);
    }

    @Override
    public DamagedItemFine registerDamage(DamagedItemFine fine) throws DuplicatedFineException {
        Long loanId = fine.getLoan().getId();
        if (repository.countDamageFinesByLoan(loanId) > 0) {
            throw new DuplicatedFineException(loanId);
        }
        return repository.save(fine);
    }

    @Override
    public Fine update(Fine fine) {
        return repository.save(fine);
    }

    @Override
    public Fine findById(Long id) throws FineNotFoundException {
        return repository.findById(id).orElseThrow(() -> new FineNotFoundException(id));
    }

    @Override
    public List<Fine> findAll() {
        return repository.findAll();
    }

    @Override
    public boolean hasUnpaidFinesByMember(Long memberId) {
        return repository.existsByLoan_Member_IdAndPaidFalse(memberId);
    }

    @Override
    public BigDecimal totalUnpaid() {
        return repository.findAll().stream()
                .filter(fine -> !fine.isPaid())
                .map(Fine::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public void deleteByMember(Long memberId) {
        repository.deleteByLoan_Member_Id(memberId);
    }

    @Override
    public void deleteByItem(Long itemId) {
        repository.deleteByLoan_Item_Id(itemId);
    }
}
