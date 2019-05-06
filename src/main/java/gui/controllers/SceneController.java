package gui.controllers;

import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXSnackbarLayout;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SceneController {

    private static final Duration DURATION_SHORT = Duration.seconds(2);
    private static final Duration DURATION_LONG = Duration.seconds(5);
    private static SceneController instance = null;

    private List<String> breadcrumbs = new ArrayList<>();
    private HashMap<String, Initializable> controllerMap = new HashMap<>();
    private HashMap<String, Parent> sceneMap = new HashMap<>();
    private Scene main;

    private SceneController(){}

    public static SceneController getInstance(){
        if(instance == null){
            instance = new SceneController();
        }
        return instance;
    }

    public void setMain(Scene main) {
        this.main = main;
    }

    public void add(String sceneName, Parent pane, Initializable controller){
        sceneMap.put(sceneName, pane);
        controllerMap.put(sceneName, controller);
    }

    Initializable getController(String sceneName){
        return controllerMap.get(sceneName);
    }

    public void activate(String sceneName){
        int indexOfBreadcrumb = breadcrumbs.indexOf(sceneName);
        if(indexOfBreadcrumb != -1){
            breadcrumbs = breadcrumbs.subList(0, indexOfBreadcrumb + 1);
        }else{
            breadcrumbs.add(sceneName);
        }
        if(sceneName.equals("movie_table")) buildBreadcrumbs();
        System.out.println(breadcrumbs.toString());
        main.setRoot(sceneMap.get(sceneName));
    }

    void createSnackbar(String message, String actionMessage, EventHandler<ActionEvent> actionHandler){
        JFXSnackbar jfxSnackbar = new JFXSnackbar((Pane) main.getRoot());
        JFXSnackbarLayout snackbarLayout = new JFXSnackbarLayout(message, actionMessage, actionHandler);
        jfxSnackbar.fireEvent(new JFXSnackbar.SnackbarEvent(snackbarLayout, DURATION_SHORT, null));
    }

    public void buildBreadcrumbs() {
        List<Node> nodes = new ArrayList<>();
        breadcrumbs.forEach(breadcrumb -> {
            Button button = new Button(breadcrumb);
            button.setOnMouseClicked(event -> activate(button.getText()));
            if(breadcrumb.equals("movie_table")) button.setDisable(true);
            /*ImageView imageView = new javafx.scene.image.ImageView(new Image(new File(separator).toURI().toString()));
            imageView.setFitHeight(20);
            imageView.setFitWidth(20);
            nodes.add(button);
            nodes.add(imageView);*/
        });
        MovieTableViewController mtvController = (MovieTableViewController) getController("movie_table");
        mtvController.getBreadcrumbsContainer().getChildren().clear();
        nodes.forEach(node -> mtvController.getBreadcrumbsContainer().getChildren().add(node));
    }

    public List<String> getBreadcrumbs() {
        return breadcrumbs;
    }
}
