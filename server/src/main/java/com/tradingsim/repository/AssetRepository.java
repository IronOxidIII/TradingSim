package com.tradingsim.repository;

import com.tradingsim.model.Asset;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class AssetRepository {

    private final Map<Integer, Asset> assets = new ConcurrentHashMap<>();
    private final AtomicInteger idGenerator = new AtomicInteger(1);

    public synchronized Asset save(Asset asset) {
        if (asset == null) {
            throw new IllegalArgumentException(
                    "Asset must not be null"
            );
        }

        if (asset.getName() == null || asset.getName().isBlank()) {
            throw new IllegalArgumentException(
                    "Asset name must not be empty"
            );
        }

        if (asset.getId() == 0) {
            int newId;

            do {
                newId = idGenerator.getAndIncrement();
            } while (assets.containsKey(newId));

            asset.setId(newId);
        } else {
            if (!assets.containsKey(asset.getId())) {
                throw new IllegalArgumentException(
                        "Asset with id " + asset.getId() + " does not exist"
                );
            }
        }

        assets.put(asset.getId(), asset);
        return asset;
    }

    public Optional<Asset> findById(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Invalid id");
        }

        return Optional.ofNullable(assets.get(id));
    }

    public Optional<Asset> findByName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(
                    "Asset name must not be empty"
            );
        }

        return assets.values().stream()
                .filter(asset -> asset.getName().equalsIgnoreCase(name))
                .findFirst();
    }

    public List<Asset> findAll() {
        return new ArrayList<>(assets.values());
    }

    public synchronized void delete(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Invalid id");
        }

        if (!assets.containsKey(id)) {
            throw new IllegalArgumentException(
                    "Asset with id " + id + " does not exist"
            );
        }

        assets.remove(id);
    }
}
