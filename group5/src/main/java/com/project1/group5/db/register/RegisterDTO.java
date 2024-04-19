package com.project1.group5.db.register;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

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
    private int age;

    public RegisterDTO() {
    }

    public RegisterDTO(String user_id, String username, String email, String password, String phone_number,
            String birth_date,
            String gender) {
        this.user_id = user_id;
        this.username = username;
        this.email = email;
        this.phone_number = phone_number;
        this.birth_date = birth_date;
        this.gender = gender;
        this.password = password;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate birthDate = LocalDate.parse(birth_date, formatter);

        // 현재 날짜를 가져옵니다.
        LocalDate currentDate = LocalDate.now();
        // System.out.println("currd:" + currentDate);
        // 두 날짜 사이의 기간을 계산합니다.
        Period period = Period.between(birthDate, currentDate);
        // System.out.println("birth:" + birthDate);

        // 기간에서 년도를 가져와서 나이를 계산합니다.
        age = period.getYears() + 1;
        // System.out.println(age);
    }
}
