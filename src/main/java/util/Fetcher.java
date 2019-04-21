package util;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import data.Movie;
import data.Result;
import data.Status;
import gui.utils.Dialog;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.Region;

import java.io.*;
import java.lang.reflect.Type;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Fetcher {

    public static final String API_TAG = "&apikey=";
    public static final String SINGLE_FETCH_OMDB = "http://www.omdbapi.com/?t=";
    public static final String MULTIPLE_FETCH_OMDB = "http://www.omdbapi.com/?s=";

    public static Movie fetchMovie(String name) {
        String json = fetchJsonFromURL(SINGLE_FETCH_OMDB + URLEncoder.encode(name, StandardCharsets.UTF_8));
        JsonElement jsonElement = new JsonParser().parse(json);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        if(jsonObject.get("Error") == null){
            //Type type = new TypeToken<Map<String, String>>(){}.getType();
            //Map<String, String> myMap = new Gson().fromJson("{'k1':'apple','k2':'orange'}", type);
            Movie movie = new Gson().fromJson(jsonObject, Movie.class);
            movie.setStatus(Status.PLANNED);
            return movie;
        }else{
            //Show error box
            return null;
        }
    }

    public static List<Result> fetchMovies(String filter) {
        String json = fetchJsonFromURL(MULTIPLE_FETCH_OMDB + URLEncoder.encode(
                filter, StandardCharsets.UTF_8));
        JsonObject jsonObject = new Gson().fromJson(json, JsonObject.class);
        try {
            JsonArray jsonArray = jsonObject.get("Search").getAsJsonArray();
            ArrayList<Result> results = new ArrayList<>();
            for (JsonElement element : jsonArray) {
                Result result = new Gson().fromJson(element.getAsJsonObject(), Result.class);
                result.setMovie(fetchMovie(result.getTitle()));
                results.add(result);
            }
            return results;
        } catch (NullPointerException npe){
            npe.printStackTrace();
            return Collections.emptyList();
        }
    }

    private static String fetchAPIKey() {
        try {
            JsonElement json = new Gson().fromJson(new FileReader(
                    "/home/bruno/IdeaProjects/accretion/config.json"), JsonElement.class);
            return json.getAsJsonObject().get("API_KEY").getAsString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    private static String fetchJsonFromURL(String encodedURLWithQuery){
        final String API_KEY = fetchAPIKey();
        String constructedURL = encodedURLWithQuery + API_TAG + API_KEY;
        BufferedReader reader;
        try {
            URL url = new URL(constructedURL);
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuilder buffer = new StringBuilder();
            int read;
            char[] chars = new char[1024];
            while ((read = reader.read(chars)) != -1)
                buffer.append(chars, 0, read);
            reader.close();
            return buffer.toString();
        } catch (MalformedURLException malformedURLException) {
            // error dialog, malformed URL
            System.out.println("Malformed URL");
        } catch (SocketException | UnknownHostException internetRelatedException){
            // log exception
            Dialog.showAlertDialog("Please check your internet connection!");
        } catch (IOException ioException){
            // log exception
            ioException.printStackTrace();
        }
        return "";
    }

    public class IgnoringFieldsNotMatchingCriteriaSerializer
            implements JsonSerializer<Movie> {

        /*@Override
        public JsonElement serialize
                (SourceClass src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject jObject = new JsonObject();

            // Criteria: intValue >= 0
            if (src.getIntValue() >= 0) {
                String intValue = "intValue";
                jObject.addProperty(intValue, src.getIntValue());
            }

            String stringValue = "stringValue";
            jObject.addProperty(stringValue, src.getStringValue());

            return jObject;
        }*/

        @Override
        public JsonElement serialize(Movie movie, Type type, JsonSerializationContext jsonSerializationContext) {
            return null;
        }
    }
}
