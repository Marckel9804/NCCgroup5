package com.project1.group5.db.question;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class QuestionDTO {
    private String que_id;
    private String thema_name;
    private String que_str;

    QuestionDTO(){}

    QuestionDTO(String que_id, String thema_name, String que_str){
        this.que_id=que_id;
        this.thema_name=thema_name;
        this.que_str=que_str;
    }
}
