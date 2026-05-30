package com.tradingsim.repository;

import com.tradingsim.common.dto.asset.AssetDto;
import com.tradingsim.common.dto.asset.PriceHistoryDto;
import com.tradingsim.exception.ValidationException;
import com.tradingsim.model.Asset;
import com.tradingsim.repository.base.AbstractInMemoryRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AssetRepositoryImpl extends AbstractInMemoryRepository<Asset>
        implements AssetRepository {

    @Override
    public Asset create(Asset asset) {
        validate(asset);
        if (asset.getId() > 0) {
            throw new ValidationException("New asset must not have an id");
        }
        return super.create(asset);
    }

    @Override
    public Asset update(Asset asset) {
        if (asset == null) {
            throw new ValidationException("Asset must not be null");
        }
        if (asset.getName() == null || asset.getName().isBlank()) {
            throw new ValidationException("Asset name must not be empty");
        }
        return super.update(asset);
    }

    @Override
    public Optional<Asset> findById(int id) {
        if (id <= 0) {
            throw new ValidationException("Invalid id");
        }
        return super.findById(id);
    }

    @Override
    public Optional<Asset> findByName(String name) {
        if (name == null || name.isBlank()) {
            throw new ValidationException("Asset name must not be empty");
        }

        return storage.values().stream()
                .filter(asset -> asset.getName() != null
                        && asset.getName().equalsIgnoreCase(name))
                .findFirst();
    }

    @Override
    public List<Asset> findAll() {
        return super.findAll();
    }

    @Override
    public void delete(int id) {
        if (id <= 0) {
            throw new ValidationException("Invalid id");
        }
        super.delete(id);
    }

    public List<AssetDto> toAssetDtoList() {
        var assets = super.findAll();
        List<AssetDto> result = new ArrayList<>();
        for (var asset : assets) {
            List<PriceHistoryDto> priceHistoryDto = new ArrayList<>();
            priceHistoryDto.add(
                    new PriceHistoryDto(LocalDateTime.MIN, "10", 50));

            result.add(new AssetDto(
                    asset.getId(),
                    asset.getName(),
                    priceHistoryDto));
        }

        return result;
    }

    private void validate(Asset asset) {
        if (asset == null) {
            throw new ValidationException("Asset must not be null");
        }

        if (asset.getName() == null || asset.getName().isBlank()) {
            throw new ValidationException("Asset name must not be empty");
        }
    }
}
