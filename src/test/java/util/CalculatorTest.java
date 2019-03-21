package util;

import data.Movie;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest {

    @Test
    void statistics() {
        ArrayList<Movie> movies = new ArrayList<>();
        Movie movie = Fetcher.fetchMovie("Inception");
        movies.add(movie);
        System.out.println(Calculator.statistics(movies, Objects::nonNull));
    }
}