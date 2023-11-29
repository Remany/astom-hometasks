package ru.romanov.tasks.task2.repository;

import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.romanov.tasks.task2.entity.Consumer;
import ru.romanov.tasks.task2.repository.impl.ConsumerRepositoryImpl;
import ru.romanov.tasks.task2.util.DbConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConsumerRepositoryTest {
    @Mock
    private HikariDataSource dataSource;
    @Mock
    private ResultSet resultSet;
    @InjectMocks
    private DbConnector dbConnector;
    @Mock
    private Connection connection;
    @Mock
    private PreparedStatement preparedStatement;
    private ConsumerRepository consumerRepository;
    private ConsumerRepository spyRepository;
    private Optional<Consumer> expectedConsumer;
    private UUID consumerId;

    @BeforeEach
    void setUp() {
        expectedConsumer = Optional.of(Consumer.builder().name("name").build());
        consumerRepository = new ConsumerRepositoryImpl(dbConnector);
        spyRepository = spy(consumerRepository);
        consumerId = UUID.randomUUID();
    }

    @Test
    @DisplayName("test create consumer when consumer was created")
    void testCreateConsumer() {
        try {
            when(dbConnector.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
            when(preparedStatement.executeUpdate()).thenReturn(1);

            consumerRepository.createConsumer(expectedConsumer.get());

            String command = "INSERT INTO Consumer (name, created_at, updated_at)"
                    + " VALUES (?, now(), now())";

            verify(connection).prepareStatement(command);
            verify(preparedStatement).setString(1, expectedConsumer.get().getName());
            verify(preparedStatement).executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testGetConsumerById() {
        try {
            when(dbConnector.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(spyRepository.getConsumerById(consumerId)).thenReturn(expectedConsumer);

            Optional<Consumer> actual = spyRepository.getConsumerById(consumerId);

            String query = "SELECT * FROM Consumer WHERE id = ?";

            assertEquals(expectedConsumer, actual);

            verify(connection).prepareStatement(query);
            verify(preparedStatement).setObject(1, consumerId, Types.OTHER);
            verify(preparedStatement).executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testBuyProduct() {
        UUID productId = UUID.randomUUID();
        try {
            when(dbConnector.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
            when(preparedStatement.executeUpdate()).thenReturn(1);

            consumerRepository.buyProduct(consumerId, productId);
            String command = "INSERT INTO Orders (consumer_id, product_id) VALUES (?, ?)";

            verify(connection).prepareStatement(command);
            verify(preparedStatement).setObject(1, consumerId, Types.OTHER);
            verify(preparedStatement).setObject(2, productId, Types.OTHER);
            verify(preparedStatement).executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}