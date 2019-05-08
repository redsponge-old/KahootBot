package com.redsponge.eventbot;

import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Quiz {

    private Question[] questions;
    private int currentQuestion;
    private TextChannel channel;

    private Timer scheduler;
    private Message currentMessage;
    private int timeLeft;
    private TimerTask clock;

    private HashMap<AnswerEmote, String> answers;
    private AnswerEmote correctAnswer;

    private boolean stopped;
    private Random r = new Random();

    private HashMap<User, AnswerEmote> guesses;
    private HashMap<User, Integer> corrects;
    private QuizManager quizManager;

    public Quiz(QuizManager quizManager, TextChannel channel, Question... questions) {
        this.quizManager = quizManager;
        this.channel = channel;
        this.questions = questions;
        currentQuestion = 0;
    }

    public void setScheduler(Timer scheduler) {
        this.scheduler = scheduler;
    }

    public void start() {
        guesses = new HashMap<>();
        corrects = new HashMap<>();
        answers = new HashMap<>();

        clock = new TimerTask() {
            @Override
            public void run() {
                if(!stopped) {
                    update();
                }
            }
        };
        timeLeft = 0;
        currentQuestion = -1;
        nextQuestion();

        scheduler.scheduleAtFixedRate(clock, 0, 1000);
    }

    public void addGuess(User user, AnswerEmote emote) {
        guesses.putIfAbsent(user, emote);
    }

    private void displayReactions() {
        String[] answers = questions[currentQuestion].getAnswers();
        AnswerEmote[] emotes = AnswerEmote.values();
        for (int i = 0; i < answers.length; i++) {
            if(i == answers.length - 1) {
                currentMessage.addReaction(emotes[i].getUnicode()).queue(aVoid -> {
                    stopped = false;
                    timeLeft = 15;
                    displayQuestion();
                });
            } else {
                currentMessage.addReaction(emotes[i].getUnicode()).complete();
            }
        }
    }

    private void update() {
        if(timeLeft <= 0) {
            displayCorrectAnswer();
            try {
                stopped = true;
                saveToScoreboard();
                printScoreboard();
                Thread.sleep(3000);
                nextQuestion();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            timeLeft--;
            displayQuestion();
        }
    }

    private void saveToScoreboard() {
        for (User user : guesses.keySet()) {
            System.out.println(user + " guessed " + guesses.get(user));
            if(guesses.get(user) == correctAnswer) {
                System.out.println("CORRECT!");
                int correct = corrects.getOrDefault(user, 0);
                corrects.put(user, correct + 1);
            }
        }
    }

    private void printScoreboard() {
        ArrayList<User> top5 = new ArrayList<>(corrects.keySet());
        top5.sort(Comparator.comparingInt(o -> corrects.getOrDefault(o, 0)));
        Collections.reverse(top5);
        StringBuilder message = new StringBuilder("**__Scoreboard__**\n\n");

        for (int i = 0; i < top5.size() && i < 5; i++) {
            message.append(i + 1).append(". ").append("**").append(top5.get(i).getAsMention()).append("** - ").append(corrects.get(top5.get(i))).append("\n");
        }
        if(top5.size() == 0) {
            message.append("Nobody Got Anything Right Yet :(\n\n...");
        }
        channel.sendMessage(message.toString()).complete();
    }

    private void nextQuestion() {
        currentQuestion++;
        guesses.clear();
        stopped = true;

        if(currentQuestion == questions.length) {
            channel.sendMessage("The Quiz Is Over!").queue();
            quizManager.removeQuiz(this);
            stopped = true;
            return;
        }

        String[] answers = questions[currentQuestion].getAnswers();
        this.answers = new HashMap<>();

        List<AnswerEmote> emoteList = new ArrayList<>(Arrays.asList(AnswerEmote.values()));
        for (int i = 0; i < answers.length; i++) {
            AnswerEmote emote = emoteList.get(r.nextInt(emoteList.size()));
            this.answers.put(emote, answers[i]);
            emoteList.remove(emote);

            if(answers[i].equals(questions[currentQuestion].getCorrectAnswer())) {
                correctAnswer = emote;
            }
        }

        currentMessage = channel.sendMessage("-").complete();
        displayReactions();
    }

    private void displayCorrectAnswer() {
        if(answers != null) {
            channel.sendMessage("The Correct Answer Was **" + questions[currentQuestion].getCorrectAnswer() + "**").complete();
        }
    }

    public void displayQuestion() {
        Question current = questions[currentQuestion];
        StringBuilder message = new StringBuilder(String.format("{%2d} Question %d: %s\n", timeLeft, currentQuestion + 1, current.getQuestion()));
        AnswerEmote[] emotes = AnswerEmote.values();

        for(int i = 0; i < emotes.length; i++) {
            message.append(emotes[i].getDiscordDisplay()).append(" - ").append(answers.get(emotes[i])).append("\n");
        }

        currentMessage.editMessage(message.toString()).queue();
    }

    public MessageChannel getChannel() {
        return channel;
    }
}
