package com.ricky.yu.HabitTracker.context

object RequestCtxHolder {
    private val threadLocal = ThreadLocal<RequestCtx>()

    fun getRequestContext(): RequestCtx {
        return threadLocal.get() ?: throw IllegalStateException("No RequestContext found")
    }

    fun setRequestContext(context: RequestCtx) {
        threadLocal.set(context)
    }

    fun clearRequestContext() {
        threadLocal.remove()
    }
}
