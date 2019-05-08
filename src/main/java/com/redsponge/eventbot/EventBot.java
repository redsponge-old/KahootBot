package com.redsponge.eventbot;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;

public class EventBot {

    public static void main(String[] args) throws Exception {
        JDA jda = new JDABuilder("NTc1MzEwMTIzNDI2MTE5Njgx.XNGFcg.1JmbYwVdMFRmWxGAFEAOo-44YF8")
                .addEventListener(new EventListener())
                .build();
    }

}
