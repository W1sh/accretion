import data.Movie;
import data.Result;
import data.Status;
import org.junit.jupiter.api.Test;

import java.util.List;

class FetcherTest {

    @Test
    void fetchMovie() {
        Movie movie = Fetcher.fetchMovie("Inception");
        movie.setStatus(Status.PLANNED);
        System.out.println(movie.toString());
    }

    @Test
    void fetchMultipleMovies() {
        List<Result> results = Fetcher.fetchMovies("Godfather");
        results.forEach(System.out::println);
    }
}