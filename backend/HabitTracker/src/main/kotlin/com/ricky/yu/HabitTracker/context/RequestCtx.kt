package com.ricky.yu.HabitTracker.context

import com.ricky.yu.HabitTracker.enums.Role

data class RequestCtx(
    val userId: Long,

    val role: Role,
    // todo: implement locale
//    val locale: Locale,
    val requestId: String
)