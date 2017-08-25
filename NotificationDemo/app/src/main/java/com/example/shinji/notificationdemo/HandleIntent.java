package com.example.shinji.notificationdemo;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * Created by shinji on 2017/08/25.
 */

public class HandleIntent extends IntentService {
    public HandleIntent(String name) {
        super("HandleIntent");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String action = intent.getAction();
        HandleAction.excuteTask(this, action);
    }
}
