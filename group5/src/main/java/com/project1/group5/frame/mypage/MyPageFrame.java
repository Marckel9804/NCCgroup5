package com.project1.group5.frame.mypage;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import com.project1.group5.db.OzoDB;
import com.project1.group5.frame.mainpage.MainPage;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class MyPageFrame extends JFrame {
    private String user_id;
    private String username;
    private String email;
    private String phone_number;
    private String birth_date;
    private String gender;
    private String password;

    private JTextField usernameField;

    private Connection con;
    private PreparedStatement pstm;
    private ResultSet rs;
    private CallableStatement cs;
    private static final String DB_URL = OzoDB.DB_URL;
    private static final String DB_USER = OzoDB.DB_USER;
    private static final String DB_PASSWORD = OzoDB.DB_PASSWORD;

    MainPage mp;

    public MyPageFrame(MainPage mp) {
        this.mp = mp;
        user_id = mp.getId();
        System.out.println("마이페이지 생성자입니다" + user_id);
        init();
    }

    private void init() {
        // 아이디를 넘겨주면 그 아이디에 해당하는 유저 정보를 가져오는 메소드 호출
        // 근데 로그인창에서 넣은 아이디를 어케 가져와야할지 몰라서 일단 임의 아이디를 넣었음 ㅠ..
        getUserInfo(user_id);

        // 디스플레이 설정
        setTitle("마이페이지");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // 메인 패널 설정
        // (하위패널들을 세로로 정렬하기위한 패널)
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        // 하위 패널 1. 유저 이름
        JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); // 하위 패널 안의 컴포넌트들은 좌우로 정렬된다.
        namePanel.setBorder(new EmptyBorder(30, 50, 0, 0)); // 간격 설정
        mainPanel.add(namePanel);

        JLabel infoName = new JLabel("이름");
        JTextField nameField = new JTextField();
        nameField.setEditable(false); // 유저 이름 변경 불가하도록 설정함.
        nameField.setBackground(Color.lightGray);
        nameField.setMaximumSize(new Dimension(500, 20)); // text filed 의 영역 설정
        nameField.setText(username); // DB에서 가져온 유저 이름을 넣는다.

        namePanel.add(infoName);
        namePanel.add(nameField);

        // 하위 패널 2. 이메일
        JPanel emailPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        emailPanel.setBorder(new EmptyBorder(0, 50, 0, 0));
        mainPanel.add(emailPanel);

        JLabel infoEmail = new JLabel("이메일");
        JTextField emailField = new JTextField();
        emailField.setEditable(false); // 이메일 변경 불가하도록 설정함.
        emailField.setBackground(Color.lightGray);
        emailField.setMaximumSize(new Dimension(500, 20));
        emailField.setText(email);

        emailPanel.add(infoEmail);
        emailPanel.add(emailField);

        // 하위 패널 3. 전화번호
        JPanel telPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        telPanel.setBorder(new EmptyBorder(0, 50, 0, 0));
        mainPanel.add(telPanel);

        JLabel infoTel = new JLabel("전화번호");
        JTextField telField = new JTextField();
        telField.setEditable(false); // 전화번호 변경 불가하도록 설정함.
        telField.setBackground(Color.lightGray);
        telField.setMaximumSize(new Dimension(500, 20));
        telField.setText(phone_number);

        telPanel.add(infoTel);
        telPanel.add(telField);

        // 하위 패널 4. 생일
        JPanel birthdayPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        birthdayPanel.setBorder(new EmptyBorder(0, 50, 0, 0));
        mainPanel.add(birthdayPanel);

        JLabel infoBirthDate = new JLabel("생일");
        JTextField birthdayField = new JTextField();
        birthdayField.setEditable(false); // 생일 변경 불가하도록 설정함.
        birthdayField.setBackground(Color.lightGray);
        birthdayField.setMaximumSize(new Dimension(500, 20));
        birthdayField.setText(birth_date);

        birthdayPanel.add(infoBirthDate);
        birthdayPanel.add(birthdayField);

        // 하위 패널 5. 성별
        JPanel genderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        genderPanel.setBorder(new EmptyBorder(0, 50, 0, 0));
        mainPanel.add(genderPanel);

        JLabel infoGender = new JLabel("성별");
        JTextField genderField = new JTextField();
        genderField.setEditable(false); // 성별 변경 불가하도록 설정함.
        genderField.setBackground(Color.lightGray);
        genderField.setMaximumSize(new Dimension(500, 20));
        genderField.setText(gender);

        genderPanel.add(infoGender);
        genderPanel.add(genderField);

        // 하위 패널 6-1. 비밀번호
        JPanel passwordPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        passwordPanel.setBorder(new EmptyBorder(0, 50, 30, 0));
        mainPanel.add(passwordPanel);

        JLabel infoPassword = new JLabel("비밀번호");
        JPasswordField passwordField = new JPasswordField();
        passwordField.setMaximumSize(new Dimension(500, 20));
        passwordField.setText(password);

        passwordPanel.add(infoPassword);
        passwordPanel.add(passwordField);

        // 하위 패널 6-2. 비밀번호 변경 버튼
        JButton editButton = new JButton("비밀번호 변경");
        // [비밀번호 변경] 버튼을 누르면 updatePassword 프로시저를 호출하여 변경사항을 반영함.
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                char[] password = passwordField.getPassword();
                String new_password = new String(password); // JPasswordField에서 새로운 정보 가져오기
                System.out.println(new_password);
                // 비밀번호 변경 메소드 실행
                updatePassword(new_password);
            }
        });
        passwordPanel.add(editButton);

        // 메인패널을 프레임에 추가
        add(mainPanel);
        setVisible(true);
    }

    // DB 연결 메소드
    public void connectDB() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            // System.out.println("데이터베이스 연결 성공");
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // DB에서 유저 정보 가져오는 메소드
    public void getUserInfo(String user_id) {
        try {
            connectDB();
            String query = "select * from users where id = ?";
            pstm = con.prepareStatement(query);
            pstm.setString(1, user_id);
            rs = pstm.executeQuery();

            while (rs.next()) {
                this.password = rs.getString(3);
                this.email = rs.getString(4);
                this.username = rs.getString(5);
                this.phone_number = rs.getString(6);
                this.birth_date = rs.getString(7);
                this.gender = rs.getString(8);
            }

            rs.close();
            pstm.close();
            con.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // 비밀번호 변경 메소드
    public void updatePassword(String new_password) {
        try {
            connectDB();
            // 비밀번호 변경 프로시저 호출
            cs = con.prepareCall("{call UpdatePassword(?, ?, ?)}");
            cs.setString(1, user_id); // 로그인한 아이디를 넘겨줘야하는데 방법을 몰라서 임의 아이디로 설정함.
            cs.setString(2, new_password);
            cs.registerOutParameter(3, Types.INTEGER);
            cs.execute();

            int resultCode = cs.getInt(3); // 실행결과 가져오기
            if (resultCode == 1) {
                JOptionPane.showMessageDialog(null, "사용자 비밀번호가 성공적으로 변경되었습니다.",
                        "비밀번호 변경 성공", JOptionPane.INFORMATION_MESSAGE);
            }
            cs.close();
            con.close();

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    // public static void main(String[] args) {
    // new MyPageFrame(null);
    // }
}