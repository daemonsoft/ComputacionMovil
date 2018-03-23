package co.edu.udea.compumovil.gr04_20171.lab3.app;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

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
