package com.project1.group5.db;

import java.sql.Connection;
import java.sql.DriverManager;

public class GetConnection {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/sm";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "1234";

    public static Connection get(String Schema) {
        Connection con = null;
        try {
            // Class 클래스로 mysql 드라이버를 로딩, -> DriverManager에 등록
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Connection 객체를 얻음.
            con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            System.out.println("연결 성공");
        } catch (Exception e) {
            System.out.println("연결 실패");
        }
        return con;
    }
}
