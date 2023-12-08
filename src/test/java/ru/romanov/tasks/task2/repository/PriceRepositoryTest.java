package ru.romanov.tasks.task2.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.romanov.tasks.task2.entity.Price;
import ru.romanov.tasks.task2.repository.impl.PriceRepositoryImpl;
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
class PriceRepositoryTest {
    @Mock
    private ResultSet resultSet;
    @Mock
    private Connection connection;
    @Mock
    private DbConnector dbConnector;
    @Mock
    private PreparedStatement preparedStatement;
    private PriceRepository priceRepository;
    private PriceRepository spyRepository;
    private Optional<Price> expectedPrice;
    private List<Price> priceList;
    private List<UUID> productIds;
    private UUID priceId;

    @BeforeEach
    void setUp() {
        priceRepository = new PriceRepositoryImpl(dbConnector);
        spyRepository = spy(priceRepository);
        priceId = UUID.randomUUID();
        expectedPrice = Optional.ofNullable(Price.builder()
                .id(priceId)
                .value(BigDecimal.valueOf(500.00))
                .build());
        priceList = List.of(
                Price.builder().value(BigDecimal.valueOf(500)).build(),
                Price.builder().value(BigDecimal.valueOf(10000)).build(),
                Price.builder().value(BigDecimal.valueOf(4000)).build(),
                Price.builder().value(BigDecimal.valueOf(1000)).build()
        );
        productIds = List.of(
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID()
        );
    }

    @Test
    void testGetListOfPricesForProducts() {
        try {
            when(dbConnector.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(spyRepository.getListOfPricesForProducts(productIds)).thenReturn(priceList);

            List<Price> actual = spyRepository.getListOfPricesForProducts(productIds);

            String query = "SELECT * FROM Price WHERE product_id IN (SELECT * FROM UNNEST(?::uuid[]))";

            assertEquals(priceList, actual);

            verify(connection).prepareStatement(query);
            verify(preparedStatement).executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("test get price when price is found")
    void testGetPrice() {
        try {
            when(dbConnector.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(spyRepository.getPriceByProductId(priceId)).thenReturn(expectedPrice);

            Optional<Price> actual = spyRepository.getPriceByProductId(priceId);

            String query = "SELECT * FROM Price WHERE product_id = ?";

            assertEquals(expectedPrice, actual);

            verify(connection).prepareStatement(query);
            verify(preparedStatement).setObject(1, priceId, Types.OTHER);
            verify(preparedStatement).executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("test create price when price was created")
    void testCreatePrice() {
        Price price = expectedPrice.get();
        try {
            when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
            when(preparedStatement.executeUpdate()).thenReturn(1);

            priceRepository.createPrice(connection, price.getProductId(), price);

            String command = "INSERT INTO Price (product_id, value, created_at, updated_at)" +
                    " VALUES (?, ?, now(), now())";

            verify(connection).prepareStatement(command);
            verify(preparedStatement).setObject(1, price.getProductId(), Types.OTHER);
            verify(preparedStatement).setObject(2, price.getValue(), Types.DECIMAL);
            verify(preparedStatement).executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testChangePrice() {
        BigDecimal price = expectedPrice.get().getValue();
        try {
            when(dbConnector.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
            when(preparedStatement.executeUpdate()).thenReturn(1);

            priceRepository.changePriceByProductId(priceId, price);

            String command = "UPDATE Price SET value = ?, updated_at = now() WHERE product_id = ?";

            verify(connection).prepareStatement(command);
            verify(preparedStatement).setObject(1, price, Types.DECIMAL);
            verify(preparedStatement).setObject(2, priceId, Types.OTHER);
            verify(preparedStatement).executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}