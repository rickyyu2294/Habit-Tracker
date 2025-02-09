package com.ricky.yu.habitTracker.context

object RequestCtxHolder {
    private val threadLocal = ThreadLocal<RequestCtx>()

    fun get(): RequestCtx {
        checkNotNull(threadLocal.get()) { "No requestContext found" }
        return threadLocal.get()
    }

    fun set(context: RequestCtx) {
        threadLocal.set(context)
    }

    fun clear() {
        threadLocal.remove()
    }
}
