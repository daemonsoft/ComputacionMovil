package co.edu.udea.compumovil.gr04_20171.lab2.activities;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import co.edu.udea.compumovil.gr04_20171.lab2.R;
import co.edu.udea.compumovil.gr04_20171.lab2.database.EventsDbUtil;
import co.edu.udea.compumovil.gr04_20171.lab2.fragments.AboutFragment;
import co.edu.udea.compumovil.gr04_20171.lab2.fragments.AccountFragment;
import co.edu.udea.compumovil.gr04_20171.lab2.fragments.ConfigFragment;
import co.edu.udea.compumovil.gr04_20171.lab2.fragments.DetailsFragment;
import co.edu.udea.compumovil.gr04_20171.lab2.fragments.EventCreateFragment;
import co.edu.udea.compumovil.gr04_20171.lab2.models.Event;
import co.edu.udea.compumovil.gr04_20171.lab2.models.User;

public class FragmentsActivity extends AppCompatActivity {
    User user;
    Event event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragments);
        user = getIntent().getParcelableExtra("USER");
        String action = getIntent().getAction();

        // Get a support ActionBar corresponding to this toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (action.equals("EVENT")) {
            event = getIntent().getParcelableExtra("EVENT");
            user = EventsDbUtil.getUser(event.getOwner(), getApplicationContext());
            setTitle(getString(R.string.details));
            Fragment detailsFragment = new DetailsFragment();

            Bundle bundle = new Bundle();
            bundle.putParcelable("USER", user);
            bundle.putParcelable("EVENT", event);
            detailsFragment.setArguments(bundle);

            android.app.FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

            fragmentTransaction.replace(R.id.main_frame, detailsFragment);

            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else if (action.equals("NEW_EVENT")) {

            Fragment eventCreateFragment = new EventCreateFragment();

            Bundle bundle = new Bundle();
            bundle.putParcelable("USER", user);
            eventCreateFragment.setArguments(bundle);

            setTitle(getString(R.string.new_event));
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            eventCreateFragment.setArguments(bundle);
            fragmentTransaction.replace(R.id.main_frame, eventCreateFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else if (action.equals("ACCOUNT")) {
            setTitle(getString(R.string.account));
            android.app.FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            Fragment accountFragment = new AccountFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable("USER", user);
            accountFragment.setArguments(bundle);
            fragmentTransaction.replace(R.id.main_frame, accountFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        } else if (action.equals("SETTINGS")) {
            setTitle(getString(R.string.settings));
            android.app.FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            Fragment configFragment = new ConfigFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable("USER", user);
            configFragment.setArguments(bundle);
            fragmentTransaction.replace(R.id.main_frame, configFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else if (action.equals("ABOUT")) {
            setTitle(getString(R.string.about));
            android.app.FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
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
        Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + event.getLatitude() + "," + event.getLongitude() + "(" + event.getLocation() + ")");

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
