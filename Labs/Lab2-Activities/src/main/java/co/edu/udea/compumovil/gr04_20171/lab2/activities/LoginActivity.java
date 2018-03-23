package co.edu.udea.compumovil.gr04_20171.lab2.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import co.edu.udea.compumovil.gr04_20171.lab2.R;
import co.edu.udea.compumovil.gr04_20171.lab2.database.EventsDbUtil;
import co.edu.udea.compumovil.gr04_20171.lab2.models.User;

public class LoginActivity extends AppCompatActivity {

    private static final int REQUEST_SIGNUP = 0;


    EditText emailText;
    EditText passwordText;
    Button loginButton;
    TextView signupLink;
    Switch loginSwitch;
    User user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = EventsDbUtil.getUserLogged(getApplicationContext());
        if (user != null) {
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
            loginSwitch = (Switch) findViewById(R.id.login_switch);
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
            onLoginFailed();
            return;
        }

        loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.Theme_AppCompat_DayNight_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.auth));
        progressDialog.show();

        final String email = emailText.getText().toString();
        String password = passwordText.getText().toString();
        boolean keepLogged = loginSwitch.isChecked();
        // TODO: Implement your own authentication logic here.
        final boolean validuser = EventsDbUtil.userLogin(email, password, keepLogged ? 1 : 0, getApplicationContext());
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        if (validuser) {
                            user = EventsDbUtil.getUser(email, getApplicationContext());
                            onLoginSuccess();
                        } else {
                            onLoginFailed();
                        }
                        progressDialog.dismiss();
                    }
                }, 1000);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                Toast.makeText(getBaseContext(), R.string.account_created, Toast.LENGTH_LONG).show();

                //this.finish();
            }
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
        intent.putExtra("USER", user);
        startActivity(intent);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), R.string.login_failed, Toast.LENGTH_LONG).show();

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

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            passwordText.setError(getString(R.string.between_chars));
            valid = false;
        } else {
            passwordText.setError(null);
        }

        return valid;
    }
}