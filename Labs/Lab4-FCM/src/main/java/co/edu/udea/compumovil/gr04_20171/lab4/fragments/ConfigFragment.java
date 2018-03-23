package co.edu.udea.compumovil.gr04_20171.lab4.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import co.edu.udea.compumovil.gr04_20171.lab4.R;
import co.edu.udea.compumovil.gr04_20171.lab4.database.Utils;
import co.edu.udea.compumovil.gr04_20171.lab4.models.User;
import co.edu.udea.compumovil.gr04_20171.lab4.services.Config;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link PreferenceFragment} subclass.
 */
public class ConfigFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = "ConfigFragment";
    EditTextPreference settingsName;
    //EditTextPreference settingsEmail;
    //EditTextPreference settingsPassword;
    EditTextPreference settingsAge;
    SwitchPreference settingsKeepSession;
    CheckBoxPreference settingsSendNotification;
    CheckBoxPreference settingsSoundNotification;
    SharedPreferences pref;
    EditTextPreference seconds;
    private DatabaseReference mDatabase;
    FirebaseAuth mFirebaseAuth;


    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);

        addPreferencesFromResource(R.xml.settings);
        mFirebaseAuth = FirebaseAuth.getInstance();
        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        mDatabase = Utils.getDatabase().getReference();
        pref = getPreferenceManager().getSharedPreferences();
        pref.registerOnSharedPreferenceChangeListener(this);
        //
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        settingsName = (EditTextPreference) findPreference("name");
        //settingsEmail = (EditTextPreference) findPreference("email");
        //settingsPassword = (EditTextPreference) findPreference("password");
        settingsAge = (EditTextPreference) findPreference("age");
        settingsKeepSession = (SwitchPreference) findPreference("keep_logged");
        settingsSendNotification = (CheckBoxPreference) findPreference("notification");
        settingsSoundNotification = (CheckBoxPreference) findPreference("sound");
        seconds = (EditTextPreference) findPreference("seconds");

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        settingsName.setText(pref.getString("name", ""));
        //settingsEmail.setText(pref.getString("email", ""));
        //settingsPassword.setText(pref.getString("password", ""));
        settingsAge.setText(pref.getString("age", ""));
        settingsKeepSession.setChecked(pref.getBoolean("keep_logged", true));
        settingsSendNotification.setChecked(pref.getBoolean("notification", true));
        settingsSoundNotification.setChecked(pref.getBoolean("sound", true));
        seconds.setText(pref.getString("seconds", ""));
        pref.registerOnSharedPreferenceChangeListener(this);//pref.registerOnSharedPreferenceChangeListener(listener);
    }

    @Override
    public void onPause() {
        super.onPause();
        pref.unregisterOnSharedPreferenceChangeListener(this);//pref.unregisterOnSharedPreferenceChangeListener(listener);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        if ("name".equals(key) || "age".equals(key) || "seconds".equals(key)) {
            Log.d(TAG, "Ingreso a sharedPreferenceChanged " + key + " " + sharedPreferences.getString(key, "") + ".");
        } else {

        }
        final User user = new User(pref.getString("name", ""), pref.getString("age", ""),
                pref.getBoolean("keep_logged", true), pref.getBoolean("notification", true),
                pref.getBoolean("sound", true), pref.getString("seconds", "60"));
        mDatabase.child("user").child(mFirebaseAuth.getCurrentUser().getUid()).setValue(user);
//        final String kkey = key;
//        if ("name".equals(key) || "age".equals(key) || "seconds".equals(key)) {
//            mDatabase.child("user").child(mFirebaseAuth.getCurrentUser().getUid())
//                    .addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(DataSnapshot dataSnapshot) {
//                            //Log.d(TAG, k)
//                            dataSnapshot.getRef().child(kkey).setValue(user);
//
//                        }
//
//                        @Override
//                        public void onCancelled(DatabaseError databaseError) {
//                            System.out.println("The read failed: " + databaseError.getMessage());
//                        }
//                    });
//        } else {// if ("keep_logged".equals(key) || "sound".equals(key)|| "notification".equals(key)) {
//            mDatabase.child("user").child(mFirebaseAuth.getCurrentUser().getUid())
//                    .addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(DataSnapshot dataSnapshot) {
//
//                            dataSnapshot.getRef().child(kkey).setValue(pref.getBoolean(kkey, true));
//
//                        }
//
//                        @Override
//                        public void onCancelled(DatabaseError databaseError) {
//                            System.out.println("The read failed: " + databaseError.getMessage());
//                        }
//                    });
//        }
    }
}
