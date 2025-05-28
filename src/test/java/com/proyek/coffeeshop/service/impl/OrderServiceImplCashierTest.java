package com.proyek.coffeeshop.service.impl;

import com.proyek.coffeeshop.dto.request.CashierOrderItemRequestDTO;
import com.proyek.coffeeshop.dto.request.CashierOrderRequestDTO;
import com.proyek.coffeeshop.dto.response.CashierOrderResponseDTO;
import com.proyek.coffeeshop.exception.BadRequestException;
import com.proyek.coffeeshop.exception.ResourceNotFoundException;
import com.proyek.coffeeshop.model.entity.*;
import com.proyek.coffeeshop.model.enums.OrderStatus;
import com.proyek.coffeeshop.model.enums.UserRole;
import com.proyek.coffeeshop.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests untuk OrderServiceImpl - createCashierOrder method
 * Validasi business logic dan data flow untuk fitur kasir
 */
@ExtendWith(MockitoExtension.class)
class OrderServiceImplCashierTest {

    @Mock
    private OrderRepository orderRepository;
    
    @Mock
    private OrderDetailRepository orderDetailRepository;
    
    @Mock
    private OrderDetailCustomizationRepository orderDetailCustomizationRepository;
    
    @Mock
    private UserRepository userRepository;
    
    @Mock
    private ProductRepository productRepository;
    
    @Mock
    private CustomizationRepository customizationRepository;
    
    @Mock
    private PaymentMethodRepository paymentMethodRepository;
    
    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    private User kasirUser;
    private PaymentMethod cashPaymentMethod;
    private Product product;
    private Customization customization;
    private CashierOrderRequestDTO validRequest;

    @BeforeEach
    void setUp() {
        // Setup kasir user
        kasirUser = new User();
        kasirUser.setUserId(1L);
        kasirUser.setUsername("kasir001");
        kasirUser.setRole(UserRole.ROLE_KASIR);

        // Setup payment method
        cashPaymentMethod = new PaymentMethod();
        cashPaymentMethod.setPaymentId(1L);
        cashPaymentMethod.setName("Cash");
        cashPaymentMethod.setDescription("Pembayaran Tunai");

        // Setup product
        product = new Product();
        product.setProductId(1L);
        product.setName("Espresso");
        product.setPrice(new BigDecimal("15000"));
        product.setAvailable(true);

        // Setup customization
        customization = new Customization();
        customization.setCustomizationId(1L);
        customization.setName("Extra Shot");
        customization.setPriceAdjustment(new BigDecimal("5000"));

        // Setup valid request
        CashierOrderItemRequestDTO item = new CashierOrderItemRequestDTO();
        item.setProductId(1L);
        item.setQuantity(2);
        item.setCustomizationIds(Arrays.asList(1L));

        validRequest = new CashierOrderRequestDTO();
        validRequest.setOrderItems(Arrays.asList(item));
        validRequest.setPaymentMethodName("Cash");
        validRequest.setAmountTendered(new BigDecimal("50000"));
        validRequest.setCustomerNotes("Walk-in customer order");
    }

    @Test
    void createCashierOrder_Success() {
        // Arrange
        when(userRepository.findByUsername("kasir001")).thenReturn(Optional.of(kasirUser));
        when(paymentMethodRepository.findByName("Cash")).thenReturn(Optional.of(cashPaymentMethod));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(customizationRepository.findById(1L)).thenReturn(Optional.of(customization));
        
        Order savedOrder = new Order();
        savedOrder.setOrderId(1L);
        savedOrder.setProcessedByKasir(kasirUser);
        savedOrder.setPaymentMethod(cashPaymentMethod);
        savedOrder.setStatus(OrderStatus.PAID);
        savedOrder.setOrderDate(LocalDateTime.now());
        savedOrder.setTotalAmount(new BigDecimal("40000")); // 2 * (15000 + 5000)
        savedOrder.setAmountTendered(new BigDecimal("50000"));
        savedOrder.setChangeGiven(new BigDecimal("10000"));
        savedOrder.setCustomerNotes("Walk-in customer order");
        
        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);
        when(orderDetailRepository.save(any(OrderDetail.class))).thenAnswer(invocation -> {
            OrderDetail detail = invocation.getArgument(0);
            detail.setDetailId(1L);
            detail.setSubtotalPrice(new BigDecimal("40000"));
            return detail;
        });
        when(orderDetailCustomizationRepository.save(any(OrderDetailCustomization.class))).thenAnswer(invocation -> {
            OrderDetailCustomization odc = invocation.getArgument(0);
            odc.setDetailCustomizationId(1L);
            return odc;
        });

        // Act
        CashierOrderResponseDTO result = orderService.createCashierOrder(validRequest, "kasir001");

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getOrderId());
        assertEquals("kasir001", result.getKasirUsername());
        assertEquals(new BigDecimal("40000"), result.getTotalPrice());
        assertEquals("Cash", result.getPaymentMethodName());
        assertEquals(OrderStatus.PAID, result.getStatus());
        assertEquals(new BigDecimal("50000"), result.getAmountTendered());
        assertEquals(new BigDecimal("10000"), result.getChangeGiven());
        assertEquals("Walk-in customer order", result.getCustomerNotes());
          // Verify interactions
        verify(userRepository).findByUsername("kasir001");
        verify(paymentMethodRepository).findByName("Cash");
        verify(productRepository).findById(1L);
        verify(customizationRepository).findById(1L);
        verify(orderRepository, times(2)).save(any(Order.class));
        verify(orderDetailRepository, times(1)).save(any(OrderDetail.class)); // Fixed: now only saves once
        verify(orderDetailCustomizationRepository).save(any(OrderDetailCustomization.class));
    }

    @Test
    void createCashierOrder_InvalidKasir_ThrowsException() {
        // Arrange
        when(userRepository.findByUsername("invalid_kasir")).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> orderService.createCashierOrder(validRequest, "invalid_kasir")
        );
        
        assertEquals("Kasir tidak ditemukan: invalid_kasir", exception.getMessage());
    }

    @Test
    void createCashierOrder_UserNotKasir_ThrowsException() {
        // Arrange
        User nonKasirUser = new User();
        nonKasirUser.setUserId(2L);
        nonKasirUser.setUsername("customer001");
        nonKasirUser.setRole(UserRole.ROLE_CUSTOMER);
        
        when(userRepository.findByUsername("customer001")).thenReturn(Optional.of(nonKasirUser));

        // Act & Assert
        BadRequestException exception = assertThrows(
            BadRequestException.class,
            () -> orderService.createCashierOrder(validRequest, "customer001")
        );
        
        assertEquals("User yang memproses bukan kasir.", exception.getMessage());
    }

    @Test
    void createCashierOrder_PaymentMethodNotFound_ThrowsException() {
        // Arrange
        when(userRepository.findByUsername("kasir001")).thenReturn(Optional.of(kasirUser));
        when(paymentMethodRepository.findByName("InvalidPayment")).thenReturn(Optional.empty());
        
        validRequest.setPaymentMethodName("InvalidPayment");

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> orderService.createCashierOrder(validRequest, "kasir001")
        );
        
        assertEquals("Metode pembayaran tidak ditemukan: InvalidPayment", exception.getMessage());
    }

    @Test
    void createCashierOrder_ProductNotAvailable_ThrowsException() {
        // Arrange
        when(userRepository.findByUsername("kasir001")).thenReturn(Optional.of(kasirUser));
        when(paymentMethodRepository.findByName("Cash")).thenReturn(Optional.of(cashPaymentMethod));
        
        Product unavailableProduct = new Product();
        unavailableProduct.setProductId(1L);
        unavailableProduct.setName("Espresso");
        unavailableProduct.setAvailable(false);
        
        when(productRepository.findById(1L)).thenReturn(Optional.of(unavailableProduct));

        // Act & Assert
        BadRequestException exception = assertThrows(
            BadRequestException.class,
            () -> orderService.createCashierOrder(validRequest, "kasir001")
        );
        
        assertEquals("Produk Espresso sedang tidak tersedia.", exception.getMessage());
    }

    @Test
    void createCashierOrder_InsufficientCashPayment_ThrowsException() {
        // Arrange
        when(userRepository.findByUsername("kasir001")).thenReturn(Optional.of(kasirUser));
        when(paymentMethodRepository.findByName("Cash")).thenReturn(Optional.of(cashPaymentMethod));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(customizationRepository.findById(1L)).thenReturn(Optional.of(customization));
        
        Order savedOrder = new Order();
        savedOrder.setOrderId(1L);
        savedOrder.setTotalAmount(new BigDecimal("40000"));
        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);
        when(orderDetailRepository.save(any(OrderDetail.class))).thenAnswer(invocation -> {
            OrderDetail detail = invocation.getArgument(0);
            detail.setDetailId(1L);
            detail.setSubtotalPrice(new BigDecimal("40000"));
            return detail;
        });
        when(orderDetailCustomizationRepository.save(any(OrderDetailCustomization.class))).thenAnswer(invocation -> {
            OrderDetailCustomization odc = invocation.getArgument(0);
            odc.setDetailCustomizationId(1L);
            return odc;
        });
        
        // Set insufficient amount
        validRequest.setAmountTendered(new BigDecimal("30000")); // Less than 40000

        // Act & Assert
        BadRequestException exception = assertThrows(
            BadRequestException.class,
            () -> orderService.createCashierOrder(validRequest, "kasir001")
        );
        
        assertEquals("Jumlah uang yang dibayarkan (amountTendered) kurang dari total belanja.", exception.getMessage());
    }

    @Test
    void createCashierOrder_CashPaymentWithoutAmount_ThrowsException() {
        // Arrange
        when(userRepository.findByUsername("kasir001")).thenReturn(Optional.of(kasirUser));
        when(paymentMethodRepository.findByName("Cash")).thenReturn(Optional.of(cashPaymentMethod));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(customizationRepository.findById(1L)).thenReturn(Optional.of(customization));
        
        Order savedOrder = new Order();
        savedOrder.setOrderId(1L);
        savedOrder.setTotalAmount(new BigDecimal("40000"));
        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);
        when(orderDetailRepository.save(any(OrderDetail.class))).thenAnswer(invocation -> {
            OrderDetail detail = invocation.getArgument(0);
            detail.setDetailId(1L);
            detail.setSubtotalPrice(new BigDecimal("40000"));
            return detail;
        });
        when(orderDetailCustomizationRepository.save(any(OrderDetailCustomization.class))).thenAnswer(invocation -> {
            OrderDetailCustomization odc = invocation.getArgument(0);
            odc.setDetailCustomizationId(1L);
            return odc;
        });
        
        // Set null amount for cash payment
        validRequest.setAmountTendered(null);

        // Act & Assert
        BadRequestException exception = assertThrows(
            BadRequestException.class,
            () -> orderService.createCashierOrder(validRequest, "kasir001")
        );
        
        assertEquals("Untuk pembayaran tunai, jumlah uang yang dibayarkan (amountTendered) harus diisi dan mencukupi.", exception.getMessage());
    }
}
