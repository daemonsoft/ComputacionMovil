package co.edu.udea.compumovil.gr04_20171.lab2.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by daemonsoft on 10/03/17.
 */

public class EventsDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 20;
    private static final String DATABASE_NAME = "events.db";
    private static final String TEXT_TYPE = " TEXT";
    private static final String PKEY_TYPE = " PRIMARY KEY";
    private static final String INT_TYPE = " INTEGER";
    private static final String DOUBLE_TYPE = " DOUBLE";
    private static final String COMMA_SEP = ", ";
    private static final String SQL_CREATE_USER_ENTRIES =
            "CREATE TABLE " + EventsContract.UserEntry.TABLE_NAME + " (" +
                    EventsContract.UserEntry._ID + INT_TYPE + PKEY_TYPE + COMMA_SEP +
                    EventsContract.UserEntry.COLUMN_NAME_USERNAME + TEXT_TYPE + COMMA_SEP +
                    EventsContract.UserEntry.COLUMN_NAME_EMAIL + TEXT_TYPE + COMMA_SEP +
                    EventsContract.UserEntry.COLUMN_NAME_PASSWORD + TEXT_TYPE + COMMA_SEP +
                    EventsContract.UserEntry.COLUMN_NAME_AGE + INT_TYPE + COMMA_SEP +
                    EventsContract.UserEntry.COLUMN_NAME_ISLOGGED + INT_TYPE + " )";
    private static final String SQL_CREATE_EVENT_ENTRIES =
            "CREATE TABLE " + EventsContract.EventEntry.TABLE_NAME + " (" +
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

    private static final String SQL_DELETE_USER_ENTRIES =
            "DROP TABLE IF EXISTS " + EventsContract.UserEntry.TABLE_NAME;
    private static final String SQL_DELETE_EVENT_ENTRIES =
            "DROP TABLE IF EXISTS " + EventsContract.EventEntry.TABLE_NAME;

    public EventsDbHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_USER_ENTRIES);
        db.execSQL(SQL_CREATE_EVENT_ENTRIES);
        ContentValues values = new ContentValues();


        // Pares clave-valor
        values.put(EventsContract.UserEntry.COLUMN_NAME_USERNAME, "admin");
        values.put(EventsContract.UserEntry.COLUMN_NAME_PASSWORD, "admin");
        values.put(EventsContract.UserEntry.COLUMN_NAME_EMAIL, "ad@min.com");
        values.put(EventsContract.UserEntry.COLUMN_NAME_AGE, "0");
        values.put(EventsContract.UserEntry.COLUMN_NAME_ISLOGGED, "0");

        // Insertar...
        db.insert(EventsContract.UserEntry.TABLE_NAME, null, values);

        values = new ContentValues();
        String uri = "android.resource://co.edu.udea.compumovil.gr04_20171.lab2/drawable/";
        // Pares clave-valor
        values.put(EventsContract.EventEntry.COLUMN_NAME_EVENTNAME, "Nacional vs Medellin");
        values.put(EventsContract.EventEntry.COLUMN_NAME_DESCRIPTION, "Encuentro deportivo");
        values.put(EventsContract.EventEntry.COLUMN_NAME_DATE, "4/12/2017");
        values.put(EventsContract.EventEntry.COLUMN_NAME_LATITUDE, "6.2572106");
        values.put(EventsContract.EventEntry.COLUMN_NAME_LONGITUDE, "-75.591042");
        values.put(EventsContract.EventEntry.COLUMN_NAME_SCORE, "3");
        values.put(EventsContract.EventEntry.COLUMN_NAME_OWNER, "ad@min.com");
        values.put(EventsContract.EventEntry.COLUMN_NAME_LOCATION, "Unidad+Deportiva+Estadio+Atanasio+Girardot");
        values.put(EventsContract.EventEntry.COLUMN_NAME_PICTURE, uri + "nalvsmed");

        // Insertar...
        db.insert(EventsContract.EventEntry.TABLE_NAME, null, values);

        values = new ContentValues();

        // Pares clave-valor
        values.put(EventsContract.EventEntry.COLUMN_NAME_EVENTNAME, "Trapitos al sol");
        values.put(EventsContract.EventEntry.COLUMN_NAME_DESCRIPTION, "Obra de teatro el aguila descalza");
        values.put(EventsContract.EventEntry.COLUMN_NAME_DATE, "3/22/2017");
        values.put(EventsContract.EventEntry.COLUMN_NAME_LATITUDE, "6.2546893");
        values.put(EventsContract.EventEntry.COLUMN_NAME_LONGITUDE, "-75.5627941");
        values.put(EventsContract.EventEntry.COLUMN_NAME_SCORE, "4");
        values.put(EventsContract.EventEntry.COLUMN_NAME_OWNER, "ad@min.com");
        values.put(EventsContract.EventEntry.COLUMN_NAME_LOCATION, "Teatro+Prado+el+Aguila+Descalza");
        values.put(EventsContract.EventEntry.COLUMN_NAME_PICTURE, uri + "aguiladescalza");

        // Insertar...
        db.insert(EventsContract.EventEntry.TABLE_NAME, null, values);

        values = new ContentValues();

        // Pares clave-valor
        values.put(EventsContract.EventEntry.COLUMN_NAME_EVENTNAME, "Caminata parque arví");
        values.put(EventsContract.EventEntry.COLUMN_NAME_DESCRIPTION, "Caminata por los senderos");
        values.put(EventsContract.EventEntry.COLUMN_NAME_DATE, "1/6/2017");
        values.put(EventsContract.EventEntry.COLUMN_NAME_LATITUDE, "6.2891132");
        values.put(EventsContract.EventEntry.COLUMN_NAME_LONGITUDE, "-75.5115368");
        values.put(EventsContract.EventEntry.COLUMN_NAME_SCORE, "5");
        values.put(EventsContract.EventEntry.COLUMN_NAME_OWNER, "ad@min.com");
        values.put(EventsContract.EventEntry.COLUMN_NAME_LOCATION, "Parque+Arví");
        values.put(EventsContract.EventEntry.COLUMN_NAME_PICTURE, uri + "arvi");

        // Insertar...
        db.insert(EventsContract.EventEntry.TABLE_NAME, null, values);


        values = new ContentValues();

        // Pares clave-valor
        values.put(EventsContract.EventEntry.COLUMN_NAME_EVENTNAME, "Ciclopaseo parque arví");
        values.put(EventsContract.EventEntry.COLUMN_NAME_DESCRIPTION, "Paseo en bicicleta por los senderos");
        values.put(EventsContract.EventEntry.COLUMN_NAME_DATE, "8/7/2017");
        values.put(EventsContract.EventEntry.COLUMN_NAME_LATITUDE, "6.2891132");
        values.put(EventsContract.EventEntry.COLUMN_NAME_LONGITUDE, "-75.5115368");
        values.put(EventsContract.EventEntry.COLUMN_NAME_SCORE, "5");
        values.put(EventsContract.EventEntry.COLUMN_NAME_OWNER, "ad@min.com");
        values.put(EventsContract.EventEntry.COLUMN_NAME_LOCATION, "Parque+Arví");
        values.put(EventsContract.EventEntry.COLUMN_NAME_PICTURE, uri + "arvi");

        // Insertar...
        db.insert(EventsContract.EventEntry.TABLE_NAME, null, values);

        values = new ContentValues();

        // Pares clave-valor
        values.put(EventsContract.EventEntry.COLUMN_NAME_EVENTNAME, "Carrera patinaje");
        values.put(EventsContract.EventEntry.COLUMN_NAME_DESCRIPTION, "Campeonato nacional de pista");
        values.put(EventsContract.EventEntry.COLUMN_NAME_DATE, "26/8/2017");
        values.put(EventsContract.EventEntry.COLUMN_NAME_LATITUDE, "6.1689853");
        values.put(EventsContract.EventEntry.COLUMN_NAME_LONGITUDE, "-75.5980797");
        values.put(EventsContract.EventEntry.COLUMN_NAME_SCORE, "4");
        values.put(EventsContract.EventEntry.COLUMN_NAME_OWNER, "ad@min.com");
        values.put(EventsContract.EventEntry.COLUMN_NAME_LOCATION, "Club+de+Patinaje+Envigado");
        values.put(EventsContract.EventEntry.COLUMN_NAME_PICTURE, uri + "clubpaen");

        // Insertar...
        db.insert(EventsContract.EventEntry.TABLE_NAME, null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_EVENT_ENTRIES);
        db.execSQL(SQL_DELETE_USER_ENTRIES);
        onCreate(db);
    }
}
