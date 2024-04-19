package com.project1.group5.db.question;

import java.sql.*;
import java.util.*;

// 현재 질문 순서 : 장르 -> 등급 -> 키워드 -> 국가 -> 러닝타임
// 해야하는 것 ,,?  : 러닝타임 범위로 묶어서 / +개봉년도 안넣었는데 넣고 러닝타임처럼 범위 묶어서 추가,,,,?
// 키워드 중복 없이 가져오기,,?

public class InsertMovie {
    public static void main(String[] args) {
        System.out.println("");
        String url = "jdbc:mysql://localhost:3306/sm";
        String username = "root";
        String password = "1234";

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

                    InMovieDTO movie = new InMovieDTO(movieId, title, genreList, keywordList, country, directorList,
                            running_time, rating);
                    movies.add(movie);
                }

                // 장르만 선택하고 종료
                String selectedGenre = selectOption("장르", movies);
                boolean isGenreSelected = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("클래스 못 찾음");
        }

        //// 장르고르고 등급
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, username, password);
            String sql = "SELECT * FROM filtered_view_genre_";

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

                    InMovieDTO movie = new InMovieDTO(movieId, title, genreList, keywordList, country, directorList,
                            running_time, rating);
                    movies.add(movie);
                }

                String selectedRating = selectOption("등급", movies);
                boolean isRatingSelected = true;

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("클래스 못 찾음");
        }

        ////// 등급찾고 키워드
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, username, password);
            String sql = "SELECT * FROM filtered_view_rating_";

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

                    InMovieDTO movie = new InMovieDTO(movieId, title, genreList, keywordList, country, directorList,
                            running_time, rating);
                    movies.add(movie);
                }

                String selectedKeyword = selectOption("키워드", movies);
                boolean isKeywordSelected = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("클래스 못 찾음");
        }

        ////// 키워드찾고 국가
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, username, password);
            String sql = "SELECT * FROM filtered_view_keyword_";

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

                    InMovieDTO movie = new InMovieDTO(movieId, title, genreList, keywordList, country, directorList,
                            running_time, rating);
                    movies.add(movie);
                }

                String selectedCountry = selectOption("국가", movies);
                boolean isCountrySelected = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("클래스 못 찾음");
        }

        ////// 국가찾고 러닝타임
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, username, password);
            String sql = "SELECT * FROM filtered_view_country_";

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

                    InMovieDTO movie = new InMovieDTO(movieId, title, genreList, keywordList, country, directorList,
                            running_time, rating);
                    movies.add(movie);
                }

                String selectedRunningTime = selectOption("러닝타임", movies);
                boolean isRunningTimeSelected = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("클래스 못 찾음");
        }

    }

    public static List<String> parseJsonArray(String jsonArrayString) {
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
    public static List<String> selectRandomOptions(String category, List<InMovieDTO> movies, int count) {
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
    public static String selectOption(String category, List<InMovieDTO> movies) {
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
        // 선택한 항목으로 뷰 생성
        String selectedOption = options.get(selectedIndex - 1);

        switch (category) {
            case "장르":
                System.out.println("지금 장르를 물어보는중");
                createFilteredGenreView(selectedOption);
                showFilteredGenreView(selectedOption);
                break;
            case "등급":
                System.out.println("지금 등급을 물어보는중");
                createFilteredRatingView(selectedOption);
                showFilteredRatingView(selectedOption);
                break;
            case "키워드":
                System.out.println("지금 키워드를 물어보는중");
                createFilteredKeywordView(selectedOption);
                showFilteredKeywordView(selectedOption);
                break;
            case "국가":
                System.out.println("지금 국가를 물어보는중");
                createFilteredCountryView(selectedOption);
                showFilteredCountryView(selectedOption);
                break;
            case "러닝타임":
                System.out.println("지금 러닝타임을 물어보는중");
                createFilteredRunningView(selectedOption);
                showFilteredRunningView(selectedOption);
                break;
            default:
                System.out.println("니 프로그램은 고장났어 병신아");
        }

        // createFilteredGenreView(selectedOption); // 프로시저 호출
        // showFilteredGenreView(selectedOption);
        return selectedOption;
    }

    // 프로시저 호출 메서드
    public static void createFilteredGenreView(String selectedGenre) {
        String url = "jdbc:mysql://localhost:3306/movie";
        String username = "root";
        String password = "9806";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, username, password);

            // 프로시저 호출을 위한 쿼리 설정
            String query = "{call createFilteredGenreView(?)}";
            CallableStatement cs = connection.prepareCall(query);
            cs.setString(1, selectedGenre); // 선택한 장르 전달

            // 프로시저 실행
            cs.execute();
            System.out.println("프로시저 호출 완료: filtered_view_genre_" + selectedGenre);

            // 자원 해제
            cs.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("클래스 못 찾음");
        }
    }

    public static void createFilteredRatingView(String selectedRating) {
        String url = "jdbc:mysql://localhost:3306/movie";
        String username = "root";
        String password = "9806";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, username, password);

            // 프로시저 호출을 위한 쿼리 설정
            String query = "{call createFilteredRatingView(?)}";
            CallableStatement cs = connection.prepareCall(query);
            cs.setString(1, selectedRating); // 선택한 등급 전달

            // 프로시저 실행
            cs.execute();
            System.out.println("프로시저 호출 완료: filtered_view_rating_" + selectedRating);

            // 자원 해제
            cs.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("클래스 못 찾음");
        }
    }

    public static void createFilteredKeywordView(String selectedKeyword) {
        String url = "jdbc:mysql://localhost:3306/movie";
        String username = "root";
        String password = "9806";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, username, password);

            // 프로시저 호출을 위한 쿼리 설정
            String query = "{call createFilteredKeywordView(?)}";
            CallableStatement cs = connection.prepareCall(query);
            cs.setString(1, selectedKeyword); // 선택한 장르 전달

            // 프로시저 실행
            cs.execute();
            System.out.println("프로시저 호출 완료: filtered_view_keyword_" + selectedKeyword);

            // 자원 해제
            cs.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("클래스 못 찾음");
        }
    }

    public static void createFilteredCountryView(String selectedCountry) {
        String url = "jdbc:mysql://localhost:3306/movie";
        String username = "root";
        String password = "9806";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, username, password);

            // 프로시저 호출을 위한 쿼리 설정
            String query = "{call createFilteredCountryView(?)}";
            CallableStatement cs = connection.prepareCall(query);
            cs.setString(1, selectedCountry); // 선택한 장르 전달

            // 프로시저 실행
            cs.execute();
            System.out.println("프로시저 호출 완료: filtered_view_country_" + selectedCountry);

            // 자원 해제
            cs.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("클래스 못 찾음");
        }
    }

    public static void createFilteredRunningView(String selectedRunningTime) {
        String url = "jdbc:mysql://localhost:3306/movie";
        String username = "root";
        String password = "9806";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, username, password);

            // 프로시저 호출을 위한 쿼리 설정
            String query = "{call createFilteredRunningView(?)}";
            CallableStatement cs = connection.prepareCall(query);
            cs.setString(1, selectedRunningTime); // 선택한 장르 전달

            // 프로시저 실행
            cs.execute();
            System.out.println("프로시저 호출 완료: filtered_view_running_time_" + selectedRunningTime);

            // 자원 해제
            cs.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("클래스 못 찾음");
        }
    }

    // 생성된 뷰 조회 메서드
    // 장르 뷰
    public static void showFilteredGenreView(String selectedGenre) {
        String url = "jdbc:mysql://localhost:3306/movie";
        String username = "root";
        String password = "9806";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, username, password);

            // 뷰 조회 쿼리 설정
            String query = "SELECT * FROM filtered_view_genre_";
            Statement statement = connection.createStatement();

            // 뷰 조회
            ResultSet resultSet = statement.executeQuery(query);
            System.out.println("생성된 뷰 내용:");

            // 결과 출력
            while (resultSet.next()) {
                String movieId = resultSet.getString("movie_id");
                String title = resultSet.getString("title");
                String genre = resultSet.getString("genre");
                String keyword = resultSet.getString("keyword");
                String country = resultSet.getString("country");
                String director = resultSet.getString("director");
                String running_time = resultSet.getString("running_time");
                String rating = resultSet.getString("rating");

                System.out.println(movieId + " " + title + " " + genre + " " + keyword + " " + country + " " + director
                        + " " + running_time + " " + rating);
            }

            // 자원 해제
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("클래스 못 찾음");
        }
    }

    // 등급 뷰
    public static void showFilteredRatingView(String selectedRating) {
        String url = "jdbc:mysql://localhost:3306/movie";
        String username = "root";
        String password = "9806";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, username, password);

            // 뷰 조회 쿼리 설정
            String query = "SELECT * FROM filtered_view_rating_";
            Statement statement = connection.createStatement();

            // 뷰 조회
            ResultSet resultSet = statement.executeQuery(query);
            System.out.println("생성된 뷰 내용:");

            // 결과 출력
            while (resultSet.next()) {
                String movieId = resultSet.getString("movie_id");
                String title = resultSet.getString("title");
                String genre = resultSet.getString("genre");
                String keyword = resultSet.getString("keyword");
                String country = resultSet.getString("country");
                String director = resultSet.getString("director");
                String running_time = resultSet.getString("running_time");
                String rating = resultSet.getString("rating");

                System.out.println(movieId + " " + title + " " + genre + " " + keyword + " " + country + " " + director
                        + " " + running_time + " " + rating);
            }

            // 자원 해제
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("클래스 못 찾음");
        }
    }

    // 키워드 뷰
    public static void showFilteredKeywordView(String selectedKeyword) {
        String url = "jdbc:mysql://localhost:3306/movie";
        String username = "root";
        String password = "9806";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, username, password);

            // 뷰 조회 쿼리 설정
            String query = "SELECT * FROM filtered_view_keyword_";
            Statement statement = connection.createStatement();

            // 뷰 조회
            ResultSet resultSet = statement.executeQuery(query);
            System.out.println("생성된 뷰 내용:");

            // 결과 출력
            while (resultSet.next()) {
                String movieId = resultSet.getString("movie_id");
                String title = resultSet.getString("title");
                String genre = resultSet.getString("genre");
                String keyword = resultSet.getString("keyword");
                String country = resultSet.getString("country");
                String director = resultSet.getString("director");
                String running_time = resultSet.getString("running_time");
                String rating = resultSet.getString("rating");

                System.out.println(movieId + " " + title + " " + genre + " " + keyword + " " + country + " " + director
                        + " " + running_time + " " + rating);
            }

            // 자원 해제
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("클래스 못 찾음");
        }
    }

    // 국가 뷰
    public static void showFilteredCountryView(String selectedCountry) {
        String url = "jdbc:mysql://localhost:3306/movie";
        String username = "root";
        String password = "9806";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, username, password);

            // 뷰 조회 쿼리 설정
            String query = "SELECT * FROM filtered_view_country_";
            Statement statement = connection.createStatement();

            // 뷰 조회
            ResultSet resultSet = statement.executeQuery(query);
            System.out.println("생성된 뷰 내용:");

            // 결과 출력
            while (resultSet.next()) {
                String movieId = resultSet.getString("movie_id");
                String title = resultSet.getString("title");
                String genre = resultSet.getString("genre");
                String keyword = resultSet.getString("keyword");
                String country = resultSet.getString("country");
                String director = resultSet.getString("director");
                String running_time = resultSet.getString("running_time");
                String rating = resultSet.getString("rating");

                System.out.println(movieId + " " + title + " " + genre + " " + keyword + " " + country + " " + director
                        + " " + running_time + " " + rating);
            }

            // 자원 해제
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("클래스 못 찾음");
        }
    }

    // 러닝타임 뷰
    public static void showFilteredRunningView(String selectedRunningTime) {
        String url = "jdbc:mysql://localhost:3306/movie";
        String username = "root";
        String password = "9806";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, username, password);

            // 뷰 조회 쿼리 설정
            String query = "SELECT * FROM filtered_view_running_time_";
            Statement statement = connection.createStatement();

            // 뷰 조회
            ResultSet resultSet = statement.executeQuery(query);
            System.out.println("생성된 뷰 내용:");

            // 결과 출력
            while (resultSet.next()) {
                String movieId = resultSet.getString("movie_id");
                String title = resultSet.getString("title");
                String genre = resultSet.getString("genre");
                String keyword = resultSet.getString("keyword");
                String country = resultSet.getString("country");
                String director = resultSet.getString("director");
                String running_time = resultSet.getString("running_time");
                String rating = resultSet.getString("rating");

                System.out.println(movieId + " " + title + " " + genre + " " + keyword + " " + country + " " + director
                        + " " + running_time + " " + rating);
            }

            // 자원 해제
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("클래스 못 찾음");
        }
    }
}