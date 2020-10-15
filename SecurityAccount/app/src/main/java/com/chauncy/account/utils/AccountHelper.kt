package com.chauncy.account.utils

import com.chauncy.account.R
import com.chauncy.account.model.AccountDataCenter
import com.chauncy.account.model.bean.AccountOrder
import com.chauncy.account.model.bean.IconText
import com.chauncy.account.model.bean.Position
import java.lang.StringBuilder
import java.util.ArrayList

object AccountHelper {

    @JvmStatic
    fun getAccountFullName(accountId: String): String {
        val accountName = getAccountPrefixName(accountId)
        return "$accountName $accountId"
    }

    @JvmStatic
    fun getAccountShortName(accountId: String): String {
        val accountName = getAccountPrefixName(accountId)
        val short = accountId.substring(accountId.length - 4)
        return "$accountName ($short)"
    }

    private fun getAccountPrefixName(accountId: String): String {
        val accountInfo = AccountDataCenter.get().getAccountInfo(accountId)
        val builder = StringBuilder()
        if (accountInfo != null) {
            builder.append(when (accountInfo.accountType) {
                AccountConstant.ACCOUNT_TYPE_CN -> Global.getString(R.string.account_type_name_cn)
                AccountConstant.ACCOUNT_TYPE_USA -> Global.getString(R.string.account_type_name_usa)
                else -> Global.getString(R.string.account_type_name_hk)
            })
            builder.append(when (accountInfo.type) {
                AccountConstant.ACCOUNT_FUNCTION_TYPE_CASH -> Global.getString(R.string.account_function_type_cash)
                AccountConstant.ACCOUNT_FUNCTION_TYPE_MARGIN -> Global.getString(R.string.account_function_type_margin)
                else -> ""
            })
        }
        return builder.toString()
    }

    @JvmStatic
     fun getFunctionItemsIconText(accountType: String): List<IconText> {
        return when (accountType) {
            AccountConstant.ACCOUNT_TYPE_HK -> Global.getImgTextList(R.array.function_item_icon_hk, R.array.function_item_text_hk)
            AccountConstant.ACCOUNT_TYPE_USA -> Global.getImgTextList(R.array.function_item_icon_usa, R.array.function_item_text_usa)
            AccountConstant.ACCOUNT_TYPE_CN -> Global.getImgTextList(R.array.function_item_icon_cn, R.array.function_item_text_cn)
            else -> Global.getImgTextList(R.array.function_item_icon_hk, R.array.function_item_text_hk)
        }
    }

    @JvmStatic
     fun filter(orderList: List<AccountOrder>, submitStatus: String): List<AccountOrder> {
        val result = ArrayList<AccountOrder>()
        for (element in orderList) {
            if (element.submitStatus == submitStatus) {
                result.add(element)
            }
        }
        return result
    }

    @JvmStatic
    fun getAssetTitle(accountType: String):String{
        var resId= when (accountType) {
            AccountConstant.ACCOUNT_TYPE_CN -> R.string.asset_title_CN
            AccountConstant.ACCOUNT_TYPE_USA -> R.string.asset_title_USA
            else -> R.string.asset_title_HK
        }
        return Global.getString(resId)
    }

    @JvmStatic
    fun getAccountType(accountId: String):String{
        val info = AccountDataCenter.get().getAccountInfo(accountId)
        return info?.accountType?:""
    }

    @JvmStatic
    fun getCurrencyType(accountType: String):String{
        return when(accountType){
            AccountConstant.ACCOUNT_TYPE_CN -> Global.getString(R.string.currency_type_cn)
            AccountConstant.ACCOUNT_TYPE_HK -> Global.getString(R.string.currency_type_hk)
            AccountConstant.ACCOUNT_TYPE_USA -> Global.getString(R.string.currency_type_usa)
            else -> Global.getString(R.string.currency_type_hk)
        }
    }

    fun getMaxProfitInfo(positionList: MutableList<Position>): Position {
        var result=positionList[0]
        for(element in positionList){
            if(element.positionProfit>result.positionProfit)
                result=element
        }
        return result
    }


}