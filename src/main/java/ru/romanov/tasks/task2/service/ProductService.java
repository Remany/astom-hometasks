package ru.romanov.tasks.task2.service;

import ru.romanov.tasks.task2.dto.ProductWithPrice;

import java.util.List;
import java.util.UUID;

public interface ProductService {
    ProductWithPrice getProduct(UUID id);

    List<ProductWithPrice> getProducts(int page, int size);

    void createProduct(ProductWithPrice dto);

    void changeProduct(UUID id, ProductWithPrice dto);

    void removeProduct(UUID id);
}
