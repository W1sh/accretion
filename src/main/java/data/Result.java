package data;

import com.google.gson.annotations.SerializedName;

public class Result {

    @SerializedName("Title") private String title;
    @SerializedName("Year") private int year;
    @SerializedName("Poster") private String poster;
    private String imdbID;

    public Result(String title, int year, String poster, String imdbID) {
        this.title = title;
        this.year = year;
        this.poster = poster;
        this.imdbID = imdbID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getImdbID() {
        return imdbID;
    }

    public void setImdbID(String imdbID) {
        this.imdbID = imdbID;
    }

    @Override
    public String toString() {
        return "Result{" +
                "title='" + title + '\'' +
                ", year=" + year +
                ", poster='" + poster + '\'' +
                ", imdbID='" + imdbID + '\'' +
                '}';
    }
}
