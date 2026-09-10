package com.ufape.libraryhub.data;

import com.ufape.libraryhub.business.model.Item;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByAvailableTrue();
}
