package com.redsponge.eventbot;

public class Answer {

    private String answerText;
    private AnswerEmote answerEmote;

    public Answer(String answerText, AnswerEmote answerEmote) {
        this.answerText = answerText;
        this.answerEmote = answerEmote;
    }

    public String getAnswerText() {
        return answerText;
    }

    public AnswerEmote getAnswerEmote() {
        return answerEmote;
    }

    @Override
    public String toString() {
        return "Answer{" +
                "answerText='" + answerText + '\'' +
                ", answerEmote=" + answerEmote +
                '}';
    }
}
