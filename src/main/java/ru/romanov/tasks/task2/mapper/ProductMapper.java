package ru.romanov.tasks.task2.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.romanov.tasks.task2.dto.ProductWithPrice;
import ru.romanov.tasks.task2.entity.Product;

@Mapper
public interface ProductMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Product toProduct(ProductWithPrice product);

    @Mapping(target = "price", ignore = true)
    ProductWithPrice toProductDto(Product product);
}
