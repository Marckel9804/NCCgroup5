package com.project1.group5.frame.register;

import com.project1.group5.db.register.RegisterDTO;
import com.project1.group5.db.register.RegisterService;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;


import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class RegisterFrame extends JFrame {
    private JTextField idField; // 아이디 입력 필드
    private JTextField usernameField; // 유저 이름 입력 필드
    private JPasswordField passwordField; // 비밀번호 입력 필드
    private JTextField emailField; // 이메일 입력 필드
    private JTextField phoneNumberField; // 전화번호 입력 필드
    private JTextField yearField;
    private JTextField monthField;
    private JTextField dayField;
    private ButtonGroup genderGroup;
    JRadioButton male;
    JRadioButton female;

    private JButton registerButton; // 회원가입 버튼

    // 데이터베이스 가져오기
    private static final String DB_URL = "jdbc:mysql://localhost:3306/project";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "1234";

    // 회원가입 프레임 생성자
    public RegisterFrame() {
        setTitle("Register"); // 프레임 타이틀 설정
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // 종료 동작 설정
        setSize(1000, 600); // 프레임 크기 설정
        setLocationRelativeTo(null); // 프레임을 화면 중앙에 배치

        initializeDB(); // 데이터베이스 초기화

        // 메인 패널 생성
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        // 회원가입 텍스트 라벨 생성

        JLabel registerLabel = new JLabel("회원가입", SwingConstants.CENTER); // 텍스트 가운데 정렬
        registerLabel.setFont(new Font("맑은 고딕", Font.BOLD, 20)); // Arial 폰트 사용
        registerLabel.setForeground(Color.BLACK); // 텍스트 색상 설정
        // 메인 패널에 회원가입 텍스트 라벨 추가

        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0)); //상단에 여백 준거
        mainPanel.add(registerLabel, BorderLayout.PAGE_START);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(3, 10, 3, 10);

        // 패널 생성
        JPanel idPanel = new JPanel(new GridBagLayout()); // GridBagLayout으로 변경
        idPanel.setBackground(Color.WHITE);
        // 아이디 입력 필드
        idField = new JTextField(12);
        idField.setPreferredSize(new Dimension(200, 35));
        idField.setForeground(Color.GRAY);
        idField.setText("아이디를 입력하세요");

        idPanel.add(idField, gbc);
        centerPanel.add(idPanel, gbc);
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridy++;
        // 아이디 입력 시 길이 확인하는 라벨
        JLabel idLengthLabel = new JLabel("아이디는 최소 6자 이상이어야 합니다.");
        idLengthLabel.setForeground(Color.RED); // 빨간색으로 설정
        idLengthLabel.setFont(idLengthLabel.getFont().deriveFont(Font.PLAIN, 10));
        centerPanel.add(idPanel, gbc);
        centerPanel.add(idLengthLabel);

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
        idField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                checkIdLength();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                checkIdLength();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                checkIdLength();
            }

            private void checkIdLength() {
                String id = idField.getText();
                if (id.length() < 6) {
                    idLengthLabel.setText("아이디는 최소 6자 이상이어야 합니다.");
                } else {
                    idLengthLabel.setText("");
                }
            }
        });
        idPanel.add(idField); // 아이디 입력 필드를 idPanel에 추가
// 아이디 입력 시 실시간 피드백 문구 처리
        idLengthLabel.setVisible(false);
        idField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                idLengthLabel.setVisible(true);
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (idField.getText().length() >= 6) {
                    idLengthLabel.setVisible(false);
                } else {
                    idLengthLabel.setVisible(true);
                }
            }
        });

        // 아이디 중복확인 버튼 추가
        JButton checkDuplicateButton = new JButton("중복확인");
        checkDuplicateButton.setPreferredSize(new Dimension(85, 35));
        checkDuplicateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String id = idField.getText();  // 입력된 아이디 가져오기

                // 데이터베이스 연결
                try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                    // SQL 쿼리 실행
                    String query = "SELECT COUNT(*) FROM users WHERE id = ?";
                    try (PreparedStatement stmt = conn.prepareStatement(query)) {
                        stmt.setString(1, id);
                        try (ResultSet rs = stmt.executeQuery()) {
                            if (rs.next()) {
                                int count = rs.getInt(1);
                                if (count > 0) {
                                    JOptionPane.showMessageDialog(RegisterFrame.this, "이미 가입된 아이디입니다.");
                                } else {
                                    JOptionPane.showMessageDialog(RegisterFrame.this, "사용 가능한 아이디입니다.");
                                }
                            }
                        }
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
        idPanel.add(checkDuplicateButton); // 버튼을 idPanel에 추가
        centerPanel.add(idPanel, gbc);
        gbc.gridy++;

// 아이디 입력 필드와 중복확인 버튼을 같은 패널에 추가
        idPanel.add(idField);
        idPanel.add(checkDuplicateButton);
        // idPanel을 centerPanel에 추가할 때, 그리드백스 레이아웃의 GridBagConstraints의 gridx 값을 1로 설정하여

// 아이디 입력 필드 바로 밑에 실시간 피드백 문구 배치

        centerPanel.add(idPanel, gbc);
        gbc.gridy++;
        centerPanel.add(idLengthLabel, gbc);
        gbc.gridy++;


        // 비밀번호 입력 필드
        passwordField = new JPasswordField(20);
        passwordField.setPreferredSize(new Dimension(200, 35));
        passwordField.setEchoChar((char) 0); // 입력 전에는 텍스트 보이도록 설정
        passwordField.setText("비밀번호를 입력하세요");
        passwordField.setForeground(Color.GRAY);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridy++;

// 비밀번호 입력 시 길이 확인하는 라벨
        JLabel passwordLengthLabel = new JLabel("비밀번호는 최소 8자 이상이어야 합니다.");
        passwordLengthLabel.setForeground(Color.RED); // 빨간색으로 설정
        passwordLengthLabel.setFont(passwordLengthLabel.getFont().deriveFont(Font.PLAIN, 10)); // 작은 글씨로 설정

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
                    passwordField.setEchoChar((char) 0); // 입력 전에는 텍스트 보이도록 설정
                }
            }
        });

        passwordField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                checkPasswordLength();
                checkPasswordMatch(); // 새로 추가된 메소드 호출
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                checkPasswordLength();
                checkPasswordMatch(); // 새로 추가된 메소드 호출
            }

            private void checkPasswordMatch() {
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                checkPasswordLength();
                checkPasswordMatch(); // 새로 추가된 메소드 호출
            }

            private void checkPasswordLength() {
                String password = String.valueOf(passwordField.getPassword());
                if (password.length() < 8) {
                    passwordLengthLabel.setText("비밀번호는 최소 8자 이상이어야 합니다.");
                } else {
                    passwordLengthLabel.setText("");
                }
            }
        });

// 비밀번호 입력 시 실시간 피드백 문구 처리
        passwordLengthLabel.setVisible(false);
        passwordField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                passwordLengthLabel.setVisible(true);
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (String.valueOf(passwordField.getPassword()).length() >= 8) {
                    passwordLengthLabel.setVisible(false);
                } else {
                    passwordLengthLabel.setVisible(true);
                }
            }
        });

// 비밀번호 확인 입력 필드
        JPasswordField confirmPasswordField = new JPasswordField(20);
        confirmPasswordField.setPreferredSize(new Dimension(200, 35));
        confirmPasswordField.setEchoChar((char) 0); // 입력 전에는 텍스트 보이도록 설정
        confirmPasswordField.setText("비밀번호를 다시 입력하세요");
        confirmPasswordField.setForeground(Color.GRAY);

        gbc.gridy++;

// 비밀번호 확인 시 일치 여부 확인하는 라벨
        JLabel confirmPasswordLabel = new JLabel("");
        confirmPasswordLabel.setForeground(Color.RED); // 빨간색으로 설정
        confirmPasswordLabel.setFont(confirmPasswordLabel.getFont().deriveFont(Font.PLAIN, 10)); // 작은 글씨로 설정

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
                    confirmPasswordField.setEchoChar((char) 0); // 입력 전에는 텍스트 보이도록 설정
                }
            }
        });

        confirmPasswordField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                checkPasswordMatch(); // 새로 추가된 메소드 호출
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                checkPasswordMatch(); // 새로 추가된 메소드 호출
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                checkPasswordMatch(); // 새로 추가된 메소드 호출
            }

            private void checkPasswordMatch() {
                String password = String.valueOf(passwordField.getPassword());
                String confirmPassword = String.valueOf(confirmPasswordField.getPassword());
                if (!confirmPassword.equals(password)) {
                    confirmPasswordLabel.setText("비밀번호가 일치하지 않습니다.");

                } else {
                    confirmPasswordLabel.setText("비밀번호가 일치합니다.");
                    confirmPasswordLabel.setForeground(new Color(0, 187, 141)); // 일치하면 초록색으로 변경
                }
            }
        });
        // 비밀번호 확인 입력 필드 바로 밑에 실시간 피드백 문구 배치
        centerPanel.add(passwordField, gbc);
        gbc.gridy++;
        centerPanel.add(passwordLengthLabel, gbc);
        gbc.gridy++;
        centerPanel.add(confirmPasswordField, gbc);
        gbc.gridy++;
        centerPanel.add(confirmPasswordLabel, gbc);
        gbc.gridy++;


        // 이메일 입력 필드
        emailField = new JTextField(20);
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
        usernameField = new JTextField(20);
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
        phoneNumberField = new JTextField(20);
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
        yearField = new JTextField(5); // 4글자로 설정
        yearField.setPreferredSize(new Dimension(70, 30)); // 입력 폼 크기 조정
        yearField.setHorizontalAlignment(JTextField.CENTER); // 텍스트 가운데 정렬
        yearField.setDocument(new JTextFieldLimit(4)); // 최대 입력 글자 수 설정
        birthDatePanel.add(yearField);
        birthDatePanel.add(new JLabel("년"));

        // 월 입력 필드
        monthField = new JTextField(4); // 2글자로 설정
        monthField.setPreferredSize(new Dimension(50, 30)); // 입력 폼 크기 조정
        monthField.setHorizontalAlignment(JTextField.CENTER); // 텍스트 가운데 정렬
        monthField.setDocument(new JTextFieldLimit(2)); // 최대 입력 글자 수 설정
        birthDatePanel.add(monthField);
        birthDatePanel.add(new JLabel("월"));

        // 일 입력 필드
        dayField = new JTextField(4); // 2글자로 설정
        dayField.setPreferredSize(new Dimension(50, 30)); // 입력 폼 크기 조정
        dayField.setHorizontalAlignment(JTextField.CENTER); // 텍스트 가운데 정렬
        dayField.setDocument(new JTextFieldLimit(2)); // 최대 입력 글자 수 설정
        birthDatePanel.add(dayField);
        birthDatePanel.add(new JLabel("일"));

        centerPanel.add(birthDatePanel, gbc);
        gbc.gridy++; // 다음 행으로 이동

        // 성별 라디오 버튼 추가
        JPanel genderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel genderLabel = new JLabel("성별");
        genderPanel.setBackground(Color.WHITE); // 배경색 변경
        genderPanel.add(genderLabel);

        male = new JRadioButton("남자");
        male.setBackground(Color.WHITE);
        female = new JRadioButton("여자");
        female.setBackground(Color.WHITE);

        genderGroup = new ButtonGroup();

        genderGroup.add(male);
        genderPanel.add(male);
        genderGroup.add(female);
        genderPanel.add(female);

        centerPanel.add(genderPanel, gbc);
        mainPanel.add(registerLabel, BorderLayout.NORTH); // 기존 코드, 텍스트 라벨을 상단에 추가

        // 회원가입 버튼 추가
        gbc.gridy++;
        registerButton = new JButton("회원가입");
        registerButton.addActionListener(new RegisterButtonListener());
        registerButton.setPreferredSize(new Dimension(200, 35));
        registerButton.setBackground(new Color(208, 154, 255)); // 배경색 설정

        registerButton.setBorder(BorderFactory.createLineBorder(new Color(143, 188, 143))); //테두리색
        registerButton.setFocusPainted(false); // 포커스 시 테두리 없애기

        registerButton.setFont(registerButton.getFont().deriveFont(Font.BOLD)); // 폰트 굵게 설정
        centerPanel.add(registerButton, gbc);

        // 중앙 패널을 중앙 정렬로 배치하기 위해 빈 패널 추가
        JPanel roundedPanel = new JPanel(new BorderLayout());
        roundedPanel.setBorder(BorderFactory.createEmptyBorder(55, 80, 55, 80));
        roundedPanel.setBackground(Color.WHITE);
        roundedPanel.add(new JPanel(), BorderLayout.NORTH);
        roundedPanel.add(new JPanel(), BorderLayout.SOUTH);
        roundedPanel.add(new JPanel(), BorderLayout.WEST);
        roundedPanel.add(new JPanel(), BorderLayout.EAST);
        roundedPanel.add(centerPanel, BorderLayout.CENTER);

        // 메인 패널에 이미지 패널과 중앙 패널 추가

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

    // 회원가입 버튼 리스너 클래스
    private class RegisterButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // RegisterDTO 객체 생성
            try {
                String gender = "";
                if (male.isSelected()) {
                    gender = male.getText();
                }
                if (female.isSelected()) {
                    gender = male.getText();
                }

                String birthday = yearField.getText() + "-" + monthField.getText() + "-" + dayField.getText();
                System.out.println("생일까지 불러오기 성공" + birthday);
                RegisterDTO dto = new RegisterDTO(idField.getText(), usernameField.getText(), emailField.getText(),
                        new String(passwordField.getPassword()), phoneNumberField.getText(), birthday,
                        gender);
                System.out.println("DTO도 생성 성공");
                RegisterService rs = new RegisterService();
                int res = rs.registerUser(dto);
                // 회원가입 반환 메시지 출력
                switch (res) {
                    case 0:
                        JOptionPane.showMessageDialog(RegisterFrame.this, "이미 가입된 아이디거나 이메일, 혹은 전화번호입니다.");
                        break;
                    case 1:
                        JOptionPane.showMessageDialog(RegisterFrame.this, "회원가입 성공");
                        SwingUtilities.invokeLater(() -> {
                            RegisterFrame.this.dispose();
                        });
                        break;
                    case 3:
                        JOptionPane.showMessageDialog(RegisterFrame.this, "ID는 6자 이상으로 만들어주세요");
                        break;
                    default:
                        JOptionPane.showMessageDialog(RegisterFrame.this, "오류__");
                        break;
                }
            } catch (Exception err) {
                // TODO: handle exception
                JOptionPane.showMessageDialog(RegisterFrame.this, "다시 한번 확인해주세요");
            }

        }
    }

    // 메인 메서드
    public static void main(String[] args) {
        SwingUtilities.invokeLater(RegisterFrame::new); // 회원가입 프레임 생성
    }
}