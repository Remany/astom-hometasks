package ru.romanov.tasks.task2.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.romanov.tasks.task2.dto.ConsumerDto;
import ru.romanov.tasks.task2.entity.Consumer;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConsumerMapperTest {
    private Consumer expConsumer;
    private ConsumerDto expConsumerDto;
    @Mock
    private ConsumerMapper consumerMapper;

    @BeforeEach
    void setUp() {
        expConsumer = Consumer.builder()
                .name("name")
                .id(UUID.randomUUID())
                .build();
        expConsumerDto = ConsumerDto.builder()
                .name("name")
                .build();
    }

    @Test
    @DisplayName("test mapping to consumer")
    void testMappingToConsumer() {
        when(consumerMapper.toConsumer(expConsumerDto)).thenReturn(expConsumer);
        Consumer actual = consumerMapper.toConsumer(expConsumerDto);
        assertEquals(expConsumer, actual);
    }

    @Test
    @DisplayName("test mapping to consumer dto")
    void testToConsumerDto() {
        when(consumerMapper.toConsumerDto(expConsumer)).thenReturn(expConsumerDto);
        ConsumerDto actual = consumerMapper.toConsumerDto(expConsumer);
        assertEquals(expConsumerDto, actual);
    }
}