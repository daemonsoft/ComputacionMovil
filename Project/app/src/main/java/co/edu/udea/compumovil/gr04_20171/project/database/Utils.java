package co.edu.udea.compumovil.gr04_20171.project.database;

import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import co.edu.udea.compumovil.gr04_20171.project.models.Table;


/**
 * Created by daemonsoft on 22/04/17.
 */

public class Utils {
    private static final String TAG = "Utils";
    private static FirebaseDatabase mDatabase;
    private static List<Table> tables;

    public static FirebaseDatabase getDatabase() {
        if (mDatabase == null) {
            mDatabase = FirebaseDatabase.getInstance();
            mDatabase.setPersistenceEnabled(true);
        }
        return mDatabase;
    }
}