package com.hkmci.csdkms.model;

import java.util.List;

import com.hkmci.csdkms.entity.ElearningQuestion;
import com.hkmci.csdkms.entity.ElearningQuiz;

public class ElearningQuizAndQuestion {
    private ElearningQuiz quiz;
    private List<ElearningQuestion> questions;

    public ElearningQuiz getQuiz() {
        return quiz;
    }

    public void setQuiz(ElearningQuiz quiz) {
        this.quiz = quiz;
    }

    public List<ElearningQuestion> getQuestions() {
        return questions;
    }

    public void setQuestions(List<ElearningQuestion> questions) {
        this.questions = questions;
    }
}