package com.ufape.libraryhub.communication.controller;

import com.ufape.libraryhub.business.facade.IFacade;
import com.ufape.libraryhub.business.model.Item;
import com.ufape.libraryhub.communication.converter.ItemConverter;
import com.ufape.libraryhub.communication.dto.request.BookDTORequest;
import com.ufape.libraryhub.communication.dto.request.MagazineDTORequest;
import com.ufape.libraryhub.communication.dto.response.ErrorDTOResponse;
import com.ufape.libraryhub.communication.dto.response.ItemDTOResponse;
import com.ufape.libraryhub.exception.ItemNotFoundException;
import com.ufape.libraryhub.exception.ItemOnLoanException;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/items")
public class ItemController {

    private final IFacade facade;

    private final ItemConverter converter;

    public ItemController(IFacade facade, ItemConverter converter) {
        this.facade = facade;
        this.converter = converter;
    }

    @GetMapping
    public List<ItemDTOResponse> list() {
        return converter.entityToResponseList(facade.listItems());
    }

    @GetMapping("/available")
    public List<ItemDTOResponse> listAvailable() {
        return converter.entityToResponseList(facade.listAvailableItems());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> find(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(converter.entityToResponse(facade.findItem(id)));
        } catch (ItemNotFoundException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDTOResponse(exception.getMessage()));
        }
    }

    @PostMapping("/books")
    public ResponseEntity<Object> createBook(@Valid @RequestBody BookDTORequest request) {
        Item item = facade.registerItem(converter.requestToEntity(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(converter.entityToResponse(item));
    }

    @PostMapping("/magazines")
    public ResponseEntity<Object> createMagazine(@Valid @RequestBody MagazineDTORequest request) {
        Item item = facade.registerItem(converter.requestToEntity(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(converter.entityToResponse(item));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        try {
            facade.removeItem(id);
            return ResponseEntity.noContent().build();
        } catch (ItemNotFoundException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDTOResponse(exception.getMessage()));
        } catch (ItemOnLoanException exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorDTOResponse(exception.getMessage()));
        }
    }
}
