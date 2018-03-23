package co.edu.udea.compumovil.gr04_20171.lab4.fragments;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import co.edu.udea.compumovil.gr04_20171.lab4.R;
import co.edu.udea.compumovil.gr04_20171.lab4.database.Utils;
import co.edu.udea.compumovil.gr04_20171.lab4.models.Event;
import co.edu.udea.compumovil.gr04_20171.lab4.services.Config;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsFragment extends Fragment {

    private static final String TAG = "DetailsFragment";
    public TextView mNameView;
    public TextView mDescriptionView;
    public TextView mDateView;
    public TextView mScoreView;
    public TextView mOwnerView;
    public ImageView mImageView;
    private DatabaseReference mDatabase;
    private Event mEvent;
    SharedPreferences pref;

    public DetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {


        //mUser = getArguments().getParcelable("USER");


        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mDatabase = Utils.getDatabase().getReference();
        pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mEvent = mEvent = new Event();
        Log.d(TAG, "getEventSnap ");


        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_details, container, false);
        mImageView = (ImageView) v.findViewById(R.id.details_picture);
        mNameView = (TextView) v.findViewById(R.id.details_name);
        mDescriptionView = (TextView) v.findViewById(R.id.details_description);
        mDateView = (TextView) v.findViewById(R.id.details_date);
        mScoreView = (TextView) v.findViewById(R.id.details_score);
        mOwnerView = (TextView) v.findViewById(R.id.details_owner_name);
        Utils.getDatabase().getReference().child("event").child(pref.getString("EVENTKEY", ""))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        Log.d(TAG, "Snap " + snapshot.toString());
                        if (null != snapshot.getValue()) {
                            Log.d(TAG, "not null snapshot.getValue()" + snapshot.child("score").getValue().toString());
                            mEvent.setName(snapshot.child("name").getValue().toString());
                            mEvent.setLocation(snapshot.child("location").getValue().toString());
                            mEvent.setScore(Integer.parseInt(snapshot.child("score").getValue().toString()));
                            mEvent.setPicture(snapshot.child("picture").getValue().toString());
                            mEvent.setOwner(snapshot.child("owner").getValue().toString());
                            mEvent.setDescription(snapshot.child("description").getValue().toString());
                            mEvent.setDate(snapshot.child("date").getValue().toString());
                            mEvent.setLatitude(Double.parseDouble(snapshot.child("latitude").getValue().toString()));
                            mEvent.setLongitude(Double.parseDouble(snapshot.child("longitude").getValue().toString()));
                            Log.d(TAG, "mEvent  casi" + mEvent.getDescription());
                            mEvent.setId(snapshot.getKey());

                            mNameView.setText(mEvent.getName());
                            mDescriptionView.setText(mEvent.getDescription());
                            mDateView.setText(mEvent.getDate());
                            Log.d(TAG, "ScoreV " + mScoreView.getText());
                            Log.d(TAG, "Score : " + mEvent.getName());
                            Log.d(TAG, "Score : " + mEvent);

                            String str = getString(R.string.event_points) + mEvent.getScore().toString();
                            mScoreView.setText(mEvent.getScore().toString());
                            mOwnerView.setText(mEvent.getOwner());//mUser.getName());
                            //mImageView.setImageBitmap(configImage(mEvent.getPicture()));
                            //String url = Config.getPhotoUrl(mEvent.getPicture());
                            //Log.d("PICASSO URL" , url);
                            //byte[] bitmapArray = Base64.decode(mEvent.getBitmapBase64(), Base64.DEFAULT);
                            //Bitmap bitmapdata = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
                            //mImageView.setImageBitmap(bitmapdata);
                            Picasso.with(v.getContext()).load(mEvent.getPicture()).into(mImageView);

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e(TAG, databaseError.toString());
                    }
                });

        return v;
    }

}
