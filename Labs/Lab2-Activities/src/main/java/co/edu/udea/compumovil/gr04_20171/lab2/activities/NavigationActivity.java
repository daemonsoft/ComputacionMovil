package co.edu.udea.compumovil.gr04_20171.lab2.activities;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import co.edu.udea.compumovil.gr04_20171.lab2.R;
import co.edu.udea.compumovil.gr04_20171.lab2.database.EventsDbUtil;
import co.edu.udea.compumovil.gr04_20171.lab2.adapters.EventsRecyclerViewAdapter;
import co.edu.udea.compumovil.gr04_20171.lab2.models.Event;
import co.edu.udea.compumovil.gr04_20171.lab2.models.User;

public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, EventsRecyclerViewAdapter.MyOnItemClickListener,
        SearchView.OnQueryTextListener, SharedPreferences.OnSharedPreferenceChangeListener {

    TextView navTextUsername;
    TextView navTextEmail;
    FloatingActionButton fab;
    User user;
    private RecyclerView mRecyclerView;
    private EventsRecyclerViewAdapter mRecyclerAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private List<Event> mEvents;

    @Override
    protected void onResume() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        pref.registerOnSharedPreferenceChangeListener(this);
        String name = pref.getString(getString(R.string.name), user.getName());
        String email = pref.getString(getString(R.string.email), user.getName());
        mEvents = EventsDbUtil.getAllEvents(getApplicationContext());
        mRecyclerAdapter.setModels(mEvents);
        mRecyclerAdapter.notifyDataSetChanged();
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = getIntent().getParcelableExtra("USER");
        setTitle(getString(R.string.events));
        setContentView(R.layout.activity_navigation);
        Resources resources = getApplicationContext().getResources();
        Uri uri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + resources.getResourcePackageName(R.drawable.nalvsmed)
                + '/' + resources.getResourceTypeName(R.drawable.nalvsmed) + '/' + resources.getResourceEntryName(R.drawable.nalvsmed));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        Resources res = getApplicationContext().getResources();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View hView = navigationView.getHeaderView(0);
        navTextUsername = (TextView) hView.findViewById(R.id.nav_header_name);
        navTextEmail = (TextView) hView.findViewById(R.id.nav_header_email);
        navTextUsername.setText(user.getName());
        navTextEmail.setText(user.getEmail());

        mRecyclerView = (RecyclerView) findViewById(R.id.list);

        //mRecyclerView.setHasFixedSize(true);

        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mEvents = EventsDbUtil.getAllEvents(getApplicationContext());
        mRecyclerAdapter = new EventsRecyclerViewAdapter(this, mEvents, getApplicationContext());
        mRecyclerView.setAdapter(mRecyclerAdapter);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        query = query.toLowerCase();
        List<Event> events = new ArrayList<>();
        for (Event e : mEvents) {
            if (e.getName().toLowerCase().lastIndexOf(query) >= 0 || e.getDescription().toLowerCase().lastIndexOf(query) >= 0) {
                events.add(e);
            }
        }
        mRecyclerAdapter.setModels(events);
        mRecyclerAdapter.notifyDataSetChanged();

        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, FragmentsActivity.class);
            intent.putExtra("USER", user);
            intent.setAction("SETTINGS");
            startActivity(intent);
        } else if (id == R.id.action_sign_out) {
            Intent intent = new Intent(this, LoginActivity.class);
            EventsDbUtil.userSignOut(user.getEmail(), getApplicationContext());
            startActivity(intent);
            finish();
        } else if (id == R.id.action_about) {
            Intent intent = new Intent(this, FragmentsActivity.class);
            intent.putExtra("USER", user);
            intent.setAction("ABOUT");
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_account) {
            Intent intent = new Intent(this, FragmentsActivity.class);
            intent.putExtra("USER", user);
            intent.setAction("ACCOUNT");

            startActivity(intent);
        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(this, FragmentsActivity.class);
            intent.putExtra("USER", user);
            intent.setAction("SETTINGS");

            startActivity(intent);
        } else if (id == R.id.nav_sign_out) {
            Intent intent = new Intent(this, LoginActivity.class);
            EventsDbUtil.userSignOut(user.getEmail(), getApplicationContext());
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_about) {
            Intent intent = new Intent(this, FragmentsActivity.class);
            intent.putExtra("USER", user);
            intent.setAction("ABOUT");

            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(EventsRecyclerViewAdapter.ViewHolder holder, Event event) {
        Intent intent = new Intent(this, FragmentsActivity.class);
        intent.putExtra("USER", user);
        intent.setAction("EVENT");
        intent.putExtra("EVENT", event);
        startActivity(intent);
    }


    public void onClickFab(View view) {
        Intent intent = new Intent(this, FragmentsActivity.class);
        intent.putExtra("USER", user);
        intent.setAction("NEW_EVENT");

        startActivity(intent);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
    }
}