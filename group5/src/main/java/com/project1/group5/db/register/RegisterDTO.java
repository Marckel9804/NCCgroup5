package com.project1.group5.db.register;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterDTO {

    private String user_id;
    private String username;
    private String email;
    private String phone_number;
    private String birth_date;
    private String gender;
    private String password;

    RegisterDTO() {
    }

    RegisterDTO(String user_id, String username, String email, String phone_number, String birth_date,
                String gender, String password) {
        this.user_id = user_id;
        this.username = username;
        this.email = email;
        this.phone_number = phone_number;
        this.birth_date = birth_date;
        this.gender = gender;
        this.password = password;
    }

}
