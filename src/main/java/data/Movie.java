package data;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public class Movie {

    @SerializedName("Title") private String title;
    @SerializedName("Year") private String year; // int
    @SerializedName("Runtime") private String runtime;
    @SerializedName("Rated") private String rating;
    @SerializedName("Released") private String released;
    @SerializedName("Director") private String director;
    @SerializedName("Writer") private String writers;
    @SerializedName("Actors") private String actors;
    @SerializedName("Genre") private String genres;
    @SerializedName("Plot") private String plot;
    @SerializedName("Language") private String language;
    @SerializedName("Country") private String country;
    @SerializedName("Awards") private String awards;
    @SerializedName("Type") private String type;
    @SerializedName("Production") private String production;
    @SerializedName("BoxOffice") private String boxOffice; // currency
    @SerializedName("Metascore") private String metascore; // int
    @SerializedName("Poster") private String poster; // int
    private String imdbRating; // double
    private String imdbVotes; // int
    private String imdbID;
    private Status status;

    public Movie() { }

    public Movie(String title, String year, String runtime, String rating, String director,
                 String writers, String actors, String genres, String plot, String language,
                 String country, String awards, String production, String boxOffice, String metascore,
                 String imdbRating, String imdbVotes, String imdbID, Status status) {
        this.title = title;
        this.year = year;
        this.runtime = runtime;
        this.rating = rating;
        this.director = director;
        this.writers = writers;
        this.actors = actors;
        this.genres = genres;
        this.plot = plot;
        this.language = language;
        this.country = country;
        this.awards = awards;
        this.production = production;
        this.boxOffice = boxOffice;
        this.metascore = metascore;
        this.imdbRating = imdbRating;
        this.imdbVotes = imdbVotes;
        this.imdbID = imdbID;
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getWriters() {
        return writers;
    }

    public void setWriters(String writers) {
        this.writers = writers;
    }

    public String getActors() {
        return actors;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }

    public String getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAwards() {
        return awards;
    }

    public void setAwards(String awards) {
        this.awards = awards;
    }

    public String getProduction() {
        return production;
    }

    public void setProduction(String production) {
        this.production = production;
    }

    public String getBoxOffice() {
        return boxOffice;
    }

    public void setBoxOffice(String boxOffice) {
        this.boxOffice = boxOffice;
    }

    public String getMetascore() {
        return metascore;
    }

    public void setMetascore(String metascore) {
        this.metascore = metascore;
    }

    public String getImdbRating() {
        return imdbRating;
    }

    public void setImdbRating(String imdbRating) {
        this.imdbRating = imdbRating;
    }

    public String getImdbVotes() {
        return imdbVotes;
    }

    public void setImdbVotes(String imdbVotes) {
        this.imdbVotes = imdbVotes;
    }

    public String getImdbID() {
        return imdbID;
    }

    public void setImdbID(String imdbID) {
        this.imdbID = imdbID;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getReleased() {
        return released;
    }

    public void setReleased(String released) {
        this.released = released;
    }

    public int numericValueOfRuntime(){
        String numericValue = getRuntime().replaceAll("[^0-9]", "");
        if (numericValue.length() > 2){
            return (Integer.parseInt(numericValue.substring(0,1)) * 60) + Integer.parseInt(numericValue.substring(1));
        }else {
            return Integer.parseInt(numericValue);
        }
    }

    @Override
    public String toString() {
        return "Movie{" +
                "title='" + title + '\'' +
                ", year=" + year +
                ", runtime='" + runtime + '\'' +
                ", rating='" + rating + '\'' +
                ", director='" + director + '\'' +
                ", writers='" + writers + '\'' +
                ", actors='" + actors + '\'' +
                ", genres='" + genres + '\'' +
                ", plot='" + plot + '\'' +
                ", language='" + language + '\'' +
                ", country='" + country + '\'' +
                ", awards='" + awards + '\'' +
                ", production='" + production + '\'' +
                ", boxOffice='" + boxOffice + '\'' +
                ", metascore=" + metascore +
                ", imdbRating=" + imdbRating +
                ", imdbVotes='" + imdbVotes + '\'' +
                ", imdbID='" + imdbID + '\'' +
                '}';
    }
}
