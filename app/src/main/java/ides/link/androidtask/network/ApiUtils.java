package ides.link.androidtask.network;

import ides.link.androidtask.utilities.Constant;

/**
 * Created by Eman on 9/27/2017.
 */

public class ApiUtils {
    public static AppServices getAppService() {
        return RetrofitClient.getClient(Constant.BASE_URL_USER).create(AppServices.class);
    }

}
