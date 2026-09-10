package com.ufape.libraryhub.data;

import com.ufape.libraryhub.business.model.Reservation;
import com.ufape.libraryhub.business.model.ReservationStatus;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IReservationRepository extends JpaRepository<Reservation, Long> {

    boolean existsByMember_IdAndItem_IdAndStatus(Long memberId, Long itemId, ReservationStatus status);

    Optional<Reservation> findFirstByItem_IdAndStatusOrderByCreatedAtAsc(Long itemId, ReservationStatus status);

    void deleteByMember_Id(Long memberId);

    void deleteByItem_Id(Long itemId);
}
