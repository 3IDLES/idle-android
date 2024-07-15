package com.idle.domain.model

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.isActive
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

class CountDownTimer @Inject constructor() {
    private var remainTime: Long = 0

    @Throws(IllegalStateException::class)
    fun start(limitTime: Long): Flow<Long> {
        require((limitTime % TICK_INTERVAL == 0L)) {
            "초 단위의 시간만 측정할 수 있습니다."
        }

        return flow {
            remainTime = limitTime

            while (remainTime > 0 && coroutineContext.isActive) {
                delay(TICK_INTERVAL)
                remainTime -= TICK_INTERVAL
                emit(remainTime)
            }
        }.onStart {
            emit(limitTime)
        }
    }

    companion object {
        const val SECONDS_PER_MINUTE = 60
        const val TICK_INTERVAL = 1000L
    }
}