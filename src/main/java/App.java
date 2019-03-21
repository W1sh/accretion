import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    static Controller controller;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Parent root;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/application.fxml"));
            root = fxmlLoader.load();
            controller = fxmlLoader.getController();
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
            return;
        }


    }
}