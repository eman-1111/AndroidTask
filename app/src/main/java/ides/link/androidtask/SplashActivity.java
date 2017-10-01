package ides.link.androidtask;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import ides.link.androidtask.models.UserResult;
import ides.link.androidtask.network.ApiUtils;
import ides.link.androidtask.network.AppServices;
import ides.link.androidtask.utilities.Constant;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = SplashActivity.class.getSimpleName();
    private static final int SPLASH_DISPLAY_LENGTH = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getUserData();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }


    private void getUserData() {
        SharedPreferences prefs = getSharedPreferences(Constant.MY_PREFS_NAME, MODE_PRIVATE);

        String userName = prefs.getString(Constant.USER_NAME, "");
        String password = prefs.getString(Constant.USER_PASSWORD, "");
        Log.e(TAG, userName + password);
        if (!userName.equals("")) {
            startLoginService(userName, password);
        } else {
            openActivity(LoginActivity.class);
        }


    }

    private void startLoginService(final String userName, final String password) {
        AppServices mService = ApiUtils.getAppService();
        mService.getLogin(userName, password).enqueue(new Callback<UserResult>() {
            @Override
            public void onResponse(Call<UserResult> call, Response<UserResult> response) {
                if (response.isSuccessful()) {

                    UserResult userResult = response.body();
                    if (userResult.getSuccess().equals("ok")) {

                        openActivity(MainActivity.class);
                    }
                } else {
                    int statusCode = response.code();
                    Log.d(TAG, "error" + statusCode);
                    openActivity(LoginActivity.class);
                }
            }
            @Override
            public void onFailure(Call<UserResult> call, Throwable t) {
                Log.d(TAG, "error loading from API");
                openActivity(LoginActivity.class);

            }
        });
    }

    private void openActivity(Class mainActivityClass) {
        Intent intent = new Intent(SplashActivity.this, mainActivityClass);
        startActivity(intent);
        SplashActivity.this.finish();
    }


}
