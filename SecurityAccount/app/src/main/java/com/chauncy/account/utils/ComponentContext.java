package com.chauncy.account.utils;

import android.content.Context;

public final class ComponentContext {
    private static Context sApplicationContext;

    public static void init(Context context) {
        context = context.getApplicationContext();
        sApplicationContext = context;
    }

    public static Context getApplicationContext() {
        return sApplicationContext;
    }
}
