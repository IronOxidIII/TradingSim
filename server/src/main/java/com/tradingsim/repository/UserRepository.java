package com.tradingsim.repository;

import com.tradingsim.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User create(User user);
    User update(User user);
    Optional<User> findById(int id);
    List<User> findAll();
    void delete(int id);
}
