package com.ufape.libraryhub.business.service;

import com.ufape.libraryhub.business.model.Reservation;
import com.ufape.libraryhub.business.model.ReservationStatus;
import com.ufape.libraryhub.data.IReservationRepository;
import com.ufape.libraryhub.exception.DuplicatedReservationException;
import com.ufape.libraryhub.exception.ReservationNotFoundException;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class ReservationService implements IReservationService {

    private final IReservationRepository repository;

    public ReservationService(IReservationRepository repository) {
        this.repository = repository;
    }

    @Override
    public Reservation create(Reservation reservation) throws DuplicatedReservationException {
        boolean duplicated = repository.existsByMember_IdAndItem_IdAndStatus(
                reservation.getMember().getId(), reservation.getItem().getId(), ReservationStatus.ACTIVE);
        if (duplicated) {
            throw new DuplicatedReservationException(
                    reservation.getMember().getName(), reservation.getItem().getTitle());
        }
        return repository.save(reservation);
    }

    @Override
    public Reservation update(Reservation reservation) {
        return repository.save(reservation);
    }

    @Override
    public Reservation findById(Long id) throws ReservationNotFoundException {
        return repository.findById(id).orElseThrow(() -> new ReservationNotFoundException(id));
    }

    @Override
    public List<Reservation> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Reservation> findNextActiveForItem(Long itemId) {
        return repository.findFirstByItem_IdAndStatusOrderByCreatedAtAsc(itemId, ReservationStatus.ACTIVE);
    }

    @Override
    public void deleteByMember(Long memberId) {
        repository.deleteByMember_Id(memberId);
    }

    @Override
    public void deleteByItem(Long itemId) {
        repository.deleteByItem_Id(itemId);
    }
}
