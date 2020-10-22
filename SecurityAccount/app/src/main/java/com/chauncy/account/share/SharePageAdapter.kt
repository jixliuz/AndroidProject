package com.chauncy.account.share

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chauncy.account.R
import com.chauncy.account.common.recyclerview.ViewHolder

class SharePageAdapter constructor(private val shareStrategy: ShareStrategy):RecyclerView.Adapter<ViewHolder>() {
    private var  mData:List<ShareItem> = shareStrategy.createShareItem()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if(viewType==ShareConstant.SHARE_PAGE_ITEM_CONTEXT_VIEW_TYPE)
            return ViewHolder.getInstance(parent.context,R.layout.share_context_layout,parent)
        return ViewHolder.getInstance(parent.context,R.layout.share_context_position_layout,parent)
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        shareStrategy.fill(holder, mData[position])
    }

    override fun getItemViewType(position: Int): Int {
        if(mData[position]==ShareItem.POSITION_LIST)
            return ShareConstant.SHARE_PAGE_ITEM_POSITION_VIEW_TYPE
        return ShareConstant.SHARE_PAGE_ITEM_CONTEXT_VIEW_TYPE
    }

}
