package ru.romanov.tasks.task2.repository.impl;

import ru.romanov.tasks.task2.entity.Consumer;
import ru.romanov.tasks.task2.repository.ConsumerRepository;
import ru.romanov.tasks.task2.util.DbConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Optional;
import java.util.UUID;

public class ConsumerRepositoryImpl implements ConsumerRepository {
    private final DbConnector dbConnector;

    public ConsumerRepositoryImpl() {
        this.dbConnector = new DbConnector();
    }

    public ConsumerRepositoryImpl(DbConnector dbConnector) {
        this.dbConnector = dbConnector;
    }

    @Override
    public void createConsumer(Consumer consumer) {
        String command = "INSERT INTO Consumer (name, created_at, updated_at)"
                + " VALUES (?, now(), now())";
        try (Connection connection = dbConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(command)) {
            preparedStatement.setString(1, consumer.getName());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Consumer> getConsumerById(UUID id) {
        Optional<Consumer> consumer = Optional.empty();
        String query = "SELECT * FROM Consumer WHERE id = ?";
        try (Connection connection = dbConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setObject(1, id, Types.OTHER);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                UUID consumerId = UUID.fromString(resultSet.getString("id"));
                String name = resultSet.getString("name");
                consumer = Optional.ofNullable(Consumer.builder()
                        .id(consumerId)
                        .name(name)
                        .build());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return consumer;
    }

    @Override
    public void buyProduct(UUID consumerId, UUID productId) {
        String command = "INSERT INTO Orders (consumer_id, product_id) VALUES (?, ?)";
        try (Connection connection = dbConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(command)) {
            preparedStatement.setObject(1, consumerId, Types.OTHER);
            preparedStatement.setObject(2, productId, Types.OTHER);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
