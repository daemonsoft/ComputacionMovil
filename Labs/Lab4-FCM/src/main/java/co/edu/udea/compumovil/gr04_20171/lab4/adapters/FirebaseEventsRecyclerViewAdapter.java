package co.edu.udea.compumovil.gr04_20171.lab4.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import co.edu.udea.compumovil.gr04_20171.lab4.R;
import co.edu.udea.compumovil.gr04_20171.lab4.activities.NavigationActivity;
import co.edu.udea.compumovil.gr04_20171.lab4.models.Event;
import co.edu.udea.compumovil.gr04_20171.lab4.services.Config;

public class FirebaseEventsRecyclerViewAdapter extends FirebaseRecyclerAdapter<Event, FirebaseEventsRecyclerViewAdapter.EventViewHolder> {

    private static final String TAG = "FirebaseAdapter";
    private static List<Event> mValues;
    private static MyOnItemClickListener mListener;

    public interface MyOnItemClickListener {
        void onClick(EventViewHolder holder, String event);
    }

    public FirebaseEventsRecyclerViewAdapter(Class<Event> modelClass, int modelLayout, Class<EventViewHolder> viewHolderClass, Query ref) {
        super(modelClass, modelLayout, viewHolderClass, ref);

        mValues = new ArrayList<>();

    }

    @Override
    protected Event parseSnapshot(DataSnapshot snapshot) {
        Event event = super.parseSnapshot(snapshot);
        if (event.getName() != null) {

                event.setId(snapshot.getKey());
                mValues.add(event);

        }
        return event;
    }


    public void setListener(MyOnItemClickListener mRef) {
        mListener = mRef;
    }

    @Override
    protected void populateViewHolder(FirebaseEventsRecyclerViewAdapter.EventViewHolder viewHolder, Event event, int position) {
        if (event != null) {

            viewHolder.mNameView.setText(event.getName());
            viewHolder.mDescriptionView.setText(event.getDescription());
            viewHolder.mScoreView.setText(String.valueOf(event.getScore().toString()));
            Picasso.with(viewHolder.mImageView.getContext()).load(event.getPicture()).resize(200, 200).into(viewHolder.mImageView);

        }

    }

    public static class EventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final View mView;
        public final TextView mNameView;
        public final TextView mDescriptionView;
        public final TextView mScoreView;
        public final ImageView mImageView;

        public EventViewHolder(View view) {
            super(view);
            mView = view;
            //if(!mView.isEnabled()) this.
            mNameView = (TextView) view.findViewById(R.id.card_name);
            mDescriptionView = (TextView) view.findViewById(R.id.card_description);
            mScoreView = (TextView) view.findViewById(R.id.card_score);
            mImageView = (ImageView) view.findViewById(R.id.card_image);
            mView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListener.onClick(this, mValues.get(getAdapterPosition()).getId());
        }

    }
}
