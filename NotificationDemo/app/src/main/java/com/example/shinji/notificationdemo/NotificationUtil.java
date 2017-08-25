package com.example.shinji.notificationdemo;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by shinji on 2017/08/25.
 */

public class NotificationUtil {
    // Notification ID used to access our notification after we displayed it.
    // This can be handy when we need to cancel the notification or update number is
    // arbitaty and can be set to anything.
    private static final int REMINDER_NOTIFICATION_ID = 1212;

    //helper methods called contentIntent with a single parameter context. It should return a pending intent.
    // This method will create a pending intent which trigger when notification is pressed. This pending intent should open up the maincactivity.
    private static final int REMINDER_PENDING_INTENT_ID = 2123;

    private static final int ACTION_DRINK_WATER_INTENTD_ID = 3324;
    private static final int ACTION_DRINK_IGNORE_INTENTD_ID = 324;

    private static PendingIntent contentIntent (Context context){
        //create an Intent that opens MainActivity
        Intent startActivityIntent = new Intent(context, Notification.class);
        //create a pending intent using get activity that will:
        //take the context passed as parametar ID and
        //when the notification is trigrred has the flag
        return PendingIntent.getActivity(
                context,
                REMINDER_PENDING_INTENT_ID,
                startActivityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
    };

    //  Create a helper method called largeIcon which takes in a Context as a parameter and
    // returns a Bitmap. This method is necessary to decode a bitmap needed for the notification.
    private static Bitmap largeIcon(Context context) {
        //  Get a Resources object from the context.
        Resources res = context.getResources();
        //  Create and return a bitmap using BitmapFactory.decodeResource, passing in the
        // resources object and R.drawable.ic_local_drink_black_24px
        Bitmap largeIcon = BitmapFactory.decodeResource(res, R.mipmap.ic_launcher);
        return largeIcon;
    }

    // lets start building the notification
    public static void reminderUser(Context context){
        //NotificationCompat.BUidler to create it
        /*
              - color of R.colorPrimary
              - small icon
              - title
              - content
              - style e.g bigText()
              - i.e. vibate
              - contentIntent
              - automatically cancel the notificaion when user click
        */
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setSmallIcon(R.drawable.ic_android_black_24dp)
                .setLargeIcon(largeIcon(context))
                .setContentTitle(context.getString(R.string.title))
                .setContentText(context.getString(R.string.content))
                //BigTextStyle: this time, its chose big notification
                .setStyle(new NotificationCompat.BigTextStyle().bigText(
                    context.getString(R.string.content)))
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentIntent(contentIntent(context))
                .addAction(drinkWaterAction(context))
                .addAction(ignoreReminderAction(context))
                .setAutoCancel(true);
        // If build version is greater than JELLYBEAN than set priority

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
            notificationBuilder.setPriority(Notification.PRIORITY_HIGH);
        }

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Trigger the notification by calling notify on the NotificationManager pass the ID value and buiod()
        notificationManager.notify(REMINDER_NOTIFICATION_ID, notificationBuilder.build());

    }
    //add a static method to drink
    private static NotificationCompat.Action drinkWaterAction(Context context){
        // create a intent to launch the reminder
        Intent drinkWaterIntent = new Intent(context, HandleAction.class);
        // set action of intent
        drinkWaterIntent.setAction(HandleAction.ACTION_DRINK_WATER);
        // create a pending intent to launch the intentSer
        PendingIntent drinkWaterPI = PendingIntent.getService(
                context,
                ACTION_DRINK_WATER_INTENTD_ID,
                drinkWaterIntent,
                PendingIntent.FLAG_CANCEL_CURRENT
        );
        NotificationCompat.Action drinkwaterAction = new NotificationCompat.Action(R.mipmap.ic_launcher, "DRINK IT",
                drinkWaterPI);

        return drinkwaterAction;
        //
    }

    private static NotificationCompat.Action ignoreReminderAction(Context context){

        Intent ignoreWaterIntent = new Intent(context, HandleAction.class);
        ignoreWaterIntent.setAction(HandleAction.ACTION_IGNORE);
        PendingIntent ignoreWaterPI = PendingIntent.getService(
                context,
                ACTION_DRINK_IGNORE_INTENTD_ID,
                ignoreWaterIntent,
                PendingIntent.FLAG_CANCEL_CURRENT
        );
        NotificationCompat.Action ignorewaterAction = new NotificationCompat.Action(R.mipmap.ic_launcher, "DRINK IT",
                ignoreWaterPI);

        return ignorewaterAction;
    }

    public static void clearAllNotification(Context context){
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
    }


}
