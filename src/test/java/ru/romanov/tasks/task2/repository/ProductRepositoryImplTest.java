package ru.romanov.tasks.task2.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.romanov.tasks.task2.entity.Price;
import ru.romanov.tasks.task2.entity.Product;
import ru.romanov.tasks.task2.repository.impl.ProductRepositoryImpl;
import ru.romanov.tasks.task2.util.DbConnector;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductRepositoryImplTest {
    @Mock
    private ResultSet resultSet;
    @Mock
    private Connection connection;
    @Mock
    private DbConnector dbConnector;
    @Mock
    private PriceRepository priceRepository;
    @Mock
    private PreparedStatement preparedStatement;
    private ProductRepositoryImpl productRepositoryImpl;
    private Optional<Product> expectedProduct;
    private ru.romanov.tasks.task2.repository.ProductRepository spyRepository;
    private List<Product> expectedList;
    private UUID productId;


    @BeforeEach
    void setUp() throws SQLException {
        productId = UUID.randomUUID();

        expectedProduct = Optional.of(Product.builder()
                .id(productId)
                .name("product")
                .description("some product")
                .build());
        expectedList = List.of(
                Product.builder()
                        .id(productId)
                        .name("product 2")
                        .description("some product 1")
                        .build(),
                Product.builder()
                        .id(productId)
                        .name("product 2")
                        .description("some product 2")
                        .build());
        productRepositoryImpl = new ProductRepositoryImpl(priceRepository, dbConnector);
        spyRepository = spy(productRepositoryImpl);
    }

    @Test
    @DisplayName("test get products when products was returned")
    void testGetProducts() {
        int limit = 25;
        int offset = 0;
        try {
            when(dbConnector.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(spyRepository.getProducts(limit, offset)).thenReturn(expectedList);

            List<Product> actual = spyRepository.getProducts(limit, offset);

            assertEquals(expectedList, actual);

            String query = "SELECT * FROM Product LIMIT ? OFFSET ?";

            verify(connection).prepareStatement(query);
            verify(preparedStatement).setInt(1, limit);
            verify(preparedStatement).setInt(2, offset);
            verify(preparedStatement).executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("test get product by Id when product was found")
    void testGetProductById() {
        ru.romanov.tasks.task2.repository.ProductRepository spyRepository = spy(productRepositoryImpl);
        try {
            when(dbConnector.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(spyRepository.getProductById(productId)).thenReturn(expectedProduct);

            Optional<Product> actual = spyRepository.getProductById(productId);

            assertEquals(expectedProduct, actual);

            String query = "SELECT * FROM Product WHERE id = ?";

            verify(connection).prepareStatement(query);
            verify(preparedStatement).setObject(1, productId, Types.OTHER);
            verify(preparedStatement).executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    @DisplayName("test create product to db")
    void testCreateProduct() throws SQLException {
        Product product = expectedProduct.get();
        Price price = Price.builder().value(BigDecimal.valueOf(500)).build();
        when(dbConnector.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        productRepositoryImpl.create(product, price);

        String command = "INSERT INTO Product (name, description, created_at, updated_at)"
                + " VALUES (?, ?, now(), now()) RETURNING id";

        verify(connection).prepareStatement(command);
        verify(preparedStatement).setString(1, product.getName());
        verify(preparedStatement).setString(2, product.getDescription());
        verify(preparedStatement).executeQuery();
    }

    @Test
    @DisplayName("test update product when product is available")
    void testUpdate() {
        Product update = Product.builder()
                .name("product")
                .description("updated product")
                .build();
        try {
            when(dbConnector.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
            when(preparedStatement.executeUpdate()).thenReturn(1);

            productRepositoryImpl.update(productId, update);

            String command = "UPDATE Product SET name = ?, description = ?,"
                    + " updated_at = now() WHERE id = ?";

            verify(connection).prepareStatement(command);
            verify(preparedStatement).setString(1, update.getName());
            verify(preparedStatement).setString(2, update.getDescription());
            verify(preparedStatement).setObject(3, productId, Types.OTHER);
            verify(preparedStatement).executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("test remove product when product is available")
    void testRemoveProduct() {
        try {
            when(dbConnector.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
            when(preparedStatement.executeUpdate()).thenReturn(1);

            productRepositoryImpl.remove(productId);

            String command = "DELETE FROM Product WHERE id = ?";
            verify(connection).prepareStatement(command);
            verify(preparedStatement).setObject(1, productId, Types.OTHER);
            verify(preparedStatement).executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}