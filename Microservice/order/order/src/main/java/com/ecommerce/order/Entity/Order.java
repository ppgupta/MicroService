package com.ecommerce.order.Entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int orderId;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate orderDate;
    private String productList;
    private int customerId;


//    public Order(int orderId, LocalDate orderDate, String productList, int customerId) {
//        this.orderId = orderId;
//        this.orderDate = orderDate;
//        this.productList = productList;
//        this.customerId = customerId;
//    }
//
//    public Order() {
//
//    }
//
//    public int getOrderId() {
//        return orderId;
//    }
//
//    public String getProductList() {
//        return productList;
//    }
//
//    public LocalDate getOrderDate() {
//        return orderDate;
//    }
//
//    public int getCustomerId() {
//        return customerId;
//    }
//
//    public void setOrderId(int orderId) {
//        this.orderId = orderId;
//    }
//
//    public void setOrderDate(LocalDate orderDate) {
//        this.orderDate = orderDate;
//    }
//
//    public void setProductList(String productList) {
//        this.productList = productList;
//    }
//
//    public void setCustomerId(int customerId) {
//        this.customerId = customerId;
//    }

}
