module org.cosmos.deliveryapp {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens org.cosmos.deliveryapp to javafx.fxml;
    exports org.cosmos.deliveryapp;
}