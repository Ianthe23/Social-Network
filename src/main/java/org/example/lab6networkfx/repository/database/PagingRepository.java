package org.example.lab6networkfx.repository.database;

import org.example.lab6networkfx.domain.Entity;
import org.example.lab6networkfx.repository.Repository;
import org.example.lab6networkfx.utils.paging.Page;
import org.example.lab6networkfx.utils.paging.Pageable;

public interface PagingRepository<ID, E extends Entity<ID>> extends Repository<ID, E> {
    Page<E> findAllOnPage(Pageable pageable);
}
