package ru.romanov.tasks.task2.repository;

import ru.romanov.tasks.task2.entity.Price;
import ru.romanov.tasks.task2.entity.Product;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository {
    List<Product> getProducts(int limit, int offset);

    Optional<Product> getProductById(UUID id);

    void create(Product product, Price price);

    void update(UUID id, Product product);

    void remove(UUID id);
}
