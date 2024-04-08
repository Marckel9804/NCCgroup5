package com.project1.group5.db;

import java.sql.*;

public class RegisterService {

    Connection con = null;
    CallableStatement cs = null;

    //유저가 회원가입 기능으로 입력한 값의 중복 여부를 확인해서 insert하는 메서드
    public void registerUser(RegisterDTO dto){

        //JDBC 연결...
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/OzO", //우리 DB이름 뭐임..?
                    "root",
                    "0000"); //비번도 우리 DB비번으로 바꾸자..
            System.out.println("데이터베이스 연결 성공");

            //신규유저의 정보가 users 테이블에 있는지 체크한 후 중복 없으면 등록하는 프로시저 호출
            String queryCheck= "{call registerUser(?, ?, ?, ?, ?, ?, ?, ?, curtime(), curtime())}";
            cs = con.prepareCall(queryCheck);
            cs.registerOutParameter(1, Types.INTEGER);
            cs.setInt(2, dto.getUser_id());
            cs.setString(3, dto.getUsername());
            cs.setString(4, dto.getEmail());
            cs.setString(5, dto.getPhone_number());
            cs.setString(6, dto.getBirth_date());
            cs.setString(7, dto.getGender());
            cs.setString(8, dto.getPassword());
            cs.execute();

            int result = cs.getInt(1);
            System.out.println("insert 실행 여부(1=true, 0=false) : " + result);

            cs.close();
            con.close();

        } catch(Exception e) {
            System.out.println("이미 존재하는 유저입니다.");
        }
    }
}
