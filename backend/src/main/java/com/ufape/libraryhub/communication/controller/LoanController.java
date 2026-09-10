package com.ufape.libraryhub.communication.controller;

import com.ufape.libraryhub.business.facade.IFacade;
import com.ufape.libraryhub.business.model.Loan;
import com.ufape.libraryhub.communication.converter.LoanConverter;
import com.ufape.libraryhub.communication.dto.request.LoanDTORequest;
import com.ufape.libraryhub.communication.dto.response.ErrorDTOResponse;
import com.ufape.libraryhub.communication.dto.response.LoanDTOResponse;
import com.ufape.libraryhub.exception.ItemNotAvailableException;
import com.ufape.libraryhub.exception.ItemNotFoundException;
import com.ufape.libraryhub.exception.ItemReservedByAnotherMemberException;
import com.ufape.libraryhub.exception.LoanAlreadyReturnedException;
import com.ufape.libraryhub.exception.LoanLimitExceededException;
import com.ufape.libraryhub.exception.LoanNotFoundException;
import com.ufape.libraryhub.exception.MemberNotFoundException;
import com.ufape.libraryhub.exception.MemberWithUnpaidFinesException;
import com.ufape.libraryhub.exception.ReservationNotActiveException;
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
@RequestMapping("/loans")
public class LoanController {

    private final IFacade facade;

    private final LoanConverter converter;

    public LoanController(IFacade facade, LoanConverter converter) {
        this.facade = facade;
        this.converter = converter;
    }

    @GetMapping
    public List<LoanDTOResponse> list() {
        return converter.entityToResponseList(facade.listLoans());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> find(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(converter.entityToResponse(facade.findLoan(id)));
        } catch (LoanNotFoundException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDTOResponse(exception.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<Object> create(@Valid @RequestBody LoanDTORequest request) {
        try {
            Loan loan = facade.registerLoan(request.memberId(), request.itemId());
            return ResponseEntity.status(HttpStatus.CREATED).body(converter.entityToResponse(loan));
        } catch (MemberNotFoundException | ItemNotFoundException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDTOResponse(exception.getMessage()));
        } catch (ItemNotAvailableException | LoanLimitExceededException | MemberWithUnpaidFinesException
                | ItemReservedByAnotherMemberException | ReservationNotActiveException exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorDTOResponse(exception.getMessage()));
        }
    }

    @PutMapping("/{id}/return")
    public ResponseEntity<Object> returnLoan(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(converter.entityToResponse(facade.returnLoan(id)));
        } catch (LoanNotFoundException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDTOResponse(exception.getMessage()));
        } catch (LoanAlreadyReturnedException exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorDTOResponse(exception.getMessage()));
        }
    }
}
