package ru.romanov.tasks.task2.repository.impl;

import ru.romanov.tasks.task2.entity.Price;
import ru.romanov.tasks.task2.entity.Product;
import ru.romanov.tasks.task2.repository.PriceRepository;
import ru.romanov.tasks.task2.repository.ProductRepository;
import ru.romanov.tasks.task2.util.DbConnector;

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

public class ProductRepositoryImpl implements ProductRepository {
    private static final String ID_KEY = "id";
    private static final String NAME_KEY = "name";
    private static final String DESCRIPTION_KEY = "description";
    private static final String CREATED_AT_KEY = "created_at";
    private static final String UPDATED_AT_KEY = "updated_at";
    private final PriceRepository priceRepository;
    private final DbConnector dbConnector;

    public ProductRepositoryImpl() {
        this.priceRepository = new PriceRepositoryImpl();
        this.dbConnector = new DbConnector();
    }

    public ProductRepositoryImpl(PriceRepository priceRepository, DbConnector dbConnector) {
        this.priceRepository = priceRepository;
        this.dbConnector = dbConnector;
    }

    @Override
    public List<Product> getProducts(int limit, int offset) {
        String query = "SELECT * FROM Product LIMIT ? OFFSET ?";
        List<Product> products = new ArrayList<>();

        try (Connection connection = dbConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, limit);
            preparedStatement.setInt(2, offset);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Product product = extractProductFromResultSet(resultSet);
                products.add(product);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return products;
    }

    private Product extractProductFromResultSet(ResultSet resultSet) throws SQLException {
        UUID productId = UUID.fromString(resultSet.getString(ID_KEY));
        String productName = resultSet.getString(NAME_KEY);
        String description = resultSet.getString(DESCRIPTION_KEY);
        LocalDateTime createdAt = resultSet.getTimestamp(CREATED_AT_KEY).toLocalDateTime();
        LocalDateTime updatedAt = resultSet.getTimestamp(UPDATED_AT_KEY).toLocalDateTime();

        return Product.builder()
                .id(productId)
                .name(productName)
                .description(description)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    }

    @Override
    public Optional<Product> getProductById(UUID id) {
        Optional<Product> result = Optional.empty();
        String query = "SELECT * FROM Product WHERE id = ?";
        try (Connection connection = dbConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setObject(1, id, Types.OTHER);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                result = Optional.ofNullable(extractProductFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    @Override
    public void create(Product product, Price price) {
        try (Connection connection = dbConnector.getConnection()) {
            connection.setAutoCommit(false);

            UUID productId = insertProduct(connection, product);
            priceRepository.createPrice(connection, productId, price);

            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при создании продукта", e);
        }
    }

    private UUID insertProduct(Connection connection, Product product) throws SQLException {
        UUID productId = null;
        String command = "INSERT INTO Product (name, description, created_at, updated_at)"
                + " VALUES (?, ?, now(), now()) RETURNING id";
        try (PreparedStatement preparedStatement = connection.prepareStatement(command)) {
            preparedStatement.setString(1, product.getName());
            preparedStatement.setString(2, product.getDescription());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                productId = UUID.fromString(resultSet.getString(ID_KEY));
            }
        }
        return productId;
    }

    @Override
    public void update(UUID id, Product product) {
        String command = "UPDATE Product SET name = ?,"
                + " description = ?, updated_at = now() WHERE id = ?";
        try (Connection connection = dbConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(command)) {
            preparedStatement.setString(1, product.getName());
            preparedStatement.setString(2, product.getDescription());
            preparedStatement.setObject(3, id, Types.OTHER);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void remove(UUID id) {
        String command = "DELETE FROM Product WHERE id = ?";
        try (Connection connection = dbConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(command)) {
            preparedStatement.setObject(1, id, Types.OTHER);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
