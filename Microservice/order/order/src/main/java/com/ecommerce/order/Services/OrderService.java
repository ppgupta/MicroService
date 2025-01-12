package com.ecommerce.order.Services;

import com.ecommerce.order.Entity.Order;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface OrderService {
    List<Order> getAllOrders();

    Order getOrderById(Long orderId);

    Order createOrder(Order order);

    Order updateOrder (Long orderId, Order updatedOrder);

    void deleteOrder(Long orderId);
    void saveOrdersFromExcel(MultipartFile file) throws Exception;

}
