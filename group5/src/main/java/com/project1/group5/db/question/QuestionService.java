package com.project1.group5.db.question;

import java.sql.*;

import com.project1.group5.db.OzoDB;

public class QuestionService {
    Connection con = null;
    CallableStatement cs = null;

    // 유저가 회원가입 기능으로 입력한 값의 중복 여부를 확인해서 insert하는 메서드
    public String selectQuestion(String keyword) {
        String res = null;

        // JDBC 연결...
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(OzoDB.DB_URL, OzoDB.DB_USER, OzoDB.DB_PASSWORD);
            System.out.println("데이터베이스 연결 성공");
            // String sql = "select *from question where thema_name='" +keyword+"';";
            String sql = "select *from question where thema_name=?;";
            PreparedStatement psmt = con.prepareStatement(sql);
            psmt.setString(1, keyword); // userid가 com인 데이터를 싹 다 가져옴

            // ResultSet: 반환 값이 여러 개의 행인 경우에 쉽게 처리할 수 있게 설계된 클래스
            ResultSet rs = psmt.executeQuery(); // 정보를 가져옴
            if (rs.next()) {
                QuestionDTO q = new QuestionDTO();

                // 테이블에 저장되어 있는 uesrid값을 가져와서
                // User 클래스에 userid라는 필드에 세팅한다 => com
                q.setQue_str(rs.getString(3));
                res = q.getQue_str();

            }
            cs.close();
            con.close();

        } catch (Exception e) {
            System.out.println("");
        }
        return res;
    }

}
