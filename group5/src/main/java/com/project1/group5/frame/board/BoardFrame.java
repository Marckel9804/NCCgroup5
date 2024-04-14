package com.project1.group5.frame.board;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class BoardFrame extends JFrame {
    private JTable table; // Jtable로 테이블 표시
    private DefaultTableModel tableModel; // 데이터 관리
    private JTextField searchField; // 검색어 관리
    private static final String DB_URL = "jdbc:mysql://localhost:3306/sm"; //DB연동
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "1234";

    public BoardFrame() {
        init(); // GUI 초기화 메서드
        setDisplay(); // GUI 설정 메서드
        addComponents(); // GUI 추가 메서드
        updateBoardTable(); // 테이블 업데이트 메서드

        String imgDir = "src/main/java/com/project1/group5/frame/reccommandImages/";    // 이미지 경로 설정

        table.getColumnModel().getColumn(0).setPreferredWidth(10);   // 게시글 너비 설정
        table.getColumnModel().getColumn(1).setPreferredWidth(150);
        table.getColumnModel().getColumn(2).setPreferredWidth(10);
        table.getColumnModel().getColumn(5).setPreferredWidth(10);

        // 이미지 표시
        JPanel panelImage = new JPanel(new BorderLayout());
        ImageIcon imageIcon = new ImageIcon(imgDir + "BoardFrame.png");
        JLabel imageLabel = new JLabel(imageIcon);
        panelImage.add(imageLabel, BorderLayout.CENTER);
        add(panelImage, BorderLayout.WEST);
    }

    private void init() { //
        tableModel = new DefaultTableModel() { // 테이블 모델 생성
            @Override
            public boolean isCellEditable(int rowIndex, int mColIndex) {
                return false;
            }
        };
        tableModel.addColumn("번호");
        tableModel.addColumn("영화 제목");
        tableModel.addColumn("평점");
        tableModel.addColumn("작성자");
        tableModel.addColumn("해시태그");
        tableModel.addColumn("조회수");

        table = new JTable(tableModel);
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    openView();
                }
            }
        });
    }

    private void setDisplay() {
        setTitle("영화 게시판");
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void addComponents() {
        JPanel panelButtons = new JPanel();
        JButton btnAdd = new JButton("게시글 추가");
        // 버튼 색상 및 디자인
        btnAdd.setBackground(Color.BLUE); //
        btnAdd.setForeground(Color.WHITE);
        // 버튼 모양 변경
        btnAdd.setFocusPainted(false);
        btnAdd.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25)); // 크기
        btnAdd.addActionListener(e -> new BoardAdd(BoardFrame.this).setVisible(true));

        JButton btnEdit = new JButton("게시글 수정");
        // 버튼 색상 및 디자인
        btnEdit.setBackground(Color.GREEN);
        btnEdit.setForeground(Color.WHITE);
        // 버튼 모양 변경
        btnEdit.setFocusPainted(false);
        btnEdit.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25)); // 크기 조절
        btnEdit.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                int boardID = (int) tableModel.getValueAt(selectedRow, 0);
                String movieName = (String) tableModel.getValueAt(selectedRow, 1);
                int rating = (int) tableModel.getValueAt(selectedRow, 2);
                String username = (String) tableModel.getValueAt(selectedRow, 3);
                String hashText = (String) tableModel.getValueAt(selectedRow, 4);
                new BoardEdit(movieName, rating, username, hashText, username, BoardFrame.this, boardID)
                        .setVisible(true);

            } else {
                JOptionPane.showMessageDialog(BoardFrame.this, "게시글을 선택해주세요.");
            }
        });

        JButton btnDelete = new JButton("게시글 삭제");
        // 버튼 색상 및 디자인
        btnDelete.setBackground(Color.RED);
        btnDelete.setForeground(Color.WHITE);
        // 버튼 모양 변경
        btnDelete.setFocusPainted(false);
        btnDelete.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25)); // 크기 조절
        btnDelete.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                int boardID = (int) tableModel.getValueAt(selectedRow, 0);
                deleteMovieBoardData(boardID);
                updateBoardTable();
            } else {
                JOptionPane.showMessageDialog(BoardFrame.this, "게시글을 선택해주세요.");
            }
        });

        JButton btnView = new JButton("게시글 보기");
        // 버튼 색상 및 디자인
        btnView.setBackground(Color.ORANGE);
        btnView.setForeground(Color.WHITE);
        // 버튼 모양 변경
        btnView.setFocusPainted(false);
        btnView.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25)); // 크기 조절
        btnView.addActionListener(e -> openView());

        panelButtons.add(btnAdd);
        panelButtons.add(btnEdit);
        panelButtons.add(btnDelete);
        panelButtons.add(btnView);

        JPanel panelSearch = new JPanel();
        JLabel lblSearch = new JLabel("검색어:");
        searchField = new JTextField(20);

        JButton btnSearch = new JButton("검색");
        btnSearch.setBackground(Color.BLACK);
        btnSearch.setForeground(Color.WHITE);
        searchField.addActionListener(e -> searchBoard(searchField.getText()));
        btnSearch.addActionListener(e -> searchBoard(searchField.getText()));

        panelSearch.add(lblSearch);
        panelSearch.add(searchField);
        panelSearch.add(btnSearch);

        add(panelButtons, BorderLayout.SOUTH);
        add(panelSearch, BorderLayout.NORTH);
    }

    public void updateBoardTable() {
        tableModel.setRowCount(0);
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT board_id, b_title, rating, username, hash_text, b_count FROM Board";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int boardID = rs.getInt("board_id");
                String movieName = rs.getString("b_title");
                int rating = rs.getInt("rating");
                String username = rs.getString("username");
                String hashText = rs.getString("hash_text");
                int viewCount = rs.getInt("b_count");
                Object[] row = {boardID, movieName, rating, username, hashText, viewCount};
                tableModel.addRow(row);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void deleteMovieBoardData(int boardID) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String deleteCommentsSQL = "DELETE FROM B_Comment WHERE board_id = ?";
            PreparedStatement deleteCommentsStmt = conn.prepareStatement(deleteCommentsSQL);
            deleteCommentsStmt.setInt(1, boardID);
            deleteCommentsStmt.executeUpdate();

            String deleteBoardSQL = "DELETE FROM Board WHERE board_id = ?";
            PreparedStatement deleteBoardStmt = conn.prepareStatement(deleteBoardSQL);
            deleteBoardStmt.setInt(1, boardID);
            deleteBoardStmt.executeUpdate();

            JOptionPane.showMessageDialog(BoardFrame.this, "게시글이 삭제되었습니다.");
            updateBoardTable();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(BoardFrame.this, "게시글 삭제에 실패했습니다.");
        }
    }

    private void increaseViewCount(int boardID) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sqlSelect = "SELECT b_count FROM Board WHERE board_id = ?";
            PreparedStatement pstmtSelect = conn.prepareStatement(sqlSelect);
            pstmtSelect.setInt(1, boardID);
            ResultSet rs = pstmtSelect.executeQuery();
            if (rs.next()) {
                int viewCount = rs.getInt("b_count");
                viewCount++;
                String sqlUpdate = "UPDATE Board SET b_count = ? WHERE board_id = ?";
                PreparedStatement pstmtUpdate = conn.prepareStatement(sqlUpdate);
                pstmtUpdate.setInt(1, viewCount);
                pstmtUpdate.setInt(2, boardID);
                pstmtUpdate.executeUpdate();
            } else {
                System.out.println("게시글을 찾을 수 없습니다.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void searchBoard(String keyword) {
        tableModel.setRowCount(0);
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT board_id, b_title, rating, username, hash_text, b_count FROM Board WHERE b_title LIKE ? OR hash_text LIKE ? OR b_review LIKE ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, "%" + keyword + "%");
            pstmt.setString(2, "%" + keyword + "%");
            pstmt.setString(3, "%" + keyword + "%");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int boardID = rs.getInt("board_id");
                String movieName = rs.getString("b_title");
                int rating = rs.getInt("rating");
                String username = rs.getString("username");
                if (username == null || username.isEmpty()) { // 작성자가 없는 경우 익명으로 처리
                    username = "익명";
                }
                String hashText = rs.getString("hash_text");
                int viewCount = rs.getInt("b_count");
                Object[] row = {boardID, movieName, rating, username, hashText, viewCount};
                tableModel.addRow(row);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // 메인 메서드
    public static void main(String[] args) {
        SwingUtilities.invokeLater(BoardFrame::new);
    }

    // 상세보기 메서드
    private void openView() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            int boardID = (int) tableModel.getValueAt(selectedRow, 0);
            increaseViewCount(boardID);
            new BoardView(boardID, BoardFrame.this).setVisible(true);
            updateBoardTable();
        } else {
            JOptionPane.showMessageDialog(BoardFrame.this, "게시글을 선택해주세요.");
        }
    }
}
