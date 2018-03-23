package co.edu.udea.compumovil.gr04_20171.lab3.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by daemonsoft on 10/03/17.
 */

public class EventsDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 7;
    private static final String DATABASE_NAME = "mobileevents.db";
    private static final String TEXT_TYPE = " TEXT";
    private static final String PKEY_TYPE = " PRIMARY KEY";
    private static final String INT_TYPE = " INTEGER";
    private static final String DOUBLE_TYPE = " DOUBLE";
    private static final String COMMA_SEP = ", ";
    private static final String SQL_CREATE_EVENT_ENTRIES =
            "CREATE TABLE IF NOT EXISTS " + EventsContract.EventEntry.TABLE_NAME + " (" +
                    EventsContract.EventEntry._ID + INT_TYPE + PKEY_TYPE + COMMA_SEP +
                    EventsContract.EventEntry.COLUMN_NAME_EVENTNAME + TEXT_TYPE + COMMA_SEP +
                    EventsContract.EventEntry.COLUMN_NAME_DESCRIPTION + TEXT_TYPE + COMMA_SEP +
                    EventsContract.EventEntry.COLUMN_NAME_OWNER + TEXT_TYPE + COMMA_SEP +
                    EventsContract.EventEntry.COLUMN_NAME_DATE + TEXT_TYPE + COMMA_SEP +
                    EventsContract.EventEntry.COLUMN_NAME_LATITUDE + DOUBLE_TYPE + COMMA_SEP +
                    EventsContract.EventEntry.COLUMN_NAME_LONGITUDE + DOUBLE_TYPE + COMMA_SEP +
                    EventsContract.EventEntry.COLUMN_NAME_LOCATION + TEXT_TYPE + COMMA_SEP +
                    EventsContract.EventEntry.COLUMN_NAME_PICTURE + TEXT_TYPE + COMMA_SEP +
                    EventsContract.EventEntry.COLUMN_NAME_SCORE + INT_TYPE + ")";

    private static final String SQL_DELETE_EVENT_ENTRIES =
            "DELETE FROM " + EventsContract.EventEntry.TABLE_NAME;

    public EventsDbHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void deleteEventEntries(SQLiteDatabase db){
        db.execSQL(SQL_DELETE_EVENT_ENTRIES);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_EVENT_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_EVENT_ENTRIES);
        onCreate(db);
    }
}
