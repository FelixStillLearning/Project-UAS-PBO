package com.proyek.coffeeshop.service.impl;

import com.proyek.coffeeshop.dto.request.OrderDetailCustomizationRequestDto;
import com.proyek.coffeeshop.dto.request.OrderDetailRequestDto;
import com.proyek.coffeeshop.dto.request.OrderRequestDto;
import com.proyek.coffeeshop.dto.response.*;
import com.proyek.coffeeshop.exception.BadRequestException;
import com.proyek.coffeeshop.exception.ResourceNotFoundException;
import com.proyek.coffeeshop.model.entity.*;
import com.proyek.coffeeshop.model.enums.OrderStatus;
import com.proyek.coffeeshop.model.enums.UserRole;
import com.proyek.coffeeshop.repository.*;
import com.proyek.coffeeshop.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementasi service untuk operasi order.
 * Menangani pembuatan, update, dan pencarian order.
 * 
 * @author Coffee Shop Team
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final OrderDetailCustomizationRepository orderDetailCustomizationRepository;
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final CustomizationRepository customizationRepository;
    private final PaymentMethodRepository paymentMethodRepository;

    @Override
    @Transactional
    public OrderResponseDto createOrder(OrderRequestDto request, String username) {
        log.info("Creating new order for user: {}", username);

        // Get customer
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User tidak ditemukan: " + username));
        
        if (user.getRole() != UserRole.ROLE_CUSTOMER) {
            throw new BadRequestException("Hanya customer yang dapat membuat order");
        }

        Customer customer = customerRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Customer tidak ditemukan"));

        // Get payment method
        PaymentMethod paymentMethod = paymentMethodRepository.findById(request.getPaymentMethodId())
                .orElseThrow(() -> new ResourceNotFoundException("Metode pembayaran tidak ditemukan"));

        // Create order
        Order order = new Order();
        order.setCustomer(customer);
        order.setOrderDate(LocalDateTime.now());
        order.setPaymentMethod(paymentMethod);
        order.setStatus(OrderStatus.WAITING_PAYMENT);
        order.setCustomerNotes(request.getCustomerNotes());

        Order savedOrder = orderRepository.save(order);

        // Create order details
        List<OrderDetail> orderDetails = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (OrderDetailRequestDto itemRequest : request.getItems()) {
            OrderDetail orderDetail = createOrderDetail(savedOrder, itemRequest);
            orderDetails.add(orderDetail);
            totalAmount = totalAmount.add(orderDetail.getSubtotalPrice());
        }

        // Update total amount
        savedOrder.setTotalAmount(totalAmount);
        savedOrder.setOrderDetails(orderDetails);
        savedOrder = orderRepository.save(savedOrder);

        log.info("Successfully created order with ID: {}", savedOrder.getOrderId());
        return convertToOrderResponseDto(savedOrder);
    }

    @Override
    public List<OrderResponseDto> getOrdersByCustomer(String username) {
        log.info("Getting orders for customer: {}", username);
        
        List<Order> orders = orderRepository.findByCustomerUsername(username);
        return orders.stream()
                .map(this::convertToOrderResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public OrderResponseDto getOrderDetailsById(Long orderId) {
        log.info("Getting order details by ID: {}", orderId);
        
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order tidak ditemukan dengan ID: " + orderId));
        
        return convertToOrderResponseDto(order);
    }

    @Override
    public Page<OrderResponseDto> getAllOrders(Pageable pageable) {
        log.info("Getting all orders with pagination");
        
        return orderRepository.findAllOrderByDateDesc(pageable)
                .map(this::convertToOrderResponseDto);
    }

    @Override
    public Page<OrderResponseDto> getOrdersByStatus(OrderStatus status, Pageable pageable) {
        log.info("Getting orders by status: {}", status);
        
        return orderRepository.findByStatus(status, pageable)
                .map(this::convertToOrderResponseDto);
    }

    @Override
    @Transactional
    public OrderResponseDto updateOrderStatus(Long orderId, OrderStatus newStatus) {
        log.info("Updating order status for order ID: {} to {}", orderId, newStatus);
        
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order tidak ditemukan dengan ID: " + orderId));
        
        // Business logic for status transitions
        validateStatusTransition(order.getStatus(), newStatus);
        
        order.setStatus(newStatus);
        Order updatedOrder = orderRepository.save(order);
        
        log.info("Successfully updated order status for order ID: {}", orderId);
        return convertToOrderResponseDto(updatedOrder);
    }

    @Override
    @Transactional
    public OrderResponseDto confirmOrderPayment(Long orderId) {
        log.info("Confirming payment for order ID: {}", orderId);
        
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order tidak ditemukan dengan ID: " + orderId));
        
        if (order.getStatus() != OrderStatus.WAITING_PAYMENT) {
            throw new BadRequestException("Order tidak dalam status menunggu pembayaran");
        }
        
        order.setStatus(OrderStatus.PROCESSING);
        Order updatedOrder = orderRepository.save(order);
        
        log.info("Successfully confirmed payment for order ID: {}", orderId);
        return convertToOrderResponseDto(updatedOrder);
    }

    @Override
    public List<OrderResponseDto> getOrdersByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        log.info("Getting orders by date range: {} to {}", startDate, endDate);
        
        List<Order> orders = orderRepository.findByOrderDateBetween(startDate, endDate);
        return orders.stream()
                .map(this::convertToOrderResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public OrderResponseDto cancelOrder(Long orderId, String username) {
        log.info("Cancelling order ID: {} by user: {}", orderId, username);
        
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order tidak ditemukan dengan ID: " + orderId));
        
        // Check if user owns the order
        if (!order.getCustomer().getUser().getUsername().equals(username)) {
            throw new BadRequestException("Anda tidak memiliki akses untuk membatalkan order ini");
        }
        
        // Only allow cancellation if order is in WAITING_PAYMENT or PROCESSING status
        if (order.getStatus() != OrderStatus.WAITING_PAYMENT && order.getStatus() != OrderStatus.PROCESSING) {
            throw new BadRequestException("Order tidak dapat dibatalkan pada status: " + order.getStatus());
        }
        
        order.setStatus(OrderStatus.CANCELLED);
        Order updatedOrder = orderRepository.save(order);
        
        log.info("Successfully cancelled order ID: {}", orderId);
        return convertToOrderResponseDto(updatedOrder);
    }

    /**
     * Create order detail dengan customizations.
     */
    private OrderDetail createOrderDetail(Order order, OrderDetailRequestDto itemRequest) {
        Product product = productRepository.findById(itemRequest.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Produk tidak ditemukan dengan ID: " + itemRequest.getProductId()));

        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrder(order);
        orderDetail.setProduct(product);
        orderDetail.setQuantity(itemRequest.getQuantity());
        orderDetail.setUnitPrice(product.getPrice());

        OrderDetail savedOrderDetail = orderDetailRepository.save(orderDetail);

        // Create customizations
        List<OrderDetailCustomization> customizations = new ArrayList<>();
        BigDecimal customizationTotal = BigDecimal.ZERO;

        if (itemRequest.getCustomizations() != null) {
            for (OrderDetailCustomizationRequestDto customizationRequest : itemRequest.getCustomizations()) {
                Customization customization = customizationRepository.findById(customizationRequest.getCustomizationId())
                        .orElseThrow(() -> new ResourceNotFoundException("Kustomisasi tidak ditemukan dengan ID: " + customizationRequest.getCustomizationId()));

                OrderDetailCustomization orderDetailCustomization = new OrderDetailCustomization();
                orderDetailCustomization.setOrderDetail(savedOrderDetail);
                orderDetailCustomization.setCustomization(customization);
                orderDetailCustomization.setCustomizationNameSnapshot(customization.getName());
                orderDetailCustomization.setPriceAdjustmentSnapshot(customization.getPriceAdjustment());

                OrderDetailCustomization savedCustomization = orderDetailCustomizationRepository.save(orderDetailCustomization);
                customizations.add(savedCustomization);
                customizationTotal = customizationTotal.add(customization.getPriceAdjustment());
            }
        }

        // Calculate subtotal
        BigDecimal basePrice = product.getPrice().multiply(BigDecimal.valueOf(itemRequest.getQuantity()));
        BigDecimal totalCustomizationPrice = customizationTotal.multiply(BigDecimal.valueOf(itemRequest.getQuantity()));
        BigDecimal subtotal = basePrice.add(totalCustomizationPrice);

        savedOrderDetail.setSubtotalPrice(subtotal);
        savedOrderDetail.setCustomizations(customizations);

        return orderDetailRepository.save(savedOrderDetail);
    }

    /**
     * Validate status transition business logic.
     */
    private void validateStatusTransition(OrderStatus currentStatus, OrderStatus newStatus) {
        switch (currentStatus) {
            case WAITING_PAYMENT:
                if (newStatus != OrderStatus.PROCESSING && newStatus != OrderStatus.CANCELLED) {
                    throw new BadRequestException("Status tidak dapat diubah dari " + currentStatus + " ke " + newStatus);
                }
                break;
            case PROCESSING:
                if (newStatus != OrderStatus.READY_FOR_PICKUP && newStatus != OrderStatus.CANCELLED) {
                    throw new BadRequestException("Status tidak dapat diubah dari " + currentStatus + " ke " + newStatus);
                }
                break;
            case READY_FOR_PICKUP:
                if (newStatus != OrderStatus.COMPLETED) {
                    throw new BadRequestException("Status tidak dapat diubah dari " + currentStatus + " ke " + newStatus);
                }
                break;
            case COMPLETED:
            case CANCELLED:
                throw new BadRequestException("Status " + currentStatus + " tidak dapat diubah lagi");
            default:
                throw new BadRequestException("Status tidak valid: " + currentStatus);
        }
    }

    /**
     * Convert Order entity to OrderResponseDto.
     */
    private OrderResponseDto convertToOrderResponseDto(Order order) {
        CustomerDto customerDto = new CustomerDto(
                order.getCustomer().getCustomerId(),
                order.getCustomer().getFullName(),
                order.getCustomer().getPhoneNumber(),
                order.getCustomer().getAddress()
        );

        PaymentMethodDto paymentMethodDto = new PaymentMethodDto(
                order.getPaymentMethod().getPaymentId(),
                order.getPaymentMethod().getType()
        );

        List<OrderDetailResponseDto> orderDetailsDto = new ArrayList<>();
        if (order.getOrderDetails() != null) {
            orderDetailsDto = order.getOrderDetails().stream()
                    .map(this::convertToOrderDetailResponseDto)
                    .collect(Collectors.toList());
        }

        return new OrderResponseDto(
                order.getOrderId(),
                customerDto,
                order.getOrderDate(),
                order.getTotalAmount(),
                order.getStatus(),
                paymentMethodDto,
                order.getCustomerNotes(),
                orderDetailsDto
        );
    }

    /**
     * Convert OrderDetail entity to OrderDetailResponseDto.
     */
    private OrderDetailResponseDto convertToOrderDetailResponseDto(OrderDetail orderDetail) {
        CategoryDto categoryDto = new CategoryDto(
                orderDetail.getProduct().getCategory().getCategoryId(),
                orderDetail.getProduct().getCategory().getName()
        );

        ProductDto productDto = new ProductDto(
                orderDetail.getProduct().getProductId(),
                orderDetail.getProduct().getName(),
                orderDetail.getProduct().getDescription(),
                orderDetail.getProduct().getPrice(),
                orderDetail.getProduct().getImageUrl(),
                categoryDto
        );

        List<OrderDetailCustomizationResponseDto> customizationsDto = new ArrayList<>();
        if (orderDetail.getCustomizations() != null) {
            customizationsDto = orderDetail.getCustomizations().stream()
                    .map(this::convertToOrderDetailCustomizationResponseDto)
                    .collect(Collectors.toList());
        }

        return new OrderDetailResponseDto(
                orderDetail.getDetailId(),
                productDto,
                orderDetail.getQuantity(),
                orderDetail.getUnitPrice(),
                orderDetail.getSubtotalPrice(),
                customizationsDto
        );
    }

    /**
     * Convert OrderDetailCustomization entity to OrderDetailCustomizationResponseDto.
     */
    private OrderDetailCustomizationResponseDto convertToOrderDetailCustomizationResponseDto(OrderDetailCustomization customization) {
        CustomizationDto customizationDto = new CustomizationDto(
                customization.getCustomization().getCustomizationId(),
                customization.getCustomization().getName(),
                customization.getCustomization().getType(),
                customization.getCustomization().getPriceAdjustment(),
                customization.getCustomization().getDescription()
        );

        return new OrderDetailCustomizationResponseDto(
                customization.getDetailCustomizationId(),
                customizationDto,
                customization.getCustomizationNameSnapshot(),
                customization.getPriceAdjustmentSnapshot()
        );
    }
}
