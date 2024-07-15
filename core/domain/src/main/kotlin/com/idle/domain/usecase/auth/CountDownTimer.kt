package com.idle.domain.usecase.auth

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class CountDownTimer @Inject constructor() {
    private var remainTime: Long = 0
    private var timerJob: Job? = null

    @Throws(IllegalStateException::class)
    fun start(limitTime: Long, scope: CoroutineScope): Flow<Long> {
        require((limitTime % TICK_INTERVAL == 0L)) {
            "초 단위의 시간만 측정할 수 있습니다."
        }

        return flow {
            remainTime = limitTime

            while (remainTime > 0) {
                delay(TICK_INTERVAL)
                remainTime -= TICK_INTERVAL
                emit(remainTime)
            }
        }.onStart {
            emit(limitTime)
        }.also {
            timerJob?.cancel()
            timerJob = null
            timerJob = it.launchIn(scope)
        }
    }

    fun cancel() {
        timerJob?.cancel()
        timerJob = null
    }


    companion object {
        const val SECONDS_PER_MINUTE = 60
        const val TICK_INTERVAL = 1000L
    }
}