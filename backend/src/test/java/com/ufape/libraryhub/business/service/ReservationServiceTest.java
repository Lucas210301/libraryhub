package com.ufape.libraryhub.business.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ufape.libraryhub.business.model.Book;
import com.ufape.libraryhub.business.model.Item;
import com.ufape.libraryhub.business.model.Member;
import com.ufape.libraryhub.business.model.Reservation;
import com.ufape.libraryhub.business.model.ReservationStatus;
import com.ufape.libraryhub.data.IReservationRepository;
import com.ufape.libraryhub.exception.DuplicatedReservationException;
import com.ufape.libraryhub.exception.ReservationNotFoundException;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private IReservationRepository repository;

    @InjectMocks
    private ReservationService service;

    @Test
    void createSavesReservationWhenThereIsNoActiveOne() throws DuplicatedReservationException {
        Reservation reservation = reservation();
        when(repository.existsByMember_IdAndItem_IdAndStatus(any(), any(), eq(ReservationStatus.ACTIVE)))
                .thenReturn(false);
        when(repository.save(reservation)).thenReturn(reservation);

        Reservation created = service.create(reservation);

        assertEquals(ReservationStatus.ACTIVE, created.getStatus());
        verify(repository).save(reservation);
    }

    @Test
    void createThrowsWhenTheMemberAlreadyReservedTheItem() {
        Reservation reservation = reservation();
        when(repository.existsByMember_IdAndItem_IdAndStatus(any(), any(), eq(ReservationStatus.ACTIVE)))
                .thenReturn(true);

        assertThrows(DuplicatedReservationException.class, () -> service.create(reservation));
        verify(repository, never()).save(any(Reservation.class));
    }

    @Test
    void findByIdThrowsWhenReservationDoesNotExist() {
        when(repository.findById(77L)).thenReturn(Optional.empty());

        assertThrows(ReservationNotFoundException.class, () -> service.findById(77L));
    }

    private Reservation reservation() {
        Member member = new Member("Ana Souza", "ana.souza@ufape.edu.br");
        Item item = new Book("Clean Code", "Robert Martin", "9780132350884");
        return new Reservation(member, item, LocalDate.of(2026, 9, 8));
    }
}
