package de.thro.inf.prg3.a11.tests;

import de.thro.inf.prg3.a11.openmensa.OpenMensaAPI;
import de.thro.inf.prg3.a11.openmensa.OpenMensaAPIService;
import de.thro.inf.prg3.a11.openmensa.model.PageInfo;
import org.junit.jupiter.api.Test;
import retrofit2.HttpException;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Peter Kurfer
 */

class OpenMensaApiTests {

    private static final int THRO_MENSA_ID = 229;
    private static final String OPEN_MENSA_DATE_FORMAT = "yyyy-MM-dd";
    private final SimpleDateFormat dateFormat;
    private final OpenMensaAPI openMensaAPI;
    private final Calendar calendar;

    OpenMensaApiTests() {
        dateFormat = new SimpleDateFormat(OPEN_MENSA_DATE_FORMAT, Locale.getDefault());
        openMensaAPI = OpenMensaAPIService.getInstance().getOpenMensaAPI();
        calendar = Calendar.getInstance();
    }

    @Test
    void testGetFirstMensaPage() throws ExecutionException, InterruptedException {
        final var canteensResponse = openMensaAPI.getCanteens().get();

        assertNotNull(canteensResponse);
        assertNotNull(canteensResponse.body());
        assertNotEquals(0, canteensResponse.body().size());

        for (var c : canteensResponse.body()) {
            System.out.println(c.getName());
        }
    }

    @Test
    void testExtractPageInfo() throws ExecutionException, InterruptedException {
        final var canteensResponse = openMensaAPI.getCanteens().get();

        final var pageInfo = PageInfo.extractFromResponse(canteensResponse);

        assertNotNull(pageInfo);
        assertEquals(canteensResponse.body().size(), pageInfo.getItemCountPerPage());
        assertTrue(pageInfo.getTotalCountOfItems() > 0);
        assertTrue(pageInfo.getTotalCountOfPages() > 0);
        assertTrue(pageInfo.getCurrentPageIndex() > 0);
    }

    @Test
    void testGetCanteenState() throws InterruptedException {
        try {
            final var mensaState = openMensaAPI.getCanteenState(THRO_MENSA_ID, dateFormat.format(calendar.getTime())).get();
            assertNotNull(mensaState);
        }catch (ExecutionException e) {
            if(e.getCause() instanceof HttpException) {
                System.out.println(String.format("HTTP error: %s", e.getCause().getMessage()));
            }
        }
    }


    @Test
    void testGetMultiplePages() throws ExecutionException, InterruptedException {
        final var firstPage = openMensaAPI.getCanteens().get();

        assertNotNull(firstPage);
        assertNotNull(firstPage.body());

        final var pageInfo = PageInfo.extractFromResponse(firstPage);
        for(var i = 2; i <= pageInfo.getTotalCountOfPages(); i++) {
            var canteensPage = openMensaAPI.getCanteens(i).get();
            assertNotNull(canteensPage);
            assertNotEquals(0, canteensPage.size());
        }
    }
}
