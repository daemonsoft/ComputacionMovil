package co.edu.udea.compumovil.gr04_20171.project.activities;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import co.edu.udea.compumovil.gr04_20171.project.R;
import co.edu.udea.compumovil.gr04_20171.project.adapters.TableRecyclerViewAdapter;
import co.edu.udea.compumovil.gr04_20171.project.database.Utils;
import co.edu.udea.compumovil.gr04_20171.project.models.Table;

public class MainActivity extends AppCompatActivity
        implements GoogleApiClient.OnConnectionFailedListener, NavigationView.OnNavigationItemSelectedListener,
        TableRecyclerViewAdapter.MyOnItemClickListener, TableRecyclerViewAdapter.MyOnLongItemClickListener {
    private static final String TAG = "MainActivity";
    private RecyclerView mRecyclerView;
    private FirebaseAuth mFirebaseAuth;
    private GoogleApiClient mGoogleApiClient;
    private DatabaseReference mDatabase;
    private TableRecyclerViewAdapter tableRecyclerViewAdapter;
    private LinearLayoutManager mLinearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mRecyclerView = (RecyclerView) findViewById(R.id.table_list);
        mDatabase = Utils.getDatabase().getReference("user").child(mFirebaseAuth.getCurrentUser().getUid());
        tableRecyclerViewAdapter = new TableRecyclerViewAdapter(
                Table.class,
                R.layout.card_table,
                TableRecyclerViewAdapter.ViewHolder.class,
                mDatabase.child("table").orderByChild("name").getRef());
        tableRecyclerViewAdapter.setListener(this);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();

        mRecyclerView.setAdapter(tableRecyclerViewAdapter);
        // Floating Button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ScanActivity.class);
                startActivity(intent);
            }
        });

        // Menu Drawer
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

      if (id == R.id.action_sign_out) {
            mFirebaseAuth.signOut();
            startActivity(new Intent(this, LoginActivity.class));
            Auth.GoogleSignInApi.signOut(mGoogleApiClient);
            finish();
        } else if (id == R.id.action_new_table) {
            //String uniqueID = UUID.randomUUID().toString();
            int tableCount = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getInt(mFirebaseAuth.getCurrentUser().getUid() + "table", 0);
            tableCount++;
            Table table = new Table(tableCount + "", "Mesa: " + tableCount, 4, Boolean.FALSE);
            table.setFree(Boolean.TRUE);
            table.setService("");
            PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putInt(mFirebaseAuth.getCurrentUser().getUid() + "table", tableCount).apply();
            mDatabase.child("table").child(String.valueOf(tableCount)).setValue(table);
            mDatabase.child("tableCount").setValue(tableCount);
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_logout) {
            mFirebaseAuth.signOut();
            startActivity(new Intent(this, LoginActivity.class));
            Auth.GoogleSignInApi.signOut(mGoogleApiClient);
            finish();
        } else if (id == R.id.nav_orders) {
            Intent intent = new Intent(this, OrdersActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(TableRecyclerViewAdapter.ViewHolder holder, Table table) {
        Intent intent = new Intent(this, MenuActivity.class);
        intent.putExtra("MESA_ID", table.getId());
        startActivity(intent);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLongClick(TableRecyclerViewAdapter.ViewHolder holder, Table table) {

    }
}
