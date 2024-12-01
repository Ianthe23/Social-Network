package org.example.lab6networkfx.repository.database;

import org.example.lab6networkfx.domain.User;
import org.example.lab6networkfx.utils.paging.Page;
import org.example.lab6networkfx.utils.paging.Pageable;

public interface UserRepo extends PagingRepository<Integer, User> {
    Page<User> findAllOnPage(Pageable pageable);
    Page<User> findAllBySearchText(Pageable pageable, String searchText);
}
