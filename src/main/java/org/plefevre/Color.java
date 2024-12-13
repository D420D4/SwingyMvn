package org.plefevre;

public class Color {
    // Codes ANSI pour les couleurs
    public static final String RESET = "\u001B[0m";
    public static final String BLACK = "\u001B[30m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";

    // Codes ANSI pour les couleurs de fond
    public static final String BG_BLACK = "\u001B[40m";
    public static final String BG_RED = "\u001B[41m";
    public static final String BG_GREEN = "\u001B[42m";
    public static final String BG_YELLOW = "\u001B[43m";
    public static final String BG_BLUE = "\u001B[44m";
    public static final String BG_PURPLE = "\u001B[45m";
    public static final String BG_CYAN = "\u001B[46m";
    public static final String BG_WHITE = "\u001B[47m";

    public static final String BOLD = "\u001B[1m";
    public static final String BOLD_OFF = "\u001B[22m";

    public static final String[] frontColor = {BLACK, RED, GREEN, YELLOW, BLUE, PURPLE, CYAN, WHITE, BLACK};
    public static final String[] backgroundColor = {BG_BLACK, BG_RED, BG_GREEN, BG_YELLOW, BG_BLUE,
            BG_PURPLE, BG_CYAN, BG_WHITE, BG_BLACK};
}