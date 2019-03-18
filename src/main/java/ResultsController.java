import data.Result;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class ResultsController implements Initializable {

    @FXML
    private TableView<Result> resultsTable;

    @FXML
    private TableColumn<Result, Image> colPoster;

    @FXML
    private TableColumn<Result, String> colInfo;

    private ArrayList<Result> results;

    public ResultsController() { }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colPoster.setCellValueFactory(param -> {
            try {
                return new ReadOnlyObjectWrapper<>(new Image(new URL(param.getValue().getPoster()).openStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        });
        colInfo.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(
                param.getValue().getTitle() + " (" + param.getValue().getYear() + ")"));
    }

    public boolean updateTable(List<Result> results) {
        if (resultsTable != null) {
            resultsTable.getItems().clear();
            ObservableSet<Result> content = FXCollections.observableSet(new HashSet<>(results));
            resultsTable.getItems().addAll(content);
            System.out.println(resultsTable.getItems().size());
            resultsTable.refresh();
            return true;
        } else {
            return false;
        }
    }

    public void setResults(ArrayList<Result> results){
        ObservableSet<Result> content = FXCollections.observableSet(new HashSet<>(results));
        resultsTable.getItems().addAll(content);
        resultsTable.refresh();
    }
}
