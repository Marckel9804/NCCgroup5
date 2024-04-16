package com.project1.group5.frame.register;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class RegisterFrame extends JFrame {
    private JTextField idField; // 아이디 입력 필드
    private JTextField usernameField; // 유저 이름 입력 필드
    private JPasswordField passwordField; // 비밀번호 입력 필드
    private JTextField emailField; // 이메일 입력 필드
    private JTextField phoneNumberField; // 전화번호 입력 필드
    private JTextField birthDateField; // 생년월일 입력 필드
    private JComboBox<String> genderComboBox; // 성별 선택 콤보 박스
    private JButton registerButton; // 회원가입 버튼

    // 데이터베이스 가져오기
    private static final String DB_URL = "jdbc:mysql://localhost:3306/project";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "1234";

    // 회원가입 프레임 생성자
    public RegisterFrame() {
        setTitle("Register"); // 프레임 타이틀 설정
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // 종료 동작 설정
        setSize(800, 600); // 프레임 크기 설정
        setLocationRelativeTo(null); // 프레임을 화면 중앙에 배치

        initializeDB(); // 데이터베이스 초기화

        // 메인 패널 생성
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        // 회원가입 텍스트 라벨 생성
        JLabel registerLabel = new JLabel("회원가입", SwingConstants.CENTER); // 텍스트 가운데 정렬
        registerLabel.setFont(new Font("맑은 고딕", Font.BOLD, 20)); // Arial 폰트 사용
        registerLabel.setForeground(Color.BLACK); // 텍스트 색상 설정
        // 회원가입 텍스트 라벨 위치 설정


        registerLabel.setBounds(300, 50, 200, 50); // x좌표, y좌표, 너비, 높이
        // 메인 패널에 회원가입 텍스트 라벨 추가
        mainPanel.add(registerLabel, BorderLayout.NORTH); // 메인 패널의 상단에 추가

        // 중앙 패널 생성
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 10, 5, 10);
        centerPanel.setBounds(100, 300, 1000, 400); //

        // 패널 생성
        JPanel idPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        idPanel.setBackground(Color.WHITE); // 패널의 배경색을 하얀색으로 설정




        // 아이디 입력 필드
        JTextField idField = new JTextField(15);
        idField.setPreferredSize(new Dimension(200, 35));
        idField.setForeground(Color.GRAY); // 회색으로 설정
        idField.setText("아이디를 입력하세요");
        idField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (idField.getText().equals("아이디를 입력하세요")) {
                    idField.setText("");
                    idField.setForeground(Color.BLACK); // 포커스를 줬을 때 텍스트 색상을 검정으로 변경
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (idField.getText().isEmpty()) {
                    idField.setText("아이디를 입력하세요");
                    idField.setForeground(Color.GRAY); // 입력한 값이 없을 때 다시 회색으로 변경
                }
            }
        });
        idPanel.add(idField); // 아이디 입력 필드를 idPanel에 추가

        // 아이디 중복확인 버튼 추가
        JButton checkDuplicateButton = new JButton("중복 확인");
        checkDuplicateButton.setPreferredSize(new Dimension(80, 35));
        checkDuplicateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // 중복 확인 로직 추가
            }
        });
        idPanel.add(checkDuplicateButton); // 버튼을 idPanel에 추가

        centerPanel.add(idPanel, gbc); // idPanel을 centerPanel에 추가
        gbc.gridy++; // 다음 행으로 이동시 가운데 정렬로 변경

        // 비밀번호 입력 필드
        JPasswordField passwordField = new JPasswordField(15);
        passwordField.setPreferredSize(new Dimension(200, 35));
        passwordField.setEchoChar((char)0); // 입력 전에는 텍스트 보이도록 설정
        passwordField.setText("비밀번호를 입력하세요");
        passwordField.setForeground(Color.GRAY);
        passwordField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (String.valueOf(passwordField.getPassword()).equals("비밀번호를 입력하세요")) {
                    passwordField.setText("");
                    passwordField.setEchoChar('*'); // 입력 시 '*' 문자 보이도록 설정
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (String.valueOf(passwordField.getPassword()).equals("")) {
                    passwordField.setText("비밀번호를 입력하세요");
                    passwordField.setEchoChar((char)0); // 입력 전에는 텍스트 보이도록 설정
                }
            }
        });
        centerPanel.add(passwordField, gbc); // 비밀번호 입력 필드를 centerPanel에 추가
        gbc.gridy++; // 다음 행으로 이동

        // 비밀번호 확인 입력 필드
        JPasswordField confirmPasswordField = new JPasswordField(15);
        confirmPasswordField.setPreferredSize(new Dimension(200, 35));
        confirmPasswordField.setEchoChar((char)0); // 입력 전에는 텍스트 보이도록 설정
        confirmPasswordField.setText("비밀번호를 다시 입력하세요");
        confirmPasswordField.setForeground(Color.GRAY);
        confirmPasswordField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (String.valueOf(confirmPasswordField.getPassword()).equals("비밀번호를 다시 입력하세요")) {
                    confirmPasswordField.setText("");
                    confirmPasswordField.setEchoChar('*'); // 입력 시 '*' 문자 보이도록 설정
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (String.valueOf(confirmPasswordField.getPassword()).equals("")) {
                    confirmPasswordField.setText("비밀번호를 다시 입력하세요");
                    confirmPasswordField.setEchoChar((char)0); // 입력 전에는 텍스트 보이도록 설정
                }
            }
        });
        centerPanel.add(confirmPasswordField, gbc); // 비밀번호 확인 입력 필드를 centerPanel에 추가
        gbc.gridy++; // 다음 행으로 이동

        // 이메일 입력 필드
        JTextField emailField = new JTextField(15);
        emailField.setPreferredSize(new Dimension(200, 35));
        emailField.setForeground(Color.GRAY); // 회색으로 설정
        emailField.setText("이메일을 입력하세요");
        emailField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (emailField.getText().equals("이메일을 입력하세요")) {
                    emailField.setText("");
                    emailField.setForeground(Color.BLACK); // 포커스를 줬을 때 텍스트 색상을 검정으로 변경
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (emailField.getText().isEmpty()) {
                    emailField.setText("이메일을 입력하세요");
                    emailField.setForeground(Color.GRAY); // 입력한 값이 없을 때 다시 회색으로 변경
                }
            }
        });
        centerPanel.add(emailField, gbc);
        gbc.gridy++;

        // 유저 이름 입력 필드
        JTextField usernameField = new JTextField(15);
        usernameField.setPreferredSize(new Dimension(200, 35));
        usernameField.setForeground(Color.GRAY); // 회색으로 설정
        usernameField.setText("이름을 입력하세요");
        usernameField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (usernameField.getText().equals("이름을 입력하세요")) {
                    usernameField.setText("");
                    usernameField.setForeground(Color.BLACK); // 포커스를 줬을 때 텍스트 색상을 검정으로 변경
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (usernameField.getText().isEmpty()) {
                    usernameField.setText("이름을 입력하세요");
                    usernameField.setForeground(Color.GRAY); // 입력한 값이 없을 때 다시 회색으로 변경
                }
            }
        });
        centerPanel.add(usernameField, gbc);
        gbc.gridy++;

        // 전화번호 입력 필드
        JTextField phoneNumberField = new JTextField(15);
        phoneNumberField.setPreferredSize(new Dimension(200, 35));
        phoneNumberField.setForeground(Color.GRAY); // 회색으로 설정
        phoneNumberField.setText("전화번호를 입력하세요");
        phoneNumberField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (phoneNumberField.getText().equals("전화번호를 입력하세요")) {
                    phoneNumberField.setText("");
                    phoneNumberField.setForeground(Color.BLACK); // 포커스를 줬을 때 텍스트 색상을 검정으로 변경
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (phoneNumberField.getText().isEmpty()) {
                    phoneNumberField.setText("전화번호를 입력하세요");
                    phoneNumberField.setForeground(Color.GRAY); // 입력한 값이 없을 때 다시 회색으로 변경
                }
            }
        });
        centerPanel.add(phoneNumberField, gbc);
        gbc.gridy++;

        // 생년월일 입력 필드
        JPanel birthDatePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        birthDatePanel.setBackground(Color.WHITE); // 배경색 변경

        // 년도 입력 필드
        JTextField yearField = new JTextField(4); // 4글자로 설정
        yearField.setPreferredSize(new Dimension(70, 30)); // 입력 폼 크기 조정
        yearField.setHorizontalAlignment(JTextField.CENTER); // 텍스트 가운데 정렬
        yearField.setDocument(new JTextFieldLimit(4)); // 최대 입력 글자 수 설정
        birthDatePanel.add(yearField);
        birthDatePanel.add(new JLabel("년"));

        // 월 입력 필드
        JTextField monthField = new JTextField(2); // 2글자로 설정
        monthField.setPreferredSize(new Dimension(50, 30)); // 입력 폼 크기 조정
        monthField.setHorizontalAlignment(JTextField.CENTER); // 텍스트 가운데 정렬
        monthField.setDocument(new JTextFieldLimit(2)); // 최대 입력 글자 수 설정
        birthDatePanel.add(monthField);
        birthDatePanel.add(new JLabel("월"));

        // 일 입력 필드
        JTextField dayField = new JTextField(2); // 2글자로 설정
        dayField.setPreferredSize(new Dimension(50, 30)); // 입력 폼 크기 조정
        dayField.setHorizontalAlignment(JTextField.CENTER); // 텍스트 가운데 정렬
        dayField.setDocument(new JTextFieldLimit(2)); // 최대 입력 글자 수 설정
        birthDatePanel.add(dayField);
        birthDatePanel.add(new JLabel("일"));

        centerPanel.add(birthDatePanel, gbc);
        gbc.gridy++; // 다음 행으로 이동

        // 성별 라디오 버튼 추가
        JPanel genderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel genderLabel = new JLabel("Gender");
        genderPanel.setBackground(Color.WHITE); // 배경색 변경
        genderPanel.add(genderLabel);

        String[] genders = {"Male", "Female"};

        ButtonGroup genderGroup = new ButtonGroup();

        for (String gender : genders) {
            JRadioButton radioButton = new JRadioButton(gender);
            radioButton.setBackground(Color.WHITE); // 배경색 변경
            genderGroup.add(radioButton);
            genderPanel.add(radioButton);
        }

        centerPanel.add(genderPanel, gbc);
        mainPanel.add(registerLabel, BorderLayout.NORTH); // 기존 코드, 텍스트 라벨을 상단에 추가





        // 회원가입 버튼 추가
        gbc.gridy++;
        registerButton = new JButton("Register");
        registerButton.addActionListener(new RegisterButtonListener());
        registerButton.setPreferredSize(new Dimension(200, 35));
        registerButton.setBackground(new Color(208, 154, 255)); // 배경색 설정
        registerButton.setBorder(BorderFactory.createLineBorder(Color.WHITE)); // 테두리를 하얀색으로 설정
        registerButton.setFocusPainted(false); // 포커스 시 테두리 없애기
        registerButton.setFont(registerButton.getFont().deriveFont(Font.BOLD)); // 폰트 굵게 설정
        centerPanel.add(registerButton, gbc);

        // 중앙 패널을 중앙 정렬로 배치하기 위해 빈 패널 추가
        JPanel roundedPanel = new JPanel(new BorderLayout());
        roundedPanel.setBorder(BorderFactory.createEmptyBorder(55, 80, 55, 80)); // 여기가 추가된 부분입니다.
        roundedPanel.setBackground(Color.WHITE);
        roundedPanel.add(new JPanel(), BorderLayout.NORTH);
        roundedPanel.add(new JPanel(), BorderLayout.SOUTH);
        roundedPanel.add(new JPanel(), BorderLayout.WEST);
        roundedPanel.add(new JPanel(), BorderLayout.EAST);
        roundedPanel.add(centerPanel, BorderLayout.CENTER);

        // 메인 패널에 이미지 패널과 중앙 패널 추가
        mainPanel.add(roundedPanel, BorderLayout.SOUTH); // 이거 수정
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

    // 회원가입 버튼 리스너 클래스
    private class RegisterButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // 회원가입 기능 구현
            // 여기에 회원가입 기능을 구현하세요
        }
    }

    // 메인 메서드
    public static void main(String[] args) {
        SwingUtilities.invokeLater(RegisterFrame::new); // 회원가입 프레임 생성
    }
}