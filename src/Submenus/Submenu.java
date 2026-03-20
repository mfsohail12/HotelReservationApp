package Submenus;

public interface Submenu {
    // Ansi colour codes for coloring output in the console
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_BLUE = "\u001B[34m";

    /**
     * Displays the menu and executes requested functionality
     */
    void showMenu();
}
