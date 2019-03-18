import com.google.gson.*;
import data.Movie;
import data.Status;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Fetcher {

    public static final String API_KEY = "&apikey=";
    public static final String SINGLE_FETCH_OMDB = "http://www.omdbapi.com/?t=";
    public static final String MULTIPLE_FETCH_OMDB = "http://www.omdbapi.com/?s=";

    public static Movie fetchMovie(String name) {
        String constructedURL = SINGLE_FETCH_OMDB + URLEncoder.encode(name, StandardCharsets.UTF_8) + API_KEY;
        String json = readUrl(constructedURL);
        JsonElement jsonElement = new JsonParser().parse(json);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        if(jsonObject.get("Error") == null){
            Movie movie = new Gson().fromJson(jsonObject, Movie.class);
            movie.setStatus(Status.PLANNED);
            return movie;
        }else{
            //Show error box
            return null;
        }
    }

    public static List<String> fetchMovies(String filter) {
        String constructedURL = MULTIPLE_FETCH_OMDB + filter + API_KEY;
        String json = readUrl(constructedURL);
        JsonObject jsonObject = new Gson().fromJson(json, JsonObject.class);
        JsonArray jsonArray = jsonObject.get("Search").getAsJsonArray();

        String title;
        int year;
        ArrayList<String> movies = new ArrayList<>();
        for (JsonElement element : jsonArray) {
            title = element.getAsJsonObject().get("Title").getAsString();
            year = element.getAsJsonObject().get("Year").getAsInt();
            movies.add(title + ", " + year);
        }
        return movies;
    }

    public static String readUrl(String urlString) {
        BufferedReader reader;
        try {
            URL url = new URL(urlString);
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuilder buffer = new StringBuilder();
            int read;
            char[] chars = new char[1024];
            while ((read = reader.read(chars)) != -1)
                buffer.append(chars, 0, read);
            reader.close();
            return buffer.toString();
        } catch (IOException ioException){
            ioException.printStackTrace();
        }
        return "";
    }
}
