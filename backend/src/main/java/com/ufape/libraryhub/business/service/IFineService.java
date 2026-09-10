package com.ufape.libraryhub.business.service;

import com.ufape.libraryhub.business.model.DamagedItemFine;
import com.ufape.libraryhub.business.model.Fine;
import com.ufape.libraryhub.business.model.OverdueFine;
import com.ufape.libraryhub.exception.DuplicatedFineException;
import com.ufape.libraryhub.exception.FineNotFoundException;
import java.math.BigDecimal;
import java.util.List;

public interface IFineService {

    OverdueFine registerOverdue(OverdueFine fine);

    DamagedItemFine registerDamage(DamagedItemFine fine) throws DuplicatedFineException;

    Fine update(Fine fine);

    Fine findById(Long id) throws FineNotFoundException;

    List<Fine> findAll();

    boolean hasUnpaidFinesByMember(Long memberId);

    BigDecimal totalUnpaid();

    void deleteByMember(Long memberId);

    void deleteByItem(Long itemId);
}
