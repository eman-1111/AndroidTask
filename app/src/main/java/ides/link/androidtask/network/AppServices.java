package ides.link.androidtask.network;

import java.util.List;

import ides.link.androidtask.models.CountriesModel;
import ides.link.androidtask.models.UserResult;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by Eman on 9/27/2017.
 */

public interface AppServices {

    @GET
    public Call<List<CountriesModel>> getCountries(@Url String url);


    //"http://www.smartpan.com.sa:5551/AndriodAPI/login?username=asd&password=123"
    @POST("login")
    Call<UserResult> getLogin(@Query("username") String username,
                              @Query("password") String password);

    //http://www.smartpan.com.sa:5551/AndriodAPI/register?
    // name=eman&username=emans&password=123&mail=eman.ashour111@gmail.com&gender=1&mobile=01144332234
    @POST("register")
    Call<UserResult> getRegister(@Query("name") String name,
                                 @Query("username") String username,
                                 @Query("password") String password,
                                 @Query("mail") String mail,
                                 @Query("gender") int gender,
                                 @Query("mobile") String mobile);
}
