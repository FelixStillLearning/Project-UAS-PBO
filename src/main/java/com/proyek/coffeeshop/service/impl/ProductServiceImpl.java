package com.proyek.coffeeshop.service.impl;

import com.proyek.coffeeshop.dto.response.CategoryDto;
import com.proyek.coffeeshop.dto.response.ProductDto;
import com.proyek.coffeeshop.exception.BadRequestException;
import com.proyek.coffeeshop.exception.ResourceNotFoundException;
import com.proyek.coffeeshop.model.entity.Category;
import com.proyek.coffeeshop.model.entity.Product;
import com.proyek.coffeeshop.repository.ProductRepository;
import com.proyek.coffeeshop.service.CategoryService;
import com.proyek.coffeeshop.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementasi service untuk operasi produk.
 * Menangani CRUD produk coffee shop.
 * 
 * @author Coffee Shop Team
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryService categoryService;    @Override
    @Transactional(readOnly = true)
    public Page<ProductDto> getAllProducts(Pageable pageable) {
        log.info("Getting all products with pagination");
        return productRepository.findAll(pageable)
                .map(this::convertToDto);
    }    @Override
    @Transactional(readOnly = true)
    public List<ProductDto> getAllProducts() {
        log.info("Getting all products");
        return productRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }    @Override
    @Transactional(readOnly = true)
    public ProductDto getProductById(Long id) {
        log.info("Getting product by ID: {}", id);
        Product product = getProductEntityById(id);
        return convertToDto(product);
    }    @Override
    @Transactional(readOnly = true)
    public List<ProductDto> getProductsByCategory(Long categoryId) {
        log.info("Getting products by category ID: {}", categoryId);
        
        Category category = categoryService.getCategoryEntityById(categoryId);
        return productRepository.findByCategory(category).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }    @Override
    @Transactional(readOnly = true)
    public List<ProductDto> searchProductsByName(String name) {
        log.info("Searching products by name: {}", name);
        return productRepository.findByNameContainingIgnoreCase(name).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }    @Override
    @Transactional(readOnly = true)
    public List<ProductDto> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        log.info("Getting products by price range: {} - {}", minPrice, maxPrice);
        
        if (minPrice.compareTo(maxPrice) > 0) {
            throw new BadRequestException("Harga minimum tidak boleh lebih besar dari harga maksimum");
        }
        
        return productRepository.findByPriceBetween(minPrice, maxPrice).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ProductDto createProduct(ProductDto productDto) {
        log.info("Creating new product: {}", productDto.getName());

        if (productRepository.existsByName(productDto.getName())) {
            throw new BadRequestException("Produk dengan nama '" + productDto.getName() + "' sudah ada");
        }

        Category category = categoryService.getCategoryEntityById(productDto.getCategory().getCategoryId());
        
        Product product = new Product();
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setImageUrl(productDto.getImageUrl());
        product.setCategory(category);

        Product savedProduct = productRepository.save(product);
        log.info("Successfully created product with ID: {}", savedProduct.getProductId());

        return convertToDto(savedProduct);
    }

    @Override
    @Transactional
    public ProductDto updateProduct(Long id, ProductDto productDto) {
        log.info("Updating product with ID: {}", id);

        Product product = getProductEntityById(id);

        // Check if new name already exists (excluding current product)
        if (!product.getName().equals(productDto.getName()) && 
            productRepository.existsByName(productDto.getName())) {
            throw new BadRequestException("Produk dengan nama '" + productDto.getName() + "' sudah ada");
        }

        Category category = categoryService.getCategoryEntityById(productDto.getCategory().getCategoryId());

        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setImageUrl(productDto.getImageUrl());
        product.setCategory(category);

        Product updatedProduct = productRepository.save(product);
        log.info("Successfully updated product with ID: {}", updatedProduct.getProductId());

        return convertToDto(updatedProduct);
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        log.info("Deleting product with ID: {}", id);

        Product product = getProductEntityById(id);
        productRepository.delete(product);
        log.info("Successfully deleted product with ID: {}", id);
    }    @Override
    @Transactional(readOnly = true)
    public Product getProductEntityById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produk tidak ditemukan dengan ID: " + id));
    }

    /**
     * Konversi Product entity ke ProductDto.
     */
    private ProductDto convertToDto(Product product) {
        CategoryDto categoryDto = new CategoryDto(
                product.getCategory().getCategoryId(),
                product.getCategory().getName()
        );

        return new ProductDto(
                product.getProductId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getImageUrl(),
                categoryDto
        );
    }
}
