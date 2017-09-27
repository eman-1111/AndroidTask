package ides.link.androidtask.network;

import java.util.List;

import ides.link.androidtask.models.CountriesModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by Eman on 9/27/2017.
 */

public interface  AppServices {
    @GET("rest/v1/all")
    Call<List<CountriesModel>> getCountries();
}
