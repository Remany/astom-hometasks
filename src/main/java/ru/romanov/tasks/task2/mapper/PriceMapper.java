package ru.romanov.tasks.task2.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.romanov.tasks.task2.dto.ProductWithPrice;
import ru.romanov.tasks.task2.entity.Price;

@Mapper
public interface PriceMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "productId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "value", source = "dto.price")
    Price toPrice(ProductWithPrice dto);
}
