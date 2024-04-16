package com.project1.group5.frame.board;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BoardEdit extends JFrame {
    private JLabel lblMovieName;
    private JLabel lblRating;
    private JLabel lblReview;
    private JLabel lblHashText;
    // private JLabel lblUsername; // 작성자 라벨 추가
    private JTextField tfMovieName;
    private JComboBox<Integer> cmbRating;
    private JTextArea taReview;
    private JTextField tfHashText;
    // private JTextField tfUsername; // 작성자 텍스트 필드 추가
    private JButton btnSave;
    private JButton btnCancel; // 추가된 취소 버튼

    // 데이터베이스 연결
    private static final String DB_URL = "jdbc:mysql://localhost:3306/sm";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "1234";
    private int board_id;

    BoardFrame board;

    public BoardEdit(String movieName, int rating, String review, String hashText, JFrame parentFrame,
            int boardID) {
        init();
        setDisplay();
        setData(movieName, rating, review, hashText);
        this.board_id = boardID; // boardID를 저장
        addListeners();
        board = (BoardFrame) parentFrame;
    }

    public void init() {
        lblMovieName = new JLabel("영화 제목");
        lblRating = new JLabel("평점");
        lblReview = new JLabel("리뷰");
        lblHashText = new JLabel("해시태그");
        // lblUsername = new JLabel("작성자"); // 작성자 라벨 초기화

        tfMovieName = new JTextField(20);
        cmbRating = new JComboBox<>(new Integer[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 });
        taReview = new JTextArea(10, 20);
        tfHashText = new JTextField(20);
        // tfUsername = new JTextField(20); // 작성자 텍스트 필드 초기화

        btnSave = new JButton("수정");
        btnCancel = new JButton("취소");
    }

    public void setDisplay() {
        setTitle("게시글 수정");
        JPanel panel = new JPanel(new GridLayout(0, 1));

        panel.add(lblMovieName);
        panel.add(tfMovieName);
        panel.add(lblRating);
        panel.add(cmbRating);
        panel.add(lblReview);
        panel.add(new JScrollPane(taReview));
        panel.add(lblHashText);
        panel.add(tfHashText);
        // panel.add(lblUsername); // 작성자 정보 추가
        // panel.add(tfUsername); // 작성자 정보 추가

        // 버튼 패널에 저장 버튼과 취소 버튼 추가
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);

        panel.add(buttonPanel);

        add(panel);

        setSize(800, 600);

        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
    }

    public void setData(String movieName, int rating, String review, String hashText) {
        tfMovieName.setText(movieName);
        cmbRating.setSelectedItem(rating);
        taReview.setText(review);
        tfHashText.setText(hashText);
        // tfUsername.setText(username); // 작성자 정보 설정
    }

    public void addListeners() {
        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String movieName = tfMovieName.getText();
                int rating = (int) cmbRating.getSelectedItem();
                String review = taReview.getText();
                String hashText = tfHashText.getText();
                // String username = tfUsername.getText(); // 작성자 정보 가져오기

                // DB에 수정된 데이터 업데이트
                updateMovieBoardData(movieName, review, hashText, board.mp.getName(), board_id, rating);

                // 수정 페이지 닫기
                dispose();

                // 부모 프레임 재표시
                board.updateBoardTable();
            }
        });

        // 취소 버튼 액션 리스너 추가
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 수정 페이지 닫기
                dispose();
            }
        });
    }

    public void updateMovieBoardData(String movieName, String review, String hashText, String username, int board_id,
            int rating) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            conn.setAutoCommit(false); // 자동 커밋 비활성화

            String deleteCommentsSQL = "DELETE FROM B_Comment WHERE board_id = ?";
            String updateBoardSQL = "UPDATE Board SET b_title = ?, b_review = ?, hash_text = ?, b_count = 0, rating = ?, username = ? WHERE board_id = ?";
            try (PreparedStatement deleteCommentsStmt = conn.prepareStatement(deleteCommentsSQL);
                    PreparedStatement updateBoardStmt = conn.prepareStatement(updateBoardSQL)) {
                deleteCommentsStmt.setInt(1, board_id);
                deleteCommentsStmt.executeUpdate(); // 해당 게시글의 댓글 삭제

                updateBoardStmt.setString(1, movieName);
                updateBoardStmt.setString(2, review);
                updateBoardStmt.setString(3, hashText);
                updateBoardStmt.setInt(4, rating);
                updateBoardStmt.setString(5, username);
                updateBoardStmt.setInt(6, board_id);

                updateBoardStmt.executeUpdate(); // 수정된 데이터 업데이트

                conn.commit();
                JOptionPane.showMessageDialog(BoardEdit.this, "게시글이 수정되었습니다.");
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(BoardEdit.this, "게시글 수정에 실패했습니다.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(BoardEdit.this, "데이터베이스 연결에 실패했습니다.");
        }
    }

    public static void main(String[] args) {
        // 예시 실행 코드
        SwingUtilities.invokeLater(() -> {
            JFrame parentFrame = new JFrame();
            new BoardEdit("Movie", 8, "Great movie", "#action #thriller", parentFrame, 1).setVisible(true);
        });
    }
}