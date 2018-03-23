package co.edu.udea.compumovil.gr04_20171.lab4.activities;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import co.edu.udea.compumovil.gr04_20171.lab4.R;
import co.edu.udea.compumovil.gr04_20171.lab4.adapters.FirebaseEventsRecyclerViewAdapter;
import co.edu.udea.compumovil.gr04_20171.lab4.database.Utils;
import co.edu.udea.compumovil.gr04_20171.lab4.models.Event;
import co.edu.udea.compumovil.gr04_20171.lab4.services.Config;

public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        SearchView.OnQueryTextListener, FirebaseEventsRecyclerViewAdapter.MyOnItemClickListener, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "NavigationActivity";
    private static final String EVENTS_CHILD = "event";
    TextView navTextUsername;
    TextView navTextEmail;
    FloatingActionButton fab;
    private RecyclerView mRecyclerView;
    //private EventsRecyclerViewAdapter mRecyclerAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private List<Event> mEvents;
    private GoogleApiClient mGoogleApiClient;
    FirebaseAuth mFirebaseAuth;
    SharedPreferences pref;
    private DatabaseReference mDatabase;
    SharedPreferences.OnSharedPreferenceChangeListener listener;
    private FirebaseEventsRecyclerViewAdapter mFirebaseAdapter;

    @Override
    protected void onResume() {

        //mEvents = EventsDbUtil.getAllEvents(getApplicationContext());
        //mRecyclerAdapter.setModels(mEvents);
        //mRecyclerAdapter.notifyDataSetChanged();
        //PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.events));
        setContentView(R.layout.activity_navigation);

        mFirebaseAuth = FirebaseAuth.getInstance();
        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        mDatabase = Utils.getDatabase().getReference();
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
        pref = PreferenceManager.getDefaultSharedPreferences(this);//getSharedPreferences(Config.SHARED_PREF_NAME, MODE_PRIVATE);

//        listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
//            @Override
//            public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
//
//            }
//        };
//
        //pref.registerOnSharedPreferenceChangeListener(listener);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View hView = navigationView.getHeaderView(0);
        navTextUsername = (TextView) hView.findViewById(R.id.nav_header_name);
        navTextEmail = (TextView) hView.findViewById(R.id.nav_header_email);
        navTextUsername.setText(pref.getString(Config.NAME_SHARED_PREF, ""));
        navTextEmail.setText(pref.getString(Config.EMAIL_SHARED_PREF, ""));

        mFirebaseAdapter = new FirebaseEventsRecyclerViewAdapter(
                Event.class,
                R.layout.fragment_events,
                FirebaseEventsRecyclerViewAdapter.EventViewHolder.class,
                mDatabase.child(EVENTS_CHILD));
        mFirebaseAdapter.setListener(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.list);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();
        //mRecyclerView.setHasFixedSize(true);

        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        //mRecyclerAdapter = new EventsRecyclerViewAdapter(this, mEvents, getApplicationContext());
        mRecyclerView.setAdapter(mFirebaseAdapter);
        //updateLocalEvents(false);

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
        Log.d(TAG, "onQueryTextChange " + query);
        //query = query.toLowerCase();
        Log.d(TAG, "mDatabase ");
        //mDatabase.child(query)..limitToLast(1);
        Log.d(TAG, "mDatabase " + mDatabase.toString());

        mFirebaseAdapter.cleanup();
        mFirebaseAdapter = new FirebaseEventsRecyclerViewAdapter(
                Event.class,
                R.layout.fragment_events,
                FirebaseEventsRecyclerViewAdapter.EventViewHolder.class,
                mDatabase.child(EVENTS_CHILD).orderByChild("name").startAt(query).endAt(query + "\uf8ff"));

        //mFirebaseAdapter.setQueryReference(query);
        //mFirebaseAdapter.notifyDataSetChanged();
        //      mFirebaseAdapter.cleanup();
//
        mRecyclerView.setAdapter(mFirebaseAdapter);

        //mFirebaseAdapter.cleanup();
        Query q = mDatabase.child("event");

        //mFirebaseAdapter = new FirebaseEventsRecyclerViewAdapter(
        //        Event.class,
        //       R.layout.fragment_events,
        //      FirebaseEventsRecyclerViewAdapter.EventViewHolder.class,
        //     mDatabase.child("event").orderByKey().startAt(query).endAt(query));
        //mFirebaseAdapter.setQueryReference(q);
        //mFirebaseAdapter.registerAdapterDataObserver();notifyItemRemoved(1);
        //mRecyclerView.setAdapter(mFirebaseAdapter);
        //mFirebaseAdapter.notifyDataSetChanged();
        //List<Event> events = new ArrayList<>();
        //mFirebaseAdapter.
        //for (Event e : mEvents) {
        //   if (e.getName().toLowerCase().lastIndexOf(query) >= 0 || e.getDescription().toLowerCase().lastIndexOf(query) >= 0) {
        //    events.add(e);
        //  }
        //}
        //mRecyclerAdapter.setModels(events);
        //mRecyclerAdapter.notifyDataSetChanged();

        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

//    public void updateLocalEvents(boolean showProgress) {
//
//        //mEvents = EventsDbUtil.getAllEvents(getApplicationContext());
//        final ProgressDialog progressDialog = new ProgressDialog(NavigationActivity.this,
//                R.style.Theme_AppCompat_DayNight_Dialog);
//        progressDialog.setIndeterminate(true);
//        progressDialog.setMessage("Actualizando...");
//        if (showProgress) progressDialog.show();
//        final List<Event> mEventsAux = EventsDbUtil.getAllRemoteEvents(getApplicationContext());
//        new android.os.Handler().postDelayed(
//                new Runnable() {
//                    public void run() {
//                        if (mEventsAux.isEmpty()) {
//                            Snackbar.make(getCurrentFocus(), "Error al actualizar, verifique la conexión", Snackbar.LENGTH_LONG).show();
//                        } else {
//                            mEvents = mEventsAux;
//                            //mRecyclerAdapter.setModels(mEvents);
//                            //mRecyclerAdapter.notifyDataSetChanged();
//                            Snackbar.make(getCurrentFocus(), "Eventos actualizados", Snackbar.LENGTH_LONG).show();
//                        }
//                        progressDialog.dismiss();
//                    }
//                }, 3000);
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_sync) {
            //updateLocalEvents(true);
        } else */
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, FragmentsActivity.class);
            intent.setAction("SETTINGS");
            startActivity(intent);
        } else if (id == R.id.action_sign_out) {
            signOut();
        } else if (id == R.id.action_about) {
            Intent intent = new Intent(this, FragmentsActivity.class);
            intent.setAction("ABOUT");
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    private void signOut() {
        Intent intent = new Intent(this, LoginActivity.class);

        pref.edit().clear().apply();
        startActivity(intent);
        mFirebaseAuth.signOut();
        Auth.GoogleSignInApi.signOut(mGoogleApiClient);
        finish();
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_account) {
            Intent intent = new Intent(this, FragmentsActivity.class);
            intent.setAction("ACCOUNT");

            startActivity(intent);
        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(this, FragmentsActivity.class);
            intent.setAction("SETTINGS");

            startActivity(intent);
        } else if (id == R.id.nav_sign_out) {
            signOut();
        } else if (id == R.id.nav_about) {
            Intent intent = new Intent(this, FragmentsActivity.class);
            intent.setAction("ABOUT");

            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

//    @Override
//    public void onClick(EventsRecyclerViewAdapter.ViewHolder holder, Event event) {
//        Intent intent = new Intent(this, FragmentsActivity.class);
//        intent.setAction("EVENT");
//        //intent.putExtra("EVENT", event);
//        startActivity(intent);
//    }


    public void onClickFab(View view) {
        Intent intent = new Intent(this, FragmentsActivity.class);
        intent.setAction("NEW_EVENT");

        startActivity(intent);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    //@Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, final String key) {
        Log.d(TAG, "Ingreso a sharedPreferenceChanged " + key);

        if ("name".equals(key) || "age".equals(key) || "seconds".equals(key)) {
            mDatabase.child("user").child(mFirebaseAuth.getCurrentUser().getUid())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            //Log.d(TAG, k)
                            dataSnapshot.getRef().child(key).push().setValue(pref.getString(key, ""));

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            System.out.println("The read failed: " + databaseError.getMessage());
                        }
                    });
        } else {// if ("keep_logged".equals(key) || "sound".equals(key)|| "notification".equals(key)) {
            mDatabase.child("user").child(mFirebaseAuth.getCurrentUser().getUid())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            dataSnapshot.getRef().child(key).setValue(pref.getBoolean(key, true));

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            System.out.println("The read failed: " + databaseError.getMessage());
                        }
                    });
        }
    }

    @Override
    public void onClick(FirebaseEventsRecyclerViewAdapter.EventViewHolder holder, String event) {
        Intent intent = new Intent(this, FragmentsActivity.class);
        intent.setAction("EVENT");
        pref.edit().putString("EVENTKEY", event).apply();

        startActivity(intent);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d("FAIL", "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }
}