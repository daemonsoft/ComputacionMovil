package co.edu.udea.compumovil.gr04_20171.project.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;

import co.edu.udea.compumovil.gr04_20171.project.R;
import co.edu.udea.compumovil.gr04_20171.project.models.Table;

/**
 * Created by David on 08/05/2017.
 */

public class TableRecyclerViewAdapter extends FirebaseRecyclerAdapter<Table, TableRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "TableRecyclerViewAdapte";

    private static MyOnItemClickListener myOnItemClickListener;
    private static MyOnLongItemClickListener myOnLongItemClickListener;
    private static HashMap<String, Table> tableList;

    public TableRecyclerViewAdapter(Class<Table> modelClass, int modelLayout, Class<ViewHolder> viewHolderClass, DatabaseReference ref) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        tableList = new HashMap<>();
    }

    public static Table getTable(String key){
        return tableList.get(key);
    }

    public void setListener(MyOnItemClickListener listener){
        myOnItemClickListener = listener;
    }

    @Override
    protected void populateViewHolder(TableRecyclerViewAdapter.ViewHolder viewHolder, Table table, int position) {
        viewHolder.mNameView.setText(table.getName());
        viewHolder.mTable = table;

        if (table.getFree() && "wait".equals(table.getService()))
            viewHolder.mIconNew.setVisibility(View.VISIBLE);
        else
            viewHolder.mIconNew.setVisibility(View.INVISIBLE);

        if ("facture".equals(table.getService()))
            viewHolder.mIconFacture.setVisibility(View.VISIBLE);
        else
            viewHolder.mIconFacture.setVisibility(View.INVISIBLE);

        if (!table.getFree() && "order".equals(table.getService()))
            viewHolder.mIconService.setVisibility(View.VISIBLE);
        else
            viewHolder.mIconService.setVisibility(View.INVISIBLE);
    }

    @Override
    protected Table parseSnapshot(DataSnapshot snapshot) {
        Table table = super.parseSnapshot(snapshot);
        if (null != table.getName()) {

           tableList.put(snapshot.getKey(), table);
        }
        return table;
    }

    public interface MyOnItemClickListener {
        void onClick(ViewHolder holder, Table table);
    }

     public interface MyOnLongItemClickListener {
        void onLongClick(ViewHolder holder, Table table);
    }



    public static class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener{
        public final View mView;
        public final TextView mNameView;
        public final TextView mDescriptionView;
        public Table mTable;
        public final TextView mScoreView;
        public final ImageView mImageView;
        public final ImageView mIconNew;
        public final ImageView mIconFacture;
        public final ImageView mIconService;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mNameView = (TextView) view.findViewById(R.id.card_name);
            mDescriptionView = (TextView) view.findViewById(R.id.card_description);
            mScoreView = (TextView) view.findViewById(R.id.card_score);
            mImageView = (ImageView) view.findViewById(R.id.card_image);
            mIconFacture = (ImageView) view.findViewById(R.id.icon_facture);
            mIconService = (ImageView) view.findViewById(R.id.icon_service);
            mIconNew = (ImageView) view.findViewById(R.id.icon_new);

            mView.setOnClickListener(this);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mNameView.getText() + "'";
        }

        @Override
        public void onClick(View v) {
            myOnItemClickListener.onClick(this, mTable);
        }


        @Override
        public boolean onLongClick(View v) {myOnLongItemClickListener.onLongClick(this, mTable);
            return false;
        }
    }

}
