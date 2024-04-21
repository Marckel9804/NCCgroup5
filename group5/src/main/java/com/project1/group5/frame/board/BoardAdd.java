package com.project1.group5.frame.board;

import javax.swing.*;

import com.project1.group5.db.OzoDB;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BoardAdd extends JFrame {

    private JLabel lblMovieName;
    private JLabel lblRating;
    private JLabel lblReview;
    private JLabel lblHashText;
    private JLabel lblUsername; // 새로운 라벨 추가
    private JTextField tfMovieName;
    private JComboBox<Integer> cmbRating;
    private JTextArea taReview;
    private JTextField tfHashText;
    private JTextField tfUsername; // 새로운 텍스트 필드 추가
    private JButton btnSave;
    private JButton btnCancel; // 추가된 취소 버튼

    // 데이터베이스 연결
    private static final String DB_URL = OzoDB.DB_URL;;
    private static final String DB_USER = OzoDB.DB_USER;;
    private static final String DB_PASSWORD = OzoDB.DB_PASSWORD;;

    BoardFrame board;
    String movieName;

    public BoardAdd(JFrame parentFrame, String mvName) {
        movieName = mvName;
        init();
        setDisplay();
        addListeners(parentFrame);
        board = (BoardFrame) parentFrame;
    }

    public BoardAdd() {
        init();
        setDisplay();
        addListeners();
    }

    private void init() {
        lblMovieName = new JLabel("영화 제목");
        lblRating = new JLabel("평점");
        lblReview = new JLabel("리뷰");
        lblHashText = new JLabel("해시태그");
        lblUsername = new JLabel("작성자"); // 새로운 라벨 초기화

        tfMovieName = new JTextField(movieName, 10);
        cmbRating = new JComboBox<>(new Integer[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 });
        taReview = new JTextArea(10, 10);
        tfHashText = new JTextField(10);
        tfUsername = new JTextField(10); // 새로운 텍스트 필드 초기화

        btnSave = new JButton("추가");
        btnCancel = new JButton("취소"); // 취소 버튼 초기화

    }

    private void setDisplay() {
        setTitle("게시글 추가");

        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 5)); // 폼 입력 필드를 위한 패널, 간격 조정
        JPanel buttonPanel = new JPanel(); // 버튼을 위한 패널

        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // 폼 입력 필드 패널의 여백 설정

        formPanel.add(lblMovieName);
        formPanel.add(tfMovieName);
        formPanel.add(lblRating);
        formPanel.add(cmbRating);
        formPanel.add(lblReview);
        formPanel.add(new JScrollPane(taReview));
        formPanel.add(lblHashText);
        formPanel.add(tfHashText);
        // formPanel.add(lblUsername);
        // formPanel.add(tfUsername);

        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel); // 취소 버튼 추가

        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        setSize(800, 600);

        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
    }

    public void addListeners(JFrame parentFrame) {
        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String movieName = tfMovieName.getText();
                Integer rating = (Integer) cmbRating.getSelectedItem();
                String review = taReview.getText();
                String hashText = tfHashText.getText();
                String username = board.mp.getName(); // 새로운 사용자 이름 가져오기

                // 새로운 게시글 추가
                addNewBoard(movieName, rating, review, hashText, username);

                // 창 닫기
                dispose();
            }
        });

        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 창 닫기
                dispose();
            }
        });
    }

    private void addListeners() {
        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String movieName = tfMovieName.getText();
                Integer rating = (Integer) cmbRating.getSelectedItem();
                String review = taReview.getText();
                String hashText = tfHashText.getText();
                String username = tfUsername.getText(); // 새로운 사용자 이름 가져오기

                // 새로운 게시글 추가
                addNewBoard(movieName, rating, review, hashText, username);

                // 창 닫기
                dispose();
            }
        });

        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 창 닫기
                dispose();
            }
        });
    }

    public void addNewBoard(String movieName, int rating, String review, String hashText, String username) {
        if (username.isEmpty()) {
            username = "익명";
        }
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "INSERT INTO Board (b_title, rating, b_review, hash_text, username) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, movieName);
            pstmt.setInt(2, rating);
            pstmt.setString(3, review);
            pstmt.setString(4, hashText);
            pstmt.setString(5, username);
            pstmt.executeUpdate();
            board.updateBoardTable();
            JOptionPane.showMessageDialog(BoardAdd.this, "새로운 게시글이 추가되었습니다.");
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(BoardAdd.this, "게시글 추가에 실패했습니다.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(BoardAdd::new);
    }
}
