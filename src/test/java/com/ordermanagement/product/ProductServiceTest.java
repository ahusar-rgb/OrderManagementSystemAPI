package com.ordermanagement.product;

import com.ordermanagement.exception.IdAlreadyInUseException;
import com.ordermanagement.exception.NotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Test
    @DisplayName("It should save product")
    void itShouldSaveProduct() {
        Product expected = Product.builder()
                .skuCode("skuCode")
                .name("name")
                .unitPrice(1.0f)
                .build();

        when(productRepository.save(expected)).thenReturn(expected);

        Product actual = productService.saveProduct(expected);

        assertEquals(expected, actual);
        verify(productRepository, times(1)).save(expected);
    }

    @Test
    @DisplayName("It shouldn't save product when skuCode is already in use")
    void itShouldNotSaveProductWhenSkuCodeIsAlreadyInUse() {
        Product expected = Product.builder()
                .skuCode("skuCode")
                .name("name")
                .unitPrice(1.0f)
                .build();

        when(productRepository.findById(expected.getSkuCode())).thenReturn(Optional.of(expected));

        assertThrows(IdAlreadyInUseException.class,
                () -> productService.saveProduct(expected)
        );

        verify(productRepository, never()).save(any());
    }

    @Test
    @DisplayName("It should find product by skuCode")
    void itShouldFindProductBySku() {
        Product expected = Product.builder()
                .skuCode("skuCode")
                .name("name")
                .unitPrice(1.0f)
                .build();

        when(productRepository.findById(expected.getSkuCode())).thenReturn(Optional.of(expected));

        Optional<Product> actual = productService.findProductBySkuCode(expected.getSkuCode());

        assertTrue(actual.isPresent());
        verify(productRepository, times(1)).findById(expected.getSkuCode());
    }

    @Test
    @DisplayName("It should find product optional by skuCode when it doesn't exist")
    void itShouldFindProductOptionalBySkuWhenDoesntExist() {
        String skuCode = "skuCode";
        when(productRepository.findById(any())).thenReturn(Optional.empty());

        Optional<Product> actual = productService.findProductBySkuCode(skuCode);
        assertTrue(actual.isEmpty());
    }

    @Test
    @DisplayName("It should delete product by id")
    void itShouldDeleteProductById() {
        String skuCode = "skuCode";
        when(productRepository.findById(skuCode)).thenReturn(Optional.of(new Product()));
        productService.deleteProductBySku(skuCode);

        verify(productRepository, times(1)).deleteById(skuCode);
    }

    @Test
    @DisplayName("It shouldn't delete product by id when it doesn't exist")
    void itShouldNotDeleteProductByIdWhenDoesntExist() {
        String skuCode = "skuCode";
        when(productRepository.findById(skuCode)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> productService.deleteProductBySku(skuCode)
        );

        verify(productRepository, never()).deleteById(skuCode);
    }

    @Test
    @DisplayName("It should update product")
    void ithShouldUpdateProduct() {
        Product expected = Product.builder()
                .skuCode("skuCode")
                .name("name")
                .unitPrice(1.0f)
                .build();

        when(productRepository.findById(expected.getSkuCode())).thenReturn(Optional.of(expected));
        when(productRepository.save(expected)).thenReturn(expected);

        Product actual = productService.updateProduct(expected);

        assertEquals(expected, actual);
        verify(productRepository, times(1)).save(expected);
    }

    @Test
    @DisplayName("It shouldn't update product when it doesn't exist")
    void itShouldNotUpdateProductWhenDoesntExist() {
        Product expected = Product.builder()
                .skuCode("skuCode")
                .name("name")
                .unitPrice(1.0f)
                .build();

        when(productRepository.findById(expected.getSkuCode())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> productService.updateProduct(expected)
        );

        verify(productRepository, never()).save(expected);
    }
}