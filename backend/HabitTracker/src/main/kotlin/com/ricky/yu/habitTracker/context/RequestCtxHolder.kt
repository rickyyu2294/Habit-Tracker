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

inline fun <T> usingTempCtx(
    tempCtx: RequestCtx,
    block: () -> T,
): T {
    RequestCtxHolder.set(tempCtx)
    return try {
        block() // Execute the function with the temp context
    } finally {
        RequestCtxHolder.clear() // Always clear the context afterward
    }
}
