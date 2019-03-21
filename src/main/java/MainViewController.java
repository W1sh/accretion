import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;

import java.io.IOException;

public class MainViewController {

    @FXML
    private MenuItem idMenuItemTable;

    @FXML
    void loadTableView(ActionEvent event) {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/application.fxml"));
            Parent root = fxmlLoader.load();
            App.controller = fxmlLoader.getController();
            Scene scene = new Scene(root);
            App.window.setScene(scene);
            App.window.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}