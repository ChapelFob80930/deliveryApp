module com.example.loginsignup {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.mail;

    opens com.example.loginsignup to javafx.fxml;
    exports com.example.loginsignup;
    exports com.example.loginsignup.DBrelated;
    opens com.example.loginsignup.DBrelated to javafx.fxml;
}