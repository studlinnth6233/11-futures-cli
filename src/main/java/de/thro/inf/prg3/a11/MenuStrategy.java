package de.thro.inf.prg3.a11;

import de.thro.inf.prg3.a11.openmensa.OpenMensaAPI;
import de.thro.inf.prg3.a11.openmensa.OpenMensaAPIService;
import de.thro.inf.prg3.a11.openmensa.model.Canteen;
import de.thro.inf.prg3.a11.openmensa.model.Meal;
import de.thro.inf.prg3.a11.openmensa.model.PageInfo;
import de.thro.inf.prg3.a11.util.ListUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public enum MenuStrategy {

	SHOW_CANTEENS {
		@Override
		void execute() {
			System.out.print("Fetching canteens [");
			try {
				openMensaAPI.getCanteens().thenApply(response -> {
					System.out.print("#");
					PageInfo pageInfo = PageInfo.extractFromResponse(response);

					List<Canteen> allCanteens;

					/* unwrapping the response body */
					if (response.body() == null) {
						/* fallback to empty list if response body was empty */
						allCanteens = new LinkedList<>();
					} else {
						allCanteens = response.body();
					}

					/* iterate all pages
					 * 2 to including 8 because page index is not 0 indexed */
					for (int i = 2; i <= pageInfo.getTotalCountOfPages(); i++) {
						System.out.print("#");
						try {
							/* you can block this thread with `get` because we are already in a
							 * background thread because of `thenApply` */
							allCanteens.addAll(openMensaAPI.getCanteens(i).get());
						} catch (InterruptedException | ExecutionException e) {
							System.out.println("Error while retrieving canteens");
						}
					}

					System.out.println("]");
					/* sort the canteens by their id and return them */
					allCanteens.sort(Comparator.comparing(Canteen::getId));
					return allCanteens;
				}).thenAccept(canteens -> {
					/* print all canteens to STDOUT */
					for (Canteen c : canteens) {
						System.out.println(c);
					}
				}).get(); /* block the thread by calling `get` to ensure that all results are retrieved when the method is completed */
			} catch (InterruptedException e) {
				System.out.println("Something happened : " + e.getMessage());
			} catch (ExecutionException e) {
				System.out.println("Something happened : " + e.getMessage());
			}
		}
	},
	SET_CANTEEN {
		@Override
		void execute() {
			readCanteen();
		}
	},
	SHOW_MEALS {
		@Override
		void execute() {
			if (currentCanteenId < 0) {
				System.out.println("No canteen selected.");
				return;
			}

			final String dateString = dateFormat.format(currentDate.getTime());

			/* fetch the state of the canteen */
			try {
				openMensaAPI.getCanteenState(currentCanteenId, dateString).thenApply(state -> {
					/* if canteen is open fetch the meals */
					if (state != null && !state.isClosed()) {
						try {
							return openMensaAPI.getMeals(currentCanteenId, dateString).get();
						} catch (InterruptedException | ExecutionException e) {
						}
					} else {
						/* if canteen is not open - print a message and return */
						System.out.println(String.format("Seems like the canteen has closed on this date: %s", dateFormat.format(currentDate.getTime())));
					}
					return new LinkedList<Meal>();
				}).thenAccept(meals -> {
					/* print the retrieved meals to the STDOUT */
					for (Meal m : meals) {
						System.out.println(m);
					}
				}).get(); /* block the thread by calling `get` to ensure that all results are retrieved when the method is completed */
			} catch (InterruptedException e) {
				System.out.println("Something happened : " + e.getMessage());
			} catch (ExecutionException e) {
				System.out.println("Something happened : " + e.getMessage());
			}
		}
	},
	SET_DATE {
		@Override
		void execute() {
			readDate();
		}
	},
	QUIT {
		@Override
		void execute() {
			System.exit(0);
		}
	};

	abstract void execute();

	private static final String OPEN_MENSA_DATE_FORMAT = "yyyy-MM-dd";

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat(OPEN_MENSA_DATE_FORMAT, Locale.getDefault());
	private static final Scanner inputScanner = new Scanner(System.in);
	private static final OpenMensaAPI openMensaAPI = OpenMensaAPIService.getInstance().getOpenMensaAPI();
	private static final Calendar currentDate = Calendar.getInstance();
	private static int currentCanteenId = -1;

	/**
	 * Utility method to select a canteen
	 */
	protected static void readCanteen() {
		/* typical input reading pattern */
		boolean readCanteenId = false;
		do {
			try {
				System.out.println("Enter canteen id:");
				currentCanteenId = inputScanner.nextInt();
				readCanteenId = true;
			} catch (Exception e) {
				System.out.println("Sorry could not read the canteen id");
			}
		} while (!readCanteenId);
	}

	/**
	 * Utility method to read a date and update the calendar
	 */
	protected static void readDate() {
		/* typical input reading pattern */
		boolean readDate = false;
		do {
			try {
				System.out.println("Pleae enter date in the format yyyy-mm-dd:");
				Date d = dateFormat.parse(inputScanner.next());
				currentDate.setTime(d);
				readDate = true;
			} catch (ParseException p) {
				System.out.println("Sorry, the entered date could not be parsed.");
			}
		} while (!readDate);

	}
}
