package com.example.shinji.notificationdemo;

import android.content.Context;

/**
 * Created by shinji on 2017/08/25.
 */

public class HandleAction {
    public static final String ACTION_DRINK_WATER = "drink";
    public static final String ACTION_IGNORE = "dismiss";

    public static void excuteTask(Context context, String action){
        if(ACTION_DRINK_WATER.equals(action)){
            NotificationUtil.clearAllNotification(context);
        }else if(ACTION_IGNORE.equals(action)){
            NotificationUtil.clearAllNotification(context);
        }else{

        }
    }


}
