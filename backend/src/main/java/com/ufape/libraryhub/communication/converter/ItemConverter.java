package com.ufape.libraryhub.communication.converter;

import com.ufape.libraryhub.business.model.Book;
import com.ufape.libraryhub.business.model.Item;
import com.ufape.libraryhub.business.model.Magazine;
import com.ufape.libraryhub.communication.dto.request.BookDTORequest;
import com.ufape.libraryhub.communication.dto.request.MagazineDTORequest;
import com.ufape.libraryhub.communication.dto.response.ItemDTOResponse;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ItemConverter {

    public Book requestToEntity(BookDTORequest request) {
        return new Book(request.title(), request.author(), request.isbn());
    }

    public Magazine requestToEntity(MagazineDTORequest request) {
        return new Magazine(request.title(), request.publisher(), request.edition());
    }

    public ItemDTOResponse entityToResponse(Item item) {
        return new ItemDTOResponse(
                item.getId(),
                item.getTitle(),
                item.getType().name(),
                item.getDescription(),
                item.isAvailable(),
                item.getLoanDurationInDays());
    }

    public List<ItemDTOResponse> entityToResponseList(List<Item> items) {
        return items.stream().map(this::entityToResponse).toList();
    }
}
