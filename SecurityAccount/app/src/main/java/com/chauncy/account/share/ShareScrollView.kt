package com.chauncy.account.share

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chauncy.account.R
import com.chauncy.account.common.recyclerview.RecyclerViewClickListener
import com.chauncy.account.common.recyclerview.ViewHolder
import com.chauncy.account.model.IconText
import com.chauncy.account.utils.Global

class ShareScrollView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet?=null,
        defStyleAttr:Int=0): FrameLayout(context,attrs,defStyleAttr) {

    private var mData= mutableListOf<ShareItem>()
    private val mRecyclerView=RecyclerView(context)
    private val mLinearLayoutManager=LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
    private val mAdapter:IconAdapter=IconAdapter(mData)


    fun setItemClickListener(listener:RecyclerViewClickListener.OnItemClickListener){
        mRecyclerView.addOnItemTouchListener(RecyclerViewClickListener(context,mRecyclerView,listener))
    }

    init {
        mRecyclerView.adapter=mAdapter
        mRecyclerView.layoutManager=mLinearLayoutManager
        mRecyclerView
        addView(mRecyclerView)
    }


    fun setShareItems(data: MutableList<ShareItem>){
        mData.clear()
        if(data!=null) {
            mData.addAll(data)
        }
        mAdapter.notifyDataSetChanged()
    }

     inner class IconAdapter(private val mData: MutableList<ShareItem>) : RecyclerView.Adapter<ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder.getInstance(parent.context, R.layout.icon_text_item_layout, parent)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item=getIconText(mData[position])
            val textView = holder.getView<TextView>(R.id.item_img_text)
            textView.text = item.text
            holder.setText(R.id.item_img_text,item.text)
            val drawable = Global.getContext().getDrawable(item.imageResource)
            drawable?.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
            textView.setCompoundDrawables(null, drawable, null, null)
        }

        private fun getIconText(shareItem: ShareItem): IconText {
            return when(shareItem){
                ShareItem.PROFIT_RATIO -> IconText(R.drawable.static_share_icon_persent, Global.getString(R.string.text_profit_amount))
                ShareItem.PROFIT_AMOUNT -> IconText(R.drawable.static_share_icon_money, Global.getString(R.string.text_profit_amount))
                ShareItem.TRADE_AMOUNT -> IconText(R.drawable.static_share_icon_amount, Global.getString(R.string.text_trade_amount))
                ShareItem.POSITION_LIST -> IconText(R.drawable.static_share_icon_list5, Global.getString(R.string.text_position_list))
                else -> IconText(R.drawable.static_share_icon_stock, Global.getString(R.string.text_stock_name))
            }
        }

        override fun getItemCount(): Int {
            return mData.size
        }
    }
}