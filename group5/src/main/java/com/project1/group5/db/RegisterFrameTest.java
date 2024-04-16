package com.project1.group5.db;

public class RegisterFrameTest {
    public static void main(String[] args) {

        RegisterService rs = new RegisterService();

        RegisterDTO dto = new RegisterDTO("101010","jeon","jeon@gmail.com","010-1010-1010","2024-04-08"
        ,"Female","1234");

        rs.registerUser(dto);

    }
}
