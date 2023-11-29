package ru.romanov.tasks.task2.repository.impl;

import ru.romanov.tasks.task2.entity.Price;
import ru.romanov.tasks.task2.repository.PriceRepository;
import ru.romanov.tasks.task2.util.DbConnector;

import java.math.BigDecimal;
import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class PriceRepositoryImpl implements PriceRepository {
    private final DbConnector dbConnector;

    public PriceRepositoryImpl() {
        this.dbConnector = new DbConnector();
    }

    public PriceRepositoryImpl(DbConnector dbConnector) {
        this.dbConnector = dbConnector;
    }

    @Override
    public List<Price> getListOfPricesForProducts(List<UUID> productIds) {
        String query = "SELECT * FROM Price WHERE product_id IN (SELECT * FROM UNNEST(?::uuid[]))";
        List<Price> priceList = new ArrayList<>();
        try (Connection connection = dbConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            Array array = connection.createArrayOf("uuid", productIds.toArray());
            preparedStatement.setArray(1, array);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Price price = extractPriceFromResultSet(resultSet);
                priceList.add(price);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return priceList;
    }

    @Override
    public Optional<Price> getPriceByProductId(UUID productId) {
        String query = "SELECT * FROM Price WHERE product_id = ?";

        try (Connection connection = dbConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setObject(1, productId, Types.OTHER);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return Optional.of(extractPriceFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    private Price extractPriceFromResultSet(ResultSet resultSet) throws SQLException {
        UUID priceId = UUID.fromString(resultSet.getString("id"));
        UUID productId = UUID.fromString(resultSet.getString("product_id"));
        BigDecimal value = resultSet.getBigDecimal("value");
        LocalDateTime createdAt = resultSet.getTimestamp("created_at").toLocalDateTime();
        LocalDateTime updatedAt = resultSet.getTimestamp("updated_at").toLocalDateTime();

        return Price.builder()
                .id(priceId)
                .productId(productId)
                .value(value)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    }

    @Override
    public void createPrice(Connection connection, UUID productId, Price price) {
        String priceCommand = "INSERT INTO Price (product_id, value, created_at, updated_at)"
                + " VALUES (?, ?, now(), now())";
        try (PreparedStatement priceStatement = connection.prepareStatement(priceCommand)) {
            priceStatement.setObject(1, productId, Types.OTHER);
            priceStatement.setObject(2, price.getValue(), Types.DECIMAL);
            priceStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void changePriceByProductId(UUID productId, BigDecimal price) {
        String command = "UPDATE Price SET value = ?, updated_at = now() WHERE product_id = ?";
        try (Connection connection = dbConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(command)) {
            preparedStatement.setObject(1, price, Types.DECIMAL);
            preparedStatement.setObject(2, productId, Types.OTHER);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
