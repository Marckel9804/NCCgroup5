package com.project1.group5.db.question;

import jakarta.persistence.criteria.CriteriaBuilder;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ViewService {
    InMovieDTO inMovieDTO = new InMovieDTO();
    String currentViewName = "moviejson";
    String url = "jdbc:mysql://localhost:3306/ozo";
    String username = "root";
    String password = "1234";
    String sql = "";
    Connection connection;
    Statement statement;

    //test를 위한 main thread
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        ViewService vs = new ViewService();
        vs.selectCreate("country", vs.currentViewName);
    }

    public void connectDb() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        connection = DriverManager.getConnection(url, username, password);
    }

    public void closeDb() throws SQLException {
        connection.close();
    }

    public void selectCreate(String option, String currentViewName) throws SQLException, ClassNotFoundException {
        connectDb();
        switch (option) {
            case "genre":
                createViewByGenre(inMovieDTO.getGenre().get(0), currentViewName);
            case "keyword":
                createViewByKeyword(inMovieDTO.getKeyword().get(0), currentViewName);
            case "year":
                createViewByYear(inMovieDTO.getRelease_year() / 10, currentViewName);
            case "country":
                createViewByCountry(inMovieDTO.getCountry(), currentViewName);
            case "rating":
                createViewByRating(inMovieDTO.getRating(), currentViewName);
            case "time":
                createViewByTime(inMovieDTO.getRunning_time(), currentViewName);
        }
        PreparedStatement ps = connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        ArrayList<InMovieDTO> movieList = new ArrayList<InMovieDTO>();
        while (rs.next()) {
            List<String> genreList = parseJsonArray(rs.getString("genre"));
            List<String> keywordList = parseJsonArray(rs.getString("keyword"));
            List<String> directorList = parseJsonArray(rs.getString("director"));

            InMovieDTO movie = new InMovieDTO(rs.getString("movie_id"), rs.getString("title"), Integer.parseInt(rs.getString("release_year"))
                    , genreList, keywordList, rs.getString("country"), directorList,
                    rs.getString("running_time"), rs.getString("rating"));

            movieList.add(movie);
        }

        for (InMovieDTO dto : movieList) {
            System.out.println(dto);
        }
        closeDb();
    }

    public void createViewByGenre(String genre, String currentViewName) {
        sql = "call createFilteredGenreView ('" + genre + ", " + currentViewName + "')";
        currentViewName = "filtered_view_genre";
    }

    public void createViewByKeyword(String keyword, String currentViewName) {
        sql = "call createFilteredKeyword ('" + keyword + ", " + currentViewName + "')";
        currentViewName = "filtered_view_keyword";
    }

    public void createViewByYear(int year, String currentViewName) {
        sql = "call createFilteredYear ('" + year + ", " + currentViewName + "')";
        currentViewName = "filtered_view_year";
    }

    public void createViewByCountry(String country, String currentViewName) {
        sql = "call createFilteredCountry ('" + country + ", " + currentViewName + "')";
        currentViewName = "filtered_view_country";
    }

    public void createViewByTime(String time, String currentViewName) {
        sql = "call createFilteredTime ('" + time + ", " + currentViewName + "')";
        currentViewName = "filtered_view_time";
    }

    public void createViewByRating(String rating, String currentViewName) {
        sql = "call createFilteredRating ('" + rating + ", " + currentViewName + "')";
        currentViewName = "filtered_view_rating";
    }

    public List<String> returnList(String option, String currentViewName) {
        List<String> res = new ArrayList<String>();
        return res;
    }

    private static List<String> parseJsonArray(String jsonArrayString) {
        List<String> result = new ArrayList<>();
        if (jsonArrayString != null && !jsonArrayString.isEmpty()) {
            jsonArrayString = jsonArrayString.replaceAll("[\\[\\]\"]", "");
            String[] tokens = jsonArrayString.split(",");
            for (String token : tokens) {
                result.add(token.trim());
            }
        }
        return result;
    }
}
