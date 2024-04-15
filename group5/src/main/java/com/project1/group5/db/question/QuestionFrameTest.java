package com.project1.group5.db.question;

public class QuestionFrameTest {


    public static void main(String[] args) {
        QuestionService qs = new QuestionService();
        QuestionDTO dto = new QuestionDTO();
        String keyword="어드벤처";
        String answer = qs.selectQuestion(keyword);
        System.out.println(answer);
    }
}
