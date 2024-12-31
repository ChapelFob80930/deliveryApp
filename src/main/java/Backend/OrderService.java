package Backend;

import java.sql.SQLException;

public class OrderService {
    private SQLAccess sqlAccess;
    private NotificationService notificationService;
    private Promotion promotion;

    public OrderService(SQLAccess sqlAccess, NotificationService notificationService, Promotion promotion) {
        this.sqlAccess = sqlAccess;
        this.notificationService = notificationService;
        this.promotion = promotion;
    }

    public void placeOrder(Order order) throws SQLException {
        double discountedPrice = promotion.applyDiscount(order.getOrderPrice(), 10.0); // Example: 10% discount
        order.setOrderPrice(discountedPrice);


        sqlAccess.addOrder(order);


        notificationService.sendNotification(order.getReceiverId(), "Order placed successfully!");
    }
}
