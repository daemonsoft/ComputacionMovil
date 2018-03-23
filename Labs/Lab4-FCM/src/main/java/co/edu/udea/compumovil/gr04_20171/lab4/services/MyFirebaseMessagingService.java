package co.edu.udea.compumovil.gr04_20171.lab4.services;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by daemonsoft on 25/04/17.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFMService";

    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Handle data payload of FCM messages.
        Log.d(TAG, "FCM Message Id: " + remoteMessage.getMessageId());
        Log.d(TAG, "FCM Notification Message: " + remoteMessage.getNotification());
        Log.d(TAG, "FCM Data Message: " + remoteMessage.getData());
        try {
            sendNotification(remoteMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void sendNotification(final RemoteMessage remoteMessage) throws Exception {

        Calendar calendar = Calendar.getInstance();
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("h:mm a");
        String strDate = sdf.format(c.getTime());

        String contentTitle = "Hay un nuevo evento";
        String contentText = "Recibido: " + strDate;

    }

}
