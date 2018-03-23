package co.edu.udea.compumovil.gr04_20171.lab3.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import co.edu.udea.compumovil.gr04_20171.lab3.R;
import co.edu.udea.compumovil.gr04_20171.lab3.app.App;
import co.edu.udea.compumovil.gr04_20171.lab3.database.EventsDbUtil;
import co.edu.udea.compumovil.gr04_20171.lab3.models.User;
import co.edu.udea.compumovil.gr04_20171.lab3.services.Config;

import android.app.ProgressDialog;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;


public class SignupActivity extends AppCompatActivity {

    EditText nameText;
    EditText emailText;
    EditText passwordText;
    Button signupButton;
    TextView loginLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        nameText = (EditText) findViewById(R.id.register_name);
        emailText = (EditText) findViewById(R.id.register_email);
        passwordText = (EditText) findViewById(R.id.register_password);
        signupButton = (Button) findViewById(R.id.btn_signup);
        loginLink = (TextView) findViewById(R.id.link_login);


    }

    public void signupButtonOnClick(View v) {
        signup();
    }


    public void loginLinkOnClick(View v) {
        // Finish the registration screen and return to the Login activity
        finish();
    }


    public void signup() {

        if (!validate()) {
            onSignupFailed();
            return;
        }

        signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.Theme_AppCompat_DayNight_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.creating_account));
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        final String name = nameText.getText().toString();
        final String email = emailText.getText().toString();
        final String password = passwordText.getText().toString();

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {

                        String url = Config.getUserPostUrl();
                        User user = new User(name, email, password);
                        String body = new GsonBuilder().create().toJson(user);
                        Request request = new JsonRequest<String>(Request.Method.POST, url, body, new Response.Listener<String>() {

                            @Override
                            public void onResponse(String response) {
                                User user = null;
                                if ("exists".equals(response)) {
                                    onSignupFailed("Email already registered");
                                } else if ("notregistered".equals(response)) {
                                    onSignupFailed("Sign up failed");
                                } else {
                                    try {
                                        user = new GsonBuilder().create().fromJson(response, User.class);
                                    } catch (Exception e) {
                                        Log.d("ERROR", "error parsing user");
                                        onSignupFailed("Sign up failed");
                                    }
                                    onSignupSuccess();
                                }
                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {

                                onSignupFailed("Network error");
                                VolleyLog.e("Error: ", error.getMessage());
                            }
                        }) {

                            @Override
                            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                                String jsonString = null;
                                try {
                                    jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                                    Log.d("NET_RESPONSE", jsonString);
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                                Response<String> result = Response.success(jsonString, HttpHeaderParser.parseCacheHeaders(response));
                                return result;
                            }
                        };

                        Log.d("Rreuquset", request.toString());
                        ((App) getApplication()).getRequestQueueQueue().add(request);

                        progressDialog.dismiss();
                    }
                }, 3000);

    }

    public void onSignupSuccess() {
        signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed(String message) {
        signupButton.setEnabled(true);
        Snackbar snackbar = Snackbar.make(getCurrentFocus(), message, Snackbar.LENGTH_LONG);
        // Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.RED);
        snackbar.show();
    }

    public void onSignupFailed() {
        signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = nameText.getText().toString();
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            nameText.setError(getString(R.string.least_chars));
            valid = false;
        } else {
            nameText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailText.setError(getString(R.string.enter_valid_email));
            valid = false;
        } else {
            emailText.setError(null);
        }
        //if (EventsDbUtil.getUser(email, getApplicationContext()) != null) {
        //   emailText.setError(getString(R.string.email_already_registered));
        //   valid = false;
        //}
        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            passwordText.setError(getString(R.string.between_chars));
            valid = false;
        } else {
            passwordText.setError(null);
        }

        return valid;
    }
}