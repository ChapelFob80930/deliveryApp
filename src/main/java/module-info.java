module org.cosmos.deliveryapp {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires java.mail;

    opens Frontend to javafx.fxml;
    exports Frontend;
}