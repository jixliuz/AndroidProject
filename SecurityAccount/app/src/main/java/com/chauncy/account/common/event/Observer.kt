package com.chauncy.account.common.event

import com.chauncy.account.model.bean.SecurityAccount

abstract class Observer{
     abstract fun update(msg: SecurityAccount)
}