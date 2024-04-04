package com.project1.group5.frame;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class BoardView extends JFrame {
    private JLabel lblMovieName;
    private JLabel lblRating;
    private JLabel lblReview;
    private JLabel lblHashText;
    private JTextField tfMovieName;
    private JTextField tfRating;
    private JTextArea taReview;
    private JTextField tfHashText;
    BoardFrame board;

    // 데이터베이스 연결
    private static final String DB_URL = "jdbc:mysql://localhost:3306/sm";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "1234";

    public BoardView(int boardID, JFrame parentFrame) {
        init();
        setDisplay();
        this.board = (BoardFrame) parentFrame;
        setMovieData(boardID);
    }
    public BoardView(int boardID) {
        init();
        setDisplay();
        setMovieData(boardID);
    }

    private void init() {
        lblMovieName = new JLabel("영화 제목:");
        lblRating = new JLabel("평점:");
        lblReview = new JLabel("리뷰:");
        lblHashText = new JLabel("해시태그:");

        tfMovieName = new JTextField(20);
        tfRating = new JTextField(20);
        taReview = new JTextArea(10, 20);
        tfHashText = new JTextField(20);
        tfMovieName.setEditable(false);
        tfRating.setEditable(false);
        taReview.setEditable(false);
        tfHashText.setEditable(false);
    }

    private void setDisplay() {
        setTitle("게시글 보기");
        JPanel panel = new JPanel(new GridLayout(0, 1));

        panel.add(lblMovieName);
        panel.add(tfMovieName);
        panel.add(lblRating);
        panel.add(tfRating);
        panel.add(lblReview);
        panel.add(new JScrollPane(taReview));
        panel.add(lblHashText);
        panel.add(tfHashText);

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

    private void setMovieData(int boardID) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT b_title, rating, b_review, hash_text FROM Board WHERE board_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, boardID);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                tfMovieName.setText(rs.getString("b_title"));
                tfRating.setText(Integer.toString(rs.getInt("rating")));
                taReview.setText(rs.getString("b_review"));
                tfHashText.setText(rs.getString("hash_text"));
            }
            board.updateBoardTable();
            board.repaint();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(BoardView.this, "게시글을 가져오는 데 실패했습니다.");
        }
    }

    // public static void main(String[] args) {
    //     SwingUtilities.invokeLater(() -> {
    //         int boardID = 1; // 원하는 게시글 ID로 설정
    //         new View(boardID).setVisible(true);
    //     });
    // }
}