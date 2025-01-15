package com.example.loginsignup;

public class Order {
    private final int orderId; // Add orderId to uniquely identify orders
    private final String courierName;
    private final String courierPhone;
    private final String receiverAddress;
    private final double orderPrice;
    private String orderStatus; // Allow modifications
    private final String description;

    // Constructor
    public Order(int orderId, String courierName, String courierPhone, String receiverAddress, double orderPrice, String orderStatus, String description) {
        this.orderId = orderId; // Initialize orderId
        this.courierName = courierName;
        this.courierPhone = courierPhone;
        this.receiverAddress = receiverAddress;
        this.orderPrice = orderPrice;
        this.orderStatus = orderStatus;
        this.description = description;
    }

    // Getters
    public int getOrderId() {
        return orderId; // Getter for orderId
    }

    public String getCourierName() {
        return courierName;
    }

    public String getCourierPhone() {
        return courierPhone;
    }

    public String getReceiverAddress() {
        return receiverAddress;
    }

    public double getOrderPrice() {
        return orderPrice;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public String getDescription() {
        return description;
    }

    // Setter for orderStatus
    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
}
