import data.Movie;
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
        List<String> json = Fetcher.fetchMovies("Godfather");
        System.out.println(json);
    }
}