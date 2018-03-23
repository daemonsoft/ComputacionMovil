package co.edu.udea.compumovil.gr04_20171.lab3.fragments;


import android.Manifest;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.util.Base64;
import android.util.Log;
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

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;

import co.edu.udea.compumovil.gr04_20171.lab3.R;
import co.edu.udea.compumovil.gr04_20171.lab3.app.App;
import co.edu.udea.compumovil.gr04_20171.lab3.database.EventsDbUtil;
import co.edu.udea.compumovil.gr04_20171.lab3.models.Event;
import co.edu.udea.compumovil.gr04_20171.lab3.models.User;
import co.edu.udea.compumovil.gr04_20171.lab3.services.Config;
import cz.msebera.android.httpclient.Header;

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
    private boolean check;
    SharedPreferences pref;
    String message;
    boolean controlDatePicker = false;
    boolean post = true;
    Bitmap bitmap;

    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

        pref = getActivity().getSharedPreferences(Config.SHARED_PREF_NAME, MODE_PRIVATE);
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

                    Log.d("SELECT_PHOTO", "clicked");
                    bitmap = BitmapFactory.decodeFile(imgPath);
                    imageView.setImageBitmap(bitmap);
                    break;
                case SELECT_PICTURE:
                    editImageView.setVisibility(View.VISIBLE);
                    Log.d("SELECT_PICTURE", "clicked");
                    Uri selectedImage = data.getData();
                    imageView.setImageURI(selectedImage);
                    //Uri path = data.getData();

                    imgPath = data.getDataString();
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.d("BITMAP", bitmap.toString());
                    Log.d("PATH", selectedImage.toString());
                    //imageView.setImageBitmap(Event.getRealPathFromURI(getActivity(), path, null));
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


        getValues();

        Log.d("PICTURE", picture);
        final String picName = picture.substring(picture.lastIndexOf('/') + 1, picture.length());
        Log.d("PICNAME", picName);
        String url = Config.getPhotoPostUrl(picName);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String body = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        Request request = new JsonRequest<String>(Request.Method.POST, url, body, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                if (response.equals("notregistered")) {
                    check = false;
                    message = "No se pudo crear el evento.";
                } else {
                    check = true;
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                message = "Error de red";
                VolleyLog.e("Error: ", error.getMessage());
                check = false;
            }
        }) {

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String jsonString = null;
                try {
                    jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                    Log.d("NET_RESPONSE", jsonString);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                Response<String> result = Response.success(jsonString, HttpHeaderParser.parseCacheHeaders(response));
                return result;
            }
        };

        Log.d("Request", request.toString());
        progressDialog.show();
        ((App) getActivity().getApplication()).getRequestQueueQueue().add(request);


        Log.d("PICNAME", picName);
        final Event event = new Event(name, pref.getString(Config.EMAIL_SHARED_PREF, ""), description, picName, date, lat, longitude, location, score);


        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        if (check) {
                            String urle = Config.getEventPostUrl();
                            String body = new GsonBuilder().create().toJson(event);
                            Request request = new JsonRequest<String>(Request.Method.POST, urle, body, new Response.Listener<String>() {

                                @Override
                                public void onResponse(String response) {
                                    if ("notregistered".equals(response)) {
                                        check = false;
                                        message = "No se pudo crear el evento.";
                                    } else {

                                        check = true;
                                    }
                                }
                            }, new Response.ErrorListener() {

                                @Override
                                public void onErrorResponse(VolleyError error) {

                                    check = false;
                                    message = "Network error";
                                    VolleyLog.e("Error: ", error.getMessage());
                                }
                            }) {

                                @Override
                                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                                    String jsonString = null;
                                    try {
                                        jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                                        Log.d("NET_RESPONSE", jsonString);
                                    } catch (UnsupportedEncodingException e) {
                                        e.printStackTrace();
                                    }
                                    Response<String> result = Response.success(jsonString, HttpHeaderParser.parseCacheHeaders(response));
                                    return result;
                                }
                            };

                            Log.d("Request", request.toString());
                            ((App) getActivity().getApplication()).getRequestQueueQueue().add(request);
                            if (check) {
                                message = "Evento creado";
                                final long rowId = EventsDbUtil.insertEvent(event, getActivity().getApplicationContext());
                                getActivity().finish();
                            }
                        }
                            Snackbar.make(fragmentView, message, Snackbar.LENGTH_LONG);

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

        if (bitmap == null) {
            Snackbar.make(fragmentView, "Debe seleccionar una imagen", Snackbar.LENGTH_LONG).show();
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