package com.chauncy.account.model

import com.chauncy.account.model.bean.AccountOrder
import com.chauncy.account.model.bean.Position
import com.chauncy.account.model.dao.AccountOrderDao
import com.chauncy.account.model.dao.PositionDao
import com.chauncy.account.utils.Global
import java.util.concurrent.Executors
import java.util.concurrent.locks.ReentrantReadWriteLock

class DbDataCenter {


    private val lock by lazy { ReentrantReadWriteLock() }
    private val positionCache: MutableMap<String, MutableList<Position>> = mutableMapOf()
    private val orderCache: MutableMap<String, MutableList<AccountOrder>> = mutableMapOf()
    private val threadPool by lazy { Executors.newCachedThreadPool() }

    private val positionDao = PositionDao(Global.getContext())
    private val orderDao = AccountOrderDao(Global.getContext())

    companion object {
        private const val TAG = "DbDataCenter"

        private val instance by lazy { DbDataCenter() }

        @JvmStatic
        fun get() = instance
    }

    fun asyncLoadData() {
        threadPool.submit {
            lock.writeLock().lock()
            saveOrder(orderDao.findAllOrder())
            savePosition(positionDao.findAllPosition())
            lock.writeLock().unlock()
        }
    }

    private fun savePosition(positionList: MutableList<Position>) {
        positionCache.clear()
        for (position in positionList) {
            val accountId = position.accountId
            if (positionCache[accountId] == null) {
                positionCache[accountId] = mutableListOf()
            }
            positionCache[accountId]?.add(position)
        }
    }

    private fun saveOrder(orderList: MutableList<AccountOrder>) {
        orderCache.clear()
        for (order in orderList) {
            val accountId = order.accountId
            if (orderCache[accountId] == null) {
                orderCache[accountId] = mutableListOf()
            }
            orderCache[accountId]?.add(order)
        }
    }

    fun getAccountOrder(accountId: String): MutableList<AccountOrder> {
        val result = mutableListOf<AccountOrder>()
        lock.readLock().lock()
        val value = orderCache[accountId]
        if (value != null) {
            result.addAll(value)
        }
        lock.readLock().unlock()
        return result
    }

    fun getAccountOrder(accountId: String, submitStatus: String): MutableList<AccountOrder> {
        val result = mutableListOf<AccountOrder>()
        lock.readLock().lock()
        val value = orderCache[accountId]
        if (value != null) {
            for (element in value) {
                if (element.submitStatus == submitStatus)
                    result.add(element)
            }
        }
        lock.readLock().unlock()
        return result
    }

    fun getAccountPosition(accountId: String): MutableList<Position> {
        val result = mutableListOf<Position>()
        lock.readLock().lock()
        val value = positionCache[accountId]
        if (value != null) {
            result.addAll(value)
        }
        lock.readLock().unlock()
        return result
    }
}