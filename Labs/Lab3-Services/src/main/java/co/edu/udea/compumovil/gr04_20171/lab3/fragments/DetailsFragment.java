package co.edu.udea.compumovil.gr04_20171.lab3.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;

import co.edu.udea.compumovil.gr04_20171.lab3.R;
import co.edu.udea.compumovil.gr04_20171.lab3.models.Event;
import co.edu.udea.compumovil.gr04_20171.lab3.models.User;
import co.edu.udea.compumovil.gr04_20171.lab3.services.Config;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsFragment extends Fragment {

    public TextView mNameView;
    public TextView mDescriptionView;
    public TextView mDateView;
    public TextView mScoreView;
    public TextView mOwnerView;
    public ImageView mImageView;

    private Event mEvent;

    public DetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mEvent = getArguments().getParcelable("EVENT");
            //mUser = getArguments().getParcelable("USER");


        }
        //Foto, ubicación,
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_details, container, false);

        mImageView = (ImageView) v.findViewById(R.id.details_picture);
        mNameView = (TextView) v.findViewById(R.id.details_name);
        mDescriptionView = (TextView) v.findViewById(R.id.details_description);
        mDateView = (TextView) v.findViewById(R.id.details_date);
        mScoreView = (TextView) v.findViewById(R.id.details_score);
        mOwnerView = (TextView) v.findViewById(R.id.details_owner_name);
        mImageView = (ImageView) v.findViewById(R.id.details_picture);

        mNameView.setText(mEvent.getName());
        mDescriptionView.setText(mEvent.getDescription());
        mDateView.setText(mEvent.getDate());
        String str = getString(R.string.event_points) + mEvent.getScore().toString();
        mScoreView.setText(str);
        mOwnerView.setText(mEvent.getOwner());//mUser.getName());
        //mImageView.setImageBitmap(configImage(mEvent.getPicture()));
        String url = Config.getPhotoUrl(mEvent.getPicture());
        Log.d("PICASSO URL" , url);
        //byte[] bitmapArray = Base64.decode(mEvent.getBitmapBase64(), Base64.DEFAULT);
        //Bitmap bitmapdata = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
        //mImageView.setImageBitmap(bitmapdata);
        Picasso.with(getActivity().getApplicationContext()).load(url).into(mImageView);
        return v;
    }

    private Bitmap configImage(String picture) {
        Uri path = Uri.parse(picture);

        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), path);
            return Bitmap.createBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
