package com.chauncy.account.utils

import android.util.Log
import java.lang.StringBuilder
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.abs

object NumberFormat {

    @JvmStatic
    fun parseToString(value: Double): String {
        val sign = if (value > 0) "+" else ""
        val res= formatToString(value)
        return sign + res
    }

    fun parseToString(value: Double,hasSign: Boolean):String{
        if(hasSign)
            return parseToString(value)
        return formatToString(abs(value))
    }

    @JvmStatic
    fun parseIntegerNumber(value: Double,hasSign:Boolean=true,hasPoint:Boolean=false):String{
       val num=value.toInt()
        val res=StringBuilder()
        if(hasSign){
            if(num>0) res.append("+")
            res.append(num)
        }else{
            res.append(abs(num))
        }
        if(hasPoint){
            res.append(".")
        }
        Log.d("NumberFormat",res.toString())
        return res.toString()
    }

    @JvmStatic
    fun parseDecimalNumber(value: Double,hasPoint:Boolean=true):String{
        val res= formatToString(value)
        val index=res.indexOf('.')
        if(index==-1){
            return "00"
        }
        var lastIndex= res.length.coerceAtMost(if (abs(value) >= 1.0) 3 + index else 5 + index)
        return if(hasPoint) "0${res.substring(index,lastIndex)}" else res.substring(index+1,lastIndex)

    }

    @JvmStatic
    private fun formatToString(value: Double):String{
        val format=DecimalFormat("###,##0.00##")
        format.roundingMode=RoundingMode.DOWN
        return format.format(value)
    }

    @JvmStatic
    fun main(args: Array<String>) {
        println(formatToString(0.0170 ))
    }




}
