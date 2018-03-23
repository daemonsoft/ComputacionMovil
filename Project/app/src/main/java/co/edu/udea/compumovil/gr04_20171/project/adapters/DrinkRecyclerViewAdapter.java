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
import co.edu.udea.compumovil.gr04_20171.project.models.Drink;

/**
 * Created by daemonsoft on 29/05/17.
 */

public class DrinkRecyclerViewAdapter extends FirebaseRecyclerAdapter<Drink, DrinkRecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "DishRecyclerViewAdapte";

    private static DrinkRecyclerViewAdapter.MyOnItemClickListener myOnItemClickListener;
    private static HashMap<String, Drink> drinkHashMap;

    public DrinkRecyclerViewAdapter(Class<Drink> modelClass, int modelLayout, Class<DrinkRecyclerViewAdapter.ViewHolder> viewHolderClass, DatabaseReference
            ref) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        drinkHashMap = new HashMap<>();
    }

    public static Drink getDrink(String key) {
        return drinkHashMap.get(key);
    }

    public void setListener(DrinkRecyclerViewAdapter.MyOnItemClickListener listener) {
        myOnItemClickListener = listener;
    }

    @Override
    protected void populateViewHolder(DrinkRecyclerViewAdapter.ViewHolder viewHolder, Drink drink, int position) {
        viewHolder.mNameView.setText(drink.getName());
        viewHolder.mDescriptionView.setText(drink.getDescription());
        viewHolder.mPriceView.setText("$ " + drink.getPrice());
        viewHolder.mDrink = drink;
        Picasso.with(viewHolder.mImageView.getContext()).load(drink.getPicture()).resize(200, 200).into(viewHolder.mImageView);

    }

    @Override
    protected Drink parseSnapshot(DataSnapshot snapshot) {
        Drink drink = super.parseSnapshot(snapshot);
        if (null != drink.getName()) {
            drink.setId(snapshot.getKey());
            drinkHashMap.put(snapshot.getKey(), drink);

        }
        return drink;
    }

    public interface MyOnItemClickListener {
        void onLongClick(DrinkRecyclerViewAdapter.ViewHolder holder, Drink drink);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnLongClickListener {
        public final View mView;
        public final TextView mNameView;
        public final TextView mDescriptionView;
        public Drink mDrink;
        public final TextView mPriceView;
        public final ImageView mImageView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mNameView = (TextView) view.findViewById(R.id.card_drink_name);
            mDescriptionView = (TextView) view.findViewById(R.id.card_drink_description);
            mPriceView = (TextView) view.findViewById(R.id.card_drink_price);
            mImageView = (ImageView) view.findViewById(R.id.card_drink_image);
            mView.setOnLongClickListener(this);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mNameView.getText() + "'";
        }

        @Override
        public boolean onLongClick(View v) {
            myOnItemClickListener.onLongClick(this, mDrink);
            return false;
        }


    }

}
