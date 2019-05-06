package gui.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Region;

public class Dialog {

    private Dialog(){}

    public static void showAlertDialog(String text){
        Alert alert = new Alert(Alert.AlertType.ERROR, text, ButtonType.OK);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.getDialogPane().setMaxWidth(Region.USE_PREF_SIZE);
        alert.getDialogPane().getStylesheets().add("/gui/dark-theme.css");
        alert.getDialogPane().getStyleClass().add("custom-alert");
        alert.showAndWait()
                .filter(response -> response == ButtonType.OK)
                .ifPresent(response -> System.out.println("FeelsOkayMan")); // log
    }
}
