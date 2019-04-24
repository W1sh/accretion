package gui;

import com.jfoenix.controls.JFXSpinner;
import data.Movie;
import data.Result;
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
import javafx.scene.layout.HBox;
import util.Fetcher;

import java.io.IOException;
import java.net.MalformedURLException;
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
    @FXML private HBox breadcrumbsContainer;

    private final BooleanProperty enterPressed = new SimpleBooleanProperty(false);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colPoster.setCellValueFactory(MainViewController::posterCellValueFactory);
        colInformation.setCellValueFactory(MainViewController::infoCellValueFactory);
        colType.setCellValueFactory(param -> {
            String value = param.getValue().getType().substring(0, 1).toUpperCase() + param.getValue().getType().substring(1);
            return new ReadOnlyObjectWrapper<>(value);
        });
        colPlot.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().getMovie().getPlot()));
        resultsTable.setPlaceholder(new Label(""));

        menuItemMovieList.setOnAction(this::loadTableView);
        assignContextMenu();

        buildBreadcrumbs();
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
        SceneController sceneController = SceneController.getInstance();
        sceneController.activate("movie_table");
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
        resultsTable.setRowFactory(MainViewController::resultsRowFactory);
    }

    private static void addEvent(TableRow<Result> row){
        Result object = row.getItem();
        SceneController sceneController = SceneController.getInstance();
        MovieTableViewController mtvController = (MovieTableViewController)
                sceneController.getController(App.View.MOVIE_TABLE_VIEW.name);
        mtvController.getMovies().add(object.getMovie());
        mtvController.updateTable();
        sceneController.createSnackbar(object.getMovie().getTitle() + " added to the list!", "Undo", null);
        // show snackbar, maybe undo
    }

    private static ReadOnlyObjectWrapper<String> infoCellValueFactory(
            TableColumn.CellDataFeatures<Result, String> param){
        return new ReadOnlyObjectWrapper<>(
                param.getValue().getTitle().trim() + " (" + param.getValue().getYear() + ")");
    }

    private static ReadOnlyObjectWrapper<ImageView> posterCellValueFactory(
            TableColumn.CellDataFeatures<Result, ImageView> param){
        ImageView imageView = new ImageView();
        imageView.setFitHeight(100);
        imageView.setFitWidth(75);
        try {
            URL posterURL = new URL(param.getValue().getPoster());
            imageView = new ImageView(new Image(posterURL.openStream()));
            imageView.setFitHeight(100);
            imageView.setFitWidth(75);
            return new ReadOnlyObjectWrapper<>(imageView);
        } catch (MalformedURLException murle){
            return new ReadOnlyObjectWrapper<>(imageView);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static TableRow<Result> resultsRowFactory(TableView<Result> tableView){
        final TableRow<Result> row = new TableRow<>();
        final ContextMenu rowMenu = new ContextMenu();
        MenuItem miAdd = new MenuItem("Add to your list");
        row.setOnMouseClicked(e -> {
            if(e.getClickCount() == 2){
                SceneController.getInstance().activate("movie_details");
                MovieDetailsViewController mdvController =
                        (MovieDetailsViewController) SceneController.getInstance().getController("movie_details");
                mdvController.showDetails(row.getItem().getMovie());
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
    }

    private void buildBreadcrumbs(){
        /*SceneController sceneController = SceneController.getInstance();

        breadcrumbsContainer.getChildren().add();*/
    }
}