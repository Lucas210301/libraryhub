package com.ufape.libraryhub.business.service;

import com.ufape.libraryhub.business.model.Item;
import com.ufape.libraryhub.exception.ItemNotFoundException;
import com.ufape.libraryhub.exception.ItemOnLoanException;
import java.util.List;

public interface IItemService {

    Item create(Item item);

    Item update(Item item);

    Item findById(Long id) throws ItemNotFoundException;

    List<Item> findAll();

    List<Item> findAvailable();

    void delete(Long id) throws ItemNotFoundException, ItemOnLoanException;
}
