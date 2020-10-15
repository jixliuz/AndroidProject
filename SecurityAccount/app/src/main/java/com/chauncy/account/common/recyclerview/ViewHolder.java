package com.chauncy.account.common.recyclerview;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder extends RecyclerView.ViewHolder {
    private View mConvertView;
    private SparseArray<View> mViews;
    private Context mContext;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        mConvertView = itemView;
        mViews = new SparseArray<>();
    }

    public ViewHolder(@NonNull Context context, @NonNull View convertView) {
        super(convertView);
        mConvertView = convertView;
        mContext = context;
        mViews = new SparseArray<>();
    }


    /**
     * 获取ViewHolder实例
     *
     * @param context
     * @param layoutId 布局文件的id
     * @param parent
     * @return
     */
    public static ViewHolder getInstance(@NonNull Context context, int layoutId, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);
        return new ViewHolder(context, view);
    }

    /**
     * 获取convertView
     *
     * @return
     */
    public View getConvertView() {
        return mConvertView;
    }

    /**
     * 获取View
     *
     * @param viewId view的id
     * @param <T>
     * @return
     */
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    public ViewHolder setText(int viewId, String text) {
        TextView tv = getView(viewId);
        tv.setText(text);
        return this;
    }

    public ViewHolder setImageResource(int viewId, int resId) {
        ImageView view = getView(viewId);
        view.setImageResource(resId);
        return this;
    }

    /**
     * 为指定View注册监听器
     *
     * @param viewId
     * @param listener
     * @return
     */
    public ViewHolder setOnListener(int viewId, View.OnClickListener listener) {
        View view = getView(viewId);
        view.setOnClickListener(listener);
        return this;
    }

    public Context getContext() {
        return mContext;
    }
}
