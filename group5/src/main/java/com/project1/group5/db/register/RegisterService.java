package com.project1.group5.db.register;

import java.sql.*;

import com.project1.group5.db.OzoDB;

public class RegisterService {

    Connection con = null;
    CallableStatement cs = null;

    // 유저가 회원가입 기능으로 입력한 값의 중복 여부를 확인해서 insert하는 메서드
    public int registerUser(RegisterDTO dto) {
        int res = -1;
        // JDBC 연결...
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(OzoDB.DB_URL, OzoDB.DB_USER, OzoDB.DB_PASSWORD);

            System.out.println("데이터베이스 연결 성공");

            // 신규유저의 정보가 users 테이블에 있는지 체크한 후 중복 없으면 등록하는 프로시저 호출
            String queryCheck = "{call registerUser(?, ?, ?, ?, ?, ?, ?, ?, ?,?)}";
            cs = con.prepareCall(queryCheck);
            cs.registerOutParameter(1, Types.INTEGER);
            cs.registerOutParameter(2, Types.VARCHAR);
            cs.setString(3, dto.getUser_id());
            cs.setString(4, dto.getPassword());
            cs.setString(5, dto.getEmail());
            cs.setString(6, dto.getUsername());
            cs.setString(7, dto.getPhone_number());
            cs.setString(8, dto.getBirth_date());
            cs.setInt(9, dto.getAge());
            cs.setString(10, dto.getGender());
             System.out.println(cs.toString());
            cs.execute();
             System.out.println("성공했냐?");

            res = cs.getInt(1);
            System.out.println("res: "+res);
            String message = cs.getString(2);
            // System.out.println(message);

            cs.close();
            con.close();

        } catch (Exception e) {
            // System.out.println("몬가 잘못됨..");
        }
        return res;
    }
}