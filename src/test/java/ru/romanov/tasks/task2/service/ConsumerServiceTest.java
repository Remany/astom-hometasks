package ru.romanov.tasks.task2.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.romanov.tasks.task2.entity.Consumer;
import ru.romanov.tasks.task2.dto.ConsumerDto;
import ru.romanov.tasks.task2.exceptions.ResourceNotFoundException;
import ru.romanov.tasks.task2.mapper.ConsumerMapper;
import ru.romanov.tasks.task2.repository.ConsumerRepository;
import ru.romanov.tasks.task2.repository.OrderRepository;
import ru.romanov.tasks.task2.service.impl.ConsumerServiceImpl;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConsumerServiceTest {
    @Mock
    private ConsumerRepository consumerRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private ConsumerMapper consumerMapper;
    private ConsumerService consumerService;
    private Optional<Consumer> expectedConsumer;
    private ConsumerDto expectedConsumerDto;
    private UUID consumerId;
    private UUID productId;

    @BeforeEach
    void setUp() {
        productId = UUID.randomUUID();
        consumerId = UUID.randomUUID();
        expectedConsumer = Optional.of(Consumer.builder().name("name").build());
        expectedConsumerDto = ConsumerDto.builder().name("name").build();
        consumerService = new ConsumerServiceImpl(consumerRepository, orderRepository, consumerMapper);
    }

    @Test
    @DisplayName("test create consumer to db")
    void testCreateConsumer() {
        when(consumerMapper.toConsumer(expectedConsumerDto)).thenReturn(expectedConsumer.get());
        consumerService.createConsumer(expectedConsumerDto);
        verify(consumerMapper).toConsumer(expectedConsumerDto);
        verify(consumerRepository).createConsumer(expectedConsumer.get());
    }

    @Test
    @DisplayName("test get consumer when consumer is available")
    void testGetConsumer() {
        when(consumerRepository.getConsumerById(consumerId)).thenReturn(expectedConsumer);
        when(consumerMapper.toConsumerDto(expectedConsumer.get())).thenReturn(expectedConsumerDto);

        ConsumerDto actual = consumerService.getConsumer(consumerId);
        assertEquals(expectedConsumerDto, actual);
    }

    @Test
    void testBuyProduct_When_ConsumerAndProductExist_ThenProductBought() {
        doNothing().when(consumerRepository).buyProduct(consumerId, productId);
        consumerService.buyProduct(consumerId, productId);

        verify(consumerRepository).buyProduct(consumerId, productId);
    }

    @Test
    void testBuyProduct_When_ConsumerDoesNotExist_Then_Throws_ResourceNotFoundException() {
        doThrow(new ResourceNotFoundException("Consumer not found")).when(consumerRepository).buyProduct(consumerId, productId);

        assertThrows(ResourceNotFoundException.class, () -> consumerService.buyProduct(consumerId, productId));
    }
}