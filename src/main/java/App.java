import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    static MainViewController mainViewController;
    static Controller controller;
    static Stage window;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        window = primaryStage;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/main.fxml"));
            Parent root = fxmlLoader.load();
            mainViewController = fxmlLoader.getController();
            primaryStage.setTitle("Accretion 1.1");
            primaryStage.setOnCloseRequest(event -> Platform.exit());
            primaryStage.setOnShowing(showEvent -> {
                // prepare table
            });
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}