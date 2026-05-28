package com.tradingsim.repository;

import com.tradingsim.exception.ValidationException;
import com.tradingsim.model.User;
import com.tradingsim.repository.base.AbstractInMemoryRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepositoryImpl extends AbstractInMemoryRepository<User>
        implements UserRepository {

    @Override
    public User create(User user) {
        validate(user);
        if (user.getId() > 0) {
            throw new ValidationException("New user must not have an id");
        }
        return super.create(user);
    }

    @Override
    public User update(User user) {
        validate(user);
        return super.update(user);
    }

    @Override
    public Optional<User> findById(int id) {
        if (id <= 0) {
            throw new ValidationException("Invalid id");
        }
        return super.findById(id);
    }

    @Override
    public List<User> findAll() {
        return super.findAll();
    }

    @Override
    public void delete(int id) {
        if (id <= 0) {
            throw new ValidationException("Invalid id");
        }
        super.delete(id);
    }

    private void validate(User user) {
        if (user == null) {
            throw new ValidationException("User must not be null");
        }

        if (user.getUsername() == null || user.getUsername().isBlank()) {
            throw new ValidationException("User name must not be empty");
        }
    }
}
