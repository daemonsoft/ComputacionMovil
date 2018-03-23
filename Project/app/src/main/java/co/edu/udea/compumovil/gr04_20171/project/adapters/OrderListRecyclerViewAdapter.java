package co.edu.udea.compumovil.gr04_20171.project.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import co.edu.udea.compumovil.gr04_20171.project.R;
import co.edu.udea.compumovil.gr04_20171.project.models.Order;
import co.edu.udea.compumovil.gr04_20171.project.models.OrderList;

/**
 * Created by daemonsoft on 29/05/17.
 */

public class OrderListRecyclerViewAdapter extends FirebaseRecyclerAdapter<OrderList, OrderListRecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "OrderListRecyclerView";

    private static OrderListRecyclerViewAdapter.MyOnItemClickListener myOnItemClickListener;
    private static HashMap<String, OrderList> orderHashMap;

    public OrderListRecyclerViewAdapter(Class<OrderList> modelClass, int modelLayout, Class<OrderListRecyclerViewAdapter.ViewHolder> viewHolderClass, DatabaseReference
            ref) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        orderHashMap = new HashMap<>();
    }

    public static OrderList getOrder(String key) {
        return orderHashMap.get(key);
    }

    public void setListener(OrderListRecyclerViewAdapter.MyOnItemClickListener listener) {
        myOnItemClickListener = listener;
    }

    @Override
    protected void populateViewHolder(OrderListRecyclerViewAdapter.ViewHolder viewHolder, OrderList orderList, int position) {
        viewHolder.mIdView.setText(orderList.getId() + "");
        viewHolder.mStatusView.setText(orderList.getStatus());
        viewHolder.mPriceView.setText("$ " + orderList.getTotalOrder());
        viewHolder.mOrderList = orderList;

    }

    @Override
    protected OrderList parseSnapshot(DataSnapshot snapshot) {
        OrderList orderList = new OrderList();
        orderList.setId(snapshot.getKey());
        if (null != snapshot.child("status").getValue())
            orderList.setStatus(snapshot.child("status").getValue().toString());
        if (null != snapshot.child("table").getValue())
            orderList.setTable(snapshot.child("table").getValue().toString());
        Order order;
        List<Order> orders = new ArrayList<>();
        for (DataSnapshot snap : snapshot.child("products").getChildren()) {
            order = new Order();
            order.setId(snap.getKey());
            order.setPrice(Integer.parseInt(snap.child("price").getValue().toString()));
            order.setName(snap.child("name").getValue().toString());
            order.setDescription(snap.child("description").getValue().toString());
            order.setPicture(snap.child("picture").getValue().toString());
            orders.add(order);
            orderList.setTotalOrder(orderList.getTotalOrder() + order.getPrice());

        }
        orderList.setProducts(orders);

        orderHashMap.put(snapshot.getKey(), orderList);

        return orderList;
    }

    public interface MyOnItemClickListener {
        void onClick(OrderListRecyclerViewAdapter.ViewHolder holder, OrderList orderList);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        public final View mView;
        public final TextView mIdView;
        public final TextView mStatusView;
        public OrderList mOrderList;
        public final TextView mPriceView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.card_order_id);
            mStatusView = (TextView) view.findViewById(R.id.card_order_status);
            mPriceView = (TextView) view.findViewById(R.id.card_order_price);
            mView.setOnClickListener(this);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mIdView.getText() + "'";
        }

        @Override
        public void onClick(View v) {
            myOnItemClickListener.onClick(this, mOrderList);
        }


    }

}
