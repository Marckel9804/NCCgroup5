package com.project1.group5.frame;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
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
    private int userId; // 유저 ID 필드 추가
    private static final String DB_URL = "jdbc:mysql://localhost:3306/sm";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "1234";

    public BoardFrame() {
        init();
        setDisplay();
        addButtons();
        addSearchField();
        updateBoardTable();
    }

    private void init() {
        tableModel = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int rowIndex, int mColIndex) {
                return false;
            }
        };
        tableModel.addColumn("게시글 번호");
        tableModel.addColumn("영화 제목");
        tableModel.addColumn("평점");
        tableModel.addColumn("리뷰");
        tableModel.addColumn("해시태그");
        tableModel.addColumn("조회수"); // 테이블에 조회수 컬럼 추가

        table = new JTable(tableModel);
                // 테이블의 선택 이벤트 리스너 추가
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    OpenView();
                }
            }
        });

    }

    private void setDisplay() {
        setTitle("게시판");
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void addButtons() {
        JPanel panelButtons = new JPanel();
        JButton btnAdd = new JButton("게시글 추가");
        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new BoardEdit("", 0, "", "", BoardFrame.this).setVisible(true);
                updateBoardTable();
            }
        });

        JButton btnEdit = new JButton("게시글 수정");
        btnEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    int boardID = (int) tableModel.getValueAt(selectedRow, 0);
                    String movieName = (String) tableModel.getValueAt(selectedRow, 1);
                    int rating = (int) tableModel.getValueAt(selectedRow, 2);
                    String review = (String) tableModel.getValueAt(selectedRow, 3);
                    String hashText = (String) tableModel.getValueAt(selectedRow, 4);
                    new BoardEdit(movieName, rating, review, hashText, BoardFrame.this, boardID).setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(BoardFrame.this, "게시글을 선택해주세요.");
                }
            }
        });

        JButton btnDelete = new JButton("게시글 삭제");
        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    int boardID = (int) tableModel.getValueAt(selectedRow, 0);
                    deleteMovieBoardData(boardID);
                    updateBoardTable();
                } else {
                    JOptionPane.showMessageDialog(BoardFrame.this, "게시글을 선택해주세요.");
                }
            }
        });

        JButton btnView = new JButton("게시글 보기");
        btnView.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                OpenView();
            }
            
        });

        panelButtons.add(btnAdd);
        panelButtons.add(btnEdit);
        panelButtons.add(btnDelete);
        panelButtons.add(btnView);

        add(panelButtons, BorderLayout.SOUTH);
    }

    

    private void addSearchField() {
        JPanel panelSearch = new JPanel();
        JLabel lblSearch = new JLabel("검색어:");
        searchField = new JTextField(20);
        JButton btnSearch = new JButton("검색");
        searchField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    String keyword = searchField.getText();
                    searchBoard(keyword);
                }
            }
            @Override
            public void keyReleased(KeyEvent e) {}
        });
        btnSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String keyword = searchField.getText();
                searchBoard(keyword);
            }
        });

        panelSearch.add(lblSearch);
        panelSearch.add(searchField);
        panelSearch.add(btnSearch);

        add(panelSearch, BorderLayout.NORTH);
    }

    public void updateBoardTable() {
        tableModel.setRowCount(0);
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT board_id, b_title, rating, b_review, hash_text, b_count FROM Board";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int boardID = rs.getInt("board_id");
                String movieName = rs.getString("b_title");
                int rating = rs.getInt("rating");
                String review = rs.getString("b_review");
                String hashText = rs.getString("hash_text");
                int viewCount = rs.getInt("b_count");
                Object[] row = {boardID, movieName, rating, review, hashText, viewCount};
                tableModel.addRow(row);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void deleteMovieBoardData(int boardID) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "DELETE FROM Board WHERE board_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, boardID);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(BoardFrame.this, "게시글이 삭제되었습니다.");
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
                viewCount++; // 조회수 증가
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
            String sql = "SELECT board_id, b_title, rating, b_review, hash_text, b_count FROM Board WHERE b_title LIKE ? or hash_text LIKE ? or b_review Like ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, "%" + keyword + "%");
            pstmt.setString(2, "%" + keyword + "%");
            pstmt.setString(3, "%" + keyword + "%");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int boardID = rs.getInt("board_id");
                String movieName = rs.getString("b_title");
                int rating = rs.getInt("rating");
                String review = rs.getString("b_review");
                String hashText = rs.getString("hash_text");
                int viewCount = rs.getInt("b_count");
                Object[] row = {boardID, movieName, rating, review, hashText, viewCount};
                tableModel.addRow(row);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private List<String> getHashtagsForBoard(int boardID) {
    List<String> hashtags = new ArrayList<>();

    try {
        Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/sm",
                "root",
                "1234"
        );

        String sql = "SELECT hash_text FROM Board WHERE board_id = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, boardID);
        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            String hashText = rs.getString("hash_text");
            // Split the hash text into individual hashtags
            String[] hashtagsArray = hashText.split("\\s+");
            for (String tag : hashtagsArray) {
                hashtags.add(tag);
            }
        }

        conn.close();

    } catch (SQLException ex) {
        ex.printStackTrace();
    }
        return hashtags;
    }
    

    public static void main(String[] args) {
        SwingUtilities.invokeLater(BoardFrame::new);
    }

    public void OpenView(){
        int selectedRow = table.getSelectedRow();
                    if (selectedRow != -1) {
                        int boardID = (int) tableModel.getValueAt(selectedRow, 0);
                        increaseViewCount(boardID); // 조회수 증가
                        new BoardView(boardID, BoardFrame.this).setVisible(true);;
                        // v.setVisible(rootPaneCheckingEnabled);
                    } else {
                        JOptionPane.showMessageDialog(BoardFrame.this, "게시글을 선택해주세요.");
                    }
    }

}

