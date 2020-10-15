package com.chauncy.account.model

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.locks.ReadWriteLock
import java.util.concurrent.locks.ReentrantReadWriteLock

class AccountDataCenter {

    private val lock: ReadWriteLock by lazy { ReentrantReadWriteLock() }

    private val cacheMap: MutableMap<String, MutableList<AccountInfo>>
            by lazy { mutableMapOf<String, MutableList<AccountInfo>>() }

    private val accountInfoCacheMap: MutableMap<String, AccountInfo>
            by lazy { mutableMapOf<String, AccountInfo>() }

    private val threadPool: ExecutorService by lazy { Executors.newCachedThreadPool() }

    companion object {
        private val instances: AccountDataCenter by lazy { AccountDataCenter() }

        @JvmStatic
        fun get() = instances
    }

    fun asyncLoadAllAccount() {
        threadPool.submit {
            lock.writeLock().lock()
            val accountInfoMap = JSONLoader.get().parseJSON(AccountInfo::class.java)
            for ((type, accountList) in accountInfoMap) {
                cacheMap[type] = accountList

                for (account in accountList) {
                    accountInfoCacheMap[account!!.accountId] = account
                }
            }
            lock.writeLock().unlock()
        }
    }


    fun getAccountInfo(accountId: String): AccountInfo? {
        lock.readLock().lock()
        val account = accountInfoCacheMap[accountId]
        lock.readLock().unlock()
        return account
    }

    fun getAllAccount(accountType: String): MutableList<SecurityAccount> {
        val list = mutableListOf<SecurityAccount>()
        lock.readLock().lock()
        for (info in cacheMap[accountType]!!) {
            list.add(SecurityAccount(info.accountId, info.accountType))
        }
        lock.readLock().unlock()
        return list
    }

    fun getDefaultAccount(accountType: String): SecurityAccount {
        val accountList = cacheMap[accountType]
        if (accountList?.size == 0)
            return SecurityAccount(accountType)
        val id = accountList?.get(0)?.accountId ?: ""
        return SecurityAccount(id, accountType)
    }


}