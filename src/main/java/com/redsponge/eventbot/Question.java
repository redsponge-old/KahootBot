package com.redsponge.eventbot;

public class Question {

    private String question;
    private String[] wrongAnswers;
    private String correctAnswer;

    public Question(String question, String correctAnswer, String... answers) {
        this.question = question;
        this.wrongAnswers = answers;
        this.correctAnswer = correctAnswer;
    }

    public String getQuestion() {
        return question;
    }

    public String[] getWrongAnswers() {
        return wrongAnswers;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public String[] getAnswers() {
        String[] out = new String[wrongAnswers.length + 1];
        System.arraycopy(wrongAnswers, 0, out, 0, wrongAnswers.length);
        out[out.length - 1] = correctAnswer;
        return out;
    }
}
