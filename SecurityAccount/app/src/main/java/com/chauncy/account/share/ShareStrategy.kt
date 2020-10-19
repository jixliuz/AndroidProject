package com.chauncy.account.share


import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chauncy.account.R
import com.chauncy.account.common.recyclerview.RecyclerViewDivider
import com.chauncy.account.common.recyclerview.ViewHolder
import com.chauncy.account.model.DbDataCenter
import com.chauncy.account.model.bean.Position
import com.chauncy.account.utils.AccountHelper
import com.chauncy.account.utils.Global
import com.chauncy.account.utils.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

abstract class ShareStrategy {
    private var info:ShareInfo?=null
    protected var positionList:MutableList<Position>?=null
    private val showPositionNumber=4
    protected var realPositionNum=0

    abstract fun createShareItem():List<ShareItem>

    abstract fun fill(holder: ViewHolder,shareItem: ShareItem)

    fun setInfo(shareInfo: ShareInfo){
        if(shareInfo==null) return
        info=shareInfo
        val list=DbDataCenter.get().getAccountPosition(info!!.accountId)
        realPositionNum=list.size
        positionList = if(list.size>showPositionNumber){
            list.subList(0,showPositionNumber)
        }else{
            list
        }
    }

    fun getInfo():ShareInfo?{
        return info
    }

    companion object Factory{
        fun create(shareType: ShareType):ShareStrategy{
           return when(shareType){
                ShareType.SHARE_ASSET -> AccountShareStrategy()
               ShareType.SHARE_STOCK -> AccountStockShareStrategy()
            }
        }
    }

     protected fun fillProfitAmount(holder: ViewHolder, info: ShareInfo) {
        val profit:Double=info.profit
        if(profit>0.0) {
            holder.setImageResource(R.id.share_item_image,R.drawable.static_share_cartoon_03)
            holder.setText(R.id.share_item_title, Global.getString(R.string.share_title_3))
            holder.setText(R.id.share_value_sign,Global.getString(R.string.positive_number_sign))
        }else if(profit<0.0){
            holder.setImageResource(R.id.share_item_image,R.drawable.static_share_cartoon_02)
            holder.setText(R.id.share_item_title, Global.getString(R.string.share_title_2))
            holder.setText(R.id.share_value_sign,Global.getString(R.string.negative_number_sign))
        }else{
            holder.setImageResource(R.id.share_item_image,R.drawable.static_share_cartoon_01)
            holder.setText(R.id.share_item_title, Global.getString(R.string.share_title_1))
            holder.getView<TextView>(R.id.share_value_sign).visibility= View.GONE
        }
        holder.setText(R.id.share_item_tag,"今日${info.accountType}盈亏金额")
        holder.setText(R.id.share_item_currency_type,AccountHelper.getCurrencyType(info.accountType))
        holder.setText(R.id.share_value_big_num,NumberFormat.parseIntegerNumber(profit,hasSign = false,hasPoint = true))
        holder.setText(R.id.share_value_small_num,NumberFormat.parseDecimalNumber(profit,hasPoint = false))
    }

     protected fun fillProfitRatio(holder: ViewHolder, info: ShareInfo) {
        val profitRatio:Double=info.profitRatio
        if(profitRatio>0.0) {
            holder.setImageResource(R.id.share_item_image,R.drawable.static_share_cartoon_03)
            holder.setText(R.id.share_item_title, Global.getString(R.string.share_title_3))
            holder.setText(R.id.share_value_sign,Global.getString(R.string.positive_number_sign))
        }else if(profitRatio<0.00){
            holder.setImageResource(R.id.share_item_image,R.drawable.static_share_cartoon_02)
            holder.setText(R.id.share_item_title, Global.getString(R.string.share_title_2))
            holder.setText(R.id.share_value_sign,Global.getString(R.string.negative_number_sign))
        }else{
            holder.setImageResource(R.id.share_item_image,R.drawable.static_share_cartoon_01)
            holder.setText(R.id.share_item_title, Global.getString(R.string.share_title_1))
            holder.getView<TextView>(R.id.share_value_sign).visibility= View.GONE
        }
        val type=AccountHelper.getAccountType(info!!.accountId)
        holder.setText(R.id.share_item_tag,"今日${type}盈亏比例")
        holder.setText(R.id.share_item_currency_type,"%")
        holder.setText(R.id.share_value_big_num,NumberFormat.parseToString(profitRatio,false))
        holder.getView<TextView>(R.id.share_value_small_num).visibility=View.GONE
    }
}


class AccountShareStrategy: ShareStrategy() {
    private val shareItems=listOf(ShareItem.PROFIT_RATIO,ShareItem.PROFIT_AMOUNT
            ,ShareItem.TRADE_AMOUNT,ShareItem.POSITION_LIST)

    override fun createShareItem(): List<ShareItem> {
        return shareItems
    }

    override fun fill(holder: ViewHolder, shareItem: ShareItem) {
        val info= getInfo() ?: return
        when(shareItem){
            ShareItem.PROFIT_RATIO -> fillProfitRatio(holder,info)
            ShareItem.PROFIT_AMOUNT -> fillProfitAmount(holder,info)
            ShareItem.TRADE_AMOUNT -> fillTradeAmount(holder,info)
            ShareItem.POSITION_LIST -> fillPosition(holder)
        }
        val format=SimpleDateFormat("yyyy/MM/dd hh:mm")
        holder.setText(R.id.share_time,format.format(info.time))
    }

    private fun fillPosition(holder: ViewHolder) {
        val shortPositionLayout=holder.getView<View>(R.id.short_position_layout)
        val positionLayout=holder.getView<View>(R.id.position_layout)
        if(positionList==null|| positionList?.size==0){
            shortPositionLayout.visibility=View.VISIBLE
            positionLayout.visibility=View.GONE
            holder.setText(R.id.share_item_title,Global.getString(R.string.share_title_1))
            holder.setImageResource(R.id.share_item_image,R.drawable.static_share_cartoon_15)
            holder.setText(R.id.share_item_title, "")
            holder.setText(R.id.share_item_tag,getInfo()?.accountType)
            holder.setText(R.id.share_value_big_num,Global.getString(R.string.short_position))
            holder.getView<TextView>(R.id.share_value_small_num).visibility=View.GONE
            holder.getView<TextView>(R.id.share_value_sign).visibility=View.GONE
            holder.getView<TextView>(R.id.share_item_currency_type).visibility=View.GONE
        }else {
            shortPositionLayout.visibility=View.GONE
            positionLayout.visibility=View.VISIBLE
            holder.setText(R.id.share_item_title,"${getInfo()?.accountType}最挣钱股票")
            val maxProfitStock=AccountHelper.getMaxProfitInfo(positionList!!)
            holder.setText(R.id.max_profit_stock_name,maxProfitStock.stockName)
            holder.setText(R.id.max_profit_stock_code,maxProfitStock.stockId)
            val mRecyclerView=holder.getView<RecyclerView>(R.id.share_position_recyclerView)
            mRecyclerView.layoutManager = LinearLayoutManager(holder.context, LinearLayoutManager.VERTICAL, false)
            mRecyclerView.addItemDecoration(RecyclerViewDivider(holder.context, LinearLayoutManager.HORIZONTAL))
            mRecyclerView.adapter = SharePositionAdapter(positionList!!)
        }
    }

    private fun fillTradeAmount(holder: ViewHolder, info: ShareInfo){
        if(realPositionNum==0){
            holder.setImageResource(R.id.share_item_image,R.drawable.static_share_cartoon_01)
            holder.setText(R.id.share_item_title, Global.getString(R.string.share_title_4))
        }else{
            holder.setImageResource(R.id.share_item_image,R.drawable.static_share_cartoon_05)
            holder.setText(R.id.share_item_title, Global.getString(R.string.share_title_3))
        }
        holder.getView<TextView>(R.id.share_value_sign).visibility= View.GONE
        holder.setText(R.id.share_item_tag,"${info.accountType}交易笔数")
        holder.setText(R.id.share_item_currency_type,"笔")
        holder.setText(R.id.share_value_big_num,realPositionNum.toString())
        holder.getView<TextView>(R.id.share_value_small_num).visibility=View.GONE
    }


     inner class SharePositionAdapter constructor(private val positionList:MutableList<Position>): RecyclerView.Adapter<ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder.getInstance(parent.context,R.layout.share_position_item_layout,parent)
        }

        override fun getItemCount(): Int {
          return positionList.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val info=positionList[position]
            holder.setText(R.id.stock_name,info.stockName)
            holder.setText(R.id.stock_code,info.stockId)
            holder.setText(R.id.position_cost,NumberFormat.parseToString(info.cost))
            holder.setText(R.id.current_price,NumberFormat.parseToString(info.price))
            holder.setText(R.id.position_profit,NumberFormat.parseToString(info.positionProfit))
        }
    }

}

class AccountStockShareStrategy: ShareStrategy() {
    private val shareItems=listOf(ShareItem.PROFIT_RATIO,ShareItem.PROFIT_AMOUNT)
    override fun createShareItem(): List<ShareItem> {
        return shareItems
    }

    override fun fill(holder: ViewHolder, shareItem: ShareItem) {
        val info= getInfo() ?: return
        when(shareItem){
            ShareItem.PROFIT_RATIO -> fillProfitRatio(holder,info)
            ShareItem.PROFIT_AMOUNT -> fillProfitAmount(holder,info)
        }
        val format=SimpleDateFormat("yyyy/MM/dd hh:mm")
        holder.setText(R.id.share_time,format.format(Date()))
    }




}
enum class ShareItem{
    PROFIT_AMOUNT,PROFIT_RATIO,TRADE_AMOUNT,POSITION_LIST,STOCK
}

enum class ShareType{
    SHARE_ASSET,SHARE_STOCK;

    companion object {
        fun create(ordinal: Int): ShareType {
            return when (ordinal) {
                0 -> SHARE_ASSET
                else -> SHARE_STOCK
            }
        }
    }
}

data class ShareInfo(val shareType: ShareType,val accountId:String,val profit:Double
                     ,val profitRatio: Double, val accountType:String,val time:Date)

class ShareConstant{
    companion object{
       const val KEY_SHARE_PROFIT="key_share_profit"
       const val KEY_SHARE_PROFIT_RATIO="key_share_profit_ratio"

       const val KEY_SHARE_ACCOUNT_ID="key_share_account_id"
       const val KEY_SHARE_TYPE="key_share_type"
       const val KEY_SHARE_ACCOUNT_TYPE="key_share_account_type"

        const val SHARE_PAGE_ITEM_CONTEXT_VIEW_TYPE=0
        const val SHARE_PAGE_ITEM_POSITION_VIEW_TYPE=1
    }
}