package com.ufape.libraryhub.data;

import com.ufape.libraryhub.business.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ILoanRepository extends JpaRepository<Loan, Long> {

    long countByMemberIdAndReturnDateIsNull(Long memberId);

    boolean existsByMemberIdAndReturnDateIsNull(Long memberId);

    void deleteByMemberId(Long memberId);

    void deleteByItemId(Long itemId);
}
