package co.edu.udea.compumovil.gr04_20171.lab3.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

import co.edu.udea.compumovil.gr04_20171.lab3.R;
import co.edu.udea.compumovil.gr04_20171.lab3.app.App;
import co.edu.udea.compumovil.gr04_20171.lab3.database.EventsDbUtil;
import co.edu.udea.compumovil.gr04_20171.lab3.models.Event;
import co.edu.udea.compumovil.gr04_20171.lab3.models.User;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class RefreshEventService extends IntentService {

    public RefreshEventService() {
        super("RefreshEventService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(android.R.drawable.stat_sys_download_done)
                .setContentTitle("Actualizando Eventos")
                .setContentText("Procesando...");

        try {
            while (true)
            //for ( int i = 0; i < 10; i++)
            {
                getEvents();
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                Thread.sleep(sharedPreferences.getInt("mili", 60000));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopForeground(true);
    }

    private void getEvents() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(android.R.drawable.stat_sys_download_done)
                .setContentTitle("Actualizando Eventos")
                .setContentText("Procesando...");
        //builder.setProgress(10, 10, false);
        startForeground(1, builder.build());

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Config.getEventsUrl(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("UPDATE_SERVICE", "Response: " + response.toString());

                        JsonParser jsonParser = new JsonParser();
                        JsonArray jo = (JsonArray) jsonParser.parse(response.toString());

                        Gson gson = new Gson();
                        Event[] events = gson.fromJson(jo, Event[].class);
                        if (null != events) {
                            EventsDbUtil.deleteAllEvents(getApplicationContext());
                            for (Event e : events) {
                                Event event = new Event(e.getName(), e.getOwner(), e.getDescription(),
                                        e.getPicture(), e.getDate(), e.getLatitude(),
                                        e.getLongitude(), e.getLocation(), e.getScore());
                                final long rowId = EventsDbUtil.insertEvent(event, getApplicationContext());
                                stopForeground(true);
                                if (rowId == -1) {
                                } else {
                                }
                            }
                        } else {

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        // Add the request to the RequestQueue.
        ((App) getApplication()).getRequestQueueQueue().add(stringRequest);
    }
}
