package gui;

import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXSnackbarLayout;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.Stack;

class SceneController {
    private static final Duration DURATION_SHORT = Duration.seconds(2);
    private static final Duration DURATION_LONG = Duration.seconds(5);
    private static SceneController instance = null;

    private Stack<String> breadcrumbs = new Stack<>();
    private HashMap<String, Initializable> controllerMap = new HashMap<>();
    private HashMap<String, Parent> sceneMap = new HashMap<>();
    private Scene main;

    private SceneController(){}

    static SceneController getInstance(){
        if(instance == null){
            instance = new SceneController();
        }
        return instance;
    }

    void setMain(Scene main) {
        this.main = main;
    }

    void add(String sceneName, Parent pane, Initializable controller){
        sceneMap.put(sceneName, pane);
        controllerMap.put(sceneName, controller);
    }

    Initializable getController(String sceneName){
        return controllerMap.get(sceneName);
    }

    void activate(String sceneName){
        //breadcrumbs.push(sceneName);
        main.setRoot(sceneMap.get(sceneName));
    }

    void createSnackbar(String message, String actionMessage, EventHandler<ActionEvent> actionHandler){
        JFXSnackbar jfxSnackbar = new JFXSnackbar((Pane) main.getRoot());
        JFXSnackbarLayout snackbarLayout = new JFXSnackbarLayout(message, actionMessage, actionHandler);
        jfxSnackbar.fireEvent(new JFXSnackbar.SnackbarEvent(snackbarLayout, DURATION_SHORT, null));
    }


}
