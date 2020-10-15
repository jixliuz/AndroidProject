package com.chauncy.account.common.event

import com.chauncy.account.model.SecurityAccount

abstract class Observer{
     abstract fun update(msg: SecurityAccount)
}