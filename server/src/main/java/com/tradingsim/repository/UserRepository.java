package com.tradingsim.repository;

import com.tradingsim.model.User;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class UserRepository {

    private final Map<Integer, User> users = new ConcurrentHashMap<>();
    private final AtomicInteger idGenerator = new AtomicInteger(1);

    public synchronized User save(User user) {
        if (user == null) {
            throw new IllegalArgumentException(
                    "User must not be null"
            );
        }

        if (user.getUsername() == null || user.getUsername().isBlank()) {
            throw new IllegalArgumentException(
                    "User name must not be empty"
            );
        }

        if (user.getId() == 0) {
            int newId;

            do {
                newId = idGenerator.getAndIncrement();
            } while (users.containsKey(newId));

            user.setId(newId);
        } else {
            if (!users.containsKey(user.getId())) {
                throw new IllegalArgumentException(
                        "User with id " + user.getId() + " does not exist"
                );
            }
        }

        users.put(user.getId(), user);
        return user;
    }

    public Optional<User> findById(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Invalid id");
        }

        return Optional.ofNullable(users.get(id));
    }

    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    public synchronized void delete(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Invalid id");
        }

        if (!users.containsKey(id)) {
            throw new IllegalArgumentException(
                    "User with id " + id + " does not exist"
            );
        }

        users.remove(id);
    }
}
