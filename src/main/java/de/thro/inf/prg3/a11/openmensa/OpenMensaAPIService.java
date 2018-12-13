package de.thro.inf.prg3.a11.openmensa;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * OpenMensaAPI service
 * holds an instance of OpenMensaAPI to avoid multiple instantiations of the API
 *
 * @author Peter Kurfer
 */

public final class OpenMensaAPIService {

	/* singleton instance */
	private static final OpenMensaAPIService ourInstance = new OpenMensaAPIService();
	private final OpenMensaAPI openMensaAPI;

	private OpenMensaAPIService() {

		/* Initialize Retrofit */
		Retrofit retrofit = new Retrofit.Builder()
			.baseUrl("http://openmensa.org/api/v2/")
			.addConverterFactory(GsonConverterFactory.create())
			.build();

		openMensaAPI = retrofit.create(OpenMensaAPI.class);
	}

	/* singleton accessor */
	public static OpenMensaAPIService getInstance() {
		return ourInstance;
	}

	public OpenMensaAPI getOpenMensaAPI() {
		return openMensaAPI;
	}
}
