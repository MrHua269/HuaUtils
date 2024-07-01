package i.mrhua269.i.mrhua269.huautils.dispatchers

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Runnable
import java.util.concurrent.Executor
import kotlin.coroutines.CoroutineContext

class ExecutorDispatcherAdapter(
    private val executor: Executor
) : CoroutineDispatcher() {

    override fun dispatch(context: CoroutineContext, block: Runnable) {
        this.executor.execute(block)
    }

}