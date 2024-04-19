package com.project1.group5.frame.movierecommand;

import com.project1.group5.frame.mypage.MyPageFrame;

import lombok.val;

import com.project1.group5.db.OzoDB;
import com.project1.group5.db.question.InMovieDTO;
import com.project1.group5.db.question.ViewService;
import com.project1.group5.frame.board.BoardFrame;
import com.project1.group5.frame.login.LoginFrame;
import com.project1.group5.frame.mainPage.MainPage;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.sql.*;

import javax.swing.*;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;

public class MovieRecommendFrame extends JFrame implements MouseListener, MouseMotionListener {

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
            "toto" };

    int charCount = imageList.length;

    // 선택지
    int years[] = { 1980, 1990, 2000, 2010, 2020 };
    String ratings[] = { "전체", "12", "15", "청불" };

    String[] choiceImgList = { "shoe", "bag", "heart", "portion", "bone" };
    String[] choices = { "어느 연도의 영화를 선호하시나요?", "몇세 이용가 영화를 선호하시나요?", "2시간보다 긴 영화를 좋아하시나요?",
            "어떤 장르를 좋아하시나요?", "어떤 키워드의 영화가 좋으신가요?" };
    String[] order = { "year", "rating", "time", "genre", "keyword" };

    String currChoice1;
    String currChoice2;

    Image conver;

    // 캐릭터 이미지 받아올 이미지 배열
    Image[] characters;
    Image[] choiceImgs;

    // 기타 임시 이미지들... 차후 삭제 예정
    Image ozo;
    Image ozland;

    // J라벨
    JLabel jl_left;
    JLabel jl_right;
    JLabel question;

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

    List<String> genres;
    List<String> keywords;
    ArrayList<InMovieDTO> movieResult;
    MainPage mp;
    ViewService vs;
    boolean otherFrame;

    public MovieRecommendFrame() {
        vs = new ViewService();
        init();
        panelForGraphics.setLayout(null);
        panelForGraphics.add(jl_left);
        panelForGraphics.add(jl_right);
        panelForGraphics.add(question);
        add(panelForGraphics);
    }

    public MovieRecommendFrame(MainPage mp) {
        this.mp = mp;
        vs = new ViewService();
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
                        MovieRecommendFrame.this.setVisible(false);
                        // otherFrame = true;
                        loginPage.addWindowListener((WindowListener) new WindowAdapter() {
                            @Override
                            public void windowClosed(WindowEvent e) {
                                MovieRecommendFrame.this.setVisible(true);
                            }
                        });

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
                    MovieRecommendFrame.this.setVisible(false);
                    b.addWindowListener((WindowListener) new WindowAdapter() {
                        @Override
                        public void windowClosed(WindowEvent e) {
                            otherFrame = false;
                            MovieRecommendFrame.this.setVisible(true);
                        }
                    });
                }
            }
        };
        toBoard = new ToJButton(f_width - 180, 10, "게시판", board);
        panelForGraphics.setLayout(null);
        panelForGraphics.add(jl_left);
        panelForGraphics.add(jl_right);
        panelForGraphics.add(question);
        panelForGraphics.add(toMyPage);
        panelForGraphics.add(toBoard);
        add(panelForGraphics);
    }

    private void init() {
        menu = 50;
        f_width = 1100;
        f_height = 550 + menu;
        otherFrame = false;

        try {
            vs.dropView("filtered_view_keyword");
        } catch (Exception e) {
        }
        try {
            vs.dropView("filtered_view_year");
        } catch (Exception e) {
        }
        try {
            vs.dropView("filtered_view_genre");
        } catch (Exception e) {
        }
        try {
            vs.dropView("filtered_view_time");
        } catch (Exception e) {
        }
        try {
            vs.dropView("filtered_view_rating");
        } catch (Exception e) {
        }
        setSize(f_width, f_height);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // 이미지 받아오기. 디렉토리는 프로젝트 폴더에 맞게 수정

        String imgDir = "src/main/java/com/project1/group5/frame/reccommandImages/";
        characters = new Image[charCount];
        choiceImgs = new Image[charCount * 2];
        for (int i = 0; i < charCount; i++) {
            characters[i] = new ImageIcon(imgDir + imageList[i] + ".png").getImage();
            choiceImgs[i * 2] = new ImageIcon(imgDir + choiceImgList[i] + 1 + ".png").getImage();
            choiceImgs[i * 2 + 1] = new ImageIcon(imgDir + choiceImgList[i] + 2 + ".png").getImage();
        }
        ozo = new ImageIcon(imgDir + "ozo.png").getImage();
        ozland = new ImageIcon(imgDir + "ozland.png").getImage();
        conver = new ImageIcon(imgDir + "recover.png").getImage();

        int yearLen = years.length;

        Random rnd = new Random();
        int yearL = rnd.nextInt(yearLen);
        int yearR = rnd.nextInt(yearLen);

        while (yearL == yearR) {
            yearR = rnd.nextInt(yearLen);
        }

        // J라벨 임의로 위치 및 텍스트 조정
        jl_left = new JLabel(years[yearL] + "");
        jl_right = new JLabel(years[yearR] + "");
        jl_left.setBounds(f_width / 2, f_height / 2 - 75, 200, 30);
        jl_right.setBounds(f_width / 2 + 290, f_height / 2 - 75, 200, 30);
        jl_right.setForeground(Color.BLACK);
        jl_left.setForeground(Color.BLACK);
        jl_left.setFont(new Font(null, Font.BOLD, 30));
        jl_right.setFont(new Font(null, Font.BOLD, 30));

        question = new JLabel(choices[nthChacracter]);
        question.setBounds(f_width / 2 + 10, f_height / 6 + 25, 500, 30);
        question.setForeground(Color.BLACK);
        question.setFont(new Font(null, Font.BOLD, 25));

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

            public void drawConver() {
                if (buffg != null) {
                    buffg.drawImage(conver, f_width / 2 - 50, menu, f_width / 2 + 50, f_height / 4 + 30, this);
                }
            }

            public void drawChoices() {
                if (buffg != null) {
                    buffg.drawImage(choiceImgs[nthChacracter * 2], f_width / 2, f_height / 2 - 30, 180, 180, this);
                    rc_s = new Rectangle2D.Double(f_width / 2, f_height / 2 - 30, 180, 180);
                    buffg.drawImage(choiceImgs[nthChacracter * 2 + 1], f_width / 2 + 250, f_height / 2 - 30, 180, 180,
                            this);
                    bc_s = new Rectangle2D.Double(f_width / 2 + 250, f_height / 2 - 30, 180, 180);
                }
            }

            public void update(Graphics g) {
                drawOzland();
                drawConver();
                drawCharacter();
                drawChoices();
                drawOzo();
                if (buffImage != null) {
                    g.drawImage(buffImage, 0, 0, this);
                }
            }
        };
        addMouseListener(this);
        addMouseMotionListener(this);
        setBackground(new Color(32, 141, 198));

        setVisible(true);
    }

    public static void main(String[] args) {
        MovieRecommendFrame ms = new MovieRecommendFrame();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        try {

            if (rc_s.contains(e.getPoint())) {// 만약 클릭된 포인트를 이전에 생성한 shape객체가 포함하고 있을 경우~ 뭐뭐 한다. 안의 코드는 차후 수정
                // 오른쪽 골랐을 때
                qOrder(nthChacracter, jl_right.getText());
                nthChacracter = (nthChacracter + 1);
            }
            if (bc_s.contains(e.getPoint())) {// 마찬가지. 애는 왼쪽 도형
                // 왼쪽 골랐을 때
                qOrder(nthChacracter, jl_left.getText());
                nthChacracter = (nthChacracter + 1);
            }
            if (nthChacracter > 4) {
                if (!otherFrame) {
                    MovieResultFrame mrf = new MovieResultFrame(movieResult.get(0));
                    MovieRecommendFrame.this.setVisible(false);
                    // otherFrame = true;
                    mrf.addWindowListener((WindowListener) new WindowAdapter() {
                        @Override
                        public void windowClosed(WindowEvent e) {
                            MovieRecommendFrame.this.dispose();
                            // System.out.println("창 꺼짐");
                        }
                    });
                }

            } else {
                question.setText(choices[nthChacracter]);
                repaint();
            }
        } catch (Exception ex) {
            // TODO: handle exception
        }
    }

    public void qOrder(int type, String val) {
        int arrLen;
        Random rnd = new Random();
        int leftNum;
        int rightNum;
        System.out.println(val + "를 고름");
        try {
            switch (type) {
                case 0:
                    vs.year = Integer.parseInt(val);
                    vs.selectCreate("year");
                    arrLen = ratings.length;

                    leftNum = rnd.nextInt(arrLen);
                    rightNum = rnd.nextInt(arrLen);

                    while (leftNum == rightNum) {
                        rightNum = rnd.nextInt(arrLen);
                    }
                    jl_left.setText(ratings[leftNum]);
                    jl_right.setText(ratings[rightNum]);
                    // vs.selectFromCurrentView();
                    break;
                case 1:
                    vs.rating = val;
                    vs.selectCreate("rating");
                    jl_left.setText("up");
                    jl_right.setText("down");
                    // vs.selectFromCurrentView();
                    break;
                case 2:
                    vs.selUpdown = val;
                    vs.selectCreate("time");
                    // vs.selectFromCurrentView();
                    genres = vs.returnList("genre");
                    arrLen = genres.size();

                    leftNum = rnd.nextInt(arrLen);
                    rightNum = rnd.nextInt(arrLen);

                    while (leftNum == rightNum) {
                        rightNum = rnd.nextInt(arrLen);
                    }

                    jl_left.setText(genres.get(leftNum));
                    jl_right.setText(genres.get(rightNum));
                    break;
                case 3:
                    vs.selGenre = val;
                    vs.selectCreate("genre");
                    // vs.selectFromCurrentView();
                    keywords = vs.returnList("keyword");
                    arrLen = keywords.size();

                    leftNum = rnd.nextInt(arrLen);
                    rightNum = rnd.nextInt(arrLen);

                    while (leftNum == rightNum) {
                        rightNum = rnd.nextInt(arrLen);
                    }

                    jl_left.setText(keywords.get(leftNum));
                    jl_right.setText(keywords.get(rightNum));
                    keywords = vs.returnList("keyword");
                    break;
                case 4:
                    vs.selKeyword = val;
                    vs.selectCreate("keyword");
                    vs.selectFromCurrentView();
                    movieResult = vs.selectFromCurrentView();
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            System.out.println("switch문 실행 안헸음");
            e.printStackTrace();
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
