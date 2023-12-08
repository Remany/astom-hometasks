package ru.romanov.tasks.task2.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.romanov.tasks.task2.dto.PriceDto;
import ru.romanov.tasks.task2.entity.Price;
import ru.romanov.tasks.task2.repository.PriceRepository;
import ru.romanov.tasks.task2.service.impl.PriceServiceImpl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PriceServiceTest {
    @Mock
    private PriceRepository priceRepository;
    private PriceService priceService;
    private Optional<Price> price;
    private List<UUID> productsIds;
    private List<Price> priceList;
    private PriceDto priceDto;
    private UUID productId;

    @BeforeEach
    void setUp() {
        productId = UUID.randomUUID();
        productsIds = List.of(
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID()
        );
        priceList = List.of(
                Price.builder().value(BigDecimal.valueOf(500)).build(),
                Price.builder().value(BigDecimal.valueOf(100)).build(),
                Price.builder().value(BigDecimal.valueOf(10000)).build(),
                Price.builder().value(BigDecimal.valueOf(300000)).build()
        );
        priceService = new PriceServiceImpl(priceRepository);
        priceDto = PriceDto.builder().value(BigDecimal.valueOf(500)).build();
        price = Optional.of(Price.builder().value(BigDecimal.valueOf(500)).build());
    }

    @Test
    @DisplayName("test get prices when prices is available")
    void testGetPrices() {
        List<BigDecimal> expected = List.of(
                BigDecimal.valueOf(500),
                BigDecimal.valueOf(100),
                BigDecimal.valueOf(10000),
                BigDecimal.valueOf(300000)
        );
        when(priceRepository.getListOfPricesForProducts(productsIds)).thenReturn(priceList);

        List<BigDecimal> actual = priceService.getPrices(productsIds);

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("test get one price by id when price is available")
    void testGetPrice() {
        when(priceRepository.getPriceByProductId(productId)).thenReturn(price);

        BigDecimal expected = BigDecimal.valueOf(500);

        BigDecimal actual = priceService.getPrice(productId).get();

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("test change price")
    void testChangePrice() {
        UUID productId = UUID.randomUUID();
        BigDecimal newPriceValue = BigDecimal.valueOf(500);
        PriceDto priceDto = PriceDto.builder().value(newPriceValue).build();

        priceService.changePrice(productId, priceDto);

        verify(priceRepository).changePriceByProductId(productId, newPriceValue);
    }
}