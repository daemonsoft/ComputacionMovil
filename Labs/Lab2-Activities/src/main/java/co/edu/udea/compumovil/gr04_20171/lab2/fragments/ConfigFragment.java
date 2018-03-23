package co.edu.udea.compumovil.gr04_20171.lab2.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import co.edu.udea.compumovil.gr04_20171.lab2.R;

/**
 * A simple {@link PreferenceFragment} subclass.
 */
public class ConfigFragment extends PreferenceFragment {

    EditTextPreference settingsName;
    EditTextPreference settingsEmail;
    EditTextPreference settingsPassword;
    EditTextPreference settingsAge;
    SwitchPreference settingsKeepSesion;
    CheckBoxPreference settingsSendNotification;
    CheckBoxPreference settingsSoundNotification;

    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);

        addPreferencesFromResource(R.xml.settings);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        settingsName = (EditTextPreference) findPreference("name");
        settingsEmail = (EditTextPreference) findPreference("email");
        settingsPassword = (EditTextPreference) findPreference("password");
        settingsAge = (EditTextPreference) findPreference("age");
        settingsKeepSesion = (SwitchPreference) findPreference("keep_logged");
        settingsSendNotification = (CheckBoxPreference) findPreference("notifications");
        settingsSoundNotification = (CheckBoxPreference) findPreference("sounds");

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());

        settingsName.setText( sharedPreferences.getString("name", "") );
        settingsEmail.setText( sharedPreferences.getString("email", "") );
        settingsPassword.setText( sharedPreferences.getString("password", "") );
        settingsAge.setText( sharedPreferences.getString("age", "") );
        settingsKeepSesion.setChecked(sharedPreferences.getBoolean("keep_logged", true));
        settingsSendNotification.setChecked(sharedPreferences.getBoolean("notifications", true));
        settingsSoundNotification.setChecked(sharedPreferences.getBoolean("sounds", true));
    }

    @Override
    public void onPause() {
        super.onPause();


    }
}
