package com.project1.group5.db.question;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ViewService {
    static InMovieDTO inMovieDTO = new InMovieDTO();
    String currentViewName = "moviejson";
    String url = "jdbc:mysql://localhost:3306/world";
    String username = "root";
    String password = "1234";
    String sql = "";
    public String selKeyword;
    public String selGenre;
    public String selUpdown;
    public String country;
    public String rating;
    public int year;

    Connection connection;
    Statement statement;

    //test를 위한 main thread
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        ViewService vs = new ViewService();

        //view 만드는 예시

        vs.year = 2021;
        System.out.println("년도로 뷰 만들기");
        vs.selectCreate("year");
        System.out.println(vs.year);
        vs.selectFromCurrentView();

        vs.country = "미국";
        System.out.println("국가 정한 뷰");
        vs.selectCreate("country");
        vs.selectFromCurrentView();

        vs.rating = "15";
        System.out.println("관람 등급으로 뷰 만들기");
        vs.selectCreate("rating");
        vs.selectFromCurrentView();

        vs.selUpdown = "up";
        System.out.println("시간 120분 이상 이하로 뷰 만들기");
        vs.selectCreate("time");
        vs.selectFromCurrentView();

        System.out.println("장르로 뷰 만들기");
        List<String> genres = vs.returnList("genre");
        System.out.println(genres);
        vs.selGenre = genres.get((int) (Math.random() * genres.size()));
        System.out.println("선택된 장르: " + vs.selGenre);
        vs.selectCreate("genre");
        vs.selectFromCurrentView();

        System.out.println("키워드로 뷰 만들기");
        List<String> keywords = vs.returnList("keyword");
        System.out.println(keywords);
        vs.selKeyword = keywords.get((int) (Math.random() * keywords.size()));
        System.out.println("선택된 키워드: " + vs.selKeyword);
        vs.selectCreate("keyword");
        vs.selectFromCurrentView();


        vs.dropView("filtered_view_keyword");
        vs.dropView("filtered_view_year");
        vs.dropView("filtered_view_genre");
        vs.dropView("filtered_view_time");
        vs.dropView("filtered_view_country");
        vs.dropView("filtered_view_rating");
    }

    // db connect, close
    public void connectDb() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        connection = DriverManager.getConnection(url, username, password);
    }

    public void closeDb() throws SQLException {
        connection.close();
    }

    // 어떤 view 만들지 선택
    public void selectCreate(String option) throws SQLException, ClassNotFoundException {
        connectDb();

        switch (option) {
            case "genre":
                createViewByGenre(selGenre);
                break;
            case "keyword":
                createViewByKeyword(selKeyword);
                break;
            case "year":
                createViewByYear(year / 10);
                break;
            case "country":
                createViewByCountry(country);
                break;
            case "rating":
                createViewByRating(rating);
                break;
            case "time":
                createViewByTime("120", selUpdown);
                break;
        }
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.executeQuery();

        closeDb();
    }

    // view 생성 프로시져 sql 작성
    public void createViewByGenre(String genre) {
        sql = "call createFilteredGenre ('" + genre + "', '" + this.currentViewName + "')";
        this.currentViewName = "filtered_view_genre";
    }

    public void createViewByKeyword(String keyword) {
        sql = "call createFilteredKeyword ('" + keyword + "', '" + this.currentViewName + "')";
        this.currentViewName = "filtered_view_keyword";
    }

    public void createViewByYear(int year) {
        sql = "call createFilteredYear ('" + year + "', '" + this.currentViewName + "')";
        this.currentViewName = "filtered_view_year";
    }

    public void createViewByCountry(String country) {
        sql = "call createFilteredCountry ('" + country + "', '" + this.currentViewName + "')";
        this.currentViewName = "filtered_view_country";
    }

    public void createViewByTime(String time, String updown) {
        sql = "call createFilteredTime (" + time + ", '" + updown + "', '" + this.currentViewName + "')";
        this.currentViewName = "filtered_view_time";
    }

    public void createViewByRating(String rating) {
        sql = "call createFilteredRating ('" + rating + "', '" + this.currentViewName + "')";
        this.currentViewName = "filtered_view_rating";
    }

    // 현재 뷰에서 영화들 정보 출력하기
    public ArrayList<InMovieDTO> selectFromCurrentView() throws SQLException, ClassNotFoundException {
        connectDb();
        sql = "call selectCurrentView( '" + this.currentViewName + "');";
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
        return movieList;
    }

    // 중복처리한 배열 안의 요소 가져오기
    public List<String> returnList(String option) throws SQLException, ClassNotFoundException {
        connectDb();
        List<String> res = new ArrayList<String>();
        sql = "call getViewAttributeList('" + option + "', '" + this.currentViewName + "');";
        PreparedStatement ps = connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            res.add(rs.getString(1));
        }
        closeDb();
        return res;
    }

    //view 지우기
    public void dropView(String viewName) throws SQLException, ClassNotFoundException {
        connectDb();
        sql = "call dropView('" + viewName + "');";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.executeQuery();
        closeDb();
        System.out.println(viewName + " is dropped");
    }

    // json 데이터 파싱
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
