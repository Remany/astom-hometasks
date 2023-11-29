package ru.romanov.tasks.task2.service;

import ru.romanov.tasks.task2.dto.PriceDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PriceService {
    List<BigDecimal> getPrices(List<UUID> productIds);

    Optional<BigDecimal> getPrice(UUID productId);

    void changePrice(UUID productId, PriceDto priceDto);
}
