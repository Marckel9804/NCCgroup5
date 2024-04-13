package com.project1.group5.frame.board;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class BoardComment extends JFrame {
    private JTextArea commentTextArea;
    private JButton submitButton;
    private int boardID;

    // 데이터베이스 연결 정보
    private static final String DB_URL = "jdbc:mysql://localhost:3306/sm";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "1234";
    public BoardComment(int boardID) {
        this.boardID = boardID;
        initComponents();
        setDisplay();
    }
    private void initComponents() {
        commentTextArea = new JTextArea(5, 30);
        submitButton = new JButton("작성");

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                submitComment();
            }
        });
    }
    private void setDisplay() {
        setTitle("댓글 작성");

        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());

        JPanel commentPanel = new JPanel();
        commentPanel.add(commentTextArea);
        commentPanel.add(submitButton);

        contentPane.add(commentPanel, BorderLayout.NORTH);

        setContentPane(contentPane);

        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }
    private void submitComment() {
        String commentText = commentTextArea.getText().trim();
        if (commentText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "댓글을 입력해주세요.");
            return;
        }
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "INSERT INTO B_Comment (board_id, c_content) VALUES (?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, boardID);
            pstmt.setString(2, commentText);
            pstmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "댓글이 등록되었습니다.");
            commentTextArea.setText(""); // 댓글 입력창 초기화


        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "댓글 등록에 실패했습니다.");
        }
    }
}
