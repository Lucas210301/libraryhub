package com.ufape.libraryhub.communication.controller;

import com.ufape.libraryhub.business.facade.IFacade;
import com.ufape.libraryhub.business.model.Reservation;
import com.ufape.libraryhub.communication.converter.ReservationConverter;
import com.ufape.libraryhub.communication.dto.request.ReservationDTORequest;
import com.ufape.libraryhub.communication.dto.response.ErrorDTOResponse;
import com.ufape.libraryhub.communication.dto.response.ReservationDTOResponse;
import com.ufape.libraryhub.exception.DuplicatedReservationException;
import com.ufape.libraryhub.exception.ItemAvailableException;
import com.ufape.libraryhub.exception.ItemNotFoundException;
import com.ufape.libraryhub.exception.MemberNotFoundException;
import com.ufape.libraryhub.exception.ReservationNotActiveException;
import com.ufape.libraryhub.exception.ReservationNotFoundException;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final IFacade facade;

    private final ReservationConverter converter;

    public ReservationController(IFacade facade, ReservationConverter converter) {
        this.facade = facade;
        this.converter = converter;
    }

    @GetMapping
    public List<ReservationDTOResponse> list() {
        return converter.entityToResponseList(facade.listReservations());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> find(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(converter.entityToResponse(facade.findReservation(id)));
        } catch (ReservationNotFoundException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDTOResponse(exception.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<Object> create(@Valid @RequestBody ReservationDTORequest request) {
        try {
            Reservation reservation = facade.reserveItem(request.memberId(), request.itemId());
            return ResponseEntity.status(HttpStatus.CREATED).body(converter.entityToResponse(reservation));
        } catch (MemberNotFoundException | ItemNotFoundException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDTOResponse(exception.getMessage()));
        } catch (ItemAvailableException | DuplicatedReservationException exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorDTOResponse(exception.getMessage()));
        }
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<Object> cancel(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(converter.entityToResponse(facade.cancelReservation(id)));
        } catch (ReservationNotFoundException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDTOResponse(exception.getMessage()));
        } catch (ReservationNotActiveException exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorDTOResponse(exception.getMessage()));
        }
    }
}
