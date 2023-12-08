package ru.romanov.tasks.task2.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.romanov.tasks.task2.dto.ProductWithPrice;
import ru.romanov.tasks.task2.entity.Price;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PriceMapperTest {
    private Price expPrice;
    private ProductWithPrice dto;
    @Mock
    private PriceMapper priceMapper;

    @BeforeEach
    void setUp() {
        expPrice = Price.builder()
                .id(UUID.randomUUID())
                .value(BigDecimal.valueOf(500))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        dto = ProductWithPrice.builder()
                .name("name")
                .price(BigDecimal.valueOf(500))
                .description("some product")
                .build();
    }

    @Test
    @DisplayName("test mapping dto to price")
    void testToPrice() {
        when(priceMapper.toPrice(dto)).thenReturn(expPrice);
        Price actual = priceMapper.toPrice(dto);
        assertEquals(expPrice, actual);
    }
}