package co.edu.udea.compumovil.gr04_20171.project.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import co.edu.udea.compumovil.gr04_20171.project.R;
import co.edu.udea.compumovil.gr04_20171.project.adapters.OrderListRecyclerViewAdapter;
import co.edu.udea.compumovil.gr04_20171.project.database.Utils;
import co.edu.udea.compumovil.gr04_20171.project.models.OrderList;

public class OrdersActivity extends AppCompatActivity implements OrderListRecyclerViewAdapter.MyOnItemClickListener {
    private RecyclerView mRecyclerView;
    private FirebaseAuth mFirebaseAuth;
    private GoogleApiClient mGoogleApiClient;
    private DatabaseReference mDatabase;
    private OrderListRecyclerViewAdapter orderListRecyclerViewAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mRecyclerView = (RecyclerView) findViewById(R.id.order_list);
        mDatabase = Utils.getDatabase().getReference("user").child(mFirebaseAuth.getCurrentUser().getUid());
        orderListRecyclerViewAdapter = new OrderListRecyclerViewAdapter(
                OrderList.class,
                R.layout.card_order_list,
                OrderListRecyclerViewAdapter.ViewHolder.class,
                mDatabase.child("orders").getRef());
        orderListRecyclerViewAdapter.setListener(this);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(orderListRecyclerViewAdapter);
    }

    @Override
    public void onClick(OrderListRecyclerViewAdapter.ViewHolder holder, OrderList orderList) {

    }
}
