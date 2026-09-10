package com.ufape.libraryhub.business.service;

import com.ufape.libraryhub.business.model.Reservation;
import com.ufape.libraryhub.exception.DuplicatedReservationException;
import com.ufape.libraryhub.exception.ReservationNotFoundException;
import java.util.List;
import java.util.Optional;

public interface IReservationService {

    Reservation create(Reservation reservation) throws DuplicatedReservationException;

    Reservation update(Reservation reservation);

    Reservation findById(Long id) throws ReservationNotFoundException;

    List<Reservation> findAll();

    Optional<Reservation> findNextActiveForItem(Long itemId);

    void deleteByMember(Long memberId);

    void deleteByItem(Long itemId);
}
