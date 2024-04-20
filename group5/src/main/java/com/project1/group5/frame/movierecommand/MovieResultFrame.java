package com.project1.group5.frame.movierecommand;

import com.project1.group5.db.question.InMovieDTO;
import com.project1.group5.db.question.ViewService;
import com.project1.group5.frame.board.BoardFrame;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;


public class MovieResultFrame extends JFrame {
    /*여기있는 이미지를 프레임에 그려줄거임.*/
    Image background = new ImageIcon("src/main/java/com/project1/group5/frame/reccommandImages/res2.png").getImage();//배경이미지
    Image poster = new ImageIcon("src/main/java/com/project1/group5/frame/reccommandImages/poster.png").getImage();//포스터 샘플
    Image conver = new ImageIcon("src/main/java/com/project1/group5/frame/reccommandImages/recover.png").getImage();
    Image oz = new ImageIcon("src/main/java/com/project1/group5/frame/reccommandImages/oz1.png").getImage();
    JPanel panelForGraphics;
    Graphics buffg;
    Image buffImage;

    //프레임 크기
    int f_width = 1200;
    int f_height = 600;

    //버튼 객체
    Buttons toBoard;
    Buttons regame;

    //라벨들 객체
    JLabel title;
    JLabel year;

    JLabel genre;

    JLabel keyword;
    JLabel country;

    JLabel director;

    JLabel runningTime;

    /*생성자*/
    MovieResultFrame(InMovieDTO movie) {
        ViewService vs = new ViewService();

        //homeframe();
        Container c = getContentPane();

        setTitle("result");
        setSize(1100, 600);//프레임의 크기
        setResizable(false);//창의 크기를 변경하지 못하게

        //영화 정보 라벨 생성


        // 버튼 두개 생성
        toBoard = new Buttons(500, 500, "게시판으로");
        regame = new Buttons(605, 500, "다시하기");

        // 버튼에 각각 이벤트 달아주기
        toBoard.addActionListener(new ActionListener() {    //게시판 창 띄우고 result창 끄기
            @Override
            public void actionPerformed(ActionEvent e) {
                new BoardFrame();
                dispose();
            }
        });
        regame.addActionListener(new ActionListener() {    //게임 다시하기
            @Override
            public void actionPerformed(ActionEvent e) {
                new MovieRecommendFrame();
                dispose();
            }
        });

        // 영화정보 내용 텍스트 라벨들

        setVisible(true);


        //배경화면 영화 포스터, 영화 내용 출력
        panelForGraphics = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                buffImage = createImage(f_width, f_height);
                buffg = buffImage.getGraphics();
                update(g);
            }

            public void drawPoster() {   //출력될 영화 포스터 그리기
                if (buffg != null) {
                    buffg.drawImage(poster, 120, 100, 200, 300, this);
                }
            }

            public void drawConver() {   // 말풍선 그리기
                if (buffg != null) {
                    buffg.drawImage(conver, 30, -90, 700, 720, this);
                }
            }

            public void drawOz() {   //오즈 그리기
                if (buffg != null) {
                    buffg.drawImage(oz, 650, 150, 450, 500, this);
                }
            }

            public void drawBackground() {  // 배경화면 그리기
                if (buffg != null) {
                    buffg.drawImage(background, 0, 0, f_width, f_height, this);
                }
            }

            public void update(Graphics g) {
                drawBackground();
                drawConver();
                drawPoster();
                drawOz();
                if (buffImage != null) {
                    g.drawImage(buffImage, 0, 0, this);
                }
            }
        };


        panelForGraphics.setLayout(null);
        //영화 정보들 선언
        title = new Labels(350, 100, "영화제목 : " + movie.getTitle(), 200, 20);
        year = new Labels(350, 120, "영화년도 : " + movie.getRelease_year(), 200, 20);
        country = new Labels(350, 140, "제작국가 : " + movie.getCountry(), 200, 20);
        director = new Labels(350, 160, "감독 : " + movie.getDirector(), 200, 20);
        runningTime = new Labels(350, 180, "상영시간 : " + movie.getRunning_time(), 200, 20);
        genre = new Labels(350, 200, "<html>장르 : <br>" + movie.getGenre() + "sssssss</html>", 200, 60);
        keyword = new Labels(350, 260, "<html>키워드 : <br>" + movie.getKeyword() + "ssssssssss</html>", 200, 60);


        //패널에 모든 라벨, 버튼들 추가
        panelForGraphics.add(title);
        panelForGraphics.add(year);
        panelForGraphics.add(country);
        panelForGraphics.add(director);
        panelForGraphics.add(runningTime);
        panelForGraphics.add(genre);
        panelForGraphics.add(keyword);
        panelForGraphics.add(toBoard);
        panelForGraphics.add(regame);
        add(panelForGraphics);

        setBackground(new Color(30, 141, 190));

        setVisible(true);
    }

    class Buttons extends JButton { //버튼 객체
        Buttons(int x, int y, String text) {
            // 위치, 글 조정
            setBounds(x, y, 100, 30);
            setText(text);
            setOpaque(false); // 투명 배경
            setBorderPainted(false); // 테두리 그리지 않음
            setContentAreaFilled(false); // 내용 영역 채우지 않음

            addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            if (getModel().isPressed()) {
                g.setColor(Color.GREEN); // 버튼이 눌렸을 때 배경색
            } else {
                g.setColor(new Color(40, 120, 100)); // 버튼이 눌리지 않았을 때 배경색
            }
            g.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20); // 둥근 모서리의 직사각형 그리기
            super.paintComponent(g); // 기존의 버튼 텍스트 그리기
        }

        @Override
        protected void paintBorder(Graphics g) { //버튼 테두리 크리기
            g.setColor(Color.white);
            g.drawRoundRect(0, 0, getWidth(), getHeight(), 20, 20); // 테두리를 둥글게 그리는 부분
        }
    }

    class Labels extends JLabel {    // 텍스트 라벨들 객체 (좌표, 들어갈 텍스트, 크기 높이)
        Labels(int x, int y, String text, int width, int height) {
            setBounds(x, y, width, height);
            setText(text);

            setFont(getFont().deriveFont(20f));
        }
    }

    public static void main(String[] args) {
        List<String> genres = new ArrayList<String>();
        List<String> keywords = new ArrayList<String>();
        List<String> diretors = new ArrayList<String>();
        genres.add("액션");
        keywords.add("사막");
        keywords.add("SF");
        keywords.add("전쟁");
        keywords.add("혁명");
        diretors.add("아담 윈가드");
        InMovieDTO movie = new InMovieDTO("m_01", "듄2", 2024, genres, keywords, "미국", diretors, "115", "12");

        new MovieResultFrame(movie);
    }
}