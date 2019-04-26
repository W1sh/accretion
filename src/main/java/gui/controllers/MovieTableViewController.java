package gui.controllers;

import data.Movie;
import data.Status;
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
        colStatus.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(
                param.getValue().getStatus().toString()));
        colName.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(
                param.getValue().getTitle() + " (" + param.getValue().getYear() + ")"));
        colGenre.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(
                param.getValue().getGenres()));
        colDirector.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(
                param.getValue().getDirector()));
        colProduction.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(
                param.getValue().getProduction()));
        colRuntime.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(
                param.getValue().getRuntime()));
        colRating.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(
                String.valueOf(param.getValue().getImdbRating())));

        List<TableColumn> columns = new ArrayList<>(entriesTable.getColumns());
        for(TableColumn column : columns){
            alterColumnCellFactory(column);
        }
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
        ArrayList<Movie> distinctMovies = (ArrayList<Movie>) movies.stream().
                filter(distinctByKey(Movie::getTitle)).
                collect(Collectors.toList());
        ObservableList<Movie> content = FXCollections.observableList(distinctMovies);
        entriesTable.getItems().addAll(content);

        /*System.out.println(content.stream()
                .mapToInt(item -> Integer.parseInt(item.getImdbVotes().replaceAll(",", "")))
                .sum());*/
        entriesLabel.setText(String.valueOf(content.size()));
        DecimalFormat numberFormat = new DecimalFormat("#.00");
        OptionalDouble averageScore = content.stream()
                .mapToDouble(a -> Double.parseDouble(a.getImdbRating()))
                .average();
        scoreLabel.setText(averageScore.isPresent() ?
                String.valueOf(numberFormat.format(averageScore.getAsDouble())) : "0");
        completedLabel.setText(String.valueOf(content.stream()
                .filter(a -> a.getStatus().toString().equals(Status.COMPLETED.toString()))
                .count()));
        ongoingLabel.setText(String.valueOf(content.stream()
                .filter(a -> a.getStatus().toString().equals(Status.ONGOING.toString()))
                .count()));
        droppedLabel.setText(String.valueOf(content.stream()
                .filter(a -> a.getStatus().toString().equals(Status.DROPPED.toString()))
                .count()));
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        String hoursWatched = new DecimalFormat("0.0", symbols).format(content.stream()
                .mapToInt(Movie::numericValueOfRuntime)
                .sum() / 60.0);
        hoursLabel.setText(hoursWatched);
        entriesTable.refresh();
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

    @FXML
    void loadHome(ActionEvent event) {
        SceneController sceneController = SceneController.getInstance();
        sceneController.activate("main");
    }

    public ArrayList<Movie> getMovies() {
        return movies;
    }

    private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

    public HBox getBreadcrumbsContainer() {
        return breadcrumbsContainer;
    }
}