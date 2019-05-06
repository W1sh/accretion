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
import java.util.Optional;
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

        String labelTitleString = movie.getTitle().isEmpty() || movie.getYear().isEmpty() ?
                "" : movie.getTitle().trim() + " (" + movie.getYear() + ")";
        fillLabel(labelTitle, labelTitleString, "No title found");
        String labelRatedString = movie.getType().isEmpty() || movie.getRating().isEmpty() ?
                "" : movie.getType().substring(0, 1).toUpperCase() + movie.getType().substring(1)
                + " - Rated " + movie.getRating();
        fillLabel(labelRated, labelRatedString, "No rating found");
        fillLabel(labelReleased, movie.getReleased(), "No release date");
        String labelRatingString = movie.getImdbRating().isEmpty() || movie.getImdbVotes().isEmpty() ?
                "" : movie.getImdbRating() + " (out of " + movie.getImdbVotes() + " votes)";
        fillLabel(labelRating, labelRatingString, "No rating found");
        fillLabel(labelRuntime, movie.getRuntime(), "No runtime found");
        fillLabel(labelAwards, movie.getAwards(), "No awards found");
        fillLabel(labelProduction, movie.getProduction(), "No production found");
        fillLabel(labelBoxOffice, movie.getBoxOffice(), "No box office information found");
        plotTextArea.setText(Optional.of(movie.getFullPlot()).filter(str -> !str.isEmpty()).orElse("No plot found"));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    private void fillLabel(Label label, String toShow, String defaultValue){
        label.setText(Optional.of(toShow).filter(str -> !str.isEmpty()).orElse(defaultValue));
    }
}
