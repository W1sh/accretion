package gui.controllers;

import com.jfoenix.controls.JFXTextArea;
import data.Movie;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MovieDetailsViewController implements Initializable {

    @FXML private ImageView posterImageView;
    @FXML private JFXTextArea plotTextArea;
    @FXML private Label labelTitle;
    @FXML private Label labelRated;
    @FXML private Label labelReleased;
    @FXML private Label labelRuntime;
    @FXML private Label labelRating;
    @FXML private Label labelAwards;
    @FXML private Label labelProduction;
    @FXML private Label labelBoxOffice;

    public void showDetails(Movie movie){
        try {
            posterImageView.setFitHeight(270);
            posterImageView.setFitWidth(215);
            posterImageView.setImage(new Image(new URL(movie.getPoster()).openStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        String labelTitleString = movie.getTitle().trim() + " (" + movie.getYear() + ")";
        labelTitle.setText(labelTitleString);
        String labelRatedString = movie.getType().substring(0, 1).toUpperCase() + movie.getType().substring(1)
                + " - " + movie.getRating();
        labelRated.setText(labelRatedString);
        labelReleased.setText(movie.getReleased());
        String labelRatingString = movie.getImdbRating() + " (out of " + movie.getImdbVotes() + " votes)";
        labelRating.setText(labelRatingString);
        labelRuntime.setText(movie.getRuntime());
        labelAwards.setText(movie.getAwards());
        String plot = movie.getPlot() + System.lineSeparator() + System.lineSeparator() + "Director: "
                + movie.getDirector() + System.lineSeparator() + "Writers: " + movie.getWriters()
                + System.lineSeparator() +  "Actors: " + movie.getActors();
        plotTextArea.setText(plot);
        labelProduction.setText(movie.getProduction());
        labelBoxOffice.setText(movie.getBoxOffice());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
