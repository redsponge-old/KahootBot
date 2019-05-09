package com.redsponge.eventbot;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;

public class EventBot {

    public static void main(String[] args) throws Exception {
        JDA jda = new JDABuilder("NTc2MDQ5MjA2MjU5NjEzNjk4.XNQ1uA.CYzLf8BvEsrgcpfj5F9YxJ1A5xw")
                .addEventListener(new EventListener())
                .build();
    }

}
