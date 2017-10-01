package ides.link.androidtask;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ides.link.androidtask.models.UserResult;
import ides.link.androidtask.network.ApiUtils;
import ides.link.androidtask.network.AppServices;
import ides.link.androidtask.utilities.CommonUtilities;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.input_name)
    EditText nameText;
    @BindView(R.id.input_user_name)
    EditText userNameText;
    @BindView(R.id.input_password)
    EditText passwordText;
    @BindView(R.id.input_mail)
    EditText mailText;
    @BindView(R.id.input_mobile)
    EditText mobileText;
    @BindView(R.id.radio_gender)
    RadioGroup genderRadio;
    @BindView(R.id.btn_register)
    Button registerButton;

    ProgressDialog progressDialog;
    AppServices mService;
    private static final String TAG = LoginActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mService = ApiUtils.getAppService();
        ButterKnife.bind(this);
        CommonUtilities.isDeviceOnline(findViewById(android.R.id.content) ,this);
    }

    @OnClick(R.id.btn_register)
    public void registerAction(View view) {
       validateUserData();
    }

    private void startRegisterService(String name,String userName,String password,
                                      String email,int gender,String mobile) {
        showProgressDialog();
        mService.getRegister(name, userName, password, email, gender, mobile).enqueue(new Callback<UserResult>() {
            @Override
            public void onResponse(Call<UserResult> call, Response<UserResult> response) {
                if (response.isSuccessful()) {
                    UserResult userResult = response.body();
                    if (userResult.getSuccess().equals("OK")) {
                        hideProgressDialog();
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                        RegisterActivity.this.finish();
                    }

                } else {
                    int statusCode = response.code();
                    Log.d(TAG, "error" + statusCode);
                }

            }
            @Override
            public void onFailure(Call<UserResult> call, Throwable t) {
                Log.d(TAG, "error loading from API");

            }
        });
    }


    public void validateUserData() {
        boolean valid = true;

        String name = passwordText.getText().toString();
        String userName = userNameText.getText().toString();
        String password = passwordText.getText().toString();
        String email = passwordText.getText().toString();
        String mobile = passwordText.getText().toString();

        int selectedId = genderRadio.getCheckedRadioButtonId();
        RadioButton radioGenderButton = (RadioButton) findViewById(selectedId);
        int gender = 0;
        if (radioGenderButton.getText().equals(getResources().getString(R.string.radio_female))) {
             gender = 1;
        }
        if (!CommonUtilities.validateUserName(name)) {
            nameText.setError(getResources().getString(R.string.name_error));

            valid = false;
        } else {
            nameText.setError(null);
        }
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
        if (!CommonUtilities.validateEmail(email)) {
            mailText.setError(getResources().getString(R.string.email_error));
            valid = false;
        } else {
            mailText.setError(null);
        }
        if (valid) {
            startRegisterService(name, userName, password,email, gender, mobile);
        }



    }


    public void showProgressDialog() {
        progressDialog = new ProgressDialog(RegisterActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.show();
    }


    public void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
