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
import co.edu.udea.compumovil.gr04_20171.project.models.Dish;

/**
 * Created by daemonsoft on 28/05/17.
 */

public class DishRecyclerViewAdapter extends FirebaseRecyclerAdapter<Dish, DishRecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "DishRecyclerViewAdapte";

    private static DishRecyclerViewAdapter.MyOnItemClickListener myOnItemClickListener;
    private static HashMap<String, Dish> dishHashMap;

    public DishRecyclerViewAdapter(Class<Dish> modelClass, int modelLayout, Class<DishRecyclerViewAdapter.ViewHolder> viewHolderClass, DatabaseReference ref) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        dishHashMap = new HashMap<>();
    }

    public static Dish getDish(String key) {
        return dishHashMap.get(key);
    }

    public void setListener(DishRecyclerViewAdapter.MyOnItemClickListener listener) {
        myOnItemClickListener = listener;
    }

    @Override
    protected void populateViewHolder(DishRecyclerViewAdapter.ViewHolder viewHolder, Dish dish, int position) {
        viewHolder.mNameView.setText(dish.getName());
        viewHolder.mDescriptionView.setText(dish.getDescription());
        viewHolder.mPriceView.setText("$ " + dish.getPrice());
        viewHolder.mDish = dish;
        Picasso.with(viewHolder.mImageView.getContext()).load(dish.getPicture()).resize(200, 200).into(viewHolder.mImageView);
    }

    @Override
    protected Dish parseSnapshot(DataSnapshot snapshot) {
        Dish dish = super.parseSnapshot(snapshot);
        if (null != dish.getName()) {
            dish.setId(snapshot.getKey());
            dishHashMap.put(snapshot.getKey(), dish);

        }
        return dish;
    }

    public interface MyOnItemClickListener {
        void onLongClick(DishRecyclerViewAdapter.ViewHolder holder, Dish dish);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnLongClickListener {
        public final View mView;
        public final TextView mNameView;
        public final TextView mDescriptionView;
        public Dish mDish;
        public final TextView mPriceView;
        public final ImageView mImageView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mNameView = (TextView) view.findViewById(R.id.card_dish_name);
            mDescriptionView = (TextView) view.findViewById(R.id.card_dish_description);
            mPriceView = (TextView) view.findViewById(R.id.card_dish_price);
            mImageView = (ImageView) view.findViewById(R.id.card_dish_image);
            mView.setOnLongClickListener(this);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mNameView.getText() + "'";
        }


        @Override
        public boolean onLongClick(View v) {
            myOnItemClickListener.onLongClick(this, mDish);
            return false;
        }
    }
}
