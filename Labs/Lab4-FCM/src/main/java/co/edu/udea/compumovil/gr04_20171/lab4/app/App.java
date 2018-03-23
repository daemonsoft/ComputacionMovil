package co.edu.udea.compumovil.gr04_20171.lab4.app;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by daemonsoft on 2/04/17.
 */

public class App extends Application {
    private RequestQueue queue;
    @Override
    public void onCreate() {
        super.onCreate();
        queue = Volley.newRequestQueue(this);
    }
    public RequestQueue getRequestQueueQueue(){
        return queue;
    }
}
