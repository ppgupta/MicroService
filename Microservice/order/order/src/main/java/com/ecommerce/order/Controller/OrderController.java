package com.ecommerce.order.Controller;

import com.ecommerce.order.Entity.Order;
import com.ecommerce.order.Exception.ResourceNotFoundException;
import com.ecommerce.order.Services.OrderService;
import com.ecommerce.order.helper.Helper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class OrderController {

//    private static final Logger log = LoggerFactory.getLogger(OrderController.class);

    OrderService orderService;

    OrderController(OrderService orderService) {
        this.orderService = orderService;
    }


    @Operation(summary = "Get All orders")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the Order",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Order.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
            @ApiResponse(responseCode = "404", description = "Order not found", content = @Content)})
    @GetMapping("/order/all")
    public ResponseEntity<List<Order>> getAllOrders() {

        String methodName = "getAllOrders()";
        log.info("[{}] started ", methodName);

        List<Order> orders = orderService.getAllOrders();

        if (orders.isEmpty()) {
            log.warn("[{}] No orders found in the database.", methodName);
            throw new ResourceNotFoundException("No orders found from Database.");
        }

        return ResponseEntity.ok(orders);
    }

    @Operation(summary = "Get Order by its Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the Order",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Order.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
            @ApiResponse(responseCode = "404", description = "Order not found", content = @Content)})
    @GetMapping("/order/{orderId}")
    public ResponseEntity<Order> getOrder(@PathVariable Long orderId) {
        String methodName = "getOrder()";
        log.info("[{}] started ", methodName);

        return ResponseEntity.ok(orderService.getOrderById(orderId));
    }


    @Operation(summary = "Order Created")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Order Created",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Order.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
            @ApiResponse(responseCode = "409", description = "Order already exists", content = @Content)})
    @PostMapping("/order/add")
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        String methodName = "createOrder()";
        log.info("[{}] started ", methodName);

        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(order));
    }

    @Operation(summary = "Order Updated")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Order Created",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Order.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
            @ApiResponse(responseCode = "409", description = "Order already exists", content = @Content)})
    @PutMapping("/order/update/{orderId}")
    public ResponseEntity<Order> updateOrder(@PathVariable Long orderId, @RequestBody Order order) {
        String methodName = "updateOrder()";
        log.info("[{}] started ", methodName);

        return ResponseEntity.ok(orderService.updateOrder(orderId, order));
    }

    @DeleteMapping("/order/delete/{orderId}")
    public ResponseEntity<String> deleteOrder(@PathVariable Long orderId) {
        String methodName = "deleteOrder()";
        log.info("[{}] started ", methodName);

        orderService.deleteOrder(orderId);
        return new ResponseEntity<>("Order deleted", HttpStatus.OK);
    }


    @Operation(summary = "Order Save from Excel to DB")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Order saved",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Order.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
            @ApiResponse(responseCode = "409", description = "Order already exists", content = @Content)})
    @PostMapping("/order/upload")
    public ResponseEntity<String> uploadFromExcel(@RequestParam("file") MultipartFile file) {
        String methodName = "uploadFromExcel()";
        log.info("[{}] started ", methodName);

        try {
            if (Helper.checkExcelFormat(file)) {
                orderService.saveOrdersFromExcel(file);

                return ResponseEntity.ok("Data from Excel file has been saved successfully!");
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please upload excel file only");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to process the Excel file: " + e.getMessage());
        }

    }
}
