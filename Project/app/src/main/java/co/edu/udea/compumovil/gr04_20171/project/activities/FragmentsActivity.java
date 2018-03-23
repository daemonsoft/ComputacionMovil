package co.edu.udea.compumovil.gr04_20171.project.activities;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.google.firebase.database.DatabaseReference;

import co.edu.udea.compumovil.gr04_20171.project.R;
import co.edu.udea.compumovil.gr04_20171.project.fragments.NewDishFragment;
import co.edu.udea.compumovil.gr04_20171.project.fragments.NewDrinkFragment;

public class FragmentsActivity extends AppCompatActivity {
    private static final String TAG = "FragmentsActivity";
    SharedPreferences pref;
private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragments);
        String action = getIntent().getAction();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (action.equals("NEW_DISH")) {

            setTitle(getString(R.string.action_new_dish));
            Fragment newTableFragment = new NewDishFragment();

            Bundle bundle = new Bundle();
            newTableFragment.setArguments(bundle);

            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

            fragmentTransaction.replace(R.id.main_frame, newTableFragment);

            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else if (action.equals("NEW_DRINK")) {

            Fragment newDrinkFragment = new NewDrinkFragment();

            Bundle bundle = new Bundle();
            //bundle.putParcelable("USER", user);
            newDrinkFragment.setArguments(bundle);

            setTitle(getString(R.string.new_drink));
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            newDrinkFragment.setArguments(bundle);
            fragmentTransaction.replace(R.id.main_frame, newDrinkFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } /*else if (action.equals("ACCOUNT")) {
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
        }*/

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
}
