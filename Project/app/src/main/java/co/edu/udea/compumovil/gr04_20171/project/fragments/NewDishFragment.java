package co.edu.udea.compumovil.gr04_20171.project.fragments;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

import co.edu.udea.compumovil.gr04_20171.project.R;
import co.edu.udea.compumovil.gr04_20171.project.database.Utils;
import co.edu.udea.compumovil.gr04_20171.project.models.Dish;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewDishFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "NewDishFragment";
    private final int MY_PERMISSIONS = 100;
    private final int PHOTO_CODE = 200;
    private final int SELECT_PICTURE = 300;
    private View fragmentView;
    private View snackView;
    private ImageButton imageView;
    private EditText textName;
    private EditText textDesc;
    private EditText textPrice;
    private Bitmap bitmap;
    private Uri selectedImage;
    private String imgPath;
    private String name;
    private String description;
    private Integer price;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;


    public NewDishFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = Utils.getDatabase().getReference("user").child(firebaseAuth.getCurrentUser().getUid());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);


        fragmentView = inflater.inflate(R.layout.fragment_new_dish, container, false);
        imageView = (ImageButton) fragmentView.findViewById(R.id.dish_image);

        textName = (EditText) fragmentView.findViewById(R.id.dish_name);
        textDesc = (EditText) fragmentView.findViewById(R.id.dish_description);
        textPrice = (EditText) fragmentView.findViewById(R.id.dish_price);
        if (requestStoragePermission())
            imageView.setEnabled(true);
        else
            imageView.setEnabled(false);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galeria = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galeria, SELECT_PICTURE);
            }
        });
        return fragmentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Button btnCreateTable = (Button) view.findViewById(R.id.btn_new_table);
        //btnCreateTable.setOnClickListener(this);
        Button buttonNewDish = (Button) view.findViewById(R.id.button_add_dish);
        buttonNewDish.setOnClickListener(this);
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


                    bitmap = BitmapFactory.decodeFile(imgPath);
                    imageView.setImageBitmap(bitmap);
                    break;
                case SELECT_PICTURE:

                    selectedImage = data.getData();
                    imageView.setImageURI(selectedImage);
                    //Uri path = data.getData();

                    imgPath = data.getDataString();
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
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
            Snackbar.make(fragmentView, "Faltan datos.", Snackbar.LENGTH_INDEFINITE);
            return;
        }

        final ProgressDialog progressDialog = new ProgressDialog(getActivity(),
                R.style.Theme_AppCompat_DayNight_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.adding));
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        final Dish dish = new Dish(name, price, description);
        final String uniqueID = UUID.randomUUID().toString();
        storageReference.child(firebaseAuth.getCurrentUser().getUid() + "/" + uniqueID).putFile(selectedImage).addOnCompleteListener(getActivity(),
                new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    @SuppressWarnings("VisibleForTests")
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {

                            dish.setPicture(task.getResult().getMetadata().getDownloadUrl().toString());
                            databaseReference.child("menu/dishes").push().setValue(dish);
                            progressDialog.dismiss();
                            getActivity().finish();
                        } else {
                            progressDialog.dismiss();

                        }
                    }
                });
    }


    public boolean validate() {
        boolean valid = true;

        if (textName.getText().toString().isEmpty()) {
            textName.setError(getString(R.string.cannot_be_empty));
            valid = false;
        }

        if (bitmap == null) {
            Snackbar.make(fragmentView, "Debe seleccionar una imagen", Snackbar.LENGTH_LONG).show();
            valid = false;
        }

        if (textDesc.getText().toString().isEmpty()) {
            textDesc.setError(getString(R.string.cannot_be_empty));
            valid = false;
        }

        if (textPrice.getText().toString().isEmpty()) {
            textPrice.setError(getString(R.string.cannot_be_empty));
            valid = false;
        }
        name = textName.getText().toString();
        description = textDesc.getText().toString();

        try {
            price = Integer.parseInt((textPrice.getText().toString()));
        } catch (NumberFormatException e) {
            price = 0;
        }
        return valid;
    }
}
