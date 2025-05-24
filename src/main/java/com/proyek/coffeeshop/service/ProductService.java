package com.proyek.coffeeshop.service;

import com.proyek.coffeeshop.dto.response.ProductDto;
import com.proyek.coffeeshop.model.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

/**
 * Service interface untuk operasi produk.
 * Menangani CRUD produk coffee shop.
 * 
 * @author Coffee Shop Team
 * @version 1.0
 */
public interface ProductService {

    /**
     * Mendapatkan semua produk dengan pagination.
     *
     * @param pageable informasi pagination
     * @return Page ProductDto semua produk
     */
    Page<ProductDto> getAllProducts(Pageable pageable);

    /**
     * Mendapatkan semua produk.
     *
     * @return List ProductDto semua produk
     */
    List<ProductDto> getAllProducts();

    /**
     * Mendapatkan produk berdasarkan ID.
     *
     * @param id ID produk
     * @return ProductDto data produk
     */
    ProductDto getProductById(Long id);

    /**
     * Mendapatkan produk berdasarkan kategori.
     *
     * @param categoryId ID kategori
     * @return List ProductDto produk dalam kategori tertentu
     */
    List<ProductDto> getProductsByCategory(Long categoryId);

    /**
     * Mencari produk berdasarkan nama.
     *
     * @param name nama produk yang dicari
     * @return List ProductDto produk yang cocok
     */
    List<ProductDto> searchProductsByName(String name);

    /**
     * Mencari produk berdasarkan range harga.
     *
     * @param minPrice harga minimum
     * @param maxPrice harga maksimum
     * @return List ProductDto produk dalam range harga
     */
    List<ProductDto> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice);

    /**
     * Membuat produk baru.
     *
     * @param productDto data produk baru
     * @return ProductDto produk yang telah dibuat
     */
    ProductDto createProduct(ProductDto productDto);

    /**
     * Update produk.
     *
     * @param id ID produk yang akan diupdate
     * @param productDto data produk baru
     * @return ProductDto produk yang telah diupdate
     */
    ProductDto updateProduct(Long id, ProductDto productDto);

    /**
     * Hapus produk.
     *
     * @param id ID produk yang akan dihapus
     */
    void deleteProduct(Long id);

    /**
     * Mendapatkan entitas produk berdasarkan ID.
     * Digunakan internal oleh service lain.
     *
     * @param id ID produk
     * @return Product entitas produk
     */
    Product getProductEntityById(Long id);
}
