package ru.romanov.tasks.task2.mapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.romanov.tasks.task2.dto.ProductWithPrice;
import ru.romanov.tasks.task2.entity.Product;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductMapperTest {
    private Product expProduct;
    private ProductWithPrice dto;
    @Mock
    private ProductMapper productMapper;

    @Test
    @DisplayName("test mapping dto to product")
    void testMappingToProduct() {
        when(productMapper.toProduct(dto)).thenReturn(expProduct);
        Product actual = productMapper.toProduct(dto);
        assertEquals(expProduct, actual);
    }

    @Test
    @DisplayName("test mapping product to dto")
    void testMappingToProductDto() {
        when(productMapper.toProductDto(expProduct)).thenReturn(dto);
        ProductWithPrice actual = productMapper.toProductDto(expProduct);
        assertEquals(dto, actual);
    }
}