package com.project1.group5.frame.board;

import javax.swing.*;

import com.project1.group5.db.OzoDB;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class BoardView extends JFrame {
    private JLabel lblMovieName;
    private JLabel lblRating;
    private JLabel lblReview;
    private JLabel lblHashText;
    private JLabel lblUsername; // 작성자 정보를 표시할 라벨 추가
    private JTextField tfMovieName;
    private JTextField tfRating;
    private JTextArea taReview;
    private JTextField tfHashText;
    private JTextArea taComments; // 댓글 텍스트 영역 추가
    private JTextArea taNewComment; // 댓글 추가 텍스트 영역 추가
    private JButton btnAddComment; // 댓글 추가 버튼 추가
    private JButton btnBack; // 뒤로가기 버튼 추가
    private int boardID; // 게시글 ID 필드 추가
    private JFrame parentFrame; // 부모 프레임 변수 추가

    // 데이터베이스 연결
    private static final String DB_URL = OzoDB.DB_URL;
    private static final String DB_USER = OzoDB.DB_USER;
    private static final String DB_PASSWORD = OzoDB.DB_PASSWORD;

    public BoardView(int boardID, JFrame parentFrame) {
        this.boardID = boardID;
        this.parentFrame = parentFrame; // 부모 프레임 초기화
        init();
        setDisplay();
        setMovieData(boardID);
        fetchComments(boardID); // 댓글 데이터 가져오기
    }

    private void init() {
        lblMovieName = new JLabel("영화 제목:");
        lblRating = new JLabel("평점:");
        lblReview = new JLabel("리뷰:");
        lblHashText = new JLabel("해시태그:");
        lblUsername = new JLabel(); // 작성자 정보 표시

        tfMovieName = new JTextField(30);
        tfRating = new JTextField(30);
        taReview = new JTextArea(10, 30);
        tfHashText = new JTextField(30);
        tfMovieName.setEditable(false);
        tfRating.setEditable(false);
        taReview.setEditable(false);
        tfHashText.setEditable(false);

        taComments = new JTextArea(5, 15); // 댓글 텍스트 영역 초기화
        taComments.setEditable(false); // 댓글 텍스트 영역 편집 불가능하도록 설정

        taNewComment = new JTextArea(5, 15); // 댓글 추가 텍스트 영역 초기화
        btnAddComment = new JButton("댓글 추가"); // 댓글 추가 버튼 초기화
        btnAddComment.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // 댓글 추가 버튼 클릭 시 동작
                String newComment = taNewComment.getText();
                if (!newComment.trim().isEmpty()) {
                    addComment(newComment);
                }
            }
        });

        btnBack = new JButton("뒤로가기"); // 뒤로가기 버튼 초기화
        btnBack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                dispose();
                if (parentFrame instanceof BoardFrame) {
                    ((BoardFrame) parentFrame).setVisible(true);
                } else {
                    System.out.println("오류");
                }
            }
        });
    }

    private void setDisplay() {
        setTitle("게시글 보기");
        JPanel mainPanel = new JPanel(new GridLayout(1, 2)); // 왼쪽과 오른쪽으로 나눔

        // 왼쪽 패널 생성
        JPanel leftPanel = new JPanel(new GridBagLayout());
        GridBagConstraints leftGbc = new GridBagConstraints();
        leftGbc.gridx = 0;
        leftGbc.gridy = 0;
        leftGbc.anchor = GridBagConstraints.EAST;
        leftGbc.insets = new Insets(5, 5, 5, 5);

        leftPanel.add(lblMovieName, leftGbc);
        leftGbc.gridy++;
        leftPanel.add(lblRating, leftGbc);
        leftGbc.gridy++;
        leftPanel.add(lblReview, leftGbc);
        leftGbc.gridy++;
        leftPanel.add(lblHashText, leftGbc);

        leftGbc.gridx = 1;
        leftGbc.gridy = 0;
        leftGbc.anchor = GridBagConstraints.WEST;
        leftPanel.add(tfMovieName, leftGbc);
        leftGbc.gridy++;
        leftPanel.add(tfRating, leftGbc);
        leftGbc.gridy++;
        leftGbc.fill = GridBagConstraints.BOTH;
        leftGbc.weighty = 1.0;
        leftPanel.add(new JScrollPane(taReview), leftGbc);
        leftGbc.gridy++;
        leftGbc.weighty = 0.0;
        leftPanel.add(tfHashText, leftGbc);
        leftGbc.gridy++;
        leftPanel.add(lblUsername, leftGbc); // 작성자 정보 표시 라벨 추가

        leftGbc.gridy++;
        leftPanel.add(btnBack, leftGbc); // 뒤로가기 버튼 추가

        // 댓글 추가 패널 생성
        JPanel commentPanel = new JPanel(new BorderLayout());
        commentPanel.setBorder(BorderFactory.createTitledBorder("댓글 추가"));
        commentPanel.add(new JScrollPane(taNewComment), BorderLayout.CENTER);
        commentPanel.add(btnAddComment, BorderLayout.SOUTH);

        // 오른쪽 패널 생성
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(new JLabel("댓글"), BorderLayout.NORTH);
        rightPanel.add(new JScrollPane(taComments), BorderLayout.CENTER);
        rightPanel.add(commentPanel, BorderLayout.SOUTH); // 댓글 추가 패널 추가

        mainPanel.add(leftPanel);
        mainPanel.add(rightPanel);

        add(mainPanel);

        setSize(800, 600);

        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
    }

    private void setMovieData(int boardID) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT b_title, rating, b_review, hash_text, username FROM Board WHERE board_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, boardID);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                tfMovieName.setText(rs.getString("b_title"));
                tfRating.setText(Integer.toString(rs.getInt("rating")));
                taReview.setText(rs.getString("b_review"));
                tfHashText.setText(rs.getString("hash_text"));
                lblUsername.setText("작성자: " + rs.getString("username")); // 작성자 정보 표시
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "게시글을 가져오는 데 실패했습니다.");
        }
    }

    private void fetchComments(int boardID) {
        StringBuilder comments = new StringBuilder();
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT c_content FROM B_Comment WHERE board_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, boardID);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String comment = rs.getString("c_content");
                comments.append(comment).append("\n");
            }
            taComments.setText(comments.toString());

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "댓글을 불러오는 데 실패했습니다.");
        }
    }

    private void addComment(String comment) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "INSERT INTO B_Comment (board_id, c_content) VALUES (?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, boardID);
            pstmt.setString(2, comment);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                // 댓글 추가 성공 시, 텍스트 필드 초기화 및 댓글 다시 불러오기
                taNewComment.setText("");
                fetchComments(boardID);
            } else {
                JOptionPane.showMessageDialog(this, "댓글 추가에 실패했습니다.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "댓글 추가에 실패했습니다.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new BoardView(1, null);
            frame.setVisible(true);
        });
    }
}