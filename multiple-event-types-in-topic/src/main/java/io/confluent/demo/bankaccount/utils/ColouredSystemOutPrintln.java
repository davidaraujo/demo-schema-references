package io.confluent.demo.bankaccount.utils;

public class ColouredSystemOutPrintln {

    public static final String ANSI_RESET = "\u001B[0m";

    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public static final String ANSI_BRIGHT_BLACK = "\u001B[90m";
    public static final String ANSI_BRIGHT_RED = "\u001B[91m";
    public static final String ANSI_BRIGHT_GREEN = "\u001B[92m";
    public static final String ANSI_BRIGHT_YELLOW = "\u001B[93m";
    public static final String ANSI_BRIGHT_BLUE = "\u001B[94m";
    public static final String ANSI_BRIGHT_PURPLE = "\u001B[95m";
    public static final String ANSI_BRIGHT_CYAN = "\u001B[96m";
    public static final String ANSI_BRIGHT_WHITE = "\u001B[97m";

    public static final String[] FOREGROUNDS = {
            ANSI_BLACK, ANSI_RED, ANSI_GREEN, ANSI_YELLOW,
            ANSI_BLUE, ANSI_PURPLE, ANSI_CYAN, ANSI_WHITE,
            ANSI_BRIGHT_BLACK, ANSI_BRIGHT_RED, ANSI_BRIGHT_GREEN, ANSI_BRIGHT_YELLOW,
            ANSI_BRIGHT_BLUE, ANSI_BRIGHT_PURPLE, ANSI_BRIGHT_CYAN, ANSI_BRIGHT_WHITE
    };

    public static final String ANSI_BG_BLACK = "\u001B[40m";
    public static final String ANSI_BG_RED = "\u001B[41m";
    public static final String ANSI_BG_GREEN = "\u001B[42m";
    public static final String ANSI_BG_YELLOW = "\u001B[43m";
    public static final String ANSI_BG_BLUE = "\u001B[44m";
    public static final String ANSI_BG_PURPLE = "\u001B[45m";
    public static final String ANSI_BG_CYAN = "\u001B[46m";
    public static final String ANSI_BG_WHITE = "\u001B[47m";

    public static final String ANSI_BRIGHT_BG_BLACK = "\u001B[100m";
    public static final String ANSI_BRIGHT_BG_RED = "\u001B[101m";
    public static final String ANSI_BRIGHT_BG_GREEN = "\u001B[102m";
    public static final String ANSI_BRIGHT_BG_YELLOW = "\u001B[103m";
    public static final String ANSI_BRIGHT_BG_BLUE = "\u001B[104m";
    public static final String ANSI_BRIGHT_BG_PURPLE = "\u001B[105m";
    public static final String ANSI_BRIGHT_BG_CYAN = "\u001B[106m";
    public static final String ANSI_BRIGHT_BG_WHITE = "\u001B[107m";

    public static final String[] BACKGROUNDS = {
            ANSI_BG_BLACK, ANSI_BG_RED, ANSI_BG_GREEN, ANSI_BG_YELLOW,
            ANSI_BG_BLUE, ANSI_BG_PURPLE, ANSI_BG_CYAN, ANSI_BG_WHITE,
            ANSI_BRIGHT_BG_BLACK, ANSI_BRIGHT_BG_RED, ANSI_BRIGHT_BG_GREEN, ANSI_BRIGHT_BG_YELLOW,
            ANSI_BRIGHT_BG_BLUE, ANSI_BRIGHT_BG_PURPLE, ANSI_BRIGHT_BG_CYAN, ANSI_BRIGHT_BG_WHITE};

    // String boldGrayLine = "\033[1mThis is a BOLD line\033[0m";
    // String setBold = "\033[1mThis keeps it bold.";
    // String normalLine = "This is a normal (default) line";
    // String setNormal = "\033[0mThis un-bolds it.";
}