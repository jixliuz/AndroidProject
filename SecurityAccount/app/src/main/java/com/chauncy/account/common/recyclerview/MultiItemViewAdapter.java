package com.chauncy.account.common.recyclerview;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MultiItemViewAdapter<T> extends RecyclerView.Adapter<ViewHolder> {
    protected Context mContext;
    protected List<T> mData;//
    protected ItemViewDelegateManager mItemViewDelegateManager;


    public MultiItemViewAdapter(@NonNull Context context) {
        mContext = context;
        mData = new ArrayList<>();
        mItemViewDelegateManager = new ItemViewDelegateManager();
    }

    public MultiItemViewAdapter(@NonNull Context context, @NonNull List<T> data) {
        mContext = context;
        mData = data;
        mItemViewDelegateManager = new ItemViewDelegateManager();
    }


    /**
     * 更新ViewHolder持有的ItemView
     *
     * @param holder
     * @param item
     */
    public void onUpdate(@NonNull ViewHolder holder, T item) {
        mItemViewDelegateManager.convert(holder, item, -1);
    }

    /**
     * 添加ItemView
     *
     * @param itemViewDelegate ItemView实例
     */
    public void addItemViewDelegate(@NonNull ItemViewDelegate<T> itemViewDelegate) {
        mItemViewDelegateManager.addDelegate(itemViewDelegate);
    }

    /**
     * 添加ItemView
     *
     * @param viewType         ItemView的类型
     * @param itemViewDelegate ItemView实例
     */
    public void addItemViewDelegate(int viewType, @NonNull ItemViewDelegate<T> itemViewDelegate) {
        mItemViewDelegateManager.addDelegate(viewType, itemViewDelegate);
    }

    /**
     * 获取指定位置ItemView的类型
     *
     * @param position ItemView的位置
     * @return ItemView的类型
     */
    @Override
    public int getItemViewType(int position) {
        if (mItemViewDelegateManager.getItemViewDelegateCount() == 0)
            return super.getItemViewType(position);
        return mItemViewDelegateManager.getItemViewType(mData.get(position), position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemViewDelegate viewDelegate = mItemViewDelegateManager.getItemViewDelegate(viewType);
        int layoutId = viewDelegate.getItemViewLayoutId();
        ViewHolder viewHolder = ViewHolder.getInstance(mContext, layoutId, parent);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        mItemViewDelegateManager.convert(holder, mData.get(position), position);
    }



    @Override
    public int getItemCount() {
        return mData.size();
    }


    public void refreshData(List<T> data){
        mData.clear();
        if(data!=null) {
            mData.addAll(data);
        }
        notifyDataSetChanged();
    }

}
