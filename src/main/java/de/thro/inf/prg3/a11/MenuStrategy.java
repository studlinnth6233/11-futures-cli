package de.thro.inf.prg3.a11;

import de.thro.inf.prg3.a11.openmensa.OpenMensaAPI;
import de.thro.inf.prg3.a11.openmensa.OpenMensaAPIService;
import de.thro.inf.prg3.a11.openmensa.model.Canteen;
import de.thro.inf.prg3.a11.openmensa.model.PageInfo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutionException;

public enum MenuStrategy
{

	SHOW_CANTEENS
		{
			@Override
			void execute()
			{
				System.out.println("Fetching canteens ...");

				try
				{
					openMensaAPI.getCanteens()
						.thenApply(response ->
						{
							List<Canteen> canteens = response.body();

							int pageMax = PageInfo.extractFromResponse(response).getTotalCountOfPages();
							int pageCur = PageInfo.extractFromResponse(response).getCurrentPageIndex();

							for (int page = pageCur + 1; page < pageMax; page++)
							{
								try
								{
									canteens.addAll(openMensaAPI.getCanteens(page).get());
								}

								catch (InterruptedException | ExecutionException e)
								{
									e.printStackTrace();
								}
							}

							return canteens;
						})
						.thenAccept(canteens ->
						{
							canteens.stream()
								.map(canteen -> String.format("[%d] : %s", canteen.getId(), canteen.getName()))
								.forEach(System.out::println);
						})
						.get();
				}

				catch (InterruptedException | ExecutionException e)
				{
					e.printStackTrace();
				}
			}
		},
	SET_CANTEEN
		{
			@Override
			void execute()
			{
				readCanteen();
			}
		},
	SHOW_MEALS
		{
			@Override
			void execute()
			{
				/* TODO fetch all meals for the currently selected canteen
				 * to avoid errors retrieve at first the state of the canteen and check if the canteen is opened at the selected day
				 * don't forget to check if a canteen was selected previously! */
			}
		},
	SET_DATE
		{
			@Override
			void execute()
			{
				readDate();
			}
		},
	QUIT
		{
			@Override
			void execute()
			{
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
	protected static void readCanteen()
	{
		/* typical input reading pattern */
		boolean readCanteenId = false;
		do
		{
			try
			{
				System.out.println("Enter canteen id:");
				currentCanteenId = inputScanner.nextInt();
				readCanteenId = true;
			} catch (Exception e)
			{
				System.out.println("Sorry could not read the canteen id");
			}
		} while (!readCanteenId);
	}

	/**
	 * Utility method to read a date and update the calendar
	 */
	protected static void readDate()
	{
		/* typical input reading pattern */
		boolean readDate = false;
		do
		{
			try
			{
				System.out.println("Please enter date in the format yyyy-mm-dd:");
				Date d = dateFormat.parse(inputScanner.next());
				currentDate.setTime(d);
				readDate = true;
			} catch (ParseException p)
			{
				System.out.println("Sorry, the entered date could not be parsed.");
			}
		} while (!readDate);

	}
}
