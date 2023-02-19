package com.ordermanagement.product;

import com.ordermanagement.exception.IdAlreadyInUseException;
import com.ordermanagement.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product saveProduct(Product product) {
        Optional<Product> found_product = productRepository.findById(product.getSkuCode());
        if (found_product.isPresent()) {
            throw new IdAlreadyInUseException("Product with sku [%s] already exists".formatted(product.getSkuCode()));
        }
        return productRepository.save(product);
    }

    public Optional<Product> findProductBySkuCode(String skuCode) {
        return productRepository.findById(skuCode);
    }

    public void deleteProductBySku(String skuCode) {
        Optional<Product> found_product = productRepository.findById(skuCode);
        if (found_product.isEmpty()) {
            throw new NotFoundException("Product with sku [%s] not found".formatted(skuCode));
        }
        productRepository.deleteById(skuCode);
    }

    public Product updateProduct(Product product) {
        Optional<Product> found_product = productRepository.findById(product.getSkuCode());
        if (found_product.isEmpty()) {
            throw new NotFoundException("Product with sku [%s] not found".formatted(product.getSkuCode()));
        }
        return productRepository.save(product);
    }
}
