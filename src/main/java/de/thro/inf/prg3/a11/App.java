package de.thro.inf.prg3.a11;

import java.util.Scanner;

/**
 * @author Peter Kurfer
 * Created on 12/16/17.
 */
public class App {

	private static final Scanner inputScanner = new Scanner(System.in);

	public static void main(String[] args) {
		MenuStrategy selection ;
		/* loop while true to get back to the menu every time an action was performed */
		while (true) {
			selection = menu();
			selection.execute();
		}
	}

	/**
	 * Utility method to print menu and read the user selection
	 *
	 * @return user selection as MenuSelection
	 */
	private static MenuStrategy menu() {
		System.out.println("##########################");
		System.out.println();
		System.out.println("1) Show canteens");
		System.out.println("2) Set canteen");
		System.out.println("3) Show meals");
		System.out.println("4) Set date");
		System.out.println("5) Quit");
		System.out.println("##########################");
		System.out.println();

		switch (inputScanner.nextInt()) {
			case 1:
				return MenuStrategy.SHOW_CANTEENS;
			case 2:
				return MenuStrategy.SET_CANTEEN;
			case 3:
				return MenuStrategy.SHOW_MEALS;
			case 4:
				return MenuStrategy.SET_DATE;
			default:
				return MenuStrategy.QUIT;
		}
	}

}
