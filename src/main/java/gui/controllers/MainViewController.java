package gui.controllers;

import com.jfoenix.controls.JFXSpinner;
import data.Movie;
import data.Result;
import gui.main.App;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import util.Fetcher;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class MainViewController implements Initializable {

    @FXML private MenuItem menuItemMovieList;
    @FXML private TableView<Result> resultsTable;
    @FXML private TableColumn<Result, ImageView> colPoster;
    @FXML private TableColumn<Result, String> colInformation;
    @FXML private TableColumn<Result, String> colType;
    @FXML private TableColumn<Result, String> colPlot;
    @FXML private TextField searchTextField;
    @FXML private GridPane tableLoadingPane;
    @FXML private JFXSpinner tableLoadingSpinner;

    private SceneMediator sceneMediator = SceneMediator.getInstance();
    private final BooleanProperty enterPressed = new SimpleBooleanProperty(false);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colPoster.setCellValueFactory(this::posterCellValueFactory);
        colInformation.setCellValueFactory(this::infoCellValueFactory);
        colType.setCellValueFactory(this::typeCellValueFactory);
        colPlot.setCellValueFactory(this::plotCellValueFactory);
        menuItemMovieList.setOnAction(this::loadTableView);
        resultsTable.setPlaceholder(new Label(""));
        assignContextMenu();
    }

    @FXML
    void searchFieldPressed(KeyEvent event) {
        if(event.getCode().equals(KeyCode.ENTER)){
            enterPressed.set(true);
            tableLoadingPane.setVisible(true);
            tableLoadingSpinner.setVisible(true);
            Executors.newSingleThreadExecutor().submit(() -> {
                List<Result> results = Fetcher.fetchMovies(searchTextField.getText());
                resultsTable.getItems().clear();
                setResults(results);
                tableLoadingPane.setVisible(false);
                tableLoadingSpinner.setVisible(false);
            });
        }
    }

    @FXML
    void searchFieldReleased(KeyEvent event) {
        if(event.getCode().equals(KeyCode.ENTER)){
            enterPressed.set(false);
        }
    }

    @FXML
    private void loadTableView(ActionEvent event) {
        SceneMediator.getInstance().activateScene(App.View.MOVIE_TABLE_VIEW.getName());
    }

    private void setResults(List<Result> results){
        if(results.isEmpty()){
            resultsTable.setPlaceholder(new Label("No results found for \"" + searchTextField.getText() + "\"."));
        }else{
            ObservableList<Result> content = FXCollections.observableList(
                    results.stream()
                            .filter(item -> !item.getType().equals("game"))
                            .collect(Collectors.toList()));
            resultsTable.getItems().addAll(content);
            resultsTable.refresh();
        }
    }


    private void assignContextMenu(){
        resultsTable.setRowFactory(this::resultsRowFactory);
    }

    private ReadOnlyObjectWrapper<String> infoCellValueFactory(
            TableColumn.CellDataFeatures<Result, String> param){
        return new ReadOnlyObjectWrapper<>(
                param.getValue().getTitle().trim() + " (" + param.getValue().getYear() + ")");
    }

    private ReadOnlyObjectWrapper<String> typeCellValueFactory(
            TableColumn.CellDataFeatures<Result, String> param){
        return new ReadOnlyObjectWrapper<>(
                param.getValue().getType().substring(0, 1).toUpperCase() + param.getValue().getType().substring(1));
    }

    private ReadOnlyObjectWrapper<String> plotCellValueFactory(
            TableColumn.CellDataFeatures<Result, String> param){
        return new ReadOnlyObjectWrapper<>(param.getValue().getMovie().getPlot());
    }

    private ReadOnlyObjectWrapper<ImageView> posterCellValueFactory(
            TableColumn.CellDataFeatures<Result, ImageView> param){
        ImageView imageView = new ImageView();
        try {
            URL posterURL = new URL(param.getValue().getPoster());
            imageView = new ImageView(new Image(posterURL.openStream()));
        } catch (IOException ioe){
            ioe.printStackTrace();
        }
        imageView.setFitHeight(100);
        imageView.setFitWidth(75);
        return new ReadOnlyObjectWrapper<>(imageView);
    }

    private TableRow<Result> resultsRowFactory(TableView<Result> tableView){
        final TableRow<Result> row = new TableRow<>();
        row.setOnMouseClicked(e -> {
            if(e.getClickCount() == 2){
                getMovieDetails(row.getItem().getMovie());
            }
        });
        final ContextMenu rowMenu = new ContextMenu();
        MenuItem miAdd = new MenuItem("Add to your list");
        miAdd.setOnAction(e -> addEvent(row));
        MenuItem miDetails = new MenuItem("Details");
        miDetails.setOnAction(e -> getMovieDetails(row.getItem().getMovie()));
        rowMenu.getItems().addAll(miAdd, miDetails);
        // only display context menu for non-null items:
        row.contextMenuProperty().bind(
                Bindings.when(Bindings.isNotNull(row.itemProperty()))
                        .then(rowMenu)
                        .otherwise((ContextMenu)null));
        return row;
    }

    private void addEvent(TableRow<Result> row){
        Result object = row.getItem();
        sceneMediator.movieTableRegisterMovie(object.getMovie());
        sceneMediator.movieTableUpdate();
        /*SceneController sceneController = SceneController.getInstance();
        MovieTableViewController mtvController = (MovieTableViewController)
                sceneController.getController(App.View.MOVIE_TABLE_VIEW.getName());
        mtvController.getMovies().add(object.getMovie());
        mtvController.updateTable();
        sceneController.createSnackbar(object.getMovie().getTitle() + " added to the list!", "Undo", null);
        */// TODO: undo add movie to table
    }

    private void getMovieDetails(Movie movie){
        sceneMediator.activateScene("Movie Details");
        sceneMediator.movieDetailsShow(movie);
        /*
        SceneController.getInstance().activate("movie_details");
        MovieDetailsViewController mdvController =
                (MovieDetailsViewController) SceneController.getInstance().getController("movie_details");
        mdvController.showDetails(movie);*/
    }
}