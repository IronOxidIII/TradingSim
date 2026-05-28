package com.tradingsim.repository;

import com.tradingsim.model.Asset;

import java.util.List;
import java.util.Optional;

public interface AssetRepository {
    Asset create(Asset asset);
    Asset update(Asset asset);
    Optional<Asset> findById(int id);
    Optional<Asset> findByName(String name);
    List<Asset> findAll();
    void delete(int id);
}
