package co.edu.udea.compumovil.gr04_20171.lab4.database;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import co.edu.udea.compumovil.gr04_20171.lab4.models.Event;


/**
 * Created by daemonsoft on 22/04/17.
 */

public class Utils {
    private static final String TAG = "Utils";
    private static FirebaseDatabase mDatabase;
    private static Event mEvent;

    public static FirebaseDatabase getDatabase() {
        if (mDatabase == null) {
            mDatabase = FirebaseDatabase.getInstance();
            mDatabase.setPersistenceEnabled(true);
        }
        return mDatabase;
    }

    public static Event getEvent(String key) {
        mEvent = new Event();
        Log.d(TAG, "getEventSnap ");
        getDatabase().getReference().child("event").child(key)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        Log.d(TAG, "Snap " + snapshot.toString());
                        if (null != snapshot.getValue()) {

                            mEvent.setName(snapshot.child("name").getValue().toString());
                            mEvent.setLocation(snapshot.child("location").getValue().toString());
                            mEvent.setScore(Integer.parseInt(snapshot.child("score").getValue().toString()));
                            mEvent.setPicture(snapshot.child("picture").getValue().toString());
                            mEvent.setOwner(snapshot.child("owner").getValue().toString());
                            mEvent.setDescription(snapshot.child("description").getValue().toString());
                            mEvent.setDate(snapshot.child("date").getValue().toString());
                            mEvent.setLatitude(Double.parseDouble(snapshot.child("latitude").getValue().toString()));
                            mEvent.setLongitude(Double.parseDouble(snapshot.child("longitude").getValue().toString()));

                            mEvent.setId(snapshot.getKey());


                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e(TAG, databaseError.toString());
                    }
                });
        Log.d(TAG, "mEvent  full" + mEvent.getDescription());
        return mEvent;
    }
}