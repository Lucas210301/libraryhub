package com.ufape.libraryhub.communication.controller;

import com.ufape.libraryhub.business.facade.IFacade;
import com.ufape.libraryhub.business.model.Fine;
import com.ufape.libraryhub.communication.converter.FineConverter;
import com.ufape.libraryhub.communication.dto.request.DamageFineDTORequest;
import com.ufape.libraryhub.communication.dto.response.ErrorDTOResponse;
import com.ufape.libraryhub.communication.dto.response.FineDTOResponse;
import com.ufape.libraryhub.communication.dto.response.FineSummaryDTOResponse;
import com.ufape.libraryhub.exception.DuplicatedFineException;
import com.ufape.libraryhub.exception.FineAlreadyPaidException;
import com.ufape.libraryhub.exception.FineNotFoundException;
import com.ufape.libraryhub.exception.LoanNotFoundException;
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
@RequestMapping("/fines")
public class FineController {

    private final IFacade facade;

    private final FineConverter converter;

    public FineController(IFacade facade, FineConverter converter) {
        this.facade = facade;
        this.converter = converter;
    }

    @GetMapping
    public List<FineDTOResponse> list() {
        return converter.entityToResponseList(facade.listFines());
    }

    @GetMapping("/summary")
    public FineSummaryDTOResponse summary() {
        return new FineSummaryDTOResponse(facade.totalUnpaidFines());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> find(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(converter.entityToResponse(facade.findFine(id)));
        } catch (FineNotFoundException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDTOResponse(exception.getMessage()));
        }
    }

    @PostMapping("/damages")
    public ResponseEntity<Object> registerDamage(@Valid @RequestBody DamageFineDTORequest request) {
        try {
            Fine fine = facade.registerDamageFine(request.loanId(), request.severity());
            return ResponseEntity.status(HttpStatus.CREATED).body(converter.entityToResponse(fine));
        } catch (LoanNotFoundException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDTOResponse(exception.getMessage()));
        } catch (DuplicatedFineException exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorDTOResponse(exception.getMessage()));
        }
    }

    @PutMapping("/{id}/pay")
    public ResponseEntity<Object> pay(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(converter.entityToResponse(facade.payFine(id)));
        } catch (FineNotFoundException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDTOResponse(exception.getMessage()));
        } catch (FineAlreadyPaidException exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorDTOResponse(exception.getMessage()));
        }
    }
}
