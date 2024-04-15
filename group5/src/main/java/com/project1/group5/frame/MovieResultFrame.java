package com.project1.group5.frame;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;


public class MovieResultFrame extends JFrame {
    /*여기있는 이미지를 프레임에 그려줄거임.*/
    Image background = new ImageIcon("src/main/java/com/project1/group5/frame/images/ozland.png").getImage();//배경이미지
    Image poster = new ImageIcon("src/main/java/com/project1/group5/frame/images/poster.png").getImage();//포스터 샘플
    JPanel panelForGraphics;
    Graphics buffg;
    Image buffImage;


    //프레임 크기
    int f_width = 1200;
    int f_height = 600;

    //버튼 객체
    Buttons toBoard;
    Buttons regame;


    //영화 포스터 그림
    //String posterUrl="";
    //Image poster = new ImageIcon();
    /*생성자*/
    MovieResultFrame() {
        //homeframe();
        Container c = getContentPane();

        setTitle("result");
        setSize(1100, 600);//프레임의 크기
        setResizable(false);//창의 크기를 변경하지 못하게

        // 버튼 두개 생성
        toBoard = new Buttons(500, 500, "게시판으로");
        regame = new Buttons(605, 500, "다시하기");


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
                    buffg.drawImage(poster, 100, 50, 300, 400, this);
                }
            }

            public void drawBackground() {  // 배경화면 그리기
                if (buffg != null) {
                    buffg.drawImage(background, 0, 0, f_width, f_height, this);
                }
            }

            public void update(Graphics g) {
                drawBackground();
                drawPoster();
                if (buffImage != null) {
                    g.drawImage(buffImage, 0, 0, this);
                }
            }
        };

        panelForGraphics.setLayout(null);
        panelForGraphics.add(toBoard);
        panelForGraphics.add(regame);
        add(panelForGraphics);

        setBackground(new Color(30, 141, 190));

        setVisible(true);
    }

    class Buttons extends JButton {
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


    public static void main(String[] args) {
        new MovieResultFrame();
    }
}

