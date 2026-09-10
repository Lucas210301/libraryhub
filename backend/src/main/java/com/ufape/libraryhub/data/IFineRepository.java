package com.ufape.libraryhub.data;

import com.ufape.libraryhub.business.model.Fine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IFineRepository extends JpaRepository<Fine, Long> {

    boolean existsByLoan_Member_IdAndPaidFalse(Long memberId);

    @Query("select count(fine) from DamagedItemFine fine where fine.loan.id = :loanId")
    long countDamageFinesByLoan(@Param("loanId") Long loanId);

    void deleteByLoan_Member_Id(Long memberId);

    void deleteByLoan_Item_Id(Long itemId);
}
