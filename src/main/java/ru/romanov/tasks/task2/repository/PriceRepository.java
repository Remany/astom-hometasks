package ru.romanov.tasks.task2.repository;

import ru.romanov.tasks.task2.entity.Price;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PriceRepository {
    List<Price> getListOfPricesForProducts(List<UUID> productIds);

    Optional<Price> getPriceByProductId(UUID productId);

    void createPrice(Connection connection, UUID productId, Price price);

    void changePriceByProductId(UUID productId, BigDecimal price);
}
