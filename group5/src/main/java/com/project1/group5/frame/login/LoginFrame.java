package com.project1.group5.frame.login;


import com.project1.group5.frame.register.RegisterFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

public class LoginFrame extends JFrame {
    private JTextField idField; // 아이디 입력 필드
    private JPasswordField passwordField; // 비밀번호 입력 필드
    private JButton loginButton; // 로그인 버튼

    //데이터베이스 가져오기
    private static final String DB_URL = "jdbc:mysql://localhost:3306/project";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "1234";

    // 로그인 프레임 생성자
    public LoginFrame() {
        setTitle("Login"); // 프레임 타이틀 설정
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 종료 동작 설정
        setSize(800, 600); // 프레임 크기 설정
        setLocationRelativeTo(null); // 프레임을 화면 중앙에 배치

        initializeDB(); // 데이터베이스 초기화

        // 메인 패널 생성
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

// 이미지 패널 생성
        JPanel imagePanel = new JPanel(null); // 레이아웃 매니저를 null로 설정하여 직접 위치 지정
        imagePanel.setBackground(Color.WHITE);
        ImageIcon icon = new ImageIcon("src/main/java/com/project1/group5/frame/loginimages/login1.png"); // 이미지 아이콘 경로
        JLabel imageLabel = new JLabel(icon);
        imageLabel.setBounds(400 - icon.getIconWidth() / 2 - 10, 30, icon.getIconWidth(), icon.getIconHeight()); // 이미지 아이콘 위치 설정
        imagePanel.add(imageLabel);

// 텍스트 레이블 추가
        JLabel userLoginLabel = new JLabel("User Login");
        userLoginLabel.setFont(new Font("Arial", Font.BOLD, 20)); // 폰트 설정
        userLoginLabel.setBounds(400 - icon.getIconWidth() / 2 - 45, 35 + icon.getIconHeight(), 150, 20); // 텍스트 레이블 위치 설정
        userLoginLabel.setHorizontalAlignment(SwingConstants.CENTER); // 가운데 정렬
        userLoginLabel.setPreferredSize(new Dimension(150, 20)); // 레이블의 크기 설정
        imagePanel.add(userLoginLabel);


        JLabel welcomeLabel = new JLabel("Welcome to the ozo");
        welcomeLabel.setFont(new Font("Arial", Font.PLAIN, 16)); // 폰트 설정
        welcomeLabel.setBounds(400 - icon.getIconWidth() / 2 - 50, 60 + icon.getIconHeight(), 150, 20); // 텍스트 레이블 위치 설정
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER); // 가운데 정렬
        imagePanel.add(welcomeLabel);

        add(imagePanel); // 이미지 패널을 프레임에 추가


        // 중앙 패널 생성
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);

        // 아이디 레이블 및 필드 추가
        JLabel usernameLabel = new JLabel("ID");
        centerPanel.add(usernameLabel, gbc);

        gbc.gridy++;
        idField = new JTextField(15);
        idField.setPreferredSize(new Dimension(200, 40));
        centerPanel.add(idField, gbc);

        // 비밀번호 레이블 및 필드 추가
        gbc.gridy++;
        JLabel passwordLabel = new JLabel("Password");
        centerPanel.add(passwordLabel, gbc);

        gbc.gridy++;
        passwordField = new JPasswordField(15);
        passwordField.setPreferredSize(new Dimension(200, 40));
        centerPanel.add(passwordField, gbc);

        // 로그인 버튼 추가
        gbc.gridy++;
        loginButton = new JButton("Login");
        loginButton.addActionListener(new LoginButtonListener());
        loginButton.setPreferredSize(new Dimension(200, 40));
        loginButton.setBackground(new Color(208, 154, 255)); // 배경색 설정
        loginButton.setBorder(BorderFactory.createLineBorder(Color.WHITE)); // 테두리를 하얀색으로 설정
        loginButton.setFocusPainted(false); // 포커스 시 테두리 없애기
        loginButton.setFont(loginButton.getFont().deriveFont(Font.BOLD)); // 폰트 굵게 설정

        centerPanel.add(loginButton, gbc);








        // 회원 가입 텍스트 추가
        gbc.gridy++;
        JLabel registerLabel = new JLabel("회원이 아니신가요? 회원가입 하기");
        registerLabel.setForeground(Color.BLACK);
        registerLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        registerLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // 회원가입 페이지로 이동하는 액션
                // 여기서는 현재 프레임을 종료하고 새로운 RegisterFrame을 생성
                dispose(); // 현재 프레임 종료
                new RegisterFrame(); // 회원가입 프레임 열기
            }
        });
        centerPanel.add(registerLabel, gbc);

        // 중앙 패널을 중앙 정렬로 배치하기 위해 빈 패널 추가
        JPanel roundedPanel = new JPanel(new BorderLayout());
        roundedPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        roundedPanel.setBackground(Color.WHITE);
        roundedPanel.add(new JPanel(), BorderLayout.NORTH);
        roundedPanel.add(new JPanel(), BorderLayout.SOUTH);
        roundedPanel.add(new JPanel(), BorderLayout.WEST);
        roundedPanel.add(new JPanel(), BorderLayout.EAST);
        roundedPanel.add(centerPanel, BorderLayout.CENTER);

        // 메인 패널에 이미지 패널과 중앙 패널 추가
        mainPanel.add(imagePanel, BorderLayout.CENTER);
        mainPanel.add(roundedPanel, BorderLayout.SOUTH);

        add(mainPanel); // 메인 패널을 프레임에 추가
        setVisible(true); // 프레임을 보이도록 설정
    }

    // 데이터베이스 초기화 메서드
    private void initializeDB() {
        try {
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            System.out.println("데이터베이스 연결 성공");
        } catch (SQLException e) {
            System.out.println("데이터베이스 연결 실패");
            e.printStackTrace();
        }
    }

    // 로그인 버튼 리스너 클래스
    private class LoginButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String id = idField.getText().trim(); // 입력된 아이디 가져오기
            String password = new String(passwordField.getPassword()); // 입력된 비밀번호 가져오기

            try {
                Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD); // 데이터베이스 연결
                CallableStatement stmt = conn.prepareCall("{CALL CheckLogin(?, ?, ?)}"); // 저장 프로시저 호출
                stmt.setString(1, id); // 아이디 설정
                stmt.setString(2, password); // 비밀번호 설정
                stmt.registerOutParameter(3, Types.INTEGER); // 결과 코드 파라미터 등록
                stmt.execute(); // 저장 프로시저 실행

                int resultCode = stmt.getInt(3); // 결과 코드 가져오기

                // 결과 코드에 따라 메시지 출력
                switch (resultCode) {
                    case 0:
                        JOptionPane.showMessageDialog(LoginFrame.this, "로그인 성공");
                        break;
                    case 1:
                        JOptionPane.showMessageDialog(LoginFrame.this, "사용자가 존재하지 않습니다");
                        break;
                    case 2:
                        JOptionPane.showMessageDialog(LoginFrame.this, "잘못된 비밀번호입니다");
                        break;
                    default:
                        JOptionPane.showMessageDialog(LoginFrame.this, "예상치 못한 결과 코드: " + resultCode);
                }

                stmt.close(); // statement 닫기
                conn.close(); // connection 닫기
            } catch (SQLException ex) {
                // SQLException 발생 시 적절한 처리
                JOptionPane.showMessageDialog(LoginFrame.this, "데이터베이스 연결 오류: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }

    // 메인 메서드
    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginFrame::new); // 로그인 프레임 생성
    }
}
