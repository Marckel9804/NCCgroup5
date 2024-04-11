package com.project1.group5.frame.register;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegisterFrame extends JFrame {
    private JTextField idField; // 아이디 입력 필드
    private JTextField usernameField; // 유저 이름 입력 필드
    private JPasswordField passwordField; // 비밀번호 입력 필드
    private JTextField emailField; // 이메일 입력 필드
    private JTextField phoneNumberField; // 전화번호 입력 필드
    private JTextField birthDateField; // 생년월일 입력 필드
    private JTextField ageField; //   나이 입력 필드

    private JComboBox<String> genderComboBox; // 성별 선택 콤보 박스
    private JButton registerButton; // 회원가입 버튼

    //데이터베이스 가져오기
    private static final String DB_URL = "jdbc:mysql://localhost:3306/project";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "1234";

    // 회원가입 프레임 생성자
    public RegisterFrame() {
        setTitle("Register"); // 프레임 타이틀 설정
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 종료 동작 설정
        setSize(800, 600); // 프레임 크기 설정
        setLocationRelativeTo(null); // 프레임을 화면 중앙에 배치

        // 메인 패널 생성
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        // 중앙 패널 생성
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);

        // 아이디 레이블 및 필드 추가
        gbc.gridy++;
        JLabel idLabel = new JLabel("ID");
        centerPanel.add(idLabel, gbc);

        gbc.gridx++;
        idField = new JTextField(15);
        idField.setPreferredSize(new Dimension(200, 40));
        centerPanel.add(idField, gbc);

        // 유저 이름 레이블 및 필드 추가
        gbc.gridy++;
        gbc.gridx = 0; // 다음 요소를 다음 행의 첫 열에 배치하기 위해 열 인덱스를 0으로 설정
        JLabel usernameLabel = new JLabel("Username");
        centerPanel.add(usernameLabel, gbc);

        gbc.gridx++;
        usernameField = new JTextField(15);
        usernameField.setPreferredSize(new Dimension(200, 40));
        centerPanel.add(usernameField, gbc);

        // 비밀번호 레이블 및 필드 추가
        gbc.gridy++;
        gbc.gridx = 0; // 다음 요소를 다음 행의 첫 열에 배치하기 위해 열 인덱스를 0으로 설정
        JLabel passwordLabel = new JLabel("Password");
        centerPanel.add(passwordLabel, gbc);

        gbc.gridx++;
        passwordField = new JPasswordField(15);
        passwordField.setPreferredSize(new Dimension(200, 40));
        centerPanel.add(passwordField, gbc);

        // 이메일 레이블 및 필드 추가
        gbc.gridy++;
        gbc.gridx = 0; // 다음 요소를 다음 행의 첫 열에 배치하기 위해 열 인덱스를 0으로 설정
        JLabel emailLabel = new JLabel("Email");
        centerPanel.add(emailLabel, gbc);

        gbc.gridx++;
        emailField = new JTextField(15);
        emailField.setPreferredSize(new Dimension(200, 40));
        centerPanel.add(emailField, gbc);

        // 전화번호 레이블 및 필드 추가
        gbc.gridy++;
        gbc.gridx = 0; // 다음 요소를 다음 행의 첫 열에 배치하기 위해 열 인덱스를 0으로 설정
        JLabel phoneNumberLabel = new JLabel("Phone Number");
        centerPanel.add(phoneNumberLabel, gbc);

        gbc.gridx++;
        phoneNumberField = new JTextField(15);
        phoneNumberField.setPreferredSize(new Dimension(200, 40));
        centerPanel.add(phoneNumberField, gbc);

        // 생년월일 레이블 및 필드 추가
        gbc.gridy++;
        gbc.gridx = 0; // 다음 요소를 다음 행의 첫 열에 배치하기 위해 열 인덱스를 0으로 설정
        JLabel birthDateLabel = new JLabel("Birth Date");
        centerPanel.add(birthDateLabel, gbc);

        gbc.gridx++;
        birthDateField = new JTextField(15);
        birthDateField.setPreferredSize(new Dimension(200, 40));
        centerPanel.add(birthDateField, gbc);

        // 나이  레이블 및 필드 추가
        gbc.gridy++;
        gbc.gridx = 0; // 다음 요소를 다음 행의 첫 열에 배치하기 위해 열 인덱스를 0으로 설정
        JLabel ageLabel = new JLabel("Age");
        centerPanel.add(birthDateLabel, gbc);

        gbc.gridx++;
        birthDateField = new JTextField(15);
        birthDateField.setPreferredSize(new Dimension(200, 40));
        centerPanel.add(birthDateField, gbc);
        // 성별 레이블 및 콤보 박스 추가
        gbc.gridy++;
        gbc.gridx = 0; // 다음 요소를 다음 행의 첫 열에 배치하기 위해 열 인덱스를 0으로 설정
        JLabel genderLabel = new JLabel("Gender");
        centerPanel.add(genderLabel, gbc);

        gbc.gridx++;
        String[] genders = {"Male", "Female"};
        genderComboBox = new JComboBox<>(genders);
        genderComboBox.setPreferredSize(new Dimension(200, 40));
        centerPanel.add(genderComboBox, gbc);

        // 회원가입 버튼 추가
        gbc.gridy++;
        registerButton = new JButton("Register");
        registerButton.addActionListener(new RegisterButtonListener());
        registerButton.setPreferredSize(new Dimension(200, 40));
        registerButton.setBackground(new Color(208, 154, 255)); // 배경색 설정
        registerButton.setBorder(BorderFactory.createLineBorder(Color.WHITE)); // 테두리를 하얀색으로 설정
        registerButton.setFocusPainted(false); // 포커스 시 테두리 없애기
        registerButton.setFont(registerButton.getFont().deriveFont(Font.BOLD)); // 폰트 굵게 설정
        centerPanel.add(registerButton, gbc);

        // 중앙 패널을 중앙 정렬로 배치하기 위해 빈 패널 추가
        JPanel roundedPanel = new JPanel(new BorderLayout());
        roundedPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        roundedPanel.setBackground(Color.WHITE);
        roundedPanel.add(new JPanel(), BorderLayout.NORTH);
        roundedPanel.add(new JPanel(), BorderLayout.SOUTH);
        roundedPanel.add(new JPanel(), BorderLayout.WEST);
        roundedPanel.add(new JPanel(), BorderLayout.EAST);
        roundedPanel.add(centerPanel, BorderLayout.CENTER);

        // 메인 패널에 중앙 패널 추가
        mainPanel.add(roundedPanel, BorderLayout.CENTER);

        add(mainPanel); // 메인 패널을 프레임에 추가
        setVisible(true); // 프레임을 보이도록 설정
    }

    // 회원가입 버튼 리스너 클래스
    private class RegisterButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // RegisterDTO 객체 생성
            RegisterDTO dto = new RegisterDTO();
            dto.setUser_id(idField.getText());
            dto.setUsername(usernameField.getText());
            dto.setEmail(emailField.getText());
            dto.setPassword(new String(passwordField.getPassword()));
            dto.setPhone_number(phoneNumberField.getText());
            dto.setBirth_date(birthDateField.getText());
            dto.setGender((String) genderComboBox.getSelectedItem());

            // 회원가입 성공 메시지 출력
            JOptionPane.showMessageDialog(RegisterFrame.this, "회원가입이 완료되었습니다.");
        }
    }

    // 메인 메서드
    public static void main(String[] args) {
        SwingUtilities.invokeLater(RegisterFrame::new); // 회원가입 프레임 생성
    }
}
