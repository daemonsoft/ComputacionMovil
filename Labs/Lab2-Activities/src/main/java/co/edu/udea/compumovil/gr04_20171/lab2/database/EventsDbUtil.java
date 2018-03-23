package co.edu.udea.compumovil.gr04_20171.lab2.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import co.edu.udea.compumovil.gr04_20171.lab2.models.Event;
import co.edu.udea.compumovil.gr04_20171.lab2.models.User;

/**
 * Created by daemonsoft on 10/03/17.
 */

public class EventsDbUtil {
    public static List<Event> getAllEvents(Context context) {
        EventsDbHelper eventsDbHelper = new EventsDbHelper(context);
        SQLiteDatabase db = eventsDbHelper.getReadableDatabase();

        Cursor c = db.query(EventsContract.EventEntry.TABLE_NAME,
                null, null, null, null, null, null);
        Event event;
        List<Event> eventList = new ArrayList<>();
        while (c.moveToNext()) {
            event = new Event();
            event.setName(c.getString(c.getColumnIndex(EventsContract.EventEntry.COLUMN_NAME_EVENTNAME)));
            event.setPicture(c.getString(c.getColumnIndex(EventsContract.EventEntry.COLUMN_NAME_PICTURE)));
            event.setDate(c.getString(c.getColumnIndex(EventsContract.EventEntry.COLUMN_NAME_DATE)));
            event.setDescription(c.getString(c.getColumnIndex(EventsContract.EventEntry.COLUMN_NAME_DESCRIPTION)));
            event.setLatitude(c.getDouble(c.getColumnIndex(EventsContract.EventEntry.COLUMN_NAME_LATITUDE)));
            event.setLongitude(c.getDouble(c.getColumnIndex(EventsContract.EventEntry.COLUMN_NAME_LONGITUDE)));
            event.setScore(c.getInt(c.getColumnIndex(EventsContract.EventEntry.COLUMN_NAME_SCORE)));
            event.setOwner(c.getString(c.getColumnIndex(EventsContract.EventEntry.COLUMN_NAME_OWNER)));
            event.setLocation(c.getString(c.getColumnIndex(EventsContract.EventEntry.COLUMN_NAME_LOCATION)));
            event.setPicture(c.getString(c.getColumnIndex(EventsContract.EventEntry.COLUMN_NAME_PICTURE)));

            eventList.add(event);
        }
        db.close();
        return eventList;
    }

    public static User getUser(String email, Context context) {
        EventsDbHelper eventsDbHelper = new EventsDbHelper(context);
        SQLiteDatabase db = eventsDbHelper.getReadableDatabase();

        Cursor c = db.query(EventsContract.UserEntry.TABLE_NAME,
                null,
                EventsContract.UserEntry.COLUMN_NAME_EMAIL + "=?",
                new String[]{email},
                null,
                null,
                null);
        User user = null;
        if (c.moveToFirst()) {
            user = new User();
            user.setId(c.getInt(c.getColumnIndex(EventsContract.UserEntry.ID)));
            user.setName(c.getString(c.getColumnIndex(EventsContract.UserEntry.COLUMN_NAME_USERNAME)));
            user.setPassword(c.getString(c.getColumnIndex(EventsContract.UserEntry.COLUMN_NAME_PASSWORD)));
            user.setEmail(c.getString(c.getColumnIndex(EventsContract.UserEntry.COLUMN_NAME_EMAIL)));
            user.setIsLogged(c.getInt(c.getColumnIndex(EventsContract.UserEntry.COLUMN_NAME_ISLOGGED)));
            user.setAge(c.getInt(c.getColumnIndex(EventsContract.UserEntry.COLUMN_NAME_AGE)));
        }
        c.close();
        db.close();
        return user;
    }

    public static User getUserLogged(Context context) {
        EventsDbHelper eventsDbHelper = new EventsDbHelper(context);
        SQLiteDatabase db = eventsDbHelper.getReadableDatabase();
        String selection = EventsContract.UserEntry.COLUMN_NAME_ISLOGGED + " = ?";
        String[] selectionArgs = {1 + ""};
        Cursor c = db.query(
                EventsContract.UserEntry.TABLE_NAME,  // The table to query
                null,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );
        User user = null;
        if (c.moveToFirst()) {
            user = new User();
            user.setId(c.getInt(c.getColumnIndex(EventsContract.UserEntry.ID)));
            user.setName(c.getString(c.getColumnIndex(EventsContract.UserEntry.COLUMN_NAME_USERNAME)));
            user.setPassword(c.getString(c.getColumnIndex(EventsContract.UserEntry.COLUMN_NAME_PASSWORD)));
            user.setEmail(c.getString(c.getColumnIndex(EventsContract.UserEntry.COLUMN_NAME_EMAIL)));
            user.setIsLogged(c.getInt(c.getColumnIndex(EventsContract.UserEntry.COLUMN_NAME_ISLOGGED)));
            user.setAge(c.getInt(c.getColumnIndex(EventsContract.UserEntry.COLUMN_NAME_AGE)));
        }
        c.close();
        db.close();
        return user;
    }

    public static boolean userLogin(String email, String password, int keepLogged, Context context) {

        User user = getUser(email, context);

        if (user != null && user.getPassword().equals(password)) {
            user.setIsLogged(keepLogged);
            updateUser(user, context);
            return true;
        } else {
            return false;
        }
    }

    public static void updateUser(User user, Context context) {
        EventsDbHelper eventsDbHelper = new EventsDbHelper(context);
        SQLiteDatabase db = eventsDbHelper.getReadableDatabase();
        // New value for one column
        ContentValues values = new ContentValues();

        values.put(EventsContract.UserEntry.COLUMN_NAME_USERNAME, user.getName());
        values.put(EventsContract.UserEntry.COLUMN_NAME_EMAIL, user.getEmail());
        values.put(EventsContract.UserEntry.COLUMN_NAME_PASSWORD, user.getPassword());
        values.put(EventsContract.UserEntry.COLUMN_NAME_ISLOGGED, user.getIsLogged());
        values.put(EventsContract.UserEntry.COLUMN_NAME_AGE, user.getAge());

        // Which row to update, based on the id
        String selection = EventsContract.UserEntry._ID + " LIKE ?";
        String[] selectionArgs = {user.getId() + ""};

        int count = db.update(
                EventsContract.UserEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);
        db.close();
    }

    public static void userSignOut(String email, Context context) {
        EventsDbHelper eventsDbHelper = new EventsDbHelper(context);
        SQLiteDatabase db = eventsDbHelper.getReadableDatabase();
        User user = getUser(email, context);
        user.setIsLogged(0);
        updateUser(user, context);
        db.close();
    }

    public static long insertUser(User user, Context context) {
        EventsDbHelper eventsDbHelper = new EventsDbHelper(context);
        SQLiteDatabase db = eventsDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(EventsContract.UserEntry.COLUMN_NAME_USERNAME, user.getName());
        values.put(EventsContract.UserEntry.COLUMN_NAME_EMAIL, user.getEmail());
        values.put(EventsContract.UserEntry.COLUMN_NAME_PASSWORD, user.getPassword());
        values.put(EventsContract.UserEntry.COLUMN_NAME_ISLOGGED, 0);
        values.put(EventsContract.UserEntry.COLUMN_NAME_AGE, 0);

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(EventsContract.UserEntry.TABLE_NAME, null, values);
        db.close();
        return newRowId;
    }

    public static long insertEvent(Event event, Context context) {
        EventsDbHelper eventsDbHelper = new EventsDbHelper(context);
        SQLiteDatabase db = eventsDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(EventsContract.EventEntry.COLUMN_NAME_EVENTNAME, event.getName());
        values.put(EventsContract.EventEntry.COLUMN_NAME_DESCRIPTION, event.getDescription());
        values.put(EventsContract.EventEntry.COLUMN_NAME_PICTURE, event.getPicture());
        values.put(EventsContract.EventEntry.COLUMN_NAME_SCORE, event.getScore());
        values.put(EventsContract.EventEntry.COLUMN_NAME_OWNER, event.getOwner());
        values.put(EventsContract.EventEntry.COLUMN_NAME_DATE, event.getDate());
        values.put(EventsContract.EventEntry.COLUMN_NAME_LATITUDE, event.getLatitude());
        values.put(EventsContract.EventEntry.COLUMN_NAME_LONGITUDE, event.getLongitude());
        values.put(EventsContract.EventEntry.COLUMN_NAME_LOCATION, event.getLocation());

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(EventsContract.EventEntry.TABLE_NAME, null, values);
        db.close();
        return newRowId;
    }
}
