package co.edu.udea.compumovil.gr04_20171.lab4.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

//import co.edu.udea.compumovil.gr04_20171.lab4.services.RefreshEventService;

public class BootReceiverBroadcast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //Intent i = new Intent(context, RefreshEventService.class);
        //context.startService(i);
    }
}
