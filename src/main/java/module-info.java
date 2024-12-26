module org.cosmos.deliveryapp {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens Frontend to javafx.fxml;
    exports Frontend;
}