package ru.romanov.tasks.task2.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.romanov.tasks.task2.dto.ProductWithPrice;
import ru.romanov.tasks.task2.entity.Price;
import ru.romanov.tasks.task2.entity.Product;
import ru.romanov.tasks.task2.mapper.PriceMapper;
import ru.romanov.tasks.task2.mapper.ProductMapper;
import ru.romanov.tasks.task2.repository.ProductRepository;
import ru.romanov.tasks.task2.service.impl.ProductServiceImpl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    private PriceMapper priceMapper;
    @Mock
    private ProductMapper productMapper;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private PriceService priceService;
    private ProductService productService;
    private UUID productId;
    private List<Product> expectedList;
    private List<ProductWithPrice> expectedListDto;
    private Optional<Price> expectedPrice;
    private Optional<Product> expectedProduct;
    private ProductWithPrice productWithPrice;

    @BeforeEach
    void setUp() {
        UUID priceId = UUID.randomUUID();
        productId = UUID.randomUUID();
        expectedPrice = Optional.of(Price.builder()
                .id(priceId)
                .value(BigDecimal.valueOf(500))
                .build());
        expectedProduct = Optional.of(Product.builder()
                .id(productId)
                .name("product")
                .description("test product")
                .build());
        productWithPrice = ProductWithPrice.builder()
                .name(expectedProduct.get().getName())
                .description(expectedProduct.get().getDescription())
                .price(expectedPrice.get().getValue())
                .build();
        expectedList = List.of(
                Product.builder()
                        .name("product 2")
                        .description("some product 1")
                        .build(),
                Product.builder()
                        .name("product 2")
                        .description("some product 2")
                        .build());
        expectedListDto = List.of(
                ProductWithPrice.builder()
                        .name("product 2")
                        .description("some product 1")
                        .price(BigDecimal.valueOf(500))
                        .build(),
                ProductWithPrice.builder()
                        .name("product 2")
                        .description("some product 2")
                        .price(BigDecimal.valueOf(600))
                        .build());
        productService = new ProductServiceImpl(productRepository, productMapper, priceService, priceMapper);
    }

    @Test
    void getProduct() {
        when(productRepository.getProductById(productId)).thenReturn(expectedProduct);

        Optional<Product> actual = productRepository.getProductById(productId);

        assertEquals(expectedProduct, actual);

        verify(productRepository).getProductById(productId);
    }

    @Test
    @DisplayName("test create product")
    void testCreateProduct() {
        when(productMapper.toProduct(productWithPrice)).thenReturn(expectedProduct.get());
        when(priceMapper.toPrice(productWithPrice)).thenReturn(expectedPrice.get());

        productService.createProduct(productWithPrice);

        verify(productMapper).toProduct(productWithPrice);
        verify(priceMapper).toPrice(productWithPrice);
        verify(productRepository).create(expectedProduct.get(), expectedPrice.get());
    }

    @Test
    @DisplayName("test change product when product is available")
    void testChangeProduct() {
        when(productMapper.toProduct(productWithPrice)).thenReturn(expectedProduct.get());

        productService.changeProduct(productId, productWithPrice);

        verify(productMapper).toProduct(productWithPrice);
        verify(productRepository).update(productId, expectedProduct.get());
    }

    @Test
    @DisplayName("test remove product")
    void testRemoveProduct() {
        productService.removeProduct(productId);
        verify(productRepository).remove(productId);
    }
}