package com.proyek.coffeeshop.controller;

import com.proyek.coffeeshop.dto.request.ProductRequestDto;
import com.proyek.coffeeshop.dto.response.CategoryDto;
import com.proyek.coffeeshop.dto.response.ProductDto;
import com.proyek.coffeeshop.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * REST Controller untuk operasi produk.
 * Menangani CRUD produk coffee shop.
 * 
 * @author Coffee Shop Team
 * @version 1.0
 */
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class ProductController {

    private final ProductService productService;

    /**
     * Endpoint untuk mendapatkan semua produk dengan pagination.
     * Dapat diakses oleh semua user yang sudah login.
     *
     * @param pageable informasi pagination
     * @return ResponseEntity dengan Page produk
     */
    @GetMapping("/page")
    public ResponseEntity<Page<ProductDto>> getAllProductsWithPagination(
            @PageableDefault(size = 10, sort = "name") Pageable pageable) {
        log.info("GET /api/products/page - Getting all products with pagination");
        
        Page<ProductDto> products = productService.getAllProducts(pageable);
        return ResponseEntity.ok(products);
    }

    /**
     * Endpoint untuk mendapatkan semua produk.
     * Dapat diakses oleh semua user yang sudah login.
     *
     * @return ResponseEntity dengan list produk
     */
    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        log.info("GET /api/products - Getting all products");
        
        List<ProductDto> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    /**
     * Endpoint untuk mendapatkan produk berdasarkan ID.
     * Dapat diakses oleh semua user yang sudah login.
     *
     * @param id ID produk
     * @return ResponseEntity dengan data produk
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long id) {
        log.info("GET /api/products/{} - Getting product by ID", id);
        
        ProductDto product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    /**
     * Endpoint untuk mendapatkan produk berdasarkan kategori.
     * Dapat diakses oleh semua user yang sudah login.
     *
     * @param categoryId ID kategori
     * @return ResponseEntity dengan list produk
     */
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ProductDto>> getProductsByCategory(@PathVariable Long categoryId) {
        log.info("GET /api/products/category/{} - Getting products by category", categoryId);
        
        List<ProductDto> products = productService.getProductsByCategory(categoryId);
        return ResponseEntity.ok(products);
    }

    /**
     * Endpoint untuk mencari produk berdasarkan nama.
     * Dapat diakses oleh semua user yang sudah login.
     *
     * @param name nama produk yang dicari
     * @return ResponseEntity dengan list produk
     */
    @GetMapping("/search")
    public ResponseEntity<List<ProductDto>> searchProductsByName(@RequestParam String name) {
        log.info("GET /api/products/search?name={} - Searching products by name", name);
        
        List<ProductDto> products = productService.searchProductsByName(name);
        return ResponseEntity.ok(products);
    }

    /**
     * Endpoint untuk mendapatkan produk berdasarkan range harga.
     * Dapat diakses oleh semua user yang sudah login.
     *
     * @param minPrice harga minimum
     * @param maxPrice harga maksimum
     * @return ResponseEntity dengan list produk
     */
    @GetMapping("/price-range")
    public ResponseEntity<List<ProductDto>> getProductsByPriceRange(
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice) {
        log.info("GET /api/products/price-range?minPrice={}&maxPrice={} - Getting products by price range", 
                minPrice, maxPrice);
        
        List<ProductDto> products = productService.getProductsByPriceRange(minPrice, maxPrice);
        return ResponseEntity.ok(products);
    }

    /**
     * Endpoint untuk membuat produk baru.
     * Hanya dapat diakses oleh admin.
     *
     * @param request data produk baru
     * @return ResponseEntity dengan produk yang telah dibuat
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductDto> createProduct(@Valid @RequestBody ProductRequestDto request) {
        log.info("POST /api/products - Creating product: {}", request.getName());
        
        CategoryDto categoryDto = new CategoryDto(request.getCategoryId(), null);
        ProductDto productDto = new ProductDto(
                null,
                request.getName(),
                request.getDescription(),
                request.getPrice(),
                request.getImageUrl(),
                categoryDto
        );
        
        ProductDto response = productService.createProduct(productDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Endpoint untuk update produk.
     * Hanya dapat diakses oleh admin.
     *
     * @param id ID produk yang akan diupdate
     * @param request data produk baru
     * @return ResponseEntity dengan produk yang telah diupdate
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductDto> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequestDto request) {
        log.info("PUT /api/products/{} - Updating product", id);
        
        CategoryDto categoryDto = new CategoryDto(request.getCategoryId(), null);
        ProductDto productDto = new ProductDto(
                id,
                request.getName(),
                request.getDescription(),
                request.getPrice(),
                request.getImageUrl(),
                categoryDto
        );
        
        ProductDto response = productService.updateProduct(id, productDto);
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint untuk menghapus produk.
     * Hanya dapat diakses oleh admin.
     *
     * @param id ID produk yang akan dihapus
     * @return ResponseEntity dengan status penghapusan
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        log.info("DELETE /api/products/{} - Deleting product", id);
        
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
