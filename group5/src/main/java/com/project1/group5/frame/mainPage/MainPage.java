package com.project1.group5.frame.mainPage;

import javax.swing.*;

import com.project1.group5.frame.board.BoardFrame;
import com.project1.group5.frame.login.LoginFrame;
import com.project1.group5.frame.movierecommand.MovieRecommendFrame;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class MainPage extends JFrame {
    int f_width;
    int f_height;

    boolean otherFrame;

    private String user_id;
    private String user_name;
    private int user_age;
    private boolean loginCheck;
    LoginPageButton loginButton;
    LoginPageButton runButton;

    ActionListener lButton;
    ActionListener rButton;

    public MainPage() {
        // 메인 프레임 생성

        init();
    }

    public void init() {
        // 메인 프레임 생성
        loginCheck = false;
        user_id = "그없";
        user_name = "그없";
        user_age = -1;
        otherFrame = false;
        f_width = 1100;
        f_height = 600;
        setDefaultCloseOperation(EXIT_ON_CLOSE); // 프레임 닫기 설정
        setSize(f_width, f_height); // 프레임 크기 설정
        setLayout(null); // 레이아웃 매니저 사용 안 함 (직접 좌표 지정)
        setLocationRelativeTo(null); // 시작 위치를 화면 중앙으로 설정
        setResizable(false); // 사용자 크기 조정 불가능
        getContentPane().setBackground(new Color(0, 187, 141)); // 배경색 설정
        setTitle("메인 페이지");

        // 이미지 로드
        String imgDir = "src/main/java/com/project1/group5/frame/LoginImages/";
        ImageIcon icon = new ImageIcon(imgDir + "ozo.png");

        JLabel imageLabel = new JLabel(icon);
        imageLabel.setBounds(0, -50, f_width, f_height); // 이미지 사이즈와 위치 설정 // 수정한거

        // 이미지를 프레임에 추가 (상단에 추가)
        add(imageLabel);

        // 로그인 버튼 생성
        loginButton = new LoginPageButton("로그인");
        loginButton.setBounds(f_width / 2 - 140 - 10, f_height - f_height / 4, 140, 40); // 좌표와 크기 설정
        lButton = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!otherFrame) {
                    LoginFrame loginPage = new LoginFrame(MainPage.this);
                    otherFrame = true;
                    loginPage.addWindowListener((WindowListener) new WindowAdapter() {
                        @Override
                        public void windowClosed(WindowEvent e) {
                            otherFrame = false;
                        }
                    });
                }
            }
        };
        loginButton.addActionListener(lButton);

        runButton = new LoginPageButton("비회원실행");
        runButton.setBounds(f_width / 2 + 10, f_height - f_height / 4, 140, 40); // 좌표와 크기 설정
        rButton = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!otherFrame) {
                    MovieRecommendFrame mf = new MovieRecommendFrame(MainPage.this);
                    otherFrame = true;
                    MainPage.this.setVisible(false);
                    mf.addWindowListener((WindowListener) new WindowAdapter() {
                        @Override
                        public void windowClosed(WindowEvent e) {
                            otherFrame = false;
                            MainPage.this.setVisible(true);
                        }
                    });
                }
            }
        };
        runButton.addActionListener(rButton);

        // 버튼을 프레임에 추가
        add(loginButton);
        add(runButton);
        // removeAll();
        // 프레임을 보이도록 설정
        setVisible(true);
    }

    public boolean getLoginCheck() {
        return loginCheck;
    }

    public void setLoginCheck(boolean b) {
        loginCheck = b;
    }

    public String getId() {
        return user_id;
    }

    public void setID(String id) {
        user_id = id;
    }

    public void setAge(int age) {
        user_age = age;
    }

    public int getAge() {
        return user_age;
    }

    public void setName(String name) {
        user_name = name;
    }

    public String getName() {
        return user_name;
    }

    public void erasePage() {
        getContentPane().removeAll();
        init();
        revalidate();
        repaint();

    }

    public void loggedInPage() {
        ActionListener newL = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!otherFrame) {
                    BoardFrame bf = new BoardFrame(MainPage.this);
                    otherFrame = true;
                    MainPage.this.setVisible(false);
                    bf.addWindowListener((WindowListener) new WindowAdapter() {
                        @Override
                        public void windowClosed(WindowEvent e) {
                            otherFrame = false;
                            MainPage.this.setVisible(true);
                        }
                    });
                }
            }
        };
        ActionListener newR = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!otherFrame) {
                    MovieRecommendFrame mf = new MovieRecommendFrame(MainPage.this);
                    otherFrame = true;
                    MainPage.this.setVisible(false);
                    mf.addWindowListener((WindowListener) new WindowAdapter() {

                        @Override
                        public void windowClosed(WindowEvent e) {
                            otherFrame = false;
                            MainPage.this.setVisible(true);
                        }
                    });
                }
            }
        };

        loginButton.changeButton("게시판", lButton, newL);

        runButton.changeButton("영화추천", rButton, newR);

        loginButton.setBounds(f_width / 2 - 140 - 10, f_height - f_height / 4, 140, 40); // 좌표와 크기 설정

        runButton.setBounds(f_width / 2 + 10, f_height - f_height / 4, 140, 40); // 좌표와 크기 설정
        // LoginPageButton lo = new LoginPageButton("로그아웃");
        // ActionListener logout = new ActionListener() {
        // @Override
        // public void actionPerformed(ActionEvent e) {
        // MainPage.this.erasePage();
        // }

        // };
        // lo.setBounds(f_width / 2 - 70, f_height - f_height / 4 + 40, 140, 40); // 좌표와
        // 크기 설정
        // lo.addActionListener(logout);
        // add(lo);
        // repaint();
    }

    public static void main(String[] args) {
        MainPage frame = new MainPage();
    }

}

// 버튼 디자인

class LoginPageButton extends JButton {
    LoginPageButton(String text) {
        int[] rgb = { 208, 154, 255 };
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

    public void changeButton(String t, ActionListener oldL, ActionListener newL) {
        setText(t);
        this.removeActionListener(oldL);
        this.addActionListener(newL);

    }
}