package ru.romanov.tasks.task2.service.impl;

import ru.romanov.tasks.task2.dto.PriceDto;
import ru.romanov.tasks.task2.entity.Price;
import ru.romanov.tasks.task2.repository.PriceRepository;
import ru.romanov.tasks.task2.repository.impl.PriceRepositoryImpl;
import ru.romanov.tasks.task2.service.PriceService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class PriceServiceImpl implements PriceService {
    private final PriceRepository priceRepository;

    public PriceServiceImpl() {
        this.priceRepository = new PriceRepositoryImpl();
    }

    public PriceServiceImpl(PriceRepository priceRepository) {
        this.priceRepository = priceRepository;
    }

    @Override
    public List<BigDecimal> getPrices(List<UUID> productIds) {
        return priceRepository.getListOfPricesForProducts(productIds)
                .stream()
                .map(Price::getValue)
                .toList();
    }

    @Override
    public Optional<BigDecimal> getPrice(UUID productId) {
        return priceRepository.getPriceByProductId(productId).map(Price::getValue);
    }

    @Override
    public void changePrice(UUID productId, PriceDto priceDto) {
        priceRepository.changePriceByProductId(productId, priceDto.getValue());
    }
}
