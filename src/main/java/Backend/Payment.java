package Backend;

import java.time.LocalDateTime;

public class Payment {
    private int paymentId;
    private int orderId;
    private double amount;
    private double systemCharge;
    private String status;
    private LocalDateTime paymentDate;

    public Payment(int paymentId, int orderId, double amount, double systemCharge, String status, LocalDateTime paymentDate) {
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.amount = amount;
        this.systemCharge = systemCharge;
        this.status = status;
        this.paymentDate = paymentDate;
    }

    // Getters and Setters
    public int getPaymentId() { return paymentId; }
    public void setPaymentId(int paymentId) { this.paymentId = paymentId; }

    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public double getSystemCharge() { return systemCharge; }
    public void setSystemCharge(double systemCharge) { this.systemCharge = systemCharge; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getPaymentDate() { return paymentDate; }
    public void setPaymentDate(LocalDateTime paymentDate) { this.paymentDate = paymentDate; }
}
