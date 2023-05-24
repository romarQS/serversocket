package logger;

public class Logger {
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_RESET = "\u001B[30m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_PURPLE = "\u001B[35m";


    public static void info(String message) {
        System.out.println(ANSI_GREEN + message + ANSI_RESET);
    }

    public static void warn(String message) {
        System.out.println(ANSI_YELLOW + message + ANSI_RESET);
    }

    public static void error(String message) {
        System.out.println(ANSI_BLUE + message + ANSI_RESET);
    }

    public static void fatal(String message) {
        System.out.println(ANSI_PURPLE + message + ANSI_RESET);
    }

    public static void error(String message, Exception e) {
        error(String.format("%s%s%s", message, " With Exception: ", e.getClass()));
    }

    public static void fatal(String message, Exception e) {
        error(String.format("%s%s%s", message, " With Exception: ", e.getClass()));
    }
}