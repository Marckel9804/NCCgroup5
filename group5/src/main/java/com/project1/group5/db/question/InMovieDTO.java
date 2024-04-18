package com.project1.group5.db.question;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class InMovieDTO {

    private String movie_id;
    private String title;
    private int release_year;
    private List<String> genre;
    private List<String> keyword;
    private String country;
    private List<String> director;
    private String running_time;
    private String rating;


    InMovieDTO() {
    }

    InMovieDTO(String movie_id, String title, int release_year, List<String> genre, List<String> keyword, String country, List<String> director, String running_time, String rating) {
        this.movie_id = movie_id;
        this.title = title;
        this.release_year = release_year;
        this.genre = genre;
        this.keyword = keyword;
        this.country = country;
        this.director = director;
        this.running_time = running_time;
        this.rating = rating;

    }

    @Override
    public String toString() {
        return movie_id + " " + title + " " + genre + " " + keyword + " " + country + " " + director + " " + running_time + " " + rating;
    }
}

