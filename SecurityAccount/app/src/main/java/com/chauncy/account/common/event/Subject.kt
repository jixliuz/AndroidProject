package com.chauncy.account.common.event

import com.chauncy.account.model.SecurityAccount

class Subject {
    private val observerList: MutableList<Observer> by lazy { mutableListOf<Observer>() }

    fun addObserver(observer: Observer) {
        observerList.add(observer)
    }

    fun notifyDataChange(account: SecurityAccount) {
        for (observer in observerList) {
            observer.update(account)
        }
    }
}