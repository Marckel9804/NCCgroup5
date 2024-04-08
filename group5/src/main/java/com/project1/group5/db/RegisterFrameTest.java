package com.project1.group5.db;

public class RegisterFrameTest {
    public static void main(String[] args) {
        RegisterService rs = new RegisterService();
        RegisterDTO dto = new RegisterDTO();

        dto.setUser_id(8);
        dto.setUsername("jang");
        dto.setEmail("jang@gmail.com");
        dto.setPhone_number("010-8888-8888");
        dto.setBirth_date("2024-04-06");
        dto.setGender("Female");
        dto.setPassword("1234");

        rs.registerUser(dto);

    }
}
