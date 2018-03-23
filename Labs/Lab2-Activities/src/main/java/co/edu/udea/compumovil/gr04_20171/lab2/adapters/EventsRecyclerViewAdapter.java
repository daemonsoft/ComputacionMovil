package co.edu.udea.compumovil.gr04_20171.lab2.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import co.edu.udea.compumovil.gr04_20171.lab2.R;
import co.edu.udea.compumovil.gr04_20171.lab2.database.EventsDbUtil;
import co.edu.udea.compumovil.gr04_20171.lab2.models.Event;


/**
 * Created by daemonsoft on 14/03/17.
 */

public class EventsRecyclerViewAdapter extends RecyclerView.Adapter<EventsRecyclerViewAdapter.ViewHolder> {

    private List<Event> mValues;
    private MyOnItemClickListener mListener;
    private Context mContext;

    public EventsRecyclerViewAdapter(MyOnItemClickListener listener, List<Event> values, Context context) {
        mValues = values;
        mListener = listener;
        mContext = context;
    }

    public void setModels(List<Event> values) {
        mValues = new ArrayList<>(values);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Create a new view.
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_events, parent, false);
        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        String str;
        holder.mEvent = mValues.get(position);
        holder.mNameView.setText(mValues.get(position).getName());
        holder.mDescriptionView.setText(mValues.get(position).getDescription());
        holder.mScoreView.setText(mValues.get(position).getScore().toString());

        str = mValues.get(position).getName();
        if (str.length() > 18) {
            str = str.substring(0, 18) + mContext.getString(R.string.points);
        }
        holder.mNameView.setText(str);
        str = mValues.get(position).getDescription();
        if (str.length() > 22) {
            str = str.substring(0, 22) + mContext.getString(R.string.points);
        }
        holder.mDescriptionView.setText(str);
        str = mContext.getString(R.string.event_points) + mValues.get(position).getScore().toString();
        holder.mScoreView.setText(str);


        Bitmap bitmap = configImage(mValues.get(position).getPicture());

        holder.mImageView.setImageBitmap(bitmap);
    }

    private Bitmap configImage(String picture) {

        Uri path = Uri.parse(picture);

        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), path);
            return Bitmap.createScaledBitmap(bitmap, (int) (bitmap.getWidth() * .1), (int) (bitmap.getHeight() * .1), false);
            // imageView.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public interface MyOnItemClickListener {
        void onClick(ViewHolder holder, Event event);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        public final View mView;
        public final TextView mNameView;
        public final TextView mDescriptionView;
        public Event mEvent;
        public final TextView mScoreView;
        public final ImageView mImageView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mNameView = (TextView) view.findViewById(R.id.card_name);
            mDescriptionView = (TextView) view.findViewById(R.id.card_description);
            mScoreView = (TextView) view.findViewById(R.id.card_score);
            mImageView = (ImageView) view.findViewById(R.id.card_image);
            mView.setOnClickListener(this);
        }


        @Override
        public String toString() {
            return super.toString() + " '" + mNameView.getText() + "'";
        }

        @Override
        public void onClick(View v) {
            mListener.onClick(this, mValues.get(getAdapterPosition()));
        }


    }
}
