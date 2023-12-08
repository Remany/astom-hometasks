package ru.romanov.tasks.task2.service.impl;

import ru.romanov.tasks.task2.dto.ProductWithPrice;
import ru.romanov.tasks.task2.entity.Product;
import ru.romanov.tasks.task2.exceptions.ResourceNotFoundException;
import ru.romanov.tasks.task2.mapper.PriceMapper;
import ru.romanov.tasks.task2.mapper.PriceMapperImpl;
import ru.romanov.tasks.task2.mapper.ProductMapper;
import ru.romanov.tasks.task2.mapper.ProductMapperImpl;
import ru.romanov.tasks.task2.repository.ProductRepository;
import ru.romanov.tasks.task2.repository.impl.ProductRepositoryImpl;
import ru.romanov.tasks.task2.service.PriceService;
import ru.romanov.tasks.task2.service.ProductService;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final PriceService priceService;
    private final PriceMapper priceMapper;

    public ProductServiceImpl() {
        this.productRepository = new ProductRepositoryImpl();
        this.productMapper = new ProductMapperImpl();
        this.priceService = new PriceServiceImpl();
        this.priceMapper = new PriceMapperImpl();
    }

    public ProductServiceImpl(ProductRepository productRepository, ProductMapper productMapper, PriceService priceService, PriceMapper priceMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.priceService = priceService;
        this.priceMapper = priceMapper;
    }

    @Override
    public ProductWithPrice getProduct(UUID id) {
        return productRepository.getProductById(id)
                .map(productMapper::toProductDto)
                .map(product -> {
                    BigDecimal price = priceService.getPrice(id).orElseThrow();
                    product.setPrice(price);
                    return product;
                }).orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    }

    @Override
    public List<ProductWithPrice> getProducts(int page, int size) {
        List<Product> products = productRepository.getProducts(size, page);
        List<UUID> productIds = products.stream().map(Product::getId).toList();
        List<BigDecimal> prices = priceService.getPrices(productIds);
        List<ProductWithPrice> result = products.stream()
                .map(productMapper::toProductDto)
                .toList();
        IntStream.range(0, Math.min(result.size(), prices.size()))
                .forEach(index -> result.get(index).setPrice(prices.get(index)));

        return result;
    }

    @Override
    public void createProduct(ProductWithPrice dto) {
        productRepository.create(productMapper.toProduct(dto),
                priceMapper.toPrice(dto));
    }

    @Override
    public void changeProduct(UUID id, ProductWithPrice dto) {
        productRepository.update(id, productMapper.toProduct(dto));
    }

    @Override
    public void removeProduct(UUID id) {
        productRepository.remove(id);
    }
}
