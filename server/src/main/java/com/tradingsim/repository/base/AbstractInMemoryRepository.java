package com.tradingsim.repository.base;

import com.tradingsim.exception.NotFoundException;
import com.tradingsim.exception.ValidationException;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class AbstractInMemoryRepository<T extends Identifiable> {

    protected final ConcurrentHashMap<Integer, T> storage =
            new ConcurrentHashMap<>();
    protected final AtomicInteger idGen = new AtomicInteger(1);

    protected T create(T entity) {
        if (entity == null) {
            throw new ValidationException("Entity must not be null");
        }

        if (entity.getId() > 0) {
            throw new ValidationException(
                    "New entity must not already have an id"
            );
        }

        int id = idGen.getAndIncrement();
        entity.setId(id);
        storage.put(id, entity);
        return entity;
    }

    protected T update(T entity) {
        if (entity == null) {
            throw new ValidationException("Entity must not be null");
        }

        int id = entity.getId();
        if (id <= 0) {
            throw new ValidationException("Entity id must be positive");
        }

        if (!storage.containsKey(id)) {
            throw new NotFoundException("Entity with id " + id + " does not exist");
        }

        storage.put(id, entity);
        return entity;
    }

    protected Optional<T> findById(int id) {
        return Optional.ofNullable(storage.get(id));
    }

    protected List<T> findAll() {
        return List.copyOf(storage.values());
    }

    protected void delete(int id) {
        T removed = storage.remove(id);
        if (removed == null) {
            throw new NotFoundException("Entity with id " + id + " does not exist");
        }
    }
}
