package co.edu.udea.compumovil.gr04_20171.lab3.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

import co.edu.udea.compumovil.gr04_20171.lab3.app.App;
import co.edu.udea.compumovil.gr04_20171.lab3.models.Event;
import co.edu.udea.compumovil.gr04_20171.lab3.services.Config;

/**
 * Created by daemonsoft on 10/03/17.
 */

public class EventsDbUtil {

    public static Event getLastEvent(Context context){
        List<Event> eventList = getAllEvents(context);
        if (eventList.isEmpty()) return null;
        return eventList.get(eventList.size()-1);

    }
    public static List<Event> getAllRemoteEvents(final Context context){
        final List<Event> eventList = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Config.getEventsUrl(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("LOGIN", "Response: " + response.toString());

                        JsonParser jsonParser = new JsonParser();
                        JsonArray jo = (JsonArray) jsonParser.parse(response.toString());

                        Gson gson;
                        gson = new Gson();
                        //Type listType = new TypeToken<ArrayList<String>>(){}.getType();
                        // List<Event> events = gson.fromJson(jo, listType);
                        Event[] events = gson.fromJson(jo, Event[].class);
                        if (null != events) {
                            EventsDbUtil.deleteAllEvents(context);
                            for (Event e : events) {
                                eventList.add(e);
                                EventsDbUtil.insertEvent(e, context);
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        // Add the request to the RequestQueue.
        ((App) context.getApplicationContext()).getRequestQueueQueue().add(stringRequest);
        return eventList;
    }
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
    public static void deleteAllEvents(Context context){
        EventsDbHelper eventsDbHelper = new EventsDbHelper(context);
        SQLiteDatabase db = eventsDbHelper.getReadableDatabase();
        eventsDbHelper.deleteEventEntries(db);
        db.close();
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
