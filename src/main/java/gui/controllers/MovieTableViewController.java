package gui.controllers;

import data.Movie;
import data.Status;
import gui.App;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MovieTableViewController implements Initializable {

    @FXML private HBox breadcrumbsContainer;
    @FXML private MenuItem menuItemHome;

    @FXML private Label entriesLabel;
    @FXML private Label hoursLabel;
    @FXML private Label scoreLabel;
    @FXML private Label completedLabel;
    @FXML private Label ongoingLabel;
    @FXML private Label droppedLabel;

    @FXML private TableView<Movie> entriesTable;
    @FXML private TableColumn<Movie, String> colStatus;
    @FXML private TableColumn<Movie, String> colName;
    @FXML private TableColumn<Movie, String> colGenre;
    @FXML private TableColumn<Movie, String> colDirector;
    @FXML private TableColumn<Movie, String> colProduction;
    @FXML private TableColumn<Movie, String> colRuntime;
    @FXML private TableColumn<Movie, String> colRating;

    @FXML private TextField searchTextField;

    private final BooleanProperty enterPressed = new SimpleBooleanProperty(false);

    private ArrayList<Movie> movies;

    public MovieTableViewController() {
        movies = new ArrayList<>();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colStatus.setCellValueFactory(this::statusCellValueFactory);
        colName.setCellValueFactory(this::nameCellValueFactory);
        colGenre.setCellValueFactory(this::genreCellValueFactory);
        colDirector.setCellValueFactory(this::directorCellValueFactory);
        colProduction.setCellValueFactory(this::productionCellValueFactory);
        colRuntime.setCellValueFactory(this::runtimeCellValueFactory);
        colRating.setCellValueFactory(this::ratingCellValueFactory);

        List<TableColumn> columns = new ArrayList<>(entriesTable.getColumns());
        columns.forEach(this::alterColumnCellFactory);

        updateTable();
        entriesTable.setPlaceholder(new Label("No movies found."));
    }

    @FXML
    void fetchPressed(KeyEvent event) {
        if(event.getCode().equals(KeyCode.ENTER)){
            enterPressed.set(true);
        }
    }

    @FXML
    void fetchReleased(KeyEvent event) {
        if(event.getCode().equals(KeyCode.ENTER)){
            enterPressed.set(false);
        }
    }

    void updateTable(){
        entriesTable.getItems().clear();
        ObservableList<Movie> observableContent = FXCollections.observableList(movies
                .stream()
                .filter(distinctByKey(Movie::getTitle))
                .collect(Collectors.toList()));
        entriesTable.getItems().addAll(observableContent);

        /*System.out.println(content.stream()
                .mapToInt(item -> Integer.parseInt(item.getImdbVotes().replaceAll(",", "")))
                .sum());*/
        entriesLabel.setText(String.valueOf(observableContent.size()));
        DecimalFormat numberFormat = new DecimalFormat("#.00");
        OptionalDouble averageScore = observableContent.stream()
                .mapToDouble(a -> Double.parseDouble(a.getImdbRating()))
                .average();
        scoreLabel.setText(averageScore.isPresent() ?
                String.valueOf(numberFormat.format(averageScore.getAsDouble())) : "0");
        completedLabel.setText(String.valueOf(observableContent.stream()
                .filter(a -> a.getStatus().toString().equals(Status.COMPLETED.toString()))
                .count()));
        ongoingLabel.setText(String.valueOf(observableContent.stream()
                .filter(a -> a.getStatus().toString().equals(Status.ONGOING.toString()))
                .count()));
        droppedLabel.setText(String.valueOf(observableContent.stream()
                .filter(a -> a.getStatus().toString().equals(Status.DROPPED.toString()))
                .count()));
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        String hoursWatched = new DecimalFormat("0.0", symbols).format(observableContent.stream()
                .mapToInt(Movie::numericValueOfRuntime)
                .sum() / 60.0);
        hoursLabel.setText(hoursWatched);
        entriesTable.refresh();
    }

    @FXML
    void loadHome(ActionEvent event) {
        SceneController.getInstance().activate(App.View.MAIN_VIEW.getName());
    }

    List<Movie> getMovies() {
        return movies;
    }

    HBox getBreadcrumbsContainer() {
        return breadcrumbsContainer;
    }

    private ReadOnlyObjectWrapper<String> statusCellValueFactory(
            TableColumn.CellDataFeatures<Movie, String> param){
        return new ReadOnlyObjectWrapper<>(param.getValue().getStatus().toString());
    }

    private ReadOnlyObjectWrapper<String> nameCellValueFactory(
            TableColumn.CellDataFeatures<Movie, String> param){
        return new ReadOnlyObjectWrapper<>(param.getValue().getTitle() + " (" + param.getValue().getYear() + ")");
    }

    private ReadOnlyObjectWrapper<String> genreCellValueFactory(
            TableColumn.CellDataFeatures<Movie, String> param){
        return new ReadOnlyObjectWrapper<>(param.getValue().getGenres());
    }

    private ReadOnlyObjectWrapper<String> directorCellValueFactory(
            TableColumn.CellDataFeatures<Movie, String> param){
        return new ReadOnlyObjectWrapper<>(param.getValue().getDirector());
    }

    private ReadOnlyObjectWrapper<String> productionCellValueFactory(
            TableColumn.CellDataFeatures<Movie, String> param){
        return new ReadOnlyObjectWrapper<>(param.getValue().getProduction());
    }

    private ReadOnlyObjectWrapper<String> runtimeCellValueFactory(
            TableColumn.CellDataFeatures<Movie, String> param){
        return new ReadOnlyObjectWrapper<>(param.getValue().getRuntime());
    }

    private ReadOnlyObjectWrapper<String> ratingCellValueFactory(
            TableColumn.CellDataFeatures<Movie, String> param){
        return new ReadOnlyObjectWrapper<>(String.valueOf(param.getValue().getImdbRating()));
    }

    private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

    @SuppressWarnings("unchecked")
    private <S, T> void alterColumnCellFactory(TableColumn column) {
        column.setCellFactory(p -> new TableCell<S, T>() {
            @Override
            public void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                if (!isEmpty() && this.getId().equals("colStatus")) {
                    switch (String.valueOf(item)) {
                        case "Planned":
                            setStyle("-fx-background-color: GREY");
                            break;
                        case "Ongoing":
                            setStyle("-fx-background-color: GREEN");
                            break;
                        case "Completed":
                            setStyle("-fx-background-color: BLUE");
                            break;
                        case "Dropped":
                            setStyle("-fx-background-color: RED");
                            break;
                        default:
                            throw new IllegalStateException("Unexpected value: " + item);
                    }
                    setText("");
                } else {
                    setText(String.valueOf(item));
                }
                if (Objects.isNull(item)) {
                    setText("");
                }
            }
        });
    }
}