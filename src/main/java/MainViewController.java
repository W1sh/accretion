import data.Movie;
import data.Result;
import de.codecentric.centerdevice.javafxsvg.SvgImageLoaderFactory;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.Callback;
import util.Fetcher;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MainViewController implements Initializable {

    @FXML private Button navigationIcon;
    @FXML private ImageView sidebarImageView;
    @FXML private Button sidebarMainMenu;
    @FXML private Button sidebarListMenu;
    @FXML private TableView<Result> resultsTableLeft;
    @FXML private TableColumn<Result, ImageView> colPosterLeft;
    @FXML private TableColumn<Result, String> colInformationLeft;
    @FXML private TableView<Result> resultsTableRight;
    @FXML private TableColumn<Result, ImageView> colPosterRight;
    @FXML private TableColumn<Result, String> colInformationRight;
    @FXML private TextField searchTextField;

    private final BooleanProperty enterPressed = new SimpleBooleanProperty(false);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        SvgImageLoaderFactory.install();
        ImageView navIconIV = new ImageView(new Image("/gui/assets/bars.svg"));
        navIconIV.setFitWidth(15);
        navIconIV.setFitHeight(15);
        navigationIcon.setGraphic(navIconIV);
        colPosterLeft.setCellValueFactory(MainViewController::posterCellValueFactory);
        colPosterRight.setCellValueFactory(MainViewController::posterCellValueFactory);
        colInformationRight.setCellValueFactory(MainViewController::infoCellValueFactory);
        colInformationLeft.setCellValueFactory(MainViewController::infoCellValueFactory);

        resultsTableRight.setPlaceholder(new Label(""));
        resultsTableLeft.setPlaceholder(new Label(""));
        assignContextMenu();
    }

    @FXML
    void searchFieldPressed(KeyEvent event) {
        if(event.getCode().equals(KeyCode.ENTER)){
            enterPressed.set(true);
            ArrayList<Result> results = (ArrayList<Result>) Fetcher.fetchMovies(searchTextField.getText());
            setResults(results);
        }
    }

    @FXML
    void searchFieldReleased(KeyEvent event) {
        if(event.getCode().equals(KeyCode.ENTER)){
            enterPressed.set(false);
        }
    }

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

    public void setResults(ArrayList<Result> results){
        ObservableList<Result> content = FXCollections.observableList(results);
        for (int i = 0; i < content.size(); i++) {
            if(i % 2 == 0){
                resultsTableLeft.getItems().add(content.get(i));
            }else{
                resultsTableRight.getItems().add(content.get(i));
            }
        }
        resultsTableLeft.refresh();
        resultsTableRight.refresh();
    }


    private void assignContextMenu(){
        resultsTableLeft.setRowFactory(MainViewController::resultsRowFactory);
        resultsTableRight.setRowFactory(MainViewController::resultsRowFactory);
    }

    private static void addEvent(TableRow<Result> row){
        Result object = row.getItem();
        System.out.println(object.getTitle());
        Movie movie = Fetcher.fetchMovie(object.getTitle());
        App.controller.getMovies().add(movie);
        App.controller.updateTable();
    }

    private static ReadOnlyObjectWrapper<String> infoCellValueFactory(
            TableColumn.CellDataFeatures<Result, String> param){
        return new ReadOnlyObjectWrapper<>(
                param.getValue().getTitle().trim() + " (" + param.getValue().getYear() + ")");
    }

    private static ReadOnlyObjectWrapper<ImageView> posterCellValueFactory(
            TableColumn.CellDataFeatures<Result, ImageView> param){
        try {
            ImageView imageView = new ImageView(new Image(new URL(param.getValue().getPoster()).openStream()));
            imageView.setFitHeight(150);
            imageView.setFitWidth(100);
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
    }
}