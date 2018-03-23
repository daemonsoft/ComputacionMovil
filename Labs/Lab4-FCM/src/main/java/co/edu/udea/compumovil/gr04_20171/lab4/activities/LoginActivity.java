package co.edu.udea.compumovil.gr04_20171.lab4.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import co.edu.udea.compumovil.gr04_20171.lab4.R;
import co.edu.udea.compumovil.gr04_20171.lab4.app.App;
import co.edu.udea.compumovil.gr04_20171.lab4.database.Utils;
import co.edu.udea.compumovil.gr04_20171.lab4.models.User;
import co.edu.udea.compumovil.gr04_20171.lab4.services.Config;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    private static final int REQUEST_SIGNUP = 0;
    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = "LoginActivity";

    EditText emailText;
    EditText passwordText;
    Button loginButton;
    TextView signupLink;
    SharedPreferences sharedPreferences;
    String email;
    private ProgressDialog mProgressDialog;
    private SignInButton mSignInButton;

    private GoogleApiClient mGoogleApiClient;
    private CallbackManager mCallbackManager;
    AuthCredential credential;
    private FirebaseAuth.AuthStateListener mAuthListener;
    // Firebase instance variables
    private FirebaseAuth mFirebaseAuth;
    FirebaseUser user;
    private DatabaseReference mUserDatabase;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        mUserDatabase = Utils.getDatabase().getReference("user");
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);//getSharedPreferences(Config.SHARED_PREF_NAME, MODE_PRIVATE);

//        email = sharedPreferences.getString(Config.EMAIL_SHARED_PREF, null);
//        mProgressDialog = new ProgressDialog(this);
//        if (email != null) {
//            final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
//                    R.style.Theme_AppCompat_DayNight_Dialog);
//            progressDialog.setIndeterminate(true);
//            progressDialog.setMessage(getString(R.string.auth));
//            progressDialog.show();
//            new android.os.Handler().postDelayed(
//                    new Runnable() {
//                        public void run() {
//                            onLoginSuccess();
//                            progressDialog.dismiss();
//                        }
//                    }, 1000);
        //} else {

        // Initialize FirebaseAuth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    onLoginSuccess();
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    Toast.makeText(LoginActivity.this, "signed_out", Toast.LENGTH_SHORT).show();
                }
            }
        };

        // Assign fields
        mSignInButton = (SignInButton) findViewById(R.id.google_sign_in_button);
        mSignInButton.setOnClickListener(this);

        //Facebook login

        mCallbackManager = CallbackManager.Factory.create();
        LoginButton facebookLoginButton = (LoginButton) findViewById(R.id.facebook_sign_in_button);
        facebookLoginButton.setReadPermissions("email", "public_profile");
        facebookLoginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                // ...
            }
        });

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        emailText = (EditText) findViewById(R.id.login_email);
        passwordText = (EditText) findViewById(R.id.login_password);
        loginButton = (Button) findViewById(R.id.btn_login);
        signupLink = (TextView) findViewById(R.id.link_signup);
        //loginSwitch = (Switch) findViewById(R.id.login_switch);
        //  }

    }

    private void loadRemotePreferences() {

        Log.d(TAG, " " + mUserDatabase.child(user.getUid()).toString());
        final SharedPreferences.Editor editor = sharedPreferences.edit();
//
//                                            editor.putString(Config.NAME_SHARED_PREF, mFirebaseAuth.getCurrentUser().getDisplayName());
//                                            editor.apply();
        mUserDatabase.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (null != snapshot.getValue()) {
                    Log.d(TAG, " " + snapshot.getValue().toString());
                    editor.putString(snapshot.child("name").getKey(), snapshot.child("name").getValue().toString());
                    editor.putString(snapshot.child("age").getKey(), snapshot.child("age").getValue().toString());
                    //editor.putBoolean(snapshot.child("keep_logged").getKey(), Boolean.parseBoolean(snapshot.child("keep_logged").getValue().toString()));
                    editor.putBoolean(snapshot.child("notification").getKey(), Boolean.parseBoolean(snapshot.child("notification").getValue().toString()));
                    editor.putBoolean(snapshot.child("sound").getKey(), Boolean.parseBoolean(snapshot.child("sound").getValue().toString()));
                    editor.putString(snapshot.child("seconds").getKey(), snapshot.child("seconds").getValue().toString());
                }
                editor.apply();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, databaseError.toString());
            }
        });
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);
        mProgressDialog.setMessage("Iniciando sesión");
        mProgressDialog.show();
        credential = FacebookAuthProvider.getCredential(token.getToken());
        firebaseLogin(credential, mProgressDialog);

//        mFirebaseAuth.getCurrentUser().linkWithCredential(credential)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());
//
//                        // If sign in fails, display a message to the user. If sign in succeeds
//                        // the auth state listener will be notified and logic to handle the
//                        // signed in user can be handled in the listener.
//                        if (!task.isSuccessful()) {
//                            Log.w(TAG, "signInWithCredential", task.getException());
//                            //Toast.makeText(LoginActivity.this, "Authentication failed.",
//                            //      Toast.LENGTH_SHORT).show();
//                            onLoginFailed("Authentication failed");
//                        } else {
//
//                            onLoginSuccess();
//                        }
//                        mProgressDialog.dismiss();
//
//                        // ...
//                    }
//                });
    }

    private void googleLogin() {
        Log.d(TAG, "Ingresa a google login");
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.Theme_AppCompat_DayNight_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.auth));
        progressDialog.show();
        Log.d(TAG, "firebaseAuthWithGooogle:" + acct.getId());
        credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebaseLogin(credential, progressDialog);
//        mFirebaseAuth.getCurrentUser().linkWithCredential(credential)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());
//
//                        // If sign in fails, display a message to the user. If sign in succeeds
//                        // the auth state listener will be notified and logic to handle the
//                        // signed in user can be handled in the listener.
//                        if (!task.isSuccessful()) {
//                            Log.w(TAG, "signInWithCredential", task.getException());
//                            Toast.makeText(LoginActivity.this, "Authentication failed.",
//                                    Toast.LENGTH_SHORT).show();
//                        } else {
//                            SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, MODE_PRIVATE);
//                            SharedPreferences.Editor editor = sharedPreferences.edit();
//                            editor.putString(Config.EMAIL_SHARED_PREF, mFirebaseAuth.getCurrentUser().getEmail());
//                            editor.putString(Config.NAME_SHARED_PREF, mFirebaseAuth.getCurrentUser().getDisplayName());
//                            editor.apply();
//                            onLoginSuccess();
//                        }
//                    }
//                });
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

        credential = EmailAuthProvider.getCredential(email, password);

        firebaseLogin(credential, progressDialog);

//        new android.os.Handler().postDelayed(
//                new Runnable() {
//                    public void run() {
//                        mFirebaseAuth.signInWithEmailAndPassword(email, password)
//                                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<AuthResult> task) {
//                                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
//
//                                        // If sign in fails, display a message to the user. If sign in succeeds
//                                        // the auth state listener will be notified and logic to handle the
//                                        // signed in user can be handled in the listener.
//                                        if (!task.isSuccessful()) {
//                                            onLoginFailed(getString(R.string.login_failed));
//                                        } else {
//                                            SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, MODE_PRIVATE);
//                                            SharedPreferences.Editor editor = sharedPreferences.edit();
//                                            editor.putString(Config.EMAIL_SHARED_PREF, mFirebaseAuth.getCurrentUser().getEmail());
//                                            editor.putString(Config.NAME_SHARED_PREF, mFirebaseAuth.getCurrentUser().getDisplayName());
//                                            editor.apply();
//                                            onLoginSuccess();
//                                        }
//                                    }
//                                });
//                        progressDialog.dismiss();
//                    }
//                }, 3000);
    }

    private void firebaseLogin(AuthCredential credential, final ProgressDialog progressDialog) {
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            onLoginFailed(getString(R.string.login_failed));
                        } else {
                            mUserDatabase.child(mFirebaseAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot snapshot) {
                                    if (!snapshot.exists()) {
                                        Log.d(TAG, "NOT EXISTS " + mUserDatabase.child(mFirebaseAuth.getCurrentUser().getUid()).toString());
                                        User user = new User("", "", true, true, true, "60");
                                        mUserDatabase.child(mFirebaseAuth.getCurrentUser().getUid()).setValue(user);
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                            loadRemotePreferences();
                            onLoginSuccess();
                        }
                        progressDialog.dismiss();
                    }
                });

    }

    private void firebaseLink(final AuthCredential credential, final ProgressDialog progressDialog) {
        mFirebaseAuth.getCurrentUser().linkWithCredential(credential)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            FirebaseUser prevUser = user;
                            try {
                                user = Tasks.await(mFirebaseAuth.signInWithCredential(credential)).getUser();
                            } catch (Exception e) {

                                e.printStackTrace();
                            }

                        }
                        if (null == user) {
                            onLoginFailed(getString(R.string.login_failed));
                        } else {

                            onLoginSuccess();
                        }
                        progressDialog.dismiss();
                    }
                });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Snackbar.make(getCurrentFocus(), "Account registered", Snackbar.LENGTH_LONG).show();
        }
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign-In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign-In failed
                Log.e(TAG, "Google Sign-In failed.");
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
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
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

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.google_sign_in_button) {
            googleLogin();
        }
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