package gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    private static final String APP_TITLE = "Accretion 1.1";

    public enum View {
        MAIN_VIEW("main", "/gui/main.fxml"),
        MOVIE_TABLE_VIEW("movie_table", "/gui/movie_table.fxml"),
        MOVIE_DETAILS_VIEW("movie_details", "/gui/movie_details.fxml");

        protected String name;
        protected String fxml;

        View(String name, String fxml){
            this.name = name;
            this.fxml = fxml;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle(APP_TITLE);
        primaryStage.setOnCloseRequest(event -> Platform.exit());
        primaryStage.setOnShowing(showEvent -> {
            // prepare table
        });
        try {
            SceneController sceneController = SceneController.getInstance();
            for (View value : View.values()) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(value.fxml));
                Parent root = loader.load();
                Initializable controller = loader.getController();
                sceneController.add(value.name, root, controller);
                if(value.name.equals("main")){
                    Scene scene = new Scene(root);
                    sceneController.setMain(scene);
                    sceneController.activate("main");
                    primaryStage.setScene(scene);
                    primaryStage.show();
                }
            }
        } catch (IOException ioe){
            ioe.printStackTrace();
        }
    }
}