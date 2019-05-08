package gui.controllers;

import data.Movie;
import gui.utils.Breadcrumb;
import gui.utils.SceneHelper;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SceneMediator implements ISceneMediator {

    private static SceneMediator instance = null;
    private final HashMap<String, Parent> scenes = new HashMap<>();

    private List<Breadcrumb> breadcrumbs = new ArrayList<>();
    private MainViewController mainViewController;
    private MovieTableViewController movieTableViewController;
    private MovieDetailsViewController movieDetailsViewController;
    private SceneHelper helper = new SceneHelper();
    private Scene main;

    private SceneMediator(){}

    public static SceneMediator getInstance(){
        if(instance == null){
            instance = new SceneMediator();
        }
        return instance;
    }

    @Override
    public void registerController(Initializable controller) {
        if (controller instanceof MainViewController){
            mainViewController = (MainViewController) controller;
        }else if (controller instanceof  MovieTableViewController){
            movieTableViewController = (MovieTableViewController) controller;
        }else if (controller instanceof  MovieDetailsViewController){
            movieDetailsViewController = (MovieDetailsViewController) controller;
        }else{
            throw new IllegalStateException("Unexpected value received. Cannot cast to possible controllers.");
        }
    }

    @Override
    public void registerScene(String sceneName, Parent pane) {
        scenes.put(sceneName, pane);
    }

    @Override
    public void activateScene(String sceneName) {
        registerBreadcrumb(sceneName);
        createBreadcrumbs(sceneName);
        main.setRoot(scenes.get(sceneName));
    }

    boolean movieTableRegisterMovie(Movie movie){
        return movieTableViewController.getMovies().add(movie);
    }

    boolean movieTableDeleteMovie(Movie movie){
        return movieTableViewController.getMovies().remove(movie);
    }

    void movieTableUpdate(){
        movieTableViewController.updateTable();
    }

    void movieDetailsShow(Movie movie){
        movieDetailsViewController.showDetails(movie);
    }

    private void registerBreadcrumb(String sceneName) {
        int indexOfBreadcrumb = -1;
        for (int i=0; i<breadcrumbs.size(); i++) {
            if(sceneName.equals(breadcrumbs.get(i).getName())){
                indexOfBreadcrumb = i;
            }
        }
        if(indexOfBreadcrumb != -1){
            breadcrumbs = breadcrumbs.subList(0, indexOfBreadcrumb + 1);
        }else{
            breadcrumbs.add(new Breadcrumb(sceneName, false));
        }
        breadcrumbs.forEach(item -> item.setDisabled(false));
        breadcrumbs.get(breadcrumbs.size() - 1).setDisabled(true);
    }

    private void createBreadcrumbs(String sceneName) {
        switch (sceneName) {
            case "Main" : break;
            case "Movie Table" :
                movieTableViewController.getBreadcrumbsContainer().getChildren().clear();
                breadcrumbs.forEach(item ->
                    movieTableViewController.getBreadcrumbsContainer().getChildren().addAll(item.getNodes()));
                break;
            case "Movie Details" : /*breadcrumbs.forEach(item ->
                    movieDetailsViewController.getBreadcrumbsContainer().getChildren().addAll(item.getNodes()));
                */
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + sceneName);
        }
    }

    public void setMain(Scene main) {
        this.main = main;
    }

    public Scene getMain() {
        return main;
    }

    SceneHelper getHelper() {
        return helper;
    }
}


