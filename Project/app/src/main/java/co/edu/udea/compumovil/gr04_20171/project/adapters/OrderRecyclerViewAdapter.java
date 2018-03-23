package co.edu.udea.compumovil.gr04_20171.project.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import co.edu.udea.compumovil.gr04_20171.project.R;
import co.edu.udea.compumovil.gr04_20171.project.models.Order;

/**
 * Created by daemonsoft on 29/05/17.
 */

public class OrderRecyclerViewAdapter extends FirebaseRecyclerAdapter<Order, OrderRecyclerViewAdapter.ViewHolder>{
    private static final String TAG = "OrderRecyclerViewAdapte";

    private static OrderRecyclerViewAdapter.MyOnItemClickListener myOnItemClickListener;
    private static HashMap<String, Order> orderHashMap;

    public OrderRecyclerViewAdapter(Class<Order> modelClass, int modelLayout, Class<OrderRecyclerViewAdapter.ViewHolder> viewHolderClass, DatabaseReference
            ref) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        orderHashMap = new HashMap<>();
    }

    public static Order getOrder(String key) {
        return orderHashMap.get(key);
    }

    public void setListener(OrderRecyclerViewAdapter.MyOnItemClickListener listener) {
        myOnItemClickListener = listener;
    }

    @Override
    protected void populateViewHolder(OrderRecyclerViewAdapter.ViewHolder viewHolder, Order order, int position) {
        viewHolder.mNameView.setText(order.getName());
        viewHolder.mDescriptionView.setText(order.getDescription());
        viewHolder.mPriceView.setText("$ " + order.getPrice());
        viewHolder.mOrder = order;
        Picasso.with(viewHolder.mImageView.getContext()).load(order.getPicture()).resize(200, 200).into(viewHolder.mImageView);

    }

    @Override
    protected Order parseSnapshot(DataSnapshot snapshot) {
        Order order = super.parseSnapshot(snapshot);
        if (null != order.getName()) {
            order.setId(snapshot.getKey());
            orderHashMap.put(snapshot.getKey(), order);

        }
        return order;
    }

    public interface MyOnItemClickListener {
        void onClick(OrderRecyclerViewAdapter.ViewHolder holder, Order drink);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        public final View mView;
        public final TextView mNameView;
        public final TextView mDescriptionView;
        public Order mOrder;
        public final TextView mPriceView;
        public final ImageView mImageView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mNameView = (TextView) view.findViewById(R.id.card_order_name);
            mDescriptionView = (TextView) view.findViewById(R.id.card_order_description);
            mPriceView = (TextView) view.findViewById(R.id.card_order_price);
            mImageView = (ImageView) view.findViewById(R.id.card_order_image);
            mView.setOnClickListener(this);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mNameView.getText() + "'";
        }

        @Override
        public void onClick(View v) {
            myOnItemClickListener.onClick(this, mOrder);
        }


    }

}
