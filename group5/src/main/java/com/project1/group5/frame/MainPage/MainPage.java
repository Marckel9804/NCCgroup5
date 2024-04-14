package com.project1.group5.frame.MainPage;

import com.project1.group5.frame.Login.LoginFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainPage {

    public static void main(String[] args) {
        // 메인 프레임 생성
        int f_width;
        int f_height;
        f_width=800;
        f_height=600;
        JFrame frame = new JFrame("메인 페이지");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 프레임 닫기 설정
        frame.setSize(f_width, f_height); // 프레임 크기 설정
        frame.setLayout(null); // 레이아웃 매니저 사용 안 함 (직접 좌표 지정)
        frame.setLocationRelativeTo(null); // 시작 위치를 화면 중앙으로 설정
        frame.setResizable(false); // 사용자 크기 조정 불가능
        frame.getContentPane().setBackground(new Color(0, 187, 141)); // 배경색 설정

        // 이미지 로드
        String imgDir = "src/main/java/com/project1/group5/frame/LoginImages/";
        ImageIcon icon = new ImageIcon(imgDir+"ozo.png");

        JLabel imageLabel = new JLabel(icon);
        imageLabel.setBounds(0, 0, f_width, f_height); // 이미지 사이즈와 위치 설정

        // 이미지를 프레임에 추가 (상단에 추가)
        frame.add(imageLabel);

        // 로그인 버튼 생성
        LoginPageButton loginButton = new LoginPageButton("로그인");
        loginButton.setBounds(f_width/2-120-10, f_height-f_height/4, 120, 40); // 좌표와 크기 설정
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //로그인 페이지로 이동하는 코드를 추가하세요.
                //Login 클래스의 인스턴스 생성
                LoginFrame loginPage = new LoginFrame();
                //보이기 메소드 호출
                loginPage.setVisible(true);
            }
        });

        // 회원가입 버튼 생성
        LoginPageButton registerButton = new LoginPageButton("회원가입");
        registerButton.setBounds(f_width/2+10, f_height-f_height/4, 120, 40); // 좌표와 크기 설정
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //임의로 만들어 놓은 거_회원가입 클래스 새로 만들거임
                JFrame registerFrame = new JFrame("회원가입 페이지");
                registerFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // 회원가입 프레임만 닫기
                registerFrame.setSize(400, 200); // 회원가입 프레임 크기 설정
                registerFrame.setLocationRelativeTo(null); // 화면 중앙 정렬
                // 회원가입 컴포넌트들을 추가하고 나머지 회원가입 화면 구현
                JLabel registerLabel = new JLabel("회원가입 페이지");
                registerLabel.setHorizontalAlignment(JLabel.CENTER);
                registerFrame.add(registerLabel, BorderLayout.NORTH);
                registerFrame.setVisible(true); // 회원가입 프레임 표시
            }
        });

        // 버튼을 프레임에 추가
        frame.add(loginButton);
        frame.add(registerButton);

        // 프레임을 보이도록 설정
        frame.setVisible(true);
    }
}


//버튼 디자인


class LoginPageButton extends JButton {
    LoginPageButton(String text) {
        int[] rgb = {208,154,255};
        setOpaque(false);
        setContentAreaFilled(false);// 배경 투명화
        setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // 안쪽 여백
        setText(text);
        setFont(new Font("SansSerif", Font.PLAIN, 20)); // 폰트 변경
        setForeground(new Color(rgb[0], rgb[1], rgb[2])); // 텍스트 색상 검은색으로 변경
        setFocusPainted(false); // 포커스 표시 비활성화

        // 마우스 호버 효과
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setForeground(Color.white); // 호버시 텍스트 색상 변경
                setCursor(new Cursor(Cursor.HAND_CURSOR)); // 호버시 손가락 모양 커서로 변경
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setForeground(new Color(rgb[0], rgb[1], rgb[2])); // 마우스가 벗어날 때 원래의 색상으로 변경
            }

            @Override
            public void mousePressed(MouseEvent e) {
                // 클릭시 버튼 크기 약간 축소
                setBounds(getX() + 2, getY() + 2, getWidth(), getHeight());
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                // 클릭 후 버튼 크기 복구
                setBounds(getX() - 2, getY() - 2, getWidth(), getHeight());
            }
        });
    }

    // 그림자 효과
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 그림자 설정
        int shadowWidth = 6;
        g2.setColor(new Color(0, 0, 0, 100));
        g2.fillRoundRect(shadowWidth, shadowWidth, getWidth() - shadowWidth * 2, getHeight() - shadowWidth * 2, 20, 20);

        super.paintComponent(g);
        g2.dispose();
    }

    // 테두리 처리
    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int borderWidth = 5; // 테두리의 두께를 원하는 값으로 설정합니다.
        int cornerRadius = 20; // 둥근 테두리의 반지름을 설정합니다.

        g2.setColor(new Color(143, 188, 143)); // 테두리 색상 변경
        g2.setStroke(new BasicStroke(borderWidth)); // 선의 굵기 조절

        // 테두리를 그리기 위한 영역 계산
        int x = borderWidth / 2;
        int y = borderWidth / 2;
        int width = getWidth() - borderWidth;
        int height = getHeight() - borderWidth;

        g2.drawRoundRect(x, y, width, height, cornerRadius, cornerRadius); // 외곽선만 그리기

        g2.dispose();
    }

    public static void main(String[] args) {
        // 테스트를 위한 JFrame 생성
        JFrame frame = new JFrame("Button Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(200, 200);
        frame.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 50)); // 레이아웃 설정

        // LoginPageButton 추가
        LoginPageButton button = new LoginPageButton("Login");
        frame.add(button);

        frame.setVisible(true);
    }
}