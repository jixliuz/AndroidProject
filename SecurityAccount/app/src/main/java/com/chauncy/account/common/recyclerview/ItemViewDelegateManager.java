package com.chauncy.account.common.recyclerview;

import android.util.SparseArray;

import androidx.annotation.NonNull;

public class ItemViewDelegateManager<T> {
    private SparseArray<ItemViewDelegate<T>> delegates;
    public ItemViewDelegateManager() {

        delegates = new SparseArray<>();
    }

    /**
     * 获取ItemView的数量
     * @return
     */
    public int getItemViewDelegateCount() {
        return delegates.size();
    }

    /**
     * 获取指定ItemView的类型
     * @param item
     * @param position
     * @return
     */
    public int getItemViewType(T item, int position) {
        int size = delegates.size();
        ItemViewDelegate viewDelegate = null;
        for (int i = 0; i < size; i++) {
            viewDelegate = delegates.valueAt(i);
            if (viewDelegate.isForViewType(item, position))
                return delegates.keyAt(i);
        }
        throw new IllegalArgumentException( "No ItemViewDelegate added that matches position=" + position + " in data source");
    }

    /**
     * 转化ViewHolder持有的View
     * @param holder
     * @param item
     * @param position
     */
    public void convert(ViewHolder holder, T item, int position) {
        int size=delegates.size();
        for(int i=size-1;i>=0;i--){
            ItemViewDelegate<T> viewDelegate=delegates.valueAt(i);
            if(viewDelegate.isForViewType(item,position)) {
                viewDelegate.convert(holder, item, position);
                return;
            }
        }
        throw new IllegalArgumentException(
                "No ItemViewDelegateManager added that matches position=" + position + " in data source");
    }


    /**
     * 获取指定view类型的布局文件Id
     * @param viewType view的类型
     * @return
     */
    public int getItemViewLayoutId(int viewType) {
        ItemViewDelegate<T> viewDelegate = getItemViewDelegate(viewType);
        return viewDelegate.getItemViewLayoutId();
    }

    /**
     * 获取指定类型的ItemView实例
     * @param viewType view的类型
     * @return ItemView的实例
     */
    public ItemViewDelegate getItemViewDelegate(int viewType){
        ItemViewDelegate<T> viewDelegate = delegates.get(viewType);
        if (viewDelegate == null)
            throw new IllegalArgumentException("ItemViewDelegates of viewType "+viewType+" is not exist");
        return viewDelegate;
    }

    /**
     * 获取指定ItemView的类型
     * @param viewDelegate 指定的ItemView
     * @return ItemView的类型
     */
    public int getItemViewType(@NonNull ItemViewDelegate<T> viewDelegate) {
        return delegates.indexOfValue(viewDelegate);
    }

    /**
     * 添加ItemView
     * @param viewDelegate ItemView的实例
     */
    public void addDelegate(@NonNull ItemViewDelegate<T> viewDelegate) {
        int size = delegates.size();
        delegates.put(size, viewDelegate);
    }

    /**
     * 添加ItemView
     * @param viewType ItemView的类型
     * @param viewDelegate ItemView的实例
     */
    public void addDelegate(int viewType, @NonNull ItemViewDelegate<T> viewDelegate) {
        if (viewType < 0)
            throw new IllegalArgumentException("The value of viewType must be greater and equal to zero");
        if (delegates.get(viewType) != null)
            throw new IllegalArgumentException("An ItemViewDelegate is already registered for the viewType = "
                    + viewType
                    + ". Already registered ItemViewDelegate is "
                    + delegates.get(viewType));
        delegates.put(viewType, viewDelegate);
    }

    /**
     * 移除ItemView
     * @param viewDelegate ItemView的实例
     */
    public void removeDelegate(@NonNull ItemViewDelegate<T> viewDelegate) {
        int indexOfRemove = delegates.indexOfValue(viewDelegate);
        if (indexOfRemove >= 0) {
            delegates.removeAt(indexOfRemove);
        }
    }


    /**
     * 移除类型为viewType的ItemView
     * @param viewType 移除ItemView的类型
     */
    public void removeDelegate(int viewType) {
        if (viewType < 0)
            throw new IllegalArgumentException("The value of viewType must be greater and equal to zero");
        delegates.removeAt(viewType);
    }

}
