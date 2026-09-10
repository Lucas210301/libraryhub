package com.ufape.libraryhub.business.service;

import com.ufape.libraryhub.business.model.Item;
import com.ufape.libraryhub.data.IItemRepository;
import com.ufape.libraryhub.exception.ItemNotFoundException;
import com.ufape.libraryhub.exception.ItemOnLoanException;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ItemService implements IItemService {

    private final IItemRepository repository;

    public ItemService(IItemRepository repository) {
        this.repository = repository;
    }

    @Override
    public Item create(Item item) {
        return repository.save(item);
    }

    @Override
    public Item update(Item item) {
        return repository.save(item);
    }

    @Override
    public Item findById(Long id) throws ItemNotFoundException {
        return repository.findById(id).orElseThrow(() -> new ItemNotFoundException(id));
    }

    @Override
    public List<Item> findAll() {
        return repository.findAll();
    }

    @Override
    public List<Item> findAvailable() {
        return repository.findByAvailableTrue();
    }

    @Override
    public void delete(Long id) throws ItemNotFoundException, ItemOnLoanException {
        Item item = findById(id);
        if (!item.isAvailable()) {
            throw new ItemOnLoanException(item.getTitle());
        }
        repository.delete(item);
    }
}
