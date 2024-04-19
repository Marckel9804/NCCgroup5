package com.project1.group5.frame.movierecommand;

import com.project1.group5.frame.mypage.MyPageFrame;
import com.project1.group5.frame.board.BoardFrame;
import com.project1.group5.frame.login.LoginFrame;
import com.project1.group5.frame.mainPage.MainPage;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;

import javax.swing.*;

public class MovieRecommendFrame extends JFrame implements KeyListener, MouseListener, MouseMotionListener, Runnable {

    // graphics 라이브러리를 사용하기 위한 객체
    Image buffImage;
    Graphics buffg;
    Image character;
    JPanel panelForGraphics;

    // 창 크기
    int f_width;
    int f_height;
    int menu;

    // 캐릭터 이미지 경로
    String[] imageList = { "pondering_dorothy", "pondering_scarecrow", "pondering_tin_man", "pondering_cowardly_lion",
            "good_witch_glinda", "wicked_witch" };

    // 선택지
    int years[] = { 1980, 1990, 2000, 2010, 2020 };
    String countries[] = { "한국", "미국" };
    String ratings[] = { "전체", "12", "15", "청불" };
    String updown[] = { "up", "down" };

    String[] choices = { "장르1", "장르2", "연도1", "연도2", "심의1", "심의2", "국가1", "국가2", "러닝타임1", "러닝타임2", "감독1", "감독2" };
    // 캐릭터 이미지 받아올 이미지 배열
    Image[] characters;

    // 기타 임시 이미지들... 차후 삭제 예정
    Image ozo;
    Image ozland;
    Image rc;
    Image bc;

    // 임시로 선택지 띄우기 위한 J라벨
    JLabel jl_left;
    JLabel jl_right;

    ToJButton toMyPage;
    ToJButton toBoard;

    // 임시로 선택지 클릭 영역 만들기 위한 도형 객체. 차후에 변수명 수정 예정
    Shape rc_s;
    Shape bc_s;

    // 이미지를 넘기기 위한 변수
    int nthChacracter = 0;

    // 키보드, 마우스 액션을 위한 변수
    int mousex;
    int mousey;

    MainPage mp;
    boolean otherFrame;

    public MovieRecommendFrame() {
        init();
        panelForGraphics.setLayout(null);
        panelForGraphics.add(jl_left);
        panelForGraphics.add(jl_right);
        add(panelForGraphics);
    }

    public MovieRecommendFrame(MainPage mp) {
        this.mp = mp;
        init();
        if (mp.getLoginCheck()) {
            ActionListener mypage = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (!otherFrame) {
                        MyPageFrame myPage = new MyPageFrame(mp);
                        myPage.setVisible(true);
                        otherFrame = true;
                        myPage.addWindowListener((WindowListener) new WindowAdapter() {
                            @Override
                            public void windowClosed(WindowEvent e) {
                                otherFrame = false;
                            }
                        });
                    }
                }
            };
            toMyPage = new ToJButton(f_width - 300, 10, "마이페이지", mypage);
        } else {
            ActionListener login = new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    if (!otherFrame) {
                        LoginFrame loginPage = new LoginFrame(mp);
                        loginPage.setVisible(true);
                        MovieRecommendFrame.this.dispose();
                    }
                }
            };
            toMyPage = new ToJButton(f_width - 300, 10, "로그인하기", login);
        }

        ActionListener board = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!otherFrame) {
                    BoardFrame b = new BoardFrame(mp);
                    b.setVisible(true);
                    otherFrame = true;
                    b.addWindowListener((WindowListener) new WindowAdapter() {
                        @Override
                        public void windowClosed(WindowEvent e) {
                            otherFrame = false;
                        }
                    });
                }
            }
        };
        toBoard = new ToJButton(f_width - 180, 10, "게시판", board);
        panelForGraphics.setLayout(null);
        panelForGraphics.add(jl_left);
        panelForGraphics.add(jl_right);
        panelForGraphics.add(toMyPage);
        panelForGraphics.add(toBoard);
        add(panelForGraphics);
    }

    private void init() {
        menu = 50;
        f_width = 1200;
        f_height = 550 + menu;
        otherFrame = false;

        setSize(f_width, f_height);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // 이미지 받아오기. 디렉토리는 프로젝트 폴더에 맞게 수정
        String imgDir = "src/main/java/com/project1/group5/frame/recommendImages/";
        characters = new Image[6];
        for (int i = 0; i < 6; i++) {
            characters[i] = new ImageIcon(imgDir + imageList[i] + ".png").getImage();
        }
        ozo = new ImageIcon(imgDir + "ozo.png").getImage();
        ozland = new ImageIcon(imgDir + "ozland.png").getImage();
        rc = new ImageIcon(imgDir + "rc.png").getImage();
        bc = new ImageIcon(imgDir + "bc.png").getImage();

        // J라벨 임의로 위치 및 텍스트 조정
        jl_left = new JLabel(choices[nthChacracter * 2]);
        jl_right = new JLabel(choices[nthChacracter * 2 + 1]);
        jl_left.setBounds(f_width / 2 + 200, f_height / 2 - 75, 200, 30);
        jl_right.setBounds(f_width / 2, f_height / 2 - 75, 200, 30);
        jl_right.setForeground(Color.red);
        jl_left.setForeground(Color.blue);
        jl_left.setFont(new Font(null, Font.BOLD, 30));
        jl_right.setFont(new Font(null, Font.BOLD, 30));

        // 그래픽을 먼저 로딩시키기 위해 사진들을 전부 Jpanel에 붙여서 프레임 위에 붙일것임
        panelForGraphics = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                buffImage = createImage(f_width, f_height);
                buffg = buffImage.getGraphics();
                update(g);
            }

            public void drawCharacter() {
                int size = 300;
                if (buffg != null) {
                    buffg.drawImage(characters[nthChacracter], 0, f_height - size, size, size, this);
                }
            }

            public void drawOzo() {
                int size = 110;
                if (buffg != null) {
                    buffg.drawImage(ozo, mousex - 50, mousey - 50, size, size, this);
                }
            }

            public void drawOzland() {
                if (buffg != null) {
                    buffg.drawImage(ozland, 0, menu, f_width, f_height - menu, this);
                }
            }

            public void drawCircle() {
                if (buffg != null) {
                    buffg.drawImage(rc, f_width / 2, f_height / 2 - 30, 150, 150, this);
                    rc_s = new Rectangle2D.Double(f_width / 2, f_height / 2 - 30, 150, 150);
                    buffg.drawImage(bc, f_width / 2 + 200, f_height / 2 - 30, 150, 150, this);
                    bc_s = new Rectangle2D.Double(f_width / 2 + 200, f_height / 2 - 30, 150, 150);
                }
            }

            public void update(Graphics g) {
                drawOzland();
                drawCharacter();
                drawOzo();
                drawCircle();
                if (buffImage != null) {
                    g.drawImage(buffImage, 0, 0, this);
                }
            }
        };
        addKeyListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);
        setBackground(new Color(32, 141, 198));

        setVisible(true);
    }

    public static void main(String[] args) {
        MovieRecommendFrame ms = new MovieRecommendFrame();
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (rc_s.contains(e.getPoint())) {
            nthChacracter = (nthChacracter + 5) % 6;
            jl_left.setText(choices[nthChacracter * 2]);
            jl_right.setText(choices[nthChacracter * 2 + 1]);
            repaint();
        }
        if (bc_s.contains(e.getPoint())) {
            nthChacracter = (nthChacracter + 1) % 6;
            jl_left.setText(choices[nthChacracter * 2]);
            jl_right.setText(choices[nthChacracter * 2 + 1]);
            repaint();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mousex = e.getX();
        mousey = e.getY();
        repaint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void run() {
        throw new UnsupportedOperationException("Unimplemented method 'run'");
    }

    class ToJButton extends JButton {

        ToJButton(int x, int y, String text, ActionListener al) {
            setBorderPainted(false);
            setContentAreaFilled(false);
            setText(text);
            setFont(new Font("Pretendard", Font.BOLD, 20));
            setForeground(Color.YELLOW);
            setBounds(x, y, 200, 30);

            addActionListener(al);
        }
    }
}
