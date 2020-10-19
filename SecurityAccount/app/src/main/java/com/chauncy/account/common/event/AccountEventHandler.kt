package com.chauncy.account.common.event

class AccountEventHandler {
    private val subject: Subject = Subject()

    companion object {
        private val instances: AccountEventHandler by lazy { AccountEventHandler() }

        @JvmStatic
        fun get() = instances
    }

    fun createSubject(): Subject {
        return subject
    }

    fun subject(observer: Observer) {
        subject.addObserver(observer)
    }
}