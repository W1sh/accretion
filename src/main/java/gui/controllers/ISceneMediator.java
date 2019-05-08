package gui.controllers;

import javafx.fxml.Initializable;
import javafx.scene.Parent;

public interface ISceneMediator {

    void registerController(Initializable controller);
    void registerScene(String sceneName, Parent pane);
    void activateScene(String sceneName);
}
