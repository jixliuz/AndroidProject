package com.chauncy.account.common.recyclerview;


public interface ItemViewDelegate<T> {
    /**
     * 获取视图的布局文件Id
     * @return 视图的布局文件ID
     */
    int getItemViewLayoutId();

    /**
     * 判断是否是指定视图
     * @param item
     * @param position
     * @return
     */
    boolean isForViewType(T item,int position);

    /**
     * 转换holder持有的视图
     * @param holder
     * @param t
     * @param position
     */
    void convert(ViewHolder holder, T t, int position);



}
