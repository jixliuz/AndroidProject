package com.chauncy.account.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SettingInfoManager {
    private static String SP_NAME="securities_account";
    private static String KEY_INFO_SHOW_STATUS="key_info_show_status";

    /**
     * 保存证券账户数据状态标识（显示/隐藏）
     * @param isShow
     */
    public static void saveInfoShowStatus(boolean isShow)
    {
        SharedPreferences sp = Global.getContext().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(KEY_INFO_SHOW_STATUS, isShow);
        editor.apply();
    }

    /**
     * 获取证券账户数据状态
     * @return
     */
    public static boolean getInfoShowStatus()
    {
        SharedPreferences sp = Global.getContext().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        return sp.getBoolean(KEY_INFO_SHOW_STATUS, true);
    }

}
