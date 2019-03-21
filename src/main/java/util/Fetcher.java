package util;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import data.Movie;
import data.Result;
import data.Status;

import java.io.*;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
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
        JsonArray jsonArray = jsonObject.get("Search").getAsJsonArray();

        ArrayList<Result> results = new ArrayList<>();
        for (JsonElement element : jsonArray) {
            Result result = new Gson().fromJson(element.getAsJsonObject(), Result.class);
            results.add(result);
        }
        return results;
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
        } catch (IOException ioException){
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
