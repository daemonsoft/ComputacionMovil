package co.edu.udea.compumovil.gr04_20171.lab4.widgets;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.util.Log;
import android.widget.RemoteViews;

import co.edu.udea.compumovil.gr04_20171.lab4.R;
import co.edu.udea.compumovil.gr04_20171.lab4.models.Event;

/**
 * Implementation of App Widget functionality.
 */
public class LastEventWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
//        Event event = EventsDbUtil.getLastEvent(context);
//
//
//        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.last_event_widget);
//        if (null != event) {
//            views.setTextViewText(R.id.widget_name, event.getName());
//            views.setTextViewText(R.id.widget_description, event.getDescription());
//            views.setTextViewText(R.id.widget_date, event.getDate());
//        } else {
//            views.setTextViewText(R.id.widget_name, "No hay evento para mostrar");
//        }
        //Bitmap b = Picasso.with(context).load(Config.getPhotoUrl(event.getPicture())).resize(200, 200).get();

        //Log.d("WIDGET", "bitmap loaded");
        //views.setImageViewResource(R.id.widget_image, b.);
        //views.setImageViewResource(R.id.widget_image, R.drawable.aguiladescalza);
        //Picasso.with(context).load().resize(50, 50).into(views.R.id.widget_image);
        Log.d("WIDGET", "onUpdateAppWidget");
        // Instruct the widget manager to update the widget
       // appWidgetManager.updateAppWidget(appWidgetId, views);

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            Log.d("WIDGET", "for onUpdate");
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
        Log.d("WIDGET", "onEnabled");
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
        Log.d("WIDGET", "onDisabled");
    }
}

