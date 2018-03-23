package co.edu.udea.compumovil.gr04_20171.project.activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import co.edu.udea.compumovil.gr04_20171.project.R;
import co.edu.udea.compumovil.gr04_20171.project.adapters.DishRecyclerViewAdapter;
import co.edu.udea.compumovil.gr04_20171.project.adapters.DrinkRecyclerViewAdapter;
import co.edu.udea.compumovil.gr04_20171.project.adapters.OrderRecyclerViewAdapter;
import co.edu.udea.compumovil.gr04_20171.project.adapters.TableRecyclerViewAdapter;
import co.edu.udea.compumovil.gr04_20171.project.database.Utils;
import co.edu.udea.compumovil.gr04_20171.project.models.Dish;
import co.edu.udea.compumovil.gr04_20171.project.models.Drink;
import co.edu.udea.compumovil.gr04_20171.project.models.Order;
import co.edu.udea.compumovil.gr04_20171.project.models.Table;

public class MenuActivity extends AppCompatActivity {

    private static final String TAG = "MenuActivity";
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private static Table table;
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabase;
    private MenuItem bellItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carta);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabase = Utils.getDatabase().getReference("user").child(mFirebaseAuth.getCurrentUser().getUid());
        // if(getIntent().hasExtra())
        table = TableRecyclerViewAdapter.getTable(getIntent().getStringExtra("MESA_ID"));


        if (null == table) {
            Toast.makeText(this, "Mesa " + getIntent().getStringExtra("MESA_ID") + " no existe", Toast.LENGTH_LONG).show();
            finish();
        } else {

            Toast.makeText(this, "Scanned/Entered " + table.getName(), Toast.LENGTH_LONG).show();
            setTitle(table.getName());
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);




    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_carta, menu);
        bellItem = menu.findItem(R.id.action_bell);
        if ("order".equals(table.getService())) {
            bellItem.setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_cancel_order) {
            DialogFragment newFragment = new CancelOrderDialogFragment();
            newFragment.show(getSupportFragmentManager(), "cancelorder");
            return true;
        } else if (id == R.id.action_new_dish) {
            Intent intent = new Intent(this, FragmentsActivity.class);
            intent.setAction("NEW_DISH");
            startActivity(intent);
            return true;
        } else if (id == R.id.action_new_drink) {
            Intent intent = new Intent(this, FragmentsActivity.class);
            intent.setAction("NEW_DRINK");
            startActivity(intent);
            return true;
        } else if (id == R.id.action_cart) {
            DialogFragment newFragment = new CloseCartDialogFragment();
            newFragment.show(getSupportFragmentManager(), "closeorder");
            return true;
        } else if (id == R.id.action_bell) {
            mDatabase.child("table/" + table.getId() + "/service").setValue("serve");
            bellItem.setVisible(Boolean.FALSE);
        }


        return super.onOptionsItemSelected(item);
    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_new_dish, container, false);

            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position) {
                case 0:
                    return DishFragment.newInstance(position + 1);
                case 1:
                    return DrinkFragment.newInstance(position + 1);
                case 2:
                    return OrderFragment.newInstance(position + 1);
            }
            return DishFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Platos";
                case 1:
                    return "Bebidas";
                case 2:
                    return "Orden";
            }
            return null;
        }
    }

    public static class DishFragment extends Fragment implements DishRecyclerViewAdapter.MyOnItemClickListener {

        private static final String ARG_SECTION_NUMBER = "section_number";
        private RecyclerView recyclerView;
        DishRecyclerViewAdapter dishRecyclerViewAdapter;
        DatabaseReference mDatabase;
        FirebaseAuth mFirebaseAuth;

        public DishFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static DishFragment newInstance(int sectionNumber) {
            DishFragment fragment = new DishFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_dish, container, false);


            mFirebaseAuth = FirebaseAuth.getInstance();
            recyclerView = (RecyclerView) rootView.findViewById(R.id.dish_list);
            mDatabase = Utils.getDatabase().getReference("user").child(mFirebaseAuth.getCurrentUser().getUid());
            dishRecyclerViewAdapter = new DishRecyclerViewAdapter(
                    Dish.class,
                    R.layout.card_dish,
                    DishRecyclerViewAdapter.ViewHolder.class,
                    mDatabase.child("menu/dishes").orderByChild("name").getRef());
            dishRecyclerViewAdapter.setListener(this);
            LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(rootView.getContext());
            recyclerView.setLayoutManager(mLinearLayoutManager);
            recyclerView.setAdapter(dishRecyclerViewAdapter);
            //TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            //textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }


        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            //Button btnCreateTable = (Button) view.findViewById(R.id.btn_new_table);
            //btnCreateTable.setOnClickListener(this);
        }

        @Override
        public void onLongClick(DishRecyclerViewAdapter.ViewHolder holder, Dish dish) {
            if (null == table.getCurrentOrder() || "0".equals(table.getCurrentOrder())) {
                int orderCount = PreferenceManager.getDefaultSharedPreferences(getActivity()).getInt(mFirebaseAuth.getCurrentUser().getUid() + "order", 0);
                orderCount++;
                PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putInt(mFirebaseAuth.getCurrentUser().getUid() + "order", orderCount).apply();
                mDatabase.child("orders/" + table.getCurrentOrder() + "/status").setValue("active");
                mDatabase.child("table/" + table.getId() + "/free").setValue(Boolean.FALSE);
                mDatabase.child("table/" + table.getId() + "/service").setValue("check");
                mDatabase.child("table/" + table.getId() + "/currentOrder").setValue(String.valueOf(orderCount));
                mDatabase.child("orders/" + orderCount + "/products").push().setValue(dish);
                mDatabase.child("orders/" + orderCount + "/table").setValue(table.getId());
                mDatabase.child("orderCount").setValue(orderCount);
                table.setCurrentOrder(String.valueOf(orderCount));

            } else {
                mDatabase.child("orders/" + table.getCurrentOrder() + "/products").push().setValue(dish);
            }
            Snackbar.make(getView(), "Agregado: " + dish.getName(), Snackbar.LENGTH_SHORT).show();
            //Toast.makeText(getActivity(), "Long click order" + table.getCurrentOrder(), Toast.LENGTH_SHORT).show();
        }
    }

    public static class DrinkFragment extends Fragment implements DrinkRecyclerViewAdapter.MyOnItemClickListener {

        private static final String ARG_SECTION_NUMBER = "section_number";
        private RecyclerView recyclerView;
        DrinkRecyclerViewAdapter drinkRecyclerViewAdapter;
        DatabaseReference mDatabase;
        FirebaseAuth mFirebaseAuth;

        public DrinkFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static DrinkFragment newInstance(int sectionNumber) {
            DrinkFragment fragment = new DrinkFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_drink, container, false);


            mFirebaseAuth = FirebaseAuth.getInstance();
            recyclerView = (RecyclerView) rootView.findViewById(R.id.drink_list);
            mDatabase = Utils.getDatabase().getReference("user").child(mFirebaseAuth.getCurrentUser().getUid());
            drinkRecyclerViewAdapter = new DrinkRecyclerViewAdapter(
                    Drink.class,
                    R.layout.card_drink,
                    DrinkRecyclerViewAdapter.ViewHolder.class,
                    mDatabase.child("menu/drinks").orderByChild("name").getRef());
            drinkRecyclerViewAdapter.setListener(this);
            LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(rootView.getContext());
            recyclerView.setLayoutManager(mLinearLayoutManager);
            recyclerView.setAdapter(drinkRecyclerViewAdapter);
            //TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            //textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }


        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            //Button btnCreateTable = (Button) view.findViewById(R.id.btn_new_table);
            //btnCreateTable.setOnClickListener(this);
        }

        @Override
        public void onLongClick(DrinkRecyclerViewAdapter.ViewHolder holder, Drink drink) {
            if (null == table.getCurrentOrder() || "0".equals(table.getCurrentOrder())) {
                int orderCount = PreferenceManager.getDefaultSharedPreferences(getActivity()).getInt(mFirebaseAuth.getCurrentUser().getUid() + "order", 0);
                orderCount++;
                PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putInt(mFirebaseAuth.getCurrentUser().getUid() + "order", orderCount).apply();
                mDatabase.child("orders/" + table.getCurrentOrder() + "/status").setValue("active");

                mDatabase.child("table/" + table.getId() + "/service").setValue("check");
                mDatabase.child("table/" + table.getId() + "/free").setValue(Boolean.FALSE);
                mDatabase.child("table/" + table.getId() + "/currentOrder").setValue(String.valueOf(orderCount));
                mDatabase.child("orders/" + orderCount + "/products").push().setValue(drink);
                mDatabase.child("orders/" + orderCount + "/table").setValue(table.getId());
                mDatabase.child("orderCount").setValue(orderCount);
                table.setCurrentOrder(String.valueOf(orderCount));

            } else {
                mDatabase.child("orders/" + table.getCurrentOrder() + "/products").push().setValue(drink);
            }
            Snackbar.make(getView(), "Agregado: " + drink.getName(), Snackbar.LENGTH_SHORT).show();
        }
    }

    public static class OrderFragment extends Fragment implements OrderRecyclerViewAdapter.MyOnItemClickListener {

        private static final String ARG_SECTION_NUMBER = "section_number";
        private RecyclerView recyclerView;
        OrderRecyclerViewAdapter orderRecyclerViewAdapter;
        DatabaseReference mDatabase;
        FirebaseAuth mFirebaseAuth;

        public OrderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static OrderFragment newInstance(int sectionNumber) {
            OrderFragment fragment = new OrderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_order, container, false);
            mFirebaseAuth = FirebaseAuth.getInstance();
            recyclerView = (RecyclerView) rootView.findViewById(R.id.order_list);
            mDatabase = Utils.getDatabase().getReference("user").child(mFirebaseAuth.getCurrentUser().getUid());
            orderRecyclerViewAdapter = new OrderRecyclerViewAdapter(
                    Order.class,
                    R.layout.card_order,
                    OrderRecyclerViewAdapter.ViewHolder.class,
                    mDatabase.child("orders/" + table.getCurrentOrder() + "/products").getRef());
            orderRecyclerViewAdapter.setListener(this);
            LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(rootView.getContext());
            recyclerView.setLayoutManager(mLinearLayoutManager);
            recyclerView.setAdapter(orderRecyclerViewAdapter);
            //TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            //textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            //Button btnCreateTable = (Button) view.findViewById(R.id.btn_new_table);
            //btnCreateTable.setOnClickListener(this);
        }

        @Override
        public void onClick(OrderRecyclerViewAdapter.ViewHolder holder, Order order) {

        }
    }

    public static class CloseCartDialogFragment extends DialogFragment {

        private DatabaseReference mDatabase;
        private FirebaseAuth mFirebaseAuth;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            mFirebaseAuth = FirebaseAuth.getInstance();
            mDatabase = Utils.getDatabase().getReference("user").child(mFirebaseAuth.getCurrentUser().getUid());
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.dialog_confirm_cart)
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            mDatabase.child("orders/" + table.getCurrentOrder() + "/status").setValue("finished");
                            table.setCurrentOrder("0");
                            mDatabase.child("table/" + table.getId() + "/free").setValue(Boolean.TRUE);
                            mDatabase.child("table/" + table.getId() + "/service").setValue("");
                            mDatabase.child("table/" + table.getId() + "/currentOrder").setValue("0");
                            getActivity().finish();
                        }
                    })
                    .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    });
            // Create the AlertDialog object and return it
            return builder.create();
        }
    }

    public static class CancelOrderDialogFragment extends DialogFragment {

        private DatabaseReference mDatabase;
        private FirebaseAuth mFirebaseAuth;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            mFirebaseAuth = FirebaseAuth.getInstance();
            mDatabase = Utils.getDatabase().getReference("user").child(mFirebaseAuth.getCurrentUser().getUid());
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.dialog_cancel_order)
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            mDatabase.child("orders/" + table.getCurrentOrder() + "/status").setValue("canceled");
                            table.setCurrentOrder("0");
                            mDatabase.child("table/" + table.getId() + "/free").setValue(Boolean.TRUE);
                            mDatabase.child("table/" + table.getId() + "/service").setValue("");
                            mDatabase.child("table/" + table.getId() + "/currentOrder").setValue("0");
                            getActivity().finish();
                        }
                    })
                    .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    });
            // Create the AlertDialog object and return it
            return builder.create();
        }
    }
}
