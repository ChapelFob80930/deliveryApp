package Frontend;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class App extends Application {

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("customer-main.fxml")));

            Scene scene = new Scene(fxmlLoader.load(), 900, 700);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("customer-style.css")).toExternalForm());

            stage.setTitle("Delivery App - Customer Interface");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
