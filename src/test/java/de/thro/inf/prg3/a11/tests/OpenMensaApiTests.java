package de.thro.inf.prg3.a11.tests;

import de.thro.inf.prg3.a11.openmensa.OpenMensaAPI;
import de.thro.inf.prg3.a11.openmensa.OpenMensaAPIService;
import de.thro.inf.prg3.a11.openmensa.model.Canteen;
import de.thro.inf.prg3.a11.openmensa.model.PageInfo;
import de.thro.inf.prg3.a11.openmensa.model.State;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import retrofit2.HttpException;
import retrofit2.Response;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Peter Kurfer
 */
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class OpenMensaApiTests
{
	private static final Logger logger = LogManager.getLogger(OpenMensaApiTests.class);
	private static final int THRO_MENSA_ID = 229;
	private static final String OPEN_MENSA_DATE_FORMAT = "yyyy-MM-dd";
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat(OPEN_MENSA_DATE_FORMAT, Locale.getDefault());

	private final OpenMensaAPI openMensaAPI;
	private final Calendar calendar;

	OpenMensaApiTests()
	{
		openMensaAPI = OpenMensaAPIService
			.getInstance()
			.getOpenMensaAPI();

		calendar = Calendar.getInstance();
	}

	@Test
	void testGetFirstMensaPage() throws ExecutionException, InterruptedException
	{
		final Response<List<Canteen>> canteensResponse = openMensaAPI.getCanteens().get();

		assertNotNull(canteensResponse);
		assertNotNull(canteensResponse.body());
		assertNotEquals(0, canteensResponse.body().size());

		for (Canteen c : canteensResponse.body())
			logger.info(c.getName());
	}

	@Test
	void testExtractPageInfo() throws ExecutionException, InterruptedException
	{
		final Response<List<Canteen>> canteensResponse = openMensaAPI.getCanteens().get();
		final PageInfo pageInfo = PageInfo.extractFromResponse(canteensResponse);

		assertNotNull(pageInfo);
		assertEquals(canteensResponse.body().size(), pageInfo.getItemCountPerPage());
		assertTrue(pageInfo.getTotalCountOfItems() > 0);
		assertTrue(pageInfo.getTotalCountOfPages() > 0);
		assertTrue(pageInfo.getCurrentPageIndex() > 0);
	}

	@Test
	void testGetCanteenState() throws InterruptedException
	{
		try
		{
			String date = dateFormat.format(calendar.getTime());
			logger.info("Fetching canteen state for date {}", date);

			final State mensaState = openMensaAPI.getCanteenState(THRO_MENSA_ID, date).get();

			assertNotNull(mensaState);
		}

		catch (ExecutionException e)
		{
			if (e.getCause() instanceof HttpException)
				logger.info("HTTP error: {}", e.getCause().getMessage());
		}
	}


	@Test
	void testGetMultiplePages() throws ExecutionException, InterruptedException
	{
		final Response<List<Canteen>> firstPage = openMensaAPI.getCanteens().get();

		assertNotNull(firstPage);
		assertNotNull(firstPage.body());

		final PageInfo pageInfo = PageInfo.extractFromResponse(firstPage);

		for (int i = 2; i <= pageInfo.getTotalCountOfPages(); i++)
		{
			List<Canteen> canteensPage = openMensaAPI.getCanteens(i).get();

			assertNotNull(canteensPage);
			assertNotEquals(0, canteensPage.size());
		}
	}
}
