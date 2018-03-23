package co.edu.udea.compumovil.gr04_20171.project.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import co.edu.udea.compumovil.gr04_20171.project.R;
import co.edu.udea.compumovil.gr04_20171.project.database.Utils;


public class SignupActivity extends AppCompatActivity {

    EditText emailText;
    EditText passwordText;
    Button signupButton;
    TextView loginLink;
    private FirebaseAuth mFirebaseAuth;
    private static final String TAG = "SignUpActivity";
    AuthCredential credential;
    // Firebase instance variables
    FirebaseUser user;

    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mUserDatabase;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        emailText = (EditText) findViewById(R.id.register_email);
        passwordText = (EditText) findViewById(R.id.register_password);
        signupButton = (Button) findViewById(R.id.btn_signup);
        loginLink = (TextView) findViewById(R.id.link_login);


        mFirebaseAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    onLoginSuccess();
                } else {
                    // User is signed out
                    //Toast.makeText(LoginActivity.this, "signed_out", Toast.LENGTH_SHORT).show();
                }
            }
        };

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

        final String email = emailText.getText().toString();
        final String password = passwordText.getText().toString();

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        mFirebaseAuth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {

                                        // If sign in fails, display a message to the user. If sign in succeeds
                                        // the auth state listener will be notified and logic to handle the
                                        // signed in user can be handled in the listener.
                                        if (!task.isSuccessful()) {
                                            onSignupFailed("Registro fallido");
                                            progressDialog.dismiss();
                                        } else {
                                            onSignupSuccess(progressDialog);
                                        }
                                    }
                                });

                    }
                }, 1000);

    }

    public void onSignupSuccess(ProgressDialog progressDialog) {
        //signupButton.setEnabled(true);

        setResult(RESULT_OK, null);
        mUserDatabase = Utils.getDatabase().getReference("user");
        //progressDialog.show();
        progressDialog.setMessage(getString(R.string.auth));
        final String email = emailText.getText().toString();
        final String password = passwordText.getText().toString();

        // TODO: Implement your own authentication logic here.

        credential = EmailAuthProvider.getCredential(email, password);

        firebaseLogin(credential, progressDialog);
    }

    public void onLoginSuccess() {
        //loginButton.setEnabled(true);
        //int i = Database.loadRemoteData(getApplicationContext());

        // mUserDatabase.child(mFirebaseAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent();

        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void firebaseLogin(AuthCredential credential, final ProgressDialog progressDialog) {
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            //onLoginFailed(getString(R.string.login_failed));
                        } else {
                            mUserDatabase.child(mFirebaseAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot snapshot) {
                                    if (!snapshot.exists()) {
                                        mUserDatabase.child(mFirebaseAuth.getCurrentUser().getUid()).child("email").setValue(user.getEmail());
                                        mUserDatabase.child(mFirebaseAuth.getCurrentUser().getUid()).child("tableCount").setValue(0);
                                        PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                                                .edit().putInt(mFirebaseAuth.getCurrentUser().getUid(), 0).apply();
                                    } else {

                                        PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                                                .edit().putInt(mFirebaseAuth.getCurrentUser().getUid(),
                                                Integer.parseInt(snapshot.child("tableCount").getValue().toString())).apply();
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                            onLoginSuccess();
                        }
                        progressDialog.dismiss();
                    }
                });

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

    @Override
    public void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthListener);
        }
    }
}