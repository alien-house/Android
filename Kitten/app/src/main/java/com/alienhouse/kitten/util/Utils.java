package com.alienhouse.kitten.util;

/**
 * Created by shinji on 2017/09/09.
 */
import android.content.Context;
import android.content.res.TypedArray;

import com.alienhouse.kitten.R;

public class Utils {

    public static String SEND_URL = "URL_WEB";
    public static String SEND_PAGE_TITLE = "URL_TITLE";
    public static int getToolbarHeight(Context context) {
        final TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(
                new int[]{R.attr.actionBarSize});
        int toolbarHeight = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();

        return toolbarHeight;
    }

    public static int getTabsHeight(Context context) {
        return (int) context.getResources().getDimension(R.dimen.tabsHeight);
    }

    public static final String[] WEEK_NAME = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
}