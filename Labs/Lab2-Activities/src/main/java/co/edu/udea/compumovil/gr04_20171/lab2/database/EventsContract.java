package co.edu.udea.compumovil.gr04_20171.lab2.database;

import android.provider.BaseColumns;

/**
 * Created by daemonsoft on 10/03/17.
 */

public final class EventsContract {

    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private EventsContract() {
    }


    /* Inner class that defines the user table contents */
    public static class UserEntry implements BaseColumns {
        public static final String ID = BaseColumns._ID; //El ID se suele definir así por convención
        public static final String TABLE_NAME = "user";
        public static final String COLUMN_NAME_USERNAME = "name";
        public static final String COLUMN_NAME_EMAIL = "email";
        public static final String COLUMN_NAME_PASSWORD = "password";
        public static final String COLUMN_NAME_AGE = "age";
        public static final String COLUMN_NAME_ISLOGGED = "islogged";

    }

    /* Inner class that defines the event table contents */
    public static class EventEntry implements BaseColumns {
        public static final String ID = BaseColumns._ID; //El ID se suele definir así por convención
        public static final String TABLE_NAME = "event";
        public static final String COLUMN_NAME_EVENTNAME = "name";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_PICTURE = "picture";
        public static final String COLUMN_NAME_SCORE = "score";
        public static final String COLUMN_NAME_OWNER = "owner";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_LATITUDE = "latitude";
        public static final String COLUMN_NAME_LONGITUDE = "longitude";
        public static final String COLUMN_NAME_LOCATION = "location";

    }

}
