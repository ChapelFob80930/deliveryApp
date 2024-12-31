package Backend;

public class Promotion {
    public static double applyDiscount(double price, double discountPercentage) {
        return price - (price * discountPercentage / 100);
    }
}
