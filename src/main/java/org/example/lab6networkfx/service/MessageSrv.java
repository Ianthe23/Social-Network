package org.example.lab6networkfx.service;

import org.example.lab6networkfx.domain.User;
import org.example.lab6networkfx.repository.Repository;

import java.util.stream.StreamSupport;

public class MessageSrv {
    protected final Repository<Integer, User> userRepo;
    protected final Repository friendshipRepo;

    public MessageSrv(Repository userRepo, Repository friendshipRepo) {
        this.userRepo = userRepo;
        this.friendshipRepo = friendshipRepo;
    }

    public User findUsername(String username) {
        return StreamSupport.stream(userRepo.findAll().spliterator(),false)
                .filter(user->user.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }
}
