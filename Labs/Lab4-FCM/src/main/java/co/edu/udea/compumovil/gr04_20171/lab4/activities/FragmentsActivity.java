package co.edu.udea.compumovil.gr04_20171.lab4.activities;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import co.edu.udea.compumovil.gr04_20171.lab4.R;
import co.edu.udea.compumovil.gr04_20171.lab4.database.Utils;
import co.edu.udea.compumovil.gr04_20171.lab4.fragments.AboutFragment;
import co.edu.udea.compumovil.gr04_20171.lab4.fragments.AccountFragment;
import co.edu.udea.compumovil.gr04_20171.lab4.fragments.ConfigFragment;
import co.edu.udea.compumovil.gr04_20171.lab4.fragments.DetailsFragment;
import co.edu.udea.compumovil.gr04_20171.lab4.fragments.EventCreateFragment;
import co.edu.udea.compumovil.gr04_20171.lab4.models.Event;
import co.edu.udea.compumovil.gr04_20171.lab4.services.Config;

public class FragmentsActivity extends AppCompatActivity {
    private static final String TAG = "FragmentsActivity";
    Event mEvent;
    SharedPreferences pref;
private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragments);
        String action = getIntent().getAction();
        pref = PreferenceManager.getDefaultSharedPreferences(this);//getPreferenceManager().getSharedPreferences();
        // Get a support ActionBar corresponding to this toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDatabase = Utils.getDatabase().getReference();
        mEvent = Utils.getEvent(pref.getString("EVENTKEY", ""));


        if (action.equals("EVENT")) {

            //user = null;//EventsDbUtil.getUser(event.getOwner(), getApplicationContext());
            setTitle(getString(R.string.details));
            Fragment detailsFragment = new DetailsFragment();

            Bundle bundle = new Bundle();
            //bundle.putParcelable("USER", user);
            //bundle.putString("EVENTKEY", getIntent().getStringExtra("EVENTKEY"));
            detailsFragment.setArguments(bundle);

            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

            fragmentTransaction.replace(R.id.main_frame, detailsFragment);

            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else if (action.equals("NEW_EVENT")) {

            Fragment eventCreateFragment = new EventCreateFragment();

            Bundle bundle = new Bundle();
            //bundle.putParcelable("USER", user);
            eventCreateFragment.setArguments(bundle);

            setTitle(getString(R.string.new_event));
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            eventCreateFragment.setArguments(bundle);
            fragmentTransaction.replace(R.id.main_frame, eventCreateFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else if (action.equals("ACCOUNT")) {
            setTitle(getString(R.string.account));
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            Fragment accountFragment = new AccountFragment();
            Bundle bundle = new Bundle();
            //bundle.putParcelable("USER", user);
            accountFragment.setArguments(bundle);
            fragmentTransaction.replace(R.id.main_frame, accountFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        } else if (action.equals("SETTINGS")) {
            setTitle(getString(R.string.settings));
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            Fragment configFragment = new ConfigFragment();
            Bundle bundle = new Bundle();
            //bundle.putParcelable("USER", user);
            configFragment.setArguments(bundle);
            fragmentTransaction.replace(R.id.main_frame, configFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else if (action.equals("ABOUT")) {
            setTitle(getString(R.string.about));
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.main_frame, new AboutFragment());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }

    }

    @Override
    public void onBackPressed() {
        finish();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent upIntent = getIntent();

                NavUtils.navigateUpTo(this, upIntent);

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onClickImage(View view) {
        // Create a Uri from an intent string. Use the result to create an Intent.
        Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + mEvent.getLatitude() + "," + mEvent.getLongitude() + "(" + mEvent.getLocation() + ")");

        // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        // Make the Intent explicit by setting the Google Maps package
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            // Attempt to start an activity that can handle the Intent
            startActivity(mapIntent);
        }
    }
}
