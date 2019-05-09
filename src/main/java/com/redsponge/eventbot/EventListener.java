package com.redsponge.eventbot;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class EventListener extends ListenerAdapter {

    private QuizManager quizManager;

    public EventListener() {
        quizManager = new QuizManager();
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if(event.getMessage().getContentRaw().equals(">beginquiz") && event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            Quiz quiz = new Quiz(quizManager, event.getTextChannel(), Constants.questions);
            quizManager.addQuiz(quiz);
            quiz.start();
        }
    }

    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event) {
        if(event.getUser().isBot()) return;
        quizManager.getQuizByChannel(event.getChannel()).addGuess(event.getUser(), AnswerEmote.getByUnicode(event.getReaction().getReactionEmote().getName()));
    }
}
