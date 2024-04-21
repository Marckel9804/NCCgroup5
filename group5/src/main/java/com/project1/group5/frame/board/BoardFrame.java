package com.project1.group5.frame.board;

import com.project1.group5.db.OzoDB;
import com.project1.group5.frame.mainPage.MainPage;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class BoardFrame extends JFrame {
    private JTable table; // Jtable로 테이블 표시
    private DefaultTableModel tableModel; // 데이터 관리
    private JTextField searchField; // 검색어 관리
    private static final String DB_URL = OzoDB.DB_URL;
    private static final String DB_USER = OzoDB.DB_USER;
    private static final String DB_PASSWORD = OzoDB.DB_PASSWORD;

    private Timer imageTimer;
    private JLabel[] imageLabels;
    private int currentImageIndex = 0;
    private String[] imagePaths = { "aa.png", "j.png", "g.png", "cc.png", "b.png",
            "ee.png", "c.png", "dd.png", "q.png", "d.png", "e.png", "f.png", "a.png", "h.png", "r.png", "ff.png",
            "gg.png", "i.png", "m.png",
            "k.png", "l.png", "bb.png", "n.png", "o.png", "p.png" };
    String imgDir = "src/main/java/com/project1/group5/frame/boardImages/"; // 이미지 경로 설정

    String movieName;

    MainPage mp;

    public BoardFrame(MainPage mp) {
        this.mp = mp;
        init(); // GUI 초기화 메서드
        setDisplay(); // GUI 설정 메서드
        addComponents(); // GUI 추가 메서드
        updateBoardTable(); // 테이블 업데이트 메서드

        table.getColumnModel().getColumn(0).setPreferredWidth(10); // 게시글 너비 설정
        table.getColumnModel().getColumn(1).setPreferredWidth(150);
        table.getColumnModel().getColumn(2).setPreferredWidth(10);
        table.getColumnModel().getColumn(5).setPreferredWidth(10);

        JPanel panelImageLeft = new JPanel(new BorderLayout()); // 이미지 표시
        JPanel panelImageRight = new JPanel(new BorderLayout());

        imageLabels = new JLabel[imagePaths.length];// 이미지 배열 생성
        for (int i = 0; i < imagePaths.length; i++) { // JLabel -> imageLabel로 저장
            ImageIcon imageIcon = new ImageIcon(imgDir + imagePaths[i]);
            Image img = imageIcon.getImage();
            Image newImg = img.getScaledInstance(250, 400, Image.SCALE_SMOOTH);
            ImageIcon resizedImageIcon = new ImageIcon(newImg);
            imageLabels[i] = new JLabel(resizedImageIcon);
        }

        // 이미지 추가
        panelImageLeft.add(imageLabels[0], BorderLayout.CENTER);
        panelImageRight.add(imageLabels[1], BorderLayout.CENTER);
        add(panelImageLeft, BorderLayout.WEST);
        add(panelImageRight, BorderLayout.EAST);
        // 이미지 패널의 배경색 설정
        panelImageLeft.setBackground(new Color(240, 255, 240));
        panelImageRight.setBackground(new Color(240, 255, 240));

        // 이미지 변경 타이머 설정
        imageTimer = new Timer(5000, new ActionListener() { // 5초마다 변경
            @Override
            public void actionPerformed(ActionEvent e) {
                currentImageIndex = (currentImageIndex + 2) % imagePaths.length;
                updateImages();
            }
        });
        imageTimer.start();
    }

    public BoardFrame() {
        this.mp = mp;
        init();
        setDisplay();
        addComponents();
        updateBoardTable();
    }

    public void setMovieName(String mvName) {
        movieName = mvName;
    }

    public void updateImages() {
        imageLabels[currentImageIndex % 2].setIcon(new ImageIcon(new ImageIcon(imgDir + imagePaths[currentImageIndex])
                .getImage().getScaledInstance(250, 400, Image.SCALE_SMOOTH)));
        imageLabels[(currentImageIndex + 1) % 2]
                .setIcon(new ImageIcon(new ImageIcon(imgDir + imagePaths[(currentImageIndex + 1) % imagePaths.length])
                        .getImage().getScaledInstance(250, 400, Image.SCALE_SMOOTH)));
    }

    public void init() { //
        movieName = "";
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
        // 게시글 너비를 설정
        table.getColumnModel().getColumn(0).setPreferredWidth(10); // 게시글 번호 열
        table.getColumnModel().getColumn(1).setPreferredWidth(150); // 영화 제목 열
        table.getColumnModel().getColumn(2).setPreferredWidth(10); // 평점 열
        table.getColumnModel().getColumn(5).setPreferredWidth(10); // 조회수 열
    }

    public void setDisplay() {
        setTitle("영화 게시판");
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane);
        setSize(1200, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
        Font font = new Font("SansSerif", Font.BOLD, 11);
        table.setFont(font);
        // 제목열
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("SansSerif", Font.BOLD, 11));
        header.setForeground(Color.WHITE);
        header.setBackground(new Color(60, 179, 113));
        // 중앙 정렬
        DefaultTableCellRenderer renderer = (DefaultTableCellRenderer) table.getTableHeader().getDefaultRenderer();
        table.setBackground(new Color(245, 255, 250));
        renderer.setHorizontalAlignment(SwingConstants.CENTER);
        scrollPane.setBackground(new Color(240, 255, 240)); // 배경색
    }

    public void addComponents() {
        JPanel panelButtons = new JPanel();
        JButton btnAdd = new JButton("게시글 추가");
        // 버튼 색상 및 디자인
        btnAdd.setBackground(new Color(30, 144, 255));
        btnAdd.setForeground(Color.WHITE);
        // 버튼 모양 변경
        btnAdd.setFocusPainted(false);
        btnAdd.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25)); // 크기
        btnAdd.addActionListener(e -> new BoardAdd(BoardFrame.this, movieName).setVisible(true));

        JButton btnEdit = new JButton("게시글 수정");
        // 버튼 색상 및 디자인
        btnEdit.setBackground(new Color(0, 128, 0));
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
                if (mp != null && mp.getName().equals(username)) {
                    new BoardEdit(movieName, rating, "수정할 내용 작성", hashText, BoardFrame.this, boardID)
                            .setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(BoardFrame.this, "타인의 게시글을 수정할 수 없습니다.");
                }
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
                String username = (String) tableModel.getValueAt(selectedRow, 3);
                if (mp != null && mp.getName().equals(username)) {
                    deleteMovieBoardData(boardID);
                } else {
                    JOptionPane.showMessageDialog(BoardFrame.this, "타인의 게시글을 삭제할 수 없습니다.");
                }
                updateBoardTable();
            } else {
                JOptionPane.showMessageDialog(BoardFrame.this, "게시글을 선택해주세요.");
            }
        });

        JButton btnView = new JButton("게시글 보기");
        // 버튼 색상 및 디자인
        btnView.setBackground(Color.BLACK);
        btnView.setForeground(Color.WHITE);
        // 버튼 모양 변경
        btnView.setFocusPainted(false);
        btnView.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25)); // 크기 조절
        // btnView.addActionListener(e -> openView());

        JButton btnBackToMain = new JButton("뒤로가기");
        btnBackToMain.setBackground(Color.WHITE);
        btnBackToMain.setForeground(Color.BLACK);
        btnView.setFocusPainted(false);
        btnView.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25)); // 크기 조절
        btnView.addActionListener(e -> openView());
        // btnBackToMain.addActionListener(e -> new MainPage());
        btnBackToMain.addActionListener(e -> {
            dispose(); // 닫기
        });
        panelButtons.setBackground(new Color(240, 255, 240));

        // panelButtons.add(btnAdd);
        // panelButtons.add(btnEdit);
        // panelButtons.add(btnDelete);

        if (mp != null && mp.getLoginCheck()) {
            panelButtons.add(btnAdd);
            panelButtons.add(btnEdit);
            panelButtons.add(btnDelete);
        }

        panelButtons.add(btnView);
        panelButtons.add(btnBackToMain);

        JPanel panelSearch = new JPanel();
        JLabel lblSearch = new JLabel("검색어:");
        searchField = new JTextField(20);

        JButton btnSearch = new JButton("검색");
        btnSearch.setBackground(Color.DARK_GRAY);
        btnSearch.setForeground(Color.WHITE);
        searchField.addActionListener(e -> searchBoard(searchField.getText()));
        btnSearch.addActionListener(e -> searchBoard(searchField.getText()));

        panelSearch.add(lblSearch);
        panelSearch.add(searchField);
        panelSearch.add(btnSearch);
        panelSearch.setBackground(new Color(240, 255, 240));
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
                Object[] row = { boardID, movieName, rating, username, hashText, viewCount };
                tableModel.addRow(row);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void deleteMovieBoardData(int boardID) {
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

    public void increaseViewCount(int boardID) {
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

    public void searchBoard(String keyword) {
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
                Object[] row = { boardID, movieName, rating, username, hashText, viewCount };
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
    public void openView() {
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