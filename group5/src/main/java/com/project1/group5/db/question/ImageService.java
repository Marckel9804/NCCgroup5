package com.project1.group5.db.question;

import javax.imageio.ImageIO;
import javax.swing.*;

import com.project1.group5.db.OzoDB;

import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.*;

public class ImageService {
    String movieId;
    String imgSrc;
    public Connection con;

    public byte[] returnImage(String id) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        con = DriverManager.getConnection(OzoDB.DB_URL, OzoDB.DB_USER, OzoDB.DB_PASSWORD);

        PreparedStatement pstmt = con.prepareStatement("SELECT * FROM movie_img WHERE movie_id = ?");
        pstmt.setString(1, id);
        ResultSet rs = pstmt.executeQuery();
        byte[] img;
        if (rs.next()) {
            img = rs.getBytes("img");
            con.close();
            return img;
        }
        return null;
    }

    public void insertImages() {
        for (int i = 1; i <= 719; i++) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection con = DriverManager.getConnection(OzoDB.DB_URL, OzoDB.DB_USER, OzoDB.DB_PASSWORD);
                String path = "C:/Users/user/Desktop/poster/m_";
                String mId = "m_";
                if (i < 10) {
                    path += "0";
                    mId += "0";
                }
                mId += i;

                File image = new File(path + i + ".jpg");
                FileInputStream fis = new FileInputStream(image);

                PreparedStatement pstmt = con.prepareStatement("INSERT INTO movie_img (movie_id, img) VALUES (?, ?)");
                pstmt.setString(1, mId);
                pstmt.setBinaryStream(2, fis, (int) image.length());
                pstmt.executeUpdate();

                con.close();
                fis.close();
                System.out.println("Image inserted successfully");
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    // main 스레드로 테스트
    public static void main(String[] args) throws SQLException, ClassNotFoundException, IOException {

        ImageService is = new ImageService();
        is.insertImages();

        // byte[] imageBytes = is.returnImage("m_02");

        // ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes);
        // BufferedImage bImage = ImageIO.read(bis);

        // // BufferedImage를 ImageIcon으로 변환
        // ImageIcon imageIcon = new ImageIcon(bImage);

        // // ImageIcon을 JLabel에 설정하여 JFrame에 표시
        // JFrame frame = new JFrame();
        // JLabel label = new JLabel(imageIcon);
        // frame.getContentPane().add(label);
        // frame.pack();
        // frame.setVisible(true);
        // System.out.println("Image downloaded successfully");

        is.con.close();
    }
}
