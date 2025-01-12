package com.ecommerce.order.Services;

import com.ecommerce.order.Entity.Order;
import com.ecommerce.order.Exception.InvalidOrderException;
import com.ecommerce.order.Exception.ResourceNotFoundException;
import com.ecommerce.order.Repository.OrderRepository;
import com.ecommerce.order.helper.Helper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;


@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderRepository orderRepository;

    // Get All Order operations
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    // in response body i want to avoid any attrubute coming from DB how to achieve that

    // in entity class we have 4 variables and i want to save only 3 of them, how to implement that

    // Get Order By Id operations
    public Order getOrderById(Long orderId) {

        Optional<Order> orderOptional = orderRepository.findById(orderId);
        return orderOptional.orElseThrow(() -> new ResourceNotFoundException("Order not found with id :" + orderId));
    }

    // Create Order operations

    public Order createOrder(Order order) {
        if (order.getCustomerId() <= 0) {
            throw new InvalidOrderException("Invalid customer ID.");
        }
        return orderRepository.save(order);
    }

    // Update Order operations

    public Order updateOrder(Long orderId, Order updatedOrder) {

        return orderRepository.findById(orderId).map(existingOrder -> {
                    existingOrder.setOrderDate(updatedOrder.getOrderDate());
                    existingOrder.setProductList(updatedOrder.getProductList());
                    existingOrder.setCustomerId(updatedOrder.getCustomerId());

                    return orderRepository.save(existingOrder);
                })
                .orElseThrow(() -> new ResourceNotFoundException("resource not found with :" + orderId));
    }

    // Delete Order operations
    public void deleteOrder(Long orderId) {
        if (orderRepository.existsById(orderId)) {
            orderRepository.deleteById(orderId);

        } else {
            throw new ResourceNotFoundException("Department Not Found with id:" + orderId);
        }
    }

    @Override
    public void saveOrdersFromExcel(MultipartFile file)  {
        try {
            List<Order> orders = Helper.convertExcelToList(file.getInputStream());

            this.orderRepository.saveAll(orders);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}