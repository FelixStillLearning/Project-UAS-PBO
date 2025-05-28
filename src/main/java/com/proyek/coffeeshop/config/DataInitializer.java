package com.proyek.coffeeshop.config;

import com.proyek.coffeeshop.model.entity.*;
import com.proyek.coffeeshop.model.enums.UserRole;
import com.proyek.coffeeshop.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Component untuk menginisialisasi data awal aplikasi.
 * Membuat data admin, kategori, produk, metode pembayaran, dan kustomisasi.
 * 
 * @author Coffee Shop Team
 * @version 1.0
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final CustomizationRepository customizationRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            log.info("Initializing application data...");
            
            createAdminUser();
            createKasirUser(); // Panggil method untuk membuat user kasir
            createCategories();
            createPaymentMethods();
            createCustomizations();
            createProducts();
            
            log.info("Data initialization completed.");
        }
    }

    private void createAdminUser() {
        // Create admin user
        User adminUser = new User();
        adminUser.setUsername("admin");
        adminUser.setEmail("admin@coffeeshop.com");
        adminUser.setPassword(passwordEncoder.encode("admin123"));
        adminUser.setRole(UserRole.ROLE_ADMIN);
        adminUser.setCreatedAt(LocalDateTime.now());
        adminUser.setUpdatedAt(LocalDateTime.now());
        userRepository.save(adminUser);
        
        log.info("Admin user created: username=admin, password=admin123");
    }

    private void createKasirUser() {
        // Create kasir user
        User kasirUser = new User();
        kasirUser.setUsername("kasir01");
        kasirUser.setEmail("kasir01@coffeeshop.com");
        kasirUser.setPassword(passwordEncoder.encode("kasir123"));
        kasirUser.setRole(UserRole.ROLE_KASIR);
        kasirUser.setCreatedAt(LocalDateTime.now());
        kasirUser.setUpdatedAt(LocalDateTime.now());
        userRepository.save(kasirUser);
        
        log.info("Kasir user created: username=kasir01, password=kasir123");
    }

    private void createCategories() {
        String[] categoryNames = {
            "Coffee", "Tea", "Pastry", "Snacks", "Cold Drinks"
        };
        
        for (String name : categoryNames) {
            Category category = new Category();
            category.setName(name);
            category.setDescription("Premium " + name.toLowerCase() + " selection");
            categoryRepository.save(category);
        }
        
        log.info("Categories created: {}", String.join(", ", categoryNames));
    }

    private void createPaymentMethods() {
        String[][] paymentMethods = {
            {"Cash", "Cash payment"},
            {"Credit Card", "Visa, Mastercard, etc."},
            {"Debit Card", "Bank debit card"},
            {"Digital Wallet", "GoPay, OVO, DANA, etc."},
            {"Bank Transfer", "Internet banking transfer"}
        };
        
        for (String[] pm : paymentMethods) {
            PaymentMethod paymentMethod = new PaymentMethod();
            paymentMethod.setName(pm[0]);
            paymentMethod.setDescription(pm[1]);
            paymentMethodRepository.save(paymentMethod);
        }
        
        log.info("Payment methods created");
    }

    private void createCustomizations() {
        // Size customizations
        createCustomization("Small Size", "size", new BigDecimal("-5000"));
        createCustomization("Large Size", "size", new BigDecimal("8000"));
        createCustomization("Extra Large", "size", new BigDecimal("15000"));
        
        // Sugar level
        createCustomization("No Sugar", "sugar", BigDecimal.ZERO);
        createCustomization("Less Sugar", "sugar", BigDecimal.ZERO);
        createCustomization("Normal Sugar", "sugar", BigDecimal.ZERO);
        createCustomization("Extra Sugar", "sugar", BigDecimal.ZERO);
        
        // Temperature
        createCustomization("Hot", "temperature", BigDecimal.ZERO);
        createCustomization("Iced", "temperature", BigDecimal.ZERO);
        createCustomization("Extra Hot", "temperature", new BigDecimal("2000"));
        
        // Add-ons
        createCustomization("Extra Shot", "addon", new BigDecimal("8000"));
        createCustomization("Whipped Cream", "addon", new BigDecimal("5000"));
        createCustomization("Extra Foam", "addon", new BigDecimal("3000"));
        createCustomization("Vanilla Syrup", "addon", new BigDecimal("5000"));
        createCustomization("Caramel Syrup", "addon", new BigDecimal("5000"));
        
        log.info("Customizations created");
    }

    private void createCustomization(String name, String type, BigDecimal price) {
        Customization customization = new Customization();
        customization.setName(name);
        customization.setType(type);
        customization.setPriceAdjustment(price);
        customizationRepository.save(customization);
    }

    private void createProducts() {
        // Get categories
        Category coffee = categoryRepository.findByName("Coffee").orElse(null);
        Category tea = categoryRepository.findByName("Tea").orElse(null);
        Category pastry = categoryRepository.findByName("Pastry").orElse(null);
        Category snacks = categoryRepository.findByName("Snacks").orElse(null);
        Category coldDrinks = categoryRepository.findByName("Cold Drinks").orElse(null);        if (coffee != null) {
            createProduct("Espresso", "Strong black coffee", new BigDecimal("25000"), coffee, 60, 15, 120);
            createProduct("Americano", "Espresso with hot water", new BigDecimal("28000"), coffee, 55, 12, 100);
            createProduct("Cappuccino", "Espresso with steamed milk and foam", new BigDecimal("35000"), coffee, 45, 10, 90);
            createProduct("Latte", "Espresso with steamed milk", new BigDecimal("38000"), coffee, 50, 12, 100);
            createProduct("Macchiato", "Espresso with a spot of steamed milk", new BigDecimal("40000"), coffee, 35, 8, 80);
            createProduct("Mocha", "Espresso with chocolate and steamed milk", new BigDecimal("42000"), coffee, 40, 10, 85);
        }

        if (tea != null) {
            createProduct("Earl Grey", "Classic black tea with bergamot", new BigDecimal("20000"), tea, 30, 8, 60);
            createProduct("Green Tea", "Fresh green tea", new BigDecimal("18000"), tea, 35, 10, 70);
            createProduct("Chamomile Tea", "Relaxing herbal tea", new BigDecimal("22000"), tea, 25, 6, 50);
            createProduct("Jasmine Tea", "Fragrant jasmine tea", new BigDecimal("25000"), tea, 28, 7, 55);
        }

        if (pastry != null) {
            createProduct("Croissant", "Buttery French pastry", new BigDecimal("15000"), pastry, 20, 5, 40);
            createProduct("Muffin", "Blueberry muffin", new BigDecimal("18000"), pastry, 25, 6, 50);
            createProduct("Danish", "Sweet pastry with fruit", new BigDecimal("20000"), pastry, 18, 4, 35);
            createProduct("Scone", "Traditional British pastry", new BigDecimal("16000"), pastry, 22, 5, 45);
        }

        if (snacks != null) {
            createProduct("Sandwich", "Grilled chicken sandwich", new BigDecimal("35000"), snacks, 15, 3, 30);
            createProduct("Salad", "Fresh garden salad", new BigDecimal("28000"), snacks, 12, 3, 25);
            createProduct("Cookies", "Chocolate chip cookies", new BigDecimal("12000"), snacks, 40, 10, 80);
        }        if (coldDrinks != null) {
            createProduct("Iced Coffee", "Cold brew coffee", new BigDecimal("30000"), coldDrinks, 35, 8, 70);
            createProduct("Smoothie", "Mixed fruit smoothie", new BigDecimal("32000"), coldDrinks, 20, 5, 40);
            createProduct("Iced Tea", "Refreshing iced tea", new BigDecimal("18000"), coldDrinks, 30, 7, 60);
            createProduct("Milkshake", "Vanilla milkshake", new BigDecimal("35000"), coldDrinks, 25, 6, 50);
        }        log.info("Products created");
    }

    // Create product with specific stock parameters
    private void createProduct(String name, String description, BigDecimal price, Category category, 
                              int stockQuantity, int minStock, int maxStock) {
        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setCategory(category);
        product.setAvailable(true);
        product.setStockQuantity(stockQuantity);
        product.setMinStockLevel(minStock);
        product.setMaxStockLevel(maxStock);
        productRepository.save(product);
    }
}
