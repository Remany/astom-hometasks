package ru.romanov.tasks.task2.repository.impl;

import ru.romanov.tasks.task2.entity.Order;
import ru.romanov.tasks.task2.repository.OrderRepository;
import ru.romanov.tasks.task2.util.DbConnector;

import java.sql.Connection;
import java.sql.Types;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OrderRepositoryImpl implements OrderRepository {
    private final DbConnector dbConnector;

    public OrderRepositoryImpl() {
        this.dbConnector = new DbConnector();
    }

    public OrderRepositoryImpl(DbConnector dbConnector) {
        this.dbConnector = dbConnector;
    }

    @Override
    public List<Order> getOrdersOfOwner(UUID ownerId) {
        String query = "SELECT o.consumer_id, o.product_id, p.name, p.description "
                + "FROM orders o JOIN Product p ON o.product_id = p.id "
                + "WHERE o.consumer_id = ?";
        List<Order> orderList = new ArrayList<>();

        try (Connection connection = dbConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setObject(1, ownerId, Types.OTHER);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Order order = extractProductFromResultSet(resultSet);
                orderList.add(order);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return orderList;
    }

    private Order extractProductFromResultSet(ResultSet resultSet) throws SQLException {
        UUID productId = UUID.fromString(resultSet.getString("product_id"));
        String productName = resultSet.getString("name");
        String description = resultSet.getString("description");

        return Order.builder()
                .productId(productId)
                .name(productName)
                .description(description)
                .build();
    }
}
