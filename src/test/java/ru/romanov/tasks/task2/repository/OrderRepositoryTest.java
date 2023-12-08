package ru.romanov.tasks.task2.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.romanov.tasks.task2.entity.Order;
import ru.romanov.tasks.task2.repository.impl.OrderRepositoryImpl;
import ru.romanov.tasks.task2.util.DbConnector;

import java.sql.*;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderRepositoryTest {
    @Mock
    private ResultSet resultSet;
    @Mock
    private Connection connection;
    @Mock
    private DbConnector dbConnector;
    @Mock
    private PreparedStatement preparedStatement;
    private OrderRepository orderRepository;
    private OrderRepository spyRepository;
    private List<Order> expList;
    private Order expOrder;
    private UUID consumerId;

    @BeforeEach
    void setUp() {
        expOrder = Order.builder()
                .productId(UUID.randomUUID())
                .name("product name")
                .description("some product")
                .build();
        expList = List.of(expOrder);
        consumerId = UUID.randomUUID();
        orderRepository = new OrderRepositoryImpl(dbConnector);
        spyRepository = spy(orderRepository);
    }

    @Test
    @DisplayName("test get list of orders")
    void testGetOrdersOfOwner() {
        try {
            when(dbConnector.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(spyRepository.getOrdersOfOwner(consumerId)).thenReturn(expList);

            List<Order> actual = spyRepository.getOrdersOfOwner(consumerId);

            assertEquals(expList, actual);

            String query = "SELECT o.consumer_id, o.product_id, p.name, p.description "
                    + "FROM orders o JOIN Product p ON o.product_id = p.id "
                    + "WHERE o.consumer_id = ?";


            verify(connection).prepareStatement(query);
            verify(preparedStatement).setObject(1, consumerId, Types.OTHER);
            verify(preparedStatement).executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}