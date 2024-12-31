package Backend;

import javafx.beans.property.*;

public class Order {
    private final IntegerProperty orderId;
    private final IntegerProperty receiverId;
    private final IntegerProperty senderId;
    private final StringProperty receiverAddress;
    private final DoubleProperty orderPrice;
    private final StringProperty estimatedTime;
    private final StringProperty orderStatus;
    private final StringProperty detailedDescription;

    public Order(int orderId, int receiverId, int senderId, String receiverAddress, double orderPrice, String estimatedTime, String orderStatus, String detailedDescription) {
        this.orderId = new SimpleIntegerProperty(orderId);
        this.receiverId = new SimpleIntegerProperty(receiverId);
        this.senderId = new SimpleIntegerProperty(senderId);
        this.receiverAddress = new SimpleStringProperty(receiverAddress);
        this.orderPrice = new SimpleDoubleProperty(orderPrice);
        this.estimatedTime = new SimpleStringProperty(estimatedTime);
        this.orderStatus = new SimpleStringProperty(orderStatus);
        this.detailedDescription = new SimpleStringProperty(detailedDescription);
    }


    public int getOrderId() { return orderId.get(); }
    public IntegerProperty orderIdProperty() { return orderId; }
    public void setOrderId(int orderId) { this.orderId.set(orderId); }

    public int getReceiverId() { return receiverId.get(); }
    public IntegerProperty receiverIdProperty() { return receiverId; }
    public void setReceiverId(int receiverId) { this.receiverId.set(receiverId); }

    public int getSenderId() { return senderId.get(); }
    public IntegerProperty senderIdProperty() { return senderId; }
    public void setSenderId(int senderId) { this.senderId.set(senderId); }

    public String getReceiverAddress() { return receiverAddress.get(); }
    public StringProperty receiverAddressProperty() { return receiverAddress; }
    public void setReceiverAddress(String receiverAddress) { this.receiverAddress.set(receiverAddress); }

    public double getOrderPrice() { return orderPrice.get(); }
    public DoubleProperty orderPriceProperty() { return orderPrice; }
    public void setOrderPrice(double orderPrice) { this.orderPrice.set(orderPrice); }

    public String getEstimatedTime() { return estimatedTime.get(); }
    public StringProperty estimatedTimeProperty() { return estimatedTime; }
    public void setEstimatedTime(String estimatedTime) { this.estimatedTime.set(estimatedTime); }

    public String getOrderStatus() { return orderStatus.get(); }
    public StringProperty orderStatusProperty() { return orderStatus; }
    public void setOrderStatus(String orderStatus) { this.orderStatus.set(orderStatus); }

    public String getDetailedDescription() { return detailedDescription.get(); }
    public StringProperty detailedDescriptionProperty() { return detailedDescription; }
    public void setDetailedDescription(String detailedDescription) { this.detailedDescription.set(detailedDescription); }
}
