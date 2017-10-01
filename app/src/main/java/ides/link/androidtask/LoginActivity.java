package ides.link.androidtask;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ides.link.androidtask.models.UserResult;
import ides.link.androidtask.network.ApiUtils;
import ides.link.androidtask.network.AppServices;
import ides.link.androidtask.utilities.CommonUtilities;
import ides.link.androidtask.utilities.Constant;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();

    @BindView(R.id.input_user_name)
    EditText userNameText;
    @BindView(R.id.input_password)
    EditText passwordText;
    @BindView(R.id.btn_login)
    Button loginButton;
    @BindView(R.id.link_register)
    TextView registerLink;
    @BindView(R.id.login_button)
    LoginButton loginFaceButton;

    AppServices mService;
    ProgressDialog progressDialog;
    CallbackManager callbackManager;

    String faceName, faceEmail;
    int faceGender;
    boolean isFaceBookLogIn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mService = ApiUtils.getAppService();
        ButterKnife.bind(this);
        intitFaceBook();
        CommonUtilities.isDeviceOnline(findViewById(android.R.id.content), this);

    }


    @OnClick(R.id.btn_login)
    public void loginAction(View view) {
        validateUserData();
    }

    @OnClick(R.id.link_register)
    public void registerAction(View view) {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    private void startLoginService(final String userName, final String password) {
        showProgressDialog();
        mService.getLogin(userName, password).enqueue(new Callback<UserResult>() {
            @Override
            public void onResponse(Call<UserResult> call, Response<UserResult> response) {
                if (response.isSuccessful()) {

                    UserResult userResult = response.body();
                    //if service is successful save user and go to main activity
                    Log.e(TAG, userResult.getSuccess() +userResult.getId() );
                    if (userResult.getSuccess().equals("ok")) {
                        saveUserData(userName, password);
                        hideProgressDialog();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                    }// if the user dose not there, chick to see if it was facebook login in, if true register the facebook user
                    else if (isFaceBookLogIn) {
                        Log.e(TAG, "startRegisterFacebookService");
                        startRegisterFacebookService(faceName, userName, password, faceEmail, faceGender);
                    }

                    if (userResult.getSuccess().equals("error")) {
                        hideProgressDialog();
                        Snackbar.make(findViewById(android.R.id.content), getString(R.string.login_error),
                                Snackbar.LENGTH_LONG).show();
                    }


                } else {
                    int statusCode = response.code();
                    Log.d(TAG, "error" + statusCode);
                    hideProgressDialog();
                }
            }

            @Override
            public void onFailure(Call<UserResult> call, Throwable t) {
                hideProgressDialog();
                Log.d(TAG, "error loading from API");

            }
        });
    }


    private void saveUserData(String userName, String password) {
        SharedPreferences.Editor editor = getSharedPreferences(Constant.MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putString(Constant.USER_NAME, userName);
        editor.putString(Constant.USER_PASSWORD, password);
        editor.apply();
    }


    public void validateUserData() {
        // keep track of data validation
        boolean valid = true;
        String userName = userNameText.getText().toString();
        String password = passwordText.getText().toString();

        if (!CommonUtilities.validateUserName(userName)) {
            userNameText.setError(getResources().getString(R.string.user_name_error));

            valid = false;
        } else {
            userNameText.setError(null);
        }
        if (!CommonUtilities.validatePassword(password)) {
            passwordText.setError(getResources().getString(R.string.password_error));
            valid = false;
        } else {
            passwordText.setError(null);
        }
        // if data is valid start login service
        if (valid) {
            startLoginService(userName, password);
        }
    }

    public void showProgressDialog() {
        progressDialog = new ProgressDialog(LoginActivity.this);
        if (!progressDialog.isShowing()) {
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage(getResources().getString(R.string.loading));
            progressDialog.show();
        }
    }


    public void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    private void intitFaceBook() {
        loginFaceButton.setReadPermissions(Arrays.asList("public_profile", "email"));
        callbackManager = CallbackManager.Factory.create();

        // Callback registration
        loginFaceButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.i(TAG, "Facebook onSuccess");

                AccessToken accessToken = loginResult.getAccessToken();

                final String uniqueFBId = loginResult.getAccessToken().getUserId();
                GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject user, GraphResponse response) {

                        try {
                            if (user != null) {
                                isFaceBookLogIn = true;
                                getDataFFacebook(user, uniqueFBId);
                                LoginManager.getInstance().logOut();
                            }
                        } catch (Exception e) {
                            Log.i(TAG, e.getMessage());
                        }
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,first_name,last_name,email,gender");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                Log.i(TAG, "Facebook onCancel");
            }

            @Override
            public void onError(FacebookException exception) {
                Log.i(TAG, "Facebook onError " + exception.getMessage());
            }
        });
    }

    private void getDataFFacebook(JSONObject user, String uniqueFBId) {
        faceName = user.optString("name");
        String first_name = user.optString("first_name");
        faceEmail = "";
        if (user.optString("email") != null)
            faceEmail = user.optString("email").toString();

        faceGender = 0;
        if (user.optString("gender").toString().equals("female"))
            faceGender = 1;

        Log.i(TAG, "name " + faceName + " first_name " + first_name +
                " email " + faceEmail + " gender" + faceGender);

        startLoginService(first_name, uniqueFBId);

    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void startRegisterFacebookService(String name, String first_name, String uniqueFBId,
                                              String email, int gender) {
        mService.getRegister(name, first_name, uniqueFBId, email, gender, "").enqueue(new Callback<UserResult>() {
            @Override
            public void onResponse(Call<UserResult> call, Response<UserResult> response) {
                if (response.isSuccessful()) {
                    UserResult userResult = response.body();
                    if (userResult.getSuccess().equals("OK")) {
                        hideProgressDialog();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }

                } else {
                    int statusCode = response.code();
                    hideProgressDialog();
                    Log.d(TAG, "error" + statusCode);
                }

            }

            @Override
            public void onFailure(Call<UserResult> call, Throwable t) {
                Log.d(TAG, "error loading from API");
                hideProgressDialog();
            }
        });
    }
}