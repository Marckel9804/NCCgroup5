package com.project1.group5.db.question;
//
//import java.sql.*;
//import java.util.ArrayList;
//import java.util.List;
//
//public class InsertMovie {
//    public static void main(String[] args) {
//        System.out.println("");
//        String url = "jdbc:mysql://localhost:3306/movie";
//        String username = "root";
//        String password = "9806";
//
//
//        try {
//            Class.forName("com.mysql.cj.jdbc.Driver");
//            Connection connection = DriverManager.getConnection(url, username, password);
//            String sql = "SELECT * FROM moviejson";
//            try (PreparedStatement statement = connection.prepareStatement(sql)) {
//                ResultSet resultSet = statement.executeQuery();
//
//                List<InMovieDTO> movies = new ArrayList<>();
//
//                while (resultSet.next()) {;
//                    String movieId = resultSet.getString("movie_id");
//                    String title = resultSet.getString("title");
//                    String genre = resultSet.getString("genre");
//                    String keyword = resultSet.getString("keyword");
//                    String country = resultSet.getString("country");
//                    String director = resultSet.getString("director");
//                    String running_time = resultSet.getString("running_time");
//                    String rating = resultSet.getString("rating");
//
//                    List<String> genreList = parseJsonArray(genre);
//                    List<String> keywordList = parseJsonArray(keyword);
//                    List<String> directorList = parseJsonArray(director);
//
//                    InMovieDTO movie = new InMovieDTO(movieId, title, genreList, keywordList, country, directorList, running_time, rating);
//
//                    movies.add(movie);
//                }
//
//  //               movies 리스트에 데이터가 들어있음
//                for (InMovieDTO movie : movies) {
//                    System.out.println(movie.getMovie_id() + " " + movie.getTitle() + " " + movie.getGenre() + " " + movie.getKeyword());
//
//
//                    if(movie.getGenre().contains("어드벤처")){
//                        System.out.println(movie.getMovie_id() + " " + movie.getTitle());
//                    }
//
//                    System.out.println(movie.getTitle());
//                    System.out.println(movie.getKeyword());
//                    //특정 키워드가 있는지 접근하기
//                    System.out.println(movie.getKeyword().contains("어드벤처"));
//                    System.out.println(movie);
//                }
//                for (int i=0; i<movies.size();i++){
//                    System.out.println(movies.get(i));
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e){
//            System.out.println("클래스 못 찾음");
//        }
//    }
//
//    private static List<String> parseJsonArray(String jsonArrayString) {
//        List<String> result = new ArrayList<>();
//        if (jsonArrayString != null && !jsonArrayString.isEmpty()) {
//            // 여기서는 간단한 JSON 파싱을 수행합니다.
//            // 실제로는 더욱 강력한 JSON 파서를 사용해야 할 수도 있습니다.
//            jsonArrayString = jsonArrayString.replaceAll("[\\[\\]\"]", ""); // 대괄호 및 쌍따옴표 제거
//            String[] tokens = jsonArrayString.split(",");
//            for (String token : tokens) {
//                result.add(token.trim());
//            }
//        }
//        return result;
//    }
//
//}

//

// 한개 view

import java.util.*;
import java.sql.*;
//
//import java.util.*;
//import java.sql.*;
//
//import java.util.*;
//import java.sql.*;
//
//import java.util.*;
//import java.sql.*;
//
//import java.util.*;
//import java.sql.*;
//
//import java.util.*;
//import java.sql.*;
//
//import java.util.*;
//import java.sql.*;
//import java.util.*;
//import java.sql.*;
//import java.util.*;
//import java.sql.*;
//
//public class InsertMovie {
//    public static void main(String[] args) {
//        System.out.println("");
//        String url = "jdbc:mysql://localhost:3306/movie";
//        String username = "root";
//        String password = "9806";
//
//        try {
//            Class.forName("com.mysql.cj.jdbc.Driver");
//            Connection connection = DriverManager.getConnection(url, username, password);
//            String sql = "SELECT * FROM moviejson";
//            try (PreparedStatement statement = connection.prepareStatement(sql)) {
//                ResultSet resultSet = statement.executeQuery();
//
//                List<InMovieDTO> movies = new ArrayList<>();
//
//                while (resultSet.next()) {
//                    String movieId = resultSet.getString("movie_id");
//                    String title = resultSet.getString("title");
//                    String genre = resultSet.getString("genre");
//                    String keyword = resultSet.getString("keyword");
//                    String country = resultSet.getString("country");
//                    String director = resultSet.getString("director");
//                    String running_time = resultSet.getString("running_time");
//                    String rating = resultSet.getString("rating");
//
//                    List<String> genreList = parseJsonArray(genre);
//                    List<String> keywordList = parseJsonArray(keyword);
//                    List<String> directorList = parseJsonArray(director);
//
//                    InMovieDTO movie = new InMovieDTO(movieId, title, genreList, keywordList, country, directorList, running_time, rating);
//
//                    movies.add(movie);
//                }
//
//                // 각 항목에서 랜덤으로 2개씩 선택하고 선택된 속성을 기반으로 데이터 필터링하여 view 생성
//                String selectedGenre = selectOption("장르", movies);
//                String selectedRating = selectOption("등급", movies);
//                String selectedKeyword = selectOption("키워드", movies);
//                String selectedCountry = selectOption("국가", movies);
//                String selectedRunningTime = selectOption("러닝타임", movies);
//
//                // 선택된 속성을 기반으로 데이터 필터링하여 view 생성
//                createFilteredView(selectedGenre, selectedRating, selectedKeyword, selectedCountry, selectedRunningTime);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            System.out.println("클래스 못 찾음");
//        }
//    }
//
//    private static List<String> parseJsonArray(String jsonArrayString) {
//        List<String> result = new ArrayList<>();
//        if (jsonArrayString != null && !jsonArrayString.isEmpty()) {
//            jsonArrayString = jsonArrayString.replaceAll("[\\[\\]\"]", "");
//            String[] tokens = jsonArrayString.split(",");
//            for (String token : tokens) {
//                result.add(token.trim());
//            }
//        }
//        return result;
//    }
//
//    // 각 항목에서 랜덤으로 count개의 옵션 선택
//    private static List<String> selectRandomOptions(String category, List<InMovieDTO> movies, int count) {
//        Random rand = new Random();
//        List<String> options = new ArrayList<>();
//
//        Set<String> categoryOptions = new HashSet<>();
//
//        // 선택지 생성
//        for (InMovieDTO movie : movies) {
//            switch (category) {
//                case "장르":
//                    categoryOptions.addAll(movie.getGenre());
//                    break;
//                case "등급":
//                    categoryOptions.add(movie.getRating());
//                    break;
//                case "키워드":
//                    categoryOptions.addAll(movie.getKeyword());
//                    break;
//                case "국가":
//                    categoryOptions.add(movie.getCountry());
//                    break;
//                case "러닝타임":
//                    categoryOptions.add(movie.getRunning_time());
//                    break;
//            }
//        }
//
//        // 랜덤으로 count개의 옵션 선택
//        List<String> optionList = new ArrayList<>(categoryOptions);
//        Collections.shuffle(optionList);
//        for (int i = 0; i < count && i < optionList.size(); i++) {
//            System.out.println((i + 1) + ". " + optionList.get(i));
//            options.add(optionList.get(i));
//        }
//
//        return options;
//    }
//
//    // 사용자로부터 한 항목을 선택받음
//    private static String selectOption(String category, List<InMovieDTO> movies) {
//        Scanner scanner = new Scanner(System.in);
//
//        System.out.println("다음 중 " + category + "를 선택하세요:");
//
//        // 랜덤으로 2개의 옵션 선택
//        List<String> options = selectRandomOptions(category, movies, 2);
//
//        // 사용자 입력 받기
//        int selectedIndex;
//        while (true) {
//            System.out.print("선택 (1 또는 2): ");
//            selectedIndex = scanner.nextInt();
//            if (selectedIndex == 1 || selectedIndex == 2) {
//                break;
//            } else {
//                System.out.println("잘못된 입력입니다. 1 또는 2를 입력하세요.");
//            }
//        }
//
//        return options.get(selectedIndex - 1);
//    }
//
//    // 선택된 속성을 기반으로 데이터 필터링하여 view 생성
//    private static void createFilteredView(String selectedGenre, String selectedRating, String selectedKeyword, String selectedCountry, String selectedRunningTime) {
//        String viewName = "filtered_view";
//        String createViewSQL = "CREATE VIEW " + viewName + " AS SELECT * FROM moviejson WHERE ";
//
//        // 필터링 조건 설정
//        createViewSQL += "genre LIKE '%" + selectedGenre + "%' AND ";
//        createViewSQL += "rating = '" + selectedRating + "' AND ";
//        createViewSQL += "keyword LIKE '%" + selectedKeyword + "%' AND ";
//        createViewSQL += "country = '" + selectedCountry + "' AND ";
//        createViewSQL += "running_time = '" + selectedRunningTime + "';";
//
//        System.out.println("뷰 생성 쿼리: " + createViewSQL);
//        // 여기서 데이터베이스에 createViewSQL을 실행하여 view 생성
//    }
//}


public class InsertMovie {
    public static void main(String[] args) {
        System.out.println("");
        String url = "jdbc:mysql://localhost:3306/movie";
        String username = "root";
        String password = "9806";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, username, password);
            String sql = "SELECT * FROM moviejson";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                ResultSet resultSet = statement.executeQuery();

                List<InMovieDTO> movies = new ArrayList<>();

                while (resultSet.next()) {
                    String movieId = resultSet.getString("movie_id");
                    String title = resultSet.getString("title");
                    String genre = resultSet.getString("genre");
                    String keyword = resultSet.getString("keyword");
                    String country = resultSet.getString("country");
                    String director = resultSet.getString("director");
                    String running_time = resultSet.getString("running_time");
                    String rating = resultSet.getString("rating");

                    List<String> genreList = parseJsonArray(genre);
                    List<String> keywordList = parseJsonArray(keyword);
                    List<String> directorList = parseJsonArray(director);

                    InMovieDTO movie = new InMovieDTO(movieId, title, genreList, keywordList, country, directorList, running_time, rating);

                    movies.add(movie);
                }

                // 각 항목에서 랜덤으로 2개씩 선택하고 선택된 속성을 기반으로 데이터 필터링하여 view 생성
                String selectedGenre = selectOption("장르", movies);
                String selectedRating = selectOption("등급", movies);
                String selectedKeyword = selectOption("키워드", movies);
                String selectedCountry = selectOption("국가", movies);
                String selectedRunningTime = selectOption("러닝타임", movies);

                // 선택된 속성을 기반으로 데이터 필터링하여 view 생성
//                createFilteredView(selectedGenre);
//                createFilteredView(selectedRating);
//                createFilteredView(selectedKeyword);
//                createFilteredView(selectedCountry);
//                createFilteredView(selectedRunningTime);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("클래스 못 찾음");
        }
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

    // 각 항목에서 랜덤으로 count개의 옵션 선택
    private static List<String> selectRandomOptions(String category, List<InMovieDTO> movies, int count) {
        Random rand = new Random();
        List<String> options = new ArrayList<>();

        Set<String> categoryOptions = new HashSet<>();

        // 선택지 생성
        for (InMovieDTO movie : movies) {
            switch (category) {
                case "장르":
                    categoryOptions.addAll(movie.getGenre());
                    break;
                case "등급":
                    categoryOptions.add(movie.getRating());
                    break;
                case "키워드":
                    categoryOptions.addAll(movie.getKeyword());
                    break;
                case "국가":
                    categoryOptions.add(movie.getCountry());
                    break;
                case "러닝타임":
                    categoryOptions.add(movie.getRunning_time());
                    break;
            }
        }

        // 랜덤으로 count개의 옵션 선택
        List<String> optionList = new ArrayList<>(categoryOptions);
        Collections.shuffle(optionList);
        for (int i = 0; i < count && i < optionList.size(); i++) {
            System.out.println((i + 1) + ". " + optionList.get(i));
            options.add(optionList.get(i));
        }

        return options;
    }

    // 사용자로부터 한 항목을 선택받음
    private static String selectOption(String category, List<InMovieDTO> movies) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("다음 중 " + category + "를 선택하세요:");

        // 랜덤으로 2개의 옵션 선택
        List<String> options = selectRandomOptions(category, movies, 2);

        // 사용자 입력 받기
        int selectedIndex;
        while (true) {
            System.out.print("선택 (1 또는 2): ");
            selectedIndex = scanner.nextInt();
            if (selectedIndex == 1 || selectedIndex == 2) {
                break;
            } else {
                System.out.println("잘못된 입력입니다. 1 또는 2를 입력하세요.");
            }
        }

        return options.get(selectedIndex - 1);
    }

//    // 선택된 속성을 기반으로 데이터 필터링하여 view 생성
//    private static void createFilteredView(String selectedGenre, String selectedRating, String selectedKeyword, String selectedCountry, String selectedRunningTime) {
//        String viewName = "filtered_view";
//        String createViewSQL = "CREATE VIEW " + viewName + " AS SELECT * FROM moviejson WHERE ";
//
//        // 필터링 조건 설정
//        createViewSQL += "genre LIKE '%" + selectedGenre + "%' AND ";
//        createViewSQL += "rating = '" + selectedRating + "' AND ";
//        createViewSQL += "keyword LIKE '%" + selectedKeyword + "%' AND ";
//        createViewSQL += "country = '" + selectedCountry + "' AND ";
//        createViewSQL += "running_time = '" + selectedRunningTime + "';";
//
//        System.out.println("뷰 생성 쿼리: " + createViewSQL);
//        // 여기서 데이터베이스에 createViewSQL을 실행하여 view 생성
//    }

    private static void createFilteredView(String selectedGenre, String selectedRating, String selectedKeyword, String selectedCountry, String selectedRunningTime) {
        String viewName;
        String createViewSQL;

        // 장르에 대한 뷰 생성
        if (!selectedGenre.isEmpty()) {
            viewName = "filtered_view_genre_" + selectedGenre;
            createViewSQL = "CREATE VIEW " + viewName + " AS SELECT * FROM moviejson WHERE genre LIKE '%" + selectedGenre + "%';";
            System.out.println("장르 뷰 생성 쿼리: " + createViewSQL);
            // 여기서 데이터베이스에 createViewSQL을 실행하여 view 생성
        }

        // 등급에 대한 뷰 생성
        if (!selectedRating.isEmpty()) {
            viewName = "filtered_view_rating_" + selectedRating;
            createViewSQL = "CREATE VIEW " + viewName + " AS SELECT * FROM moviejson WHERE rating = '" + selectedRating + "';";
            System.out.println("등급 뷰 생성 쿼리: " + createViewSQL);
            // 여기서 데이터베이스에 createViewSQL을 실행하여 view 생성
        }

        // 키워드에 대한 뷰 생성
        if (!selectedKeyword.isEmpty()) {
            viewName = "filtered_view_keyword_" + selectedKeyword;
            createViewSQL = "CREATE VIEW " + viewName + " AS SELECT * FROM moviejson WHERE keyword LIKE '%" + selectedKeyword + "%';";
            System.out.println("키워드 뷰 생성 쿼리: " + createViewSQL);
            // 여기서 데이터베이스에 createViewSQL을 실행하여 view 생성
        }

        // 국가에 대한 뷰 생성
        if (!selectedCountry.isEmpty()) {
            viewName = "filtered_view_country_" + selectedCountry;
            createViewSQL = "CREATE VIEW " + viewName + " AS SELECT * FROM moviejson WHERE country = '" + selectedCountry + "';";
            System.out.println("국가 뷰 생성 쿼리: " + createViewSQL);
            // 여기서 데이터베이스에 createViewSQL을 실행하여 view 생성
        }

        // 러닝타임에 대한 뷰 생성
        if (!selectedRunningTime.isEmpty()) {
            viewName = "filtered_view_running_time_" + selectedRunningTime;
            createViewSQL = "CREATE VIEW " + viewName + " AS SELECT * FROM moviejson WHERE running_time = '" + selectedRunningTime + "';";
            System.out.println("러닝타임 뷰 생성 쿼리: " + createViewSQL);
            // 여기서 데이터베이스에 createViewSQL을 실행하여 view 생성
        }
    }

}
