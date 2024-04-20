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

public class BoardEdit extends JFrame {
    private JLabel lblMovieName;
    private JLabel lblRating;
    private JLabel lblReview;
    private JLabel lblHashText;
    private JTextField tfMovieName;
    private JComboBox<Integer> cmbRating;
    private JTextArea taReview;
    private JTextField tfHashText;
    private JButton btnSave;

    // 데이터베이스 연결
    private static final String DB_URL = OzoDB.DB_URL;
    private static final String DB_USER = OzoDB.DB_USER;
    private static final String DB_PASSWORD = OzoDB.DB_PASSWORD;
    private int board_id;

    BoardFrame board;

    public BoardEdit(String movieName, int rating, String review, String hashText, JFrame parentFrame) {
        init();
        setDisplay();
        setData(movieName, rating, review, hashText);
        addListeners(parentFrame);
        // board = (Board) parentFrame;
    }

    public BoardEdit(String movieName, int rating, String review, String hashText, JFrame parentFrame, int boardID) {
        init();
        setDisplay();
        setData(movieName, rating, review, hashText);
        this.board_id = boardID; // boardID를 저장
        addListeners(parentFrame);
        board = (BoardFrame) parentFrame;
    }

    public BoardEdit(String movieName, int rating, String username, String hashText, String username1,
            BoardFrame boardFrame, int boardID) {
    }

    public void init() {
        lblMovieName = new JLabel("영화 제목:");
        lblRating = new JLabel("평점:");
        lblReview = new JLabel("리뷰:");
        lblHashText = new JLabel("해시태그:");

        tfMovieName = new JTextField(20);
        cmbRating = new JComboBox<>(new Integer[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 });
        taReview = new JTextArea(10, 20);
        tfHashText = new JTextField(20);

        btnSave = new JButton("저장");
    }

    public void setDisplay() {
        JPanel panel = new JPanel(new GridLayout(0, 1));

        panel.add(lblMovieName);
        panel.add(tfMovieName);
        panel.add(lblRating);
        panel.add(cmbRating);
        panel.add(lblReview);
        panel.add(new JScrollPane(taReview));
        panel.add(lblHashText);
        panel.add(tfHashText);
        panel.add(btnSave);

        for (Component component : panel.getComponents()) {
            if (component instanceof JLabel) {
                ((JLabel) component).setHorizontalAlignment(SwingConstants.RIGHT);
            }
        }

        add(panel);

        setSize(600, 600);

        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
    }

    public void setData(String movieName, int rating, String review, String hashText) {
        tfMovieName.setText(movieName);
        cmbRating.setSelectedItem(rating);
        taReview.setText(review);
        tfHashText.setText(hashText);
    }

    public void addListeners(JFrame parentFrame) {
        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String movieName = tfMovieName.getText();
                Integer rating = (Integer) cmbRating.getSelectedItem(); // Integer로 변경
                String review = taReview.getText();
                String hashText = tfHashText.getText();

                // DB에 수정된 데이터 업데이트
                updateMovieBoardData(movieName, review, hashText, board_id, rating); // rating을 Integer로 변경

                // 수정 페이지 닫기
                dispose();

                // 부모 프레임 재표시
                board.updateBoardTable(); // DB갖다와라
                board.repaint();
                // parentFrame.setVisible(true);
            }
        });
    }

    public void updateMovieBoardData(String movieName, String review, String hashText, int board_id, Integer rating) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "UPDATE Board SET b_title = ?, b_review = ?, hash_text = ?,b_count=0, rating = ? WHERE board_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, movieName);
            pstmt.setString(2, review);
            pstmt.setString(3, hashText);
            pstmt.setInt(4, rating); // 업데이트할 rating 값 설정
            pstmt.setInt(5, board_id); // 이 부분에서 board_id를 사용하도록 변경
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(BoardEdit.this, "게시글이 수정되었습니다.");
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(BoardEdit.this, "게시글 수정에 실패했습니다.");
        }
    }
}