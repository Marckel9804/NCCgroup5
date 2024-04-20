package com.project1.group5.frame.login;

import com.project1.group5.db.OzoDB;
import com.project1.group5.frame.mainPage.MainPage;
import com.project1.group5.frame.register.RegisterFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class LoginFrame extends JFrame {
    private JTextField idField; // 아이디 입력 필드
    private JPasswordField passwordField; // 비밀번호 입력 필드
    private JButton loginButton; // 로그인 버튼
    // 데이터베이스 가져오기
    private static final String DB_URL = OzoDB.DB_URL;
    private static final String DB_USER = OzoDB.DB_USER;
    private static final String DB_PASSWORD = OzoDB.DB_PASSWORD;
    // 데이터베이스 가져오기
    private String loggedInUsername = null;
    MainPage mp;

    // 로그인 프레임 생성자

    public LoginFrame(MainPage mp) {
        this.mp = mp;
        int f_width = 1100;
        int f_height = 600;
        setTitle("Login"); // 프레임 타이틀 설정
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 종료 동작 설정
        setSize(f_width, f_height); // 프레임 크기 설정
        setLocationRelativeTo(null); // 프레임을 화면 중앙에 배치
        initializeDB(); // 데이터베이스 초기화

        // 메인 패널 생성
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        // 이미지 패널 생성
        JPanel imagePanel = new JPanel(null); // 레이아웃 매니저를 null로 설정하여 직접 위치 지정
        imagePanel.setBackground(Color.WHITE);
        ImageIcon icon = new ImageIcon("src/main/java/com/project1/group5/frame/loginImages/login1.png"); // 이미지 아이콘 경로
        JLabel imageLabel = new JLabel(icon);
        imageLabel.setBounds(f_width / 2 - icon.getIconWidth() / 2 - 10, 50, icon.getIconWidth(), icon.getIconHeight()); // 이미지
        imagePanel.add(imageLabel);

        // 텍스트 레이블 추가
        JLabel userLoginLabel = new JLabel("User Login");
        userLoginLabel.setFont(new Font("Arial", Font.BOLD, 20)); // 폰트 설정

        userLoginLabel.setBounds(f_width / 2 - icon.getIconWidth() / 2 - 45, 60 + icon.getIconHeight(), 150, 20); // 텍스트
        // 레이블
        // 위치
        // 설정
        userLoginLabel.setHorizontalAlignment(SwingConstants.CENTER); // 가운데 정렬
        userLoginLabel.setPreferredSize(new Dimension(150, 20)); // 레이블의 크기 설정
        imagePanel.add(userLoginLabel);

        JLabel welcomeLabel = new JLabel("Welcome to the ozo");
        welcomeLabel.setFont(new Font("Arial", Font.PLAIN, 16)); // 폰트 설정
        welcomeLabel.setBounds(f_width / 2 - icon.getIconWidth() / 2 - 50, 80 + icon.getIconHeight(), 150, 20); // 텍스트
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER); // 가운데 정렬
        imagePanel.add(welcomeLabel);

        add(imagePanel); // 이미지 패널을 프레임에 추가

        // 중앙 패널 생성
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(8, 10, 8, 10);

        // 아이디 레이블 및 필드 추가
        JLabel usernameLabel = new JLabel("ID");
        usernameLabel.setForeground(Color.WHITE);
        centerPanel.add(usernameLabel, gbc);

        gbc.gridy++;
        JPanel idPanel = new JPanel(new BorderLayout(10, 0)); // 가로 간격 10픽셀로 설정
        idPanel.setBackground(Color.WHITE); // 배경색 바꿈
        idField = new JTextField("");
        idField = new JTextField("아이디를 입력하세요", 16);
        idField.setPreferredSize(new Dimension(200, 40));
        idField.setForeground(Color.GRAY);

        // 아이콘 크기 조정
        ImageIcon userIcon = new ImageIcon("C:\\Users\\lg\\Desktop\\idimage.png");
        Image userIconImage = userIcon.getImage();
        Image resizedUserIconImage = userIconImage.getScaledInstance(25, 25, Image.SCALE_SMOOTH);
        ImageIcon resizedUserIcon = new ImageIcon(resizedUserIconImage);

        // 아이콘과 입력 필드 결합
        JLabel userIconLabel = new JLabel(resizedUserIcon);
        userIconLabel.setOpaque(true); // 불투명 설정
        userIconLabel.setBackground(Color.WHITE); // 배경색 하얀색으로 설정
        idPanel.add(userIconLabel, BorderLayout.WEST);
        idPanel.add(idField, BorderLayout.CENTER);
        centerPanel.add(idPanel, gbc);

        // 아이디 필드에 포커스 이벤트 추가
        idField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                // 텍스트 필드에 포커스를 얻으면, 현재 텍스트가 "아이디를 입력하세요"인 경우에만 텍스트를 지우고, 색상을 검정색으로 변경
                if (idField.getText().equals("아이디를 입력하세요")) {
                    idField.setText("");
                    idField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                // 포커스를 잃으면, 텍스트 필드가 비어있는 경우 다시 안내 문구를 추가하고, 색상을 회색으로 변경
                if (idField.getText().isEmpty()) {
                    idField.setText("아이디를 입력하세요");
                    idField.setForeground(Color.GRAY);
                }
            }
        });

        // 비밀번호 레이블 및 필드 추가
        gbc.gridy++;
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setForeground(Color.WHITE); // 글씨 색상을 흰색으로 설정
        centerPanel.add(passwordLabel, gbc);

        gbc.gridy++;
        JPanel passwordPanel = new JPanel(new BorderLayout(10, 0)); // 가로 간격 10픽셀로 설정
        passwordPanel.setBackground(Color.WHITE); // JPanel의 배경색 설정
        passwordField = new JPasswordField("비밀번호를 입력하세요", 16);
        passwordField.setPreferredSize(new Dimension(200, 40));
        passwordField.setForeground(Color.GRAY);
        passwordField.setBackground(Color.WHITE); // 입력 필드 배경색 설정
        passwordField.setEchoChar((char) 0); // 입력 전에는 텍스트 보이도록 설정

        // 아이콘 크기 조정
        ImageIcon lockIcon = new ImageIcon("C:\\Users\\lg\\Desktop\\passwordimage.png");
        Image lockIconImage = lockIcon.getImage();
        Image resizedLockIconImage = lockIconImage.getScaledInstance(25, 25, Image.SCALE_SMOOTH);
        ImageIcon resizedLockIcon = new ImageIcon(resizedLockIconImage);

        // 아이콘과 입력 필드 결합
        JLabel lockIconLabel = new JLabel(resizedLockIcon);
        lockIconLabel.setOpaque(true); // 불투명 설정
        lockIconLabel.setBackground(Color.WHITE); // 배경색 하얀색으로 설정
        passwordPanel.add(lockIconLabel, BorderLayout.WEST);
        passwordPanel.add(passwordField, BorderLayout.CENTER);
        centerPanel.add(passwordPanel, gbc);

        // 비밀번호 필드에 포커스 이벤트 추가
        passwordField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                // 비밀번호 필드에 포커스를 얻으면, 현재 텍스트가 "비밀번호를 입력하세요"인 경우에만 텍스트를 지우고, 색상을 검정색으로 변경
                if (String.valueOf(passwordField.getPassword()).equals("비밀번호를 입력하세요")) {
                    passwordField.setText("");
                    passwordField.setForeground(Color.BLACK);
                    passwordField.setEchoChar('*'); // 비밀번호 입력 중에는 *로 표시
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                // 포커스를 잃으면, 비밀번호 필드가 비어있는 경우 다시 안내 문구를 추가하고, 색상을 회색으로 변경
                if (String.valueOf(passwordField.getPassword()).isEmpty()) {
                    passwordField.setText("비밀번호를 입력하세요");
                    passwordField.setForeground(Color.GRAY);
                    passwordField.setEchoChar((char) 0); // 입력 전에는 텍스트 보이도록 설정
                }
            }
        });

        // 로그인 버튼 추가
        gbc.gridy++;
        loginButton = new JButton("로그인");
        loginButton.addActionListener(new LoginButtonListener());
        loginButton.setPreferredSize(new Dimension(200, 40));
        loginButton.setBackground(new Color(208, 154, 255)); // 배경색 설정
        loginButton.setBorder(BorderFactory.createLineBorder(new Color(143, 188, 143))); // 테두리를 생성하여 설정

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
                CallableStatement stmt = conn.prepareCall("{CALL CheckLogin(?, ?, ?, ?, ?, ?)}"); // 저장 프로시저 호출
                stmt.setString(1, id); // 아이디 설정
                stmt.setString(2, password); // 비밀번호 설정
                stmt.registerOutParameter(3, Types.INTEGER); // 결과 코드 파라미터 등록
                stmt.registerOutParameter(4, Types.VARCHAR); // 유저 이름 반환
                stmt.registerOutParameter(5, Types.INTEGER); // 나이 반환
                stmt.registerOutParameter(6, Types.VARCHAR); // 아이디 반환
                stmt.execute(); // 저장 프로시저 실행\

                int resultCode = stmt.getInt(3); // 결과 코드 가져오기
                String userName = stmt.getString(4);
                int userAge = stmt.getInt(5);
                String userId = stmt.getString(6);

                if (resultCode == 0) {
                    loggedInUsername = userName;
                }

                // 결과 코드에 따라 메시지 출력
                switch (resultCode) {
                    case 0:
                        loggedInUsername = userName; // Set loggedInUsername only when login is successful
                        JOptionPane.showMessageDialog(LoginFrame.this, "로그인 성공");
                        SwingUtilities.invokeLater(() -> {
                            mp.setLoginCheck(true);
                            mp.setID(userId);
                            mp.setName(userName);
                            mp.setAge(userAge);
                            mp.loggedInPage();
                            LoginFrame.this.dispose();
                        });
                        break;
                    case 1:
                        JOptionPane.showMessageDialog(LoginFrame.this, "사용자가 존재하지 않습니다");
                        loggedInUsername = null;
                        break;
                    case 2:
                        JOptionPane.showMessageDialog(LoginFrame.this, "잘못된 비밀번호입니다");
                        loggedInUsername = null;
                        break;
                    default:
                        JOptionPane.showMessageDialog(LoginFrame.this, "예상치 못한 결과 코드: " + resultCode);
                        loggedInUsername = null;
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

    }
}
