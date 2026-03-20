package Submenus;

import java.util.Scanner;

public class MakeReservationMenu implements Submenu {
    @Override
    public void showMenu() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println(ANSI_BLUE + "This is the make reservation submenu" + ANSI_RESET);
            System.out.println("Enter 0 to return to the main menu");

            int input = scanner.nextInt();

            if (input == 0) return;
        }
    }
}
