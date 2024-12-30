package com.ricky.yu.habitTracker.context

object RequestCtxHolder {
    private val threadLocal = ThreadLocal<RequestCtx>()

    fun getRequestContext(): RequestCtx {
        checkNotNull(threadLocal.get()) { "No requestContext found" }
        return threadLocal.get()
    }

    fun setRequestContext(context: RequestCtx) {
        threadLocal.set(context)
    }

    fun clearRequestContext() {
        threadLocal.remove()
    }
}
