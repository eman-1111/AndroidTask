package ides.link.androidtask.network;

/**
 * Created by Eman on 9/27/2017.
 */

public class ApiUtils {

    public static AppServices getAppService(String BASE_URL) {
        return RetrofitClient.getClient(BASE_URL).create(AppServices.class);
    }
}
