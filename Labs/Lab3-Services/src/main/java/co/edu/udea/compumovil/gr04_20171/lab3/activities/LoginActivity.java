package co.edu.udea.compumovil.gr04_20171.lab3.activities;

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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import co.edu.udea.compumovil.gr04_20171.lab3.R;
import co.edu.udea.compumovil.gr04_20171.lab3.app.App;
import co.edu.udea.compumovil.gr04_20171.lab3.models.User;
import co.edu.udea.compumovil.gr04_20171.lab3.services.Config;

public class LoginActivity extends AppCompatActivity {

    private static final int REQUEST_SIGNUP = 0;


    EditText emailText;
    EditText passwordText;
    Button loginButton;
    TextView signupLink;
    SharedPreferences sharedPreferences;
    String email;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, MODE_PRIVATE);
        email = sharedPreferences.getString(Config.EMAIL_SHARED_PREF, null);

        if (email != null) {
            final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                    R.style.Theme_AppCompat_DayNight_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage(getString(R.string.auth));
            progressDialog.show();
            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            onLoginSuccess();
                            progressDialog.dismiss();
                        }
                    }, 1000);
        } else {
            setContentView(R.layout.activity_login);
            emailText = (EditText) findViewById(R.id.login_email);
            passwordText = (EditText) findViewById(R.id.login_password);
            loginButton = (Button) findViewById(R.id.btn_login);
            signupLink = (TextView) findViewById(R.id.link_signup);
            //loginSwitch = (Switch) findViewById(R.id.login_switch);
        }
    }

    public void loginOnClick(View v) {
        login();
    }

    public void singUpOnClick(View v) {
        // Start the Signup activity
        Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
        startActivityForResult(intent, REQUEST_SIGNUP);
    }


    public void login() {

        if (!validate()) {
            onLoginFailed(getString(R.string.login_failed));
            return;
        }

        loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.Theme_AppCompat_DayNight_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.auth));
        progressDialog.show();

        final String email = emailText.getText().toString();
        final String password = passwordText.getText().toString();

        // TODO: Implement your own authentication logic here.
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {

                        String url = Config.getUserUrl(email);

                        // Request a string response from the provided URL.
                        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        Log.d("LOGIN", "Response: " + response.toString());

                                        JsonParser jsonParser = new JsonParser();
                                        JsonObject jo = (JsonObject) jsonParser.parse(response.toString());

                                        Gson gson;
                                        gson = new Gson();
                                        User user = gson.fromJson(jo, User.class);

                                        if (null != user) {
                                            if (user.getPassword().equals(password)) {
                                                SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, MODE_PRIVATE);
                                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                                editor.putString(Config.EMAIL_SHARED_PREF, user.getEmail());
                                                editor.putString(Config.NAME_SHARED_PREF, user.getName());
                                                editor.putBoolean(Config.KEEP_SESSION_SHARED_PREF, user.getKeepSession());
                                                editor.putInt(Config.AGE_SHARED_PREF, user.getAge());
                                                editor.apply();
                                                onLoginSuccess();
                                            }

                                        } else {
                                            onLoginFailed(getString(R.string.login_failed));
                                        }
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                onLoginFailed("Check network");
                            }
                        });
                        // Add the request to the RequestQueue.
                        ((App) getApplication()).getRequestQueueQueue().add(stringRequest);

                        progressDialog.dismiss();
                    }
                }, 3000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            Snackbar.make(getCurrentFocus(), "Account registered", Snackbar.LENGTH_LONG).show();
        }

    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        //loginButton.setEnabled(true);
        Intent intent = new Intent(this, NavigationActivity.class);
        startActivity(intent);
        finish();
    }

    public void onLoginFailed(String message) {
        Snackbar.make(getCurrentFocus(), message, Snackbar.LENGTH_LONG).show();
        loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailText.setError(getString(R.string.enter_valid_email));
            valid = false;
        } else {
            emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 6 || password.length() > 10) {
            passwordText.setError(getString(R.string.between_chars));
            valid = false;
        } else {
            passwordText.setError(null);
        }

        return valid;
    }
}