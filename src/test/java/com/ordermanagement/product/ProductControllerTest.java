package com.ordermanagement.product;

import com.ordermanagement.exception.IdAlreadyInUseException;
import com.ordermanagement.exception.NotFoundException;
import com.ordermanagement.order.OrderService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {
    @Mock
    private ProductService productService;

    @Mock
    private OrderService orderService;

    @InjectMocks
    private ProductController productController;

    @Test
    @DisplayName("It should return OK when [create product]")
    void itShouldReturnOkWhenCreateProduct() {
        Product expected = Product.builder()
                .skuCode("skuCode")
                .name("name")
                .unitPrice(1.0f)
                .build();

        when(productService.saveProduct(expected)).thenReturn(expected);

        ResponseEntity<Product> product = productController.createProduct(expected);
        assertEquals(expected, product.getBody());
        assertEquals(HttpStatus.OK, product.getStatusCode());
    }

    @Test
    @DisplayName("It should return BAD_REQUEST when [saveProduct] and skuCode is already in use")
    void itShouldReturnBadRequestWhenSkuCodeIsAlreadyInUse() {
        Product expected = Product.builder()
                .skuCode("skuCode")
                .name("name")
                .unitPrice(1.0f)
                .build();

        when(productService.saveProduct(expected)).thenThrow(IdAlreadyInUseException.class);

        ResponseEntity<Product> product = productController.createProduct(expected);
        assertEquals(HttpStatus.BAD_REQUEST, product.getStatusCode());
    }

    @Test
    @DisplayName("It should return OK when [delete product]")
    void itShouldReturnOkWhenDeleteProduct() {
        Product expected = Product.builder()
                .skuCode("skuCode")
                .name("name")
                .unitPrice(1.0f)
                .build();

        ResponseEntity<Object> response = productController.deleteProductBySku(expected.getSkuCode());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("It should return NOT_FOUND when [delete product] and skuCode is not found")
    void itShouldReturnNotFoundWhenDeleteProductAndWhenSkuCodeIsNotFound() {
        Product expected = Product.builder()
                .skuCode("skuCode")
                .name("name")
                .unitPrice(1.0f)
                .build();

        doThrow(NotFoundException.class).when(productService).deleteProductBySku(expected.getSkuCode());

        ResponseEntity<Object> response = productController.deleteProductBySku(expected.getSkuCode());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @DisplayName("It should return OK and Product when [find product by sku]")
    void itShouldReturnOkAndProductWhenFindProductBySku() {
        Product expected = Product.builder()
                .skuCode("skuCode")
                .name("name")
                .unitPrice(1.0f)
                .build();

        when(productService.findProductBySkuCode(expected.getSkuCode())).thenReturn(Optional.of(expected));

        ResponseEntity<Product> product = productController.findProductBySku(expected.getSkuCode());
        assertEquals(expected, product.getBody());
        assertEquals(HttpStatus.OK, product.getStatusCode());
    }

    @Test
    @DisplayName("It should return BAD_REQUEST when [find product by sku] and skuCode is not found")
    void itShouldReturnBadRequestAndProductWhenFindProductBySkuAndSkuCodeNotFound() {
        Product expected = Product.builder()
                .skuCode("skuCode")
                .name("name")
                .unitPrice(1.0f)
                .build();

        when(productService.findProductBySkuCode(expected.getSkuCode())).thenReturn(Optional.empty());

        ResponseEntity<Product> product = productController.findProductBySku(expected.getSkuCode());
        assertEquals(HttpStatus.NOT_FOUND, product.getStatusCode());
    }

    @Test
    @DisplayName("It should call [find orders by product sku] from orderService")
    void itShouldFindOrdersByProductSku() {
        productController.findOrdersByProductSku("skuCode");
        verify(orderService, times(1)).findOrdersByProductSku("skuCode");
    }
}