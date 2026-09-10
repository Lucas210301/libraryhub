package com.ufape.libraryhub.communication.controller;

import com.ufape.libraryhub.business.facade.IFacade;
import com.ufape.libraryhub.business.model.Member;
import com.ufape.libraryhub.communication.converter.MemberConverter;
import com.ufape.libraryhub.communication.dto.request.MemberDTORequest;
import com.ufape.libraryhub.communication.dto.response.ErrorDTOResponse;
import com.ufape.libraryhub.communication.dto.response.MemberDTOResponse;
import com.ufape.libraryhub.exception.MemberAlreadyRegisteredException;
import com.ufape.libraryhub.exception.MemberNotFoundException;
import com.ufape.libraryhub.exception.MemberWithActiveLoansException;
import com.ufape.libraryhub.exception.MemberWithUnpaidFinesException;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/members")
public class MemberController {

    private final IFacade facade;

    private final MemberConverter converter;

    public MemberController(IFacade facade, MemberConverter converter) {
        this.facade = facade;
        this.converter = converter;
    }

    @GetMapping
    public List<MemberDTOResponse> list() {
        return converter.entityToResponseList(facade.listMembers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> find(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(converter.entityToResponse(facade.findMember(id)));
        } catch (MemberNotFoundException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDTOResponse(exception.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<Object> create(@Valid @RequestBody MemberDTORequest request) {
        try {
            Member member = facade.registerMember(converter.requestToEntity(request));
            return ResponseEntity.status(HttpStatus.CREATED).body(converter.entityToResponse(member));
        } catch (MemberAlreadyRegisteredException exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorDTOResponse(exception.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable Long id, @Valid @RequestBody MemberDTORequest request) {
        try {
            Member member = facade.updateMember(id, converter.requestToEntity(request));
            return ResponseEntity.ok(converter.entityToResponse(member));
        } catch (MemberNotFoundException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDTOResponse(exception.getMessage()));
        } catch (MemberAlreadyRegisteredException exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorDTOResponse(exception.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        try {
            facade.removeMember(id);
            return ResponseEntity.noContent().build();
        } catch (MemberNotFoundException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDTOResponse(exception.getMessage()));
        } catch (MemberWithActiveLoansException | MemberWithUnpaidFinesException exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorDTOResponse(exception.getMessage()));
        }
    }
}
