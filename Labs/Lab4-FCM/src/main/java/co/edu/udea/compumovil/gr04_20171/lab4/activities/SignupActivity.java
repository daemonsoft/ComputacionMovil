package co.edu.udea.compumovil.gr04_20171.lab4.activities;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import co.edu.udea.compumovil.gr04_20171.lab4.R;


public class SignupActivity extends AppCompatActivity {

    EditText nameText;
    EditText emailText;
    EditText passwordText;
    Button signupButton;
    TextView loginLink;
    private FirebaseAuth mAuth;
    private static final String TAG = "SignUpActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        nameText = (EditText) findViewById(R.id.register_name);
        emailText = (EditText) findViewById(R.id.register_email);
        passwordText = (EditText) findViewById(R.id.register_password);
        signupButton = (Button) findViewById(R.id.btn_signup);
        loginLink = (TextView) findViewById(R.id.link_login);


        mAuth = FirebaseAuth.getInstance();

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
                        mAuth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                                        // If sign in fails, display a message to the user. If sign in succeeds
                                        // the auth state listener will be notified and logic to handle the
                                        // signed in user can be handled in the listener.
                                        if (!task.isSuccessful()) {
                                            onSignupFailed("Sign up failed");
                                        } else {
                                            onSignupSuccess();
                                        }
                                    }
                                });
                        progressDialog.dismiss();
                    }
                }, 3000);

    }

    public void onSignupSuccess() {
        //signupButton.setEnabled(true);

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
        if (password.isEmpty() || password.length() < 6 || password.length() > 10) {
            passwordText.setError(getString(R.string.between_chars));
            valid = false;
        } else {
            passwordText.setError(null);
        }

        return valid;
    }
}