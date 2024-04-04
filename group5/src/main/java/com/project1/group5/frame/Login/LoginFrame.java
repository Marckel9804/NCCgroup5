package com.project1.group5.frame.Login;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private Connection conn;

    private static final String DB_URL = "jdbc:mysql://localhost:3306/project";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "1234";

    public LoginFrame() {
        setTitle("Login"); // 로그인 창 제목 설정
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 창 닫기 동작 설정
        setSize(800, 600); // 창 크기 설정
        setLocationRelativeTo(null); // 화면 중앙에 창 배치

        initializeDB(); // 데이터베이스 초기화

        // 메인 패널 설정
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(230, 230, 230)); // 연한 회색 배경 설정

        // 둥근 모서리 패널 설정
        JPanel roundedPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                int width = getWidth() - 200;
                int height = getHeight() - 200;
                int radius = 30;
                int x = (getWidth() - width) / 2;
                int y = (getHeight() - height) / 2;
                g.setColor(Color.WHITE);
                g.fillRoundRect(x, y, width, height, radius, radius);
            }
        };
        roundedPanel.setOpaque(false); // 투명한 배경 설정

        // 센터 패널 설정
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        centerPanel.setOpaque(false);

        // 이미지 패널 설정
        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setOpaque(false);
        String imgDir = "src/main/java/com/project1/group5/frame/LoginImages/";
        ImageIcon icon = new ImageIcon(imgDir+"Login.png"); // 이미지 아이콘 경로 설정
        JLabel imageLabel = new JLabel(icon);
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imageLabel.setVerticalAlignment(JLabel.CENTER); // 이미지 중앙 정렬
        imagePanel.add(imageLabel, BorderLayout.CENTER);

        // 입력 폼 패널 설정
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel usernameLabel = new JLabel("ID:"); // 아이디 라벨
        formPanel.add(usernameLabel, gbc);

        gbc.gridy++;
        usernameField = new JTextField(15); // 아이디 입력 필드
        formPanel.add(usernameField, gbc);

        gbc.gridy++;
        JLabel passwordLabel = new JLabel("Password:"); // 비밀번호 라벨
        formPanel.add(passwordLabel, gbc);

        gbc.gridy++;
        passwordField = new JPasswordField(15); // 비밀번호 입력 필드
        formPanel.add(passwordField, gbc);

        gbc.gridy++;
        loginButton = new JButton("Login"); // 로그인 버튼
        loginButton.addActionListener(new LoginButtonListener());
        formPanel.add(loginButton, gbc);

        centerPanel.add(imagePanel);
        centerPanel.add(formPanel);

        roundedPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(roundedPanel, BorderLayout.CENTER);

        add(mainPanel); // 메인 패널을 프레임에 추가
        setVisible(true); // 화면에 보이도록 설정
    }

    // 데이터베이스 초기화 메소드
    private void initializeDB() {
        try {
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD); // 데이터베이스 연결
            System.out.println("Database connected."); // 연결 확인 메시지 출력
        } catch (SQLException e) {
            System.out.println("Failed to connect database!"); // 연결 실패 메시지 출력
            e.printStackTrace();
        }
    }

    // 로그인 버튼 리스너 클래스
    private class LoginButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = usernameField.getText().trim(); // 입력한 아이디 가져오기
            String password = new String(passwordField.getPassword()); // 입력한 비밀번호 가져오기

            try {
                // 저장 프로시저 호출 준비
                CallableStatement stmt = conn.prepareCall("{CALL CheckLogin(?, ?, ?)}");
                stmt.setString(1, username);
                stmt.setString(2, password);
                stmt.registerOutParameter(3, Types.INTEGER); // 결과 코드 등록
                stmt.execute(); // 프로시저 실행

                int resultCode = stmt.getInt(3); // 결과 코드 가져오기

                // 결과 코드에 따른 메시지 출력
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
                    case 3:
                        JOptionPane.showMessageDialog(LoginFrame.this, "로그인 시도 횟수가 너무 많습니다. 나중에 다시 시도하세요.");
                        break;
                    default:
                        JOptionPane.showMessageDialog(LoginFrame.this, "예상치 못한 결과 코드: " + resultCode);
                }

                stmt.close(); // 프로시저 종료
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    // 메인 메소드
    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginFrame::new); // 프로그램 시작
    }
}