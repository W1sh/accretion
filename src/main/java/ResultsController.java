import data.Movie;
import data.Result;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import util.Fetcher;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class ResultsController implements Initializable {

    @FXML private TableView<Result> resultsTable;
    @FXML private TableColumn<Result, ImageView> colPoster;
    @FXML private TableColumn<Result, String> colInfo;

    public ResultsController() { }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colPoster.setCellValueFactory(param -> {
            try {
                ImageView imageView = new ImageView(new Image(new URL(param.getValue().getPoster()).openStream()));
                imageView.setFitHeight(150);
                imageView.setFitWidth(100);
                return new ReadOnlyObjectWrapper<>(imageView);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        });
        colInfo.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(
                param.getValue().getTitle() + " (" + param.getValue().getYear() + ")"));

        assignContextMenu();
    }

    public void setResults(ArrayList<Result> results){
        ObservableSet<Result> content = FXCollections.observableSet(new HashSet<>(results));
        resultsTable.getItems().addAll(content);
        resultsTable.refresh();
    }

    private void assignContextMenu(){
        resultsTable.setRowFactory(tableView1 -> {
            final TableRow<Result> row = new TableRow<>();
            final ContextMenu rowMenu = new ContextMenu();
            MenuItem miAdd = new MenuItem("Add to your list");
            row.setOnMouseClicked(e -> {
                if(e.getClickCount() == 2){
                    addEvent(row);
                }
            });
            miAdd.setOnAction(e -> addEvent(row));
            rowMenu.getItems().add(miAdd);
            // only display context menu for non-null items:
            row.contextMenuProperty().bind(
                    Bindings.when(Bindings.isNotNull(row.itemProperty()))
                            .then(rowMenu)
                            .otherwise((ContextMenu)null));
            return row;
        });
    }

    private void addEvent(TableRow<Result> row){
        Result object = row.getItem();
        System.out.println(object.getTitle());
        Movie movie = Fetcher.fetchMovie(object.getTitle());
        App.controller.getMovies().add(movie);
        App.controller.updateTable();
    }
}
