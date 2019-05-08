package gui.utils;

import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXSnackbarLayout;
import gui.controllers.SceneMediator;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class SceneHelper {

    private static final Duration DURATION_SHORT = Duration.seconds(3);

    public void createSnackbar(String message, String actionMessage, Runnable runnable){
        Pane pane = (Pane) SceneMediator.getInstance().getMain().getRoot();
        JFXSnackbar jfxSnackbar = new JFXSnackbar(pane);
        EventHandler<ActionEvent> actionHandler = actionEvent -> {
            jfxSnackbar.unregisterSnackbarContainer(pane);
            runnable.run();
        };
        JFXSnackbarLayout snackbarLayout = new JFXSnackbarLayout(message, actionMessage, actionHandler);
        jfxSnackbar.fireEvent(new JFXSnackbar.SnackbarEvent(snackbarLayout, DURATION_SHORT, null));
    }
}
