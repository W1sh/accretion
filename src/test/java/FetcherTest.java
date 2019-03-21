import data.Movie;
import data.Result;
import org.junit.jupiter.api.Test;
import util.Fetcher;

import java.util.List;

class FetcherTest {

    @Test
    void fetchMovie() {
        Movie movie = Fetcher.fetchMovie("Inception");
        System.out.println(movie.toString());
    }

    @Test
    void fetchMultipleMovies() {
        List<Result> results = Fetcher.fetchMovies("Godfather");
        results.forEach(System.out::println);
    }
}