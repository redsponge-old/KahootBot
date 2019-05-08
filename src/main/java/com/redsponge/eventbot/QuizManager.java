package com.redsponge.eventbot;

import net.dv8tion.jda.core.entities.MessageChannel;

import java.util.ArrayList;
import java.util.Timer;

public class QuizManager {

    private ArrayList<Quiz> runningQuizzes;
    private Timer timer;

    public QuizManager() {
        runningQuizzes = new ArrayList<>();
        timer = new Timer();
    }

    public void addQuiz(Quiz quiz) {
        runningQuizzes.add(quiz);
        quiz.setScheduler(timer);
    }

    public Quiz getQuiz(int index) {
        return runningQuizzes.get(index);
    }

    public Quiz getQuizByChannel(MessageChannel channel) {
        for (Quiz runningQuiz : runningQuizzes) {
            if(runningQuiz.getChannel() == channel) {
                return runningQuiz;
            }
        }
        return null;
    }

    public void removeQuiz(Quiz quiz) {
        runningQuizzes.remove(quiz);
    }
}
