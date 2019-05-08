package gui.main;

import gui.controllers.SceneMediator;
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
        MAIN_VIEW("Main", "/gui/main.fxml"),
        MOVIE_TABLE_VIEW("Movie Table", "/gui/movie_table.fxml"),
        MOVIE_DETAILS_VIEW("Movie Details", "/gui/movie_details.fxml");

        protected String name;
        protected String fxml;

        View(String name, String fxml){
            this.name = name;
            this.fxml = fxml;
        }

        public String getName() {
            return name;
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
            SceneMediator sceneMediator = SceneMediator.getInstance();
            for (View value : View.values()) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(value.fxml));
                Parent root = loader.load();
                Initializable controller = loader.getController();
                sceneMediator.registerScene(value.name, root);
                sceneMediator.registerController(controller);
                if("Main".equals(value.name)){
                    Scene scene = new Scene(root);
                    sceneMediator.setMain(scene);
                    sceneMediator.activateScene("Main");
                    primaryStage.setScene(scene);
                    primaryStage.show();
                }
            }
        } catch (IOException ioe){
            ioe.printStackTrace();
        }
    }
}