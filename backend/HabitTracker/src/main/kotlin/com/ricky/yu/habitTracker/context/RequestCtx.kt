package com.ricky.yu.habitTracker.context

import com.ricky.yu.habitTracker.enums.Role

data class RequestCtx(
    val userId: Long,
    val role: Role,
    // todo: implement locale
//    val locale: Locale,
    val requestId: String,
)
