package com.chauncy.account.utils;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.widget.TextView;

import com.chauncy.account.model.bean.IconText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public final class Global {
    private static final String TAG = "Global";

    public static Context getContext() {
        return ComponentContext.getApplicationContext();
    }


    public static String getString(int resId) {
        return getContext().getString(resId);
    }

    public static Resources getResources() {
        return getContext().getResources();
    }

    public static Drawable getDrawable(int resId){
        return getResources().getDrawable(resId);
    }

    public static String[] getStringArray(int resId) {
        return getResources().getStringArray(resId);
    }


    public static DisplayMetrics getDisplayMetrics() {
        return getResources().getDisplayMetrics();
    }

    public static float getDensity() {
        return getDisplayMetrics().density;
    }

    public static float getDimension(int resId) {
        return getResources().getDimension(resId);
    }

    public static int getDimensionPixelSize(int resId) {
        return getResources().getDimensionPixelSize(resId);
    }

    public static int px2sp(float pxValue) {
        float fontScale = getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5F);
    }

    public static int dp2px(float dpValue) {
        float scale = getDensity();
        return (int) (dpValue * scale + 0.5f);
    }


    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     *
     * @param context
     * @param dpValue 要转换的dp值
     */
    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     *
     * @param context
     * @param pxValue 要转换的px值
     */
    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


    public static int[] getImageResourceArray(int resId) {
        TypedArray typedArray = getResources().obtainTypedArray(resId);
        int[] images = new int[typedArray.length()];
        for (int i = 0; i < images.length; i++)
            images[i] = typedArray.getResourceId(i, -1);
        return images;
    }

    public static IconText[] getImgTextArray(int imgResId, int textResId) {
        String[] textArray = getStringArray(textResId);
        TypedArray typedArray = getResources().obtainTypedArray(imgResId);
        int size;
        if ((size = textArray.length) != typedArray.length()) {
            throw new IllegalArgumentException("图片与文本数量不匹配,图片的数量为:" + typedArray.length()
                                                       + ", 文本的数量为:" + textArray.length);
        }
        IconText[] res = new IconText[size];
        for (int i = 0; i < size; i++) {
            IconText element = new IconText(typedArray.getResourceId(i, -1), textArray[i]);
            res[i] = element;
        }
        return res;
    }

    public static List<IconText> getImgTextList(int imgResId, int textResId) {
        List<IconText> list = new ArrayList<>(Arrays.asList(getImgTextArray(imgResId, textResId)));
        return list;
    }

    public static void resetColor(TextView textView) {
       textView.setTextColor(Color.GRAY);
    }

    public static void setTextAndColor(TextView textView, double value, boolean hasPercentSign){
        if (value > 0.00) {
            textView.setTextColor(Color.RED);
        }else if(value<0.0) {
            textView.setTextColor(Color.GREEN);
        }else {
            textView.setTextColor(Color.GRAY);
        }
        String num=NumberFormat.parseToString(value);
        if(hasPercentSign)
            num+="%";
        textView.setText(num);
    }

    public static boolean isDouble(String value){
        Pattern pattern = Pattern.compile("^[-\\+]?[.\\d]*[%]?$");
        return pattern.matcher(value).matches();
    }


}
