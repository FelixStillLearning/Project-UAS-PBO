package com.proyek.coffeeshop.service.impl;

import com.proyek.coffeeshop.dto.response.DailyKasirReportDTO;
import com.proyek.coffeeshop.dto.response.ProductSalesReportDTO;
import com.proyek.coffeeshop.dto.response.TransactionReportDTO;
import com.proyek.coffeeshop.model.entity.Order;
import com.proyek.coffeeshop.model.entity.OrderDetail;
import com.proyek.coffeeshop.model.entity.Product;
import com.proyek.coffeeshop.repository.OrderRepository;
import com.proyek.coffeeshop.repository.ProductRepository;
import com.proyek.coffeeshop.repository.UserRepository;
import com.proyek.coffeeshop.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Override    public DailyKasirReportDTO generateDailyKasirReport(Long kasirId, LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
        
        // Get orders processed by the kasir on the specified date
        List<Order> kasirOrders = orderRepository.findByProcessedByKasirIdAndOrderDateBetween(
            kasirId, startOfDay, endOfDay);

        DailyKasirReportDTO report = new DailyKasirReportDTO();
        report.setReportDate(date);
        report.setKasirId(kasirId);

        // Get kasir name
        userRepository.findById(kasirId).ifPresent(kasir -> report.setKasirName(kasir.getUsername()));

        if (kasirOrders.isEmpty()) {
            report.setTotalOrders(0);
            report.setTotalRevenue(BigDecimal.ZERO);
            report.setAverageOrderValue(BigDecimal.ZERO);
            report.setTotalItemsSold(0);
            return report;
        }

        // Calculate statistics
        int totalOrders = kasirOrders.size();
        BigDecimal totalRevenue = kasirOrders.stream()
            .map(Order::getTotalAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal averageOrderValue = totalRevenue.divide(
            BigDecimal.valueOf(totalOrders), 2, RoundingMode.HALF_UP);        int totalItemsSold = kasirOrders.stream()
            .flatMap(order -> order.getOrderDetails().stream())
            .mapToInt(OrderDetail::getQuantity)
            .sum();

        // Find most popular product
        Map<String, Integer> productSales = new HashMap<>();
        kasirOrders.stream()
            .flatMap(order -> order.getOrderDetails().stream())
            .forEach(item -> {
                String productName = item.getProduct().getName();
                productSales.merge(productName, item.getQuantity(), Integer::sum);
            });

        String mostPopularProduct = productSales.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse("N/A");        // Set report values
        report.setTotalOrders(totalOrders);
        report.setTotalRevenue(totalRevenue);
        report.setAverageOrderValue(averageOrderValue);
        report.setTotalItemsSold(totalItemsSold);
        report.setMostPopularProduct(mostPopularProduct);

        return report;
    }

    @Override
    public TransactionReportDTO generateTransactionReport(LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

        List<Order> orders = orderRepository.findByOrderDateBetween(startDateTime, endDateTime);

        TransactionReportDTO report = new TransactionReportDTO();
        report.setReportDate(startDate); // Using start date as report date

        if (orders.isEmpty()) {
            report.setTotalTransactions(0);
            report.setTotalRevenue(BigDecimal.ZERO);
            report.setAverageOrderValue(BigDecimal.ZERO);
            report.setTotalItemsSold(0);
            report.setMostPopularProduct("N/A");
            report.setPaymentMethodBreakdown("No transactions");
            return report;
        }

        // Calculate statistics
        int totalTransactions = orders.size();
        BigDecimal totalRevenue = orders.stream()
            .map(Order::getTotalAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal averageOrderValue = totalRevenue.divide(
            BigDecimal.valueOf(totalTransactions), 2, RoundingMode.HALF_UP);        int totalItemsSold = orders.stream()
            .flatMap(order -> order.getOrderDetails().stream())
            .mapToInt(OrderDetail::getQuantity)
            .sum();

        // Find most popular product
        Map<String, Integer> productSales = new HashMap<>();
        orders.stream()
            .flatMap(order -> order.getOrderDetails().stream())
            .forEach(item -> {
                String productName = item.getProduct().getName();
                productSales.merge(productName, item.getQuantity(), Integer::sum);
            });

        String mostPopularProduct = productSales.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse("N/A");        // Payment method breakdown
        Map<String, Long> paymentMethods = orders.stream()
            .collect(Collectors.groupingBy(order -> order.getPaymentMethod().getName(), Collectors.counting()));

        String paymentMethodBreakdown = paymentMethods.entrySet().stream()
            .map(entry -> entry.getKey() + ": " + entry.getValue())
            .collect(Collectors.joining(", "));

        // Set report values
        report.setTotalTransactions(totalTransactions);
        report.setTotalRevenue(totalRevenue);
        report.setAverageOrderValue(averageOrderValue);
        report.setTotalItemsSold(totalItemsSold);
        report.setMostPopularProduct(mostPopularProduct);
        report.setPaymentMethodBreakdown(paymentMethodBreakdown);

        return report;
    }

    @Override
    public TransactionReportDTO generateDailyTransactionReport(LocalDate date) {
        return generateTransactionReport(date, date);
    }

    @Override
    public List<ProductSalesReportDTO> generateProductSalesReport(LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

        List<Order> orders = orderRepository.findByOrderDateBetween(startDateTime, endDateTime);

        // Calculate sales for each product
        Map<Long, ProductSalesData> productSalesMap = new HashMap<>();        orders.stream()
            .flatMap(order -> order.getOrderDetails().stream())
            .forEach(item -> {
                Long productId = item.getProduct().getProductId();
                ProductSalesData salesData = productSalesMap.computeIfAbsent(productId, 
                    id -> new ProductSalesData(item.getProduct()));
                
                salesData.addSale(item.getQuantity(), item.getUnitPrice());
            });// Convert to DTO list
        return productSalesMap.values().stream()
            .map(salesData -> new ProductSalesReportDTO(
                salesData.getProduct().getProductId(),
                salesData.getProduct().getName(),
                salesData.getProduct().getCategory().getName(),
                salesData.getQuantitySold(),
                salesData.getProduct().getPrice(),
                salesData.getTotalRevenue(),
                salesData.getProduct().getStockQuantity(),
                salesData.getProduct().isLowStock(),
                startDate
            ))
            .collect(Collectors.toList());
    }

    @Override
    public List<ProductSalesReportDTO> generateDailyProductSalesReport(LocalDate date) {
        return generateProductSalesReport(date, date);
    }

    @Override
    public List<ProductSalesReportDTO> generateTopSellingProductsReport(LocalDate startDate, LocalDate endDate, int limit) {
        List<ProductSalesReportDTO> productSales = generateProductSalesReport(startDate, endDate);
        
        return productSales.stream()
            .sorted((p1, p2) -> Integer.compare(p2.getQuantitySold(), p1.getQuantitySold()))
            .limit(limit)
            .collect(Collectors.toList());
    }

    @Override
    public List<ProductSalesReportDTO> generateLowStockReport() {
        List<Product> lowStockProducts = productRepository.findByStockQuantityLessThanMinStockLevel();
        LocalDate today = LocalDate.now();        return lowStockProducts.stream()
            .map(product -> new ProductSalesReportDTO(
                product.getProductId(),
                product.getName(),
                product.getCategory().getName(),
                0, // No sales data for low stock report
                product.getPrice(),
                BigDecimal.ZERO, // No revenue data for low stock report
                product.getStockQuantity(),
                product.isLowStock(),
                today
            ))
            .collect(Collectors.toList());
    }

    // Helper class to track product sales data
    private static class ProductSalesData {
        private final Product product;
        private int quantitySold = 0;
        private BigDecimal totalRevenue = BigDecimal.ZERO;

        public ProductSalesData(Product product) {
            this.product = product;
        }

        public void addSale(int quantity, BigDecimal price) {
            this.quantitySold += quantity;
            this.totalRevenue = this.totalRevenue.add(price.multiply(BigDecimal.valueOf(quantity)));
        }

        public Product getProduct() {
            return product;
        }

        public int getQuantitySold() {
            return quantitySold;
        }

        public BigDecimal getTotalRevenue() {
            return totalRevenue;
        }
    }
}
