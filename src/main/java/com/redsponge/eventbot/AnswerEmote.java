package com.redsponge.eventbot;

public enum AnswerEmote {

    RED(":red_circle:", "\uD83D\uDD34"),
    BLUE(":large_blue_diamond:", "\uD83D\uDD37"),
    GREEN(":green_heart:", "\uD83D\uDC9A"),
    YELLOW(":triangular_ruler:", "\uD83D\uDCD0");

    private final String discordDisplay, unicode;

    AnswerEmote(String discordDisplay, String unicode) {
        this.discordDisplay = discordDisplay;
        this.unicode = unicode;
    }

    public static AnswerEmote getByUnicode(String unicode) {
        for (AnswerEmote value : values()) {
            if(value.unicode.equals(unicode)) {
                return value;
            }
        }
        return null;
    }

    public String getDiscordDisplay() {
        return discordDisplay;
    }

    public String getUnicode() {
        return unicode;
    }
}
