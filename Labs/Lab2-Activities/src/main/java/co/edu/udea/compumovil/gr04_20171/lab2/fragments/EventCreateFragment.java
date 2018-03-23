package co.edu.udea.compumovil.gr04_20171.lab2.fragments;


import android.Manifest;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import co.edu.udea.compumovil.gr04_20171.lab2.R;
import co.edu.udea.compumovil.gr04_20171.lab2.database.EventsDbUtil;
import co.edu.udea.compumovil.gr04_20171.lab2.models.Event;
import co.edu.udea.compumovil.gr04_20171.lab2.models.User;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.app.Activity.*;

public class EventCreateFragment extends Fragment implements View.OnClickListener {

    private final int MY_PERMISSIONS = 100;
    private final int PHOTO_CODE = 200;
    private final int SELECT_PICTURE = 300;

    View fragmentView;
    ImageView editImageView;
    ImageView imageView;
    View snackView;
    TextView textName;
    EditText textDesc;
    EditText textDate;
    TextView textLat;
    TextView textLong;
    TextView textLugar;
    RatingBar ratingScore;
    private String imgPath;
    String picture;
    String name;
    String description;
    String location;
    String date;
    Double lat;
    Double longitude;
    int score;

    boolean controlDatePicker = false;

    private User mUser;

    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        if (getArguments() != null) {
            mUser = getArguments().getParcelable("USER");
        }
        fragmentView = inflater.inflate(R.layout.fragment_event_create, parent, false);
        imageView = (ImageView) fragmentView.findViewById(R.id.imgEventLoad);
        editImageView = (ImageView) fragmentView.findViewById(R.id.editImgEventLoad);
        textName = (TextView) fragmentView.findViewById(R.id.eventNombre);
        textDesc = (EditText) fragmentView.findViewById(R.id.eventDesc);
        textDate = (EditText) fragmentView.findViewById(R.id.eventDate);
        textLugar = (EditText) fragmentView.findViewById(R.id.eventLugar);
        textLat = (TextView) fragmentView.findViewById(R.id.eventLat);
        textLong = (TextView) fragmentView.findViewById(R.id.eventLong);
        ratingScore = (RatingBar) fragmentView.findViewById(R.id.ratingScore);

        if (requestStoragePermission())
            imageView.setEnabled(true);
        else
            imageView.setEnabled(false);

        /*
        view.findViewById(R.id.editImgEventLoad)
        */
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galeria = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galeria, SELECT_PICTURE);
            }
        });
        textDate.setKeyListener(null);
        textDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (controlDatePicker) {
                    controlDatePicker = false;
                    return;
                } else
                    controlDatePicker = true;
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int dyear, int dmonth, int dday) {
                        if (String.valueOf(dyear).length() == 4)
                            textDate.setText(dday + "/" + (dmonth + 1) + "/" + dyear);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });
        return fragmentView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUser = getArguments().getParcelable("USER");
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button btnCreateEvent = (Button) view.findViewById(R.id.btnCreateEvent);
        btnCreateEvent.setOnClickListener(this);
    }

    private boolean requestStoragePermission() {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return true;

        if ((getActivity().checkSelfPermission(WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) &&
                (getActivity().checkSelfPermission(CAMERA) == PackageManager.PERMISSION_GRANTED))
            return true;

        if ((shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)) || (shouldShowRequestPermissionRationale(CAMERA))) {
            Snackbar.make(snackView, getString(R.string.need_permissions),
                    Snackbar.LENGTH_INDEFINITE).setAction(android.R.string.ok, new View.OnClickListener() {
                @TargetApi(Build.VERSION_CODES.M)
                @Override
                public void onClick(View v) {
                    requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, MY_PERMISSIONS);
                }
            });
        } else {
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, MY_PERMISSIONS);
        }

        return false;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("image", imgPath);
        //outState.putString("name", textName.getText().toString());
        //outState.putString("desc", textDesc.getText().toString());
        //outState.putString("lat", textLat.getText().toString());
        //outState.putString("long", textLong.getText().toString());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            if (savedInstanceState.getString("image") != null)
                imageView.setImageURI(Uri.parse(savedInstanceState.getString("image")));

            textName.setText(savedInstanceState.getString("name"));
            textDesc.setText(savedInstanceState.getString("desc"));
            textLat.setText(savedInstanceState.getString("lat"));
            textLong.setText(savedInstanceState.getString("long"));
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap;
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PHOTO_CODE:
                    MediaScannerConnection.scanFile(
                            getActivity().getApplicationContext(),
                            new String[]{imgPath}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String path, Uri uri) {
                                    //Log.i("ExternalStorage", "Scanned " + path + ":");
                                    // Log.i("ExternalStorage", "-> Uri = " + uri);
                                }
                            });


                    bitmap = BitmapFactory.decodeFile(imgPath);
                    imageView.setImageBitmap(bitmap);
                    break;
                case SELECT_PICTURE:
                    editImageView.setVisibility(View.VISIBLE);

                    Uri path = data.getData();
                    imgPath = data.getDataString();
                    imageView.setImageBitmap(Event.getRealPathFromURI(getActivity(), path, null));
                    break;

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission granted and now can proceed
                    // mymethod(); //a sample method called

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(getActivity(), "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            // add other cases for more permissions
        }
    }

    @Override
    public void onClick(View v) {

        if (!validate()) {
            Snackbar.make(fragmentView, "Valide todos los campos.", Snackbar.LENGTH_INDEFINITE);
            return;
        }

        final ProgressDialog progressDialog = new ProgressDialog(getActivity(),
                R.style.Theme_AppCompat_DayNight_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.creating_event));
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        getValues();
        if (null == picture){
            picture = "android.resource://co.edu.udea.compumovil.gr04_20171.lab2/drawable/noimage";
        }
        Event event = new Event(name, mUser.getEmail(), description, picture, date, lat, longitude, location, score);

        final long rowId = EventsDbUtil.insertEvent(event, getActivity().getApplicationContext());
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        if (rowId == -1) {
                            Snackbar.make(fragmentView, "No se pudo crear el evento.", Snackbar.LENGTH_INDEFINITE);
                        } else {
                            getActivity().finish();
                        }
                        progressDialog.dismiss();
                    }
                }, 1000);
    }

    public void getValues() {
        picture = imgPath;
        name = textName.getText().toString();
        name = textName.getText().toString();
        description = textDesc.getText().toString();
        date = textDate.getText().toString();
        location = textLugar.getText().toString();
        try {
            lat = Double.parseDouble(textLat.getText().toString());
        } catch (NumberFormatException e) {
            lat = 0d;
        }
        try {
            longitude = Double.parseDouble(textLong.getText().toString());
        } catch (NumberFormatException e) {
            longitude = 0d;
        }
        score = (int) ratingScore.getRating();
    }

    public boolean validate() {
        boolean valid = true;

        if (textName.getText().toString().isEmpty()) {
            textName.setError(getString(R.string.name_not_empty));
            valid = false;
        }

        if (textDesc.getText().toString().isEmpty()) {
            textDesc.setError(getString(R.string.description_not_empty));
            valid = false;
        }

        if (textLat.getText().toString().isEmpty()) {
            textLat.setError(getString(R.string.enter_latitude));
            valid = false;
        }

        if (textLong.getText().toString().isEmpty()) {
            textLong.setError(getString(R.string.enter_longitude));
            valid = false;
        }

        if (textLugar.getText().toString().isEmpty()) {
            textLugar.setError(getString(R.string.enter_place));
            valid = false;
        }

        return valid;
    }
}