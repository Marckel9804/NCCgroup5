package com.project1.group5.frame.board;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BoardFrame extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private static final String DB_URL = "jdbc:mysql://localhost:3306/sm";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "1234";

    public BoardFrame() {
        init();
        setDisplay();
        addComponents();
        updateBoardTable();

        // 게시글 너비를  설정
        table.getColumnModel().getColumn(0).setPreferredWidth(10); // 게시글 번호 열
        table.getColumnModel().getColumn(1).setPreferredWidth(150); // 영화 제목 열
        table.getColumnModel().getColumn(2).setPreferredWidth(10); // 평점 열
        table.getColumnModel().getColumn(5).setPreferredWidth(10); //조회수 열

    }

    private void init() {
        tableModel = new DefaultTableModel() {
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
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void addComponents() {
        JPanel panelButtons = new JPanel();
        JButton btnAdd = new JButton("게시글 추가");
        btnAdd.addActionListener(e -> new BoardAdd(BoardFrame.this).setVisible(true));

        JButton btnEdit = new JButton("게시글 수정");
        btnEdit.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                int boardID = (int) tableModel.getValueAt(selectedRow, 0);
                String movieName = (String) tableModel.getValueAt(selectedRow, 1);
                int rating = (int) tableModel.getValueAt(selectedRow, 2);
                String username = (String) tableModel.getValueAt(selectedRow, 3);
                String hashText = (String) tableModel.getValueAt(selectedRow, 4);
                new BoardEdit(movieName, rating, username, hashText, username, BoardFrame.this, boardID).setVisible(true);

            } else {
                JOptionPane.showMessageDialog(BoardFrame.this, "게시글을 선택해주세요.");
            }
        });

        JButton btnDelete = new JButton("게시글 삭제");
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
        btnView.addActionListener(e -> openView());

        panelButtons.add(btnAdd);
        panelButtons.add(btnEdit);
        panelButtons.add(btnDelete);
        panelButtons.add(btnView);

        JPanel panelSearch = new JPanel();
        JLabel lblSearch = new JLabel("검색어:");
        searchField = new JTextField(20);
        JButton btnSearch = new JButton("검색");
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

    //메인 메서드
    public static void main(String[] args) {
        SwingUtilities.invokeLater(BoardFrame::new);
    }
    //상세보기 메서드
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
