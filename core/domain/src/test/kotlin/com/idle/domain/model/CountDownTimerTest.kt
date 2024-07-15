package com.idle.domain.model

import com.idle.domain.model.CountDownTimer.Companion.TICK_INTERVAL
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CountDownTimerTest {

    private lateinit var countDownTimer: CountDownTimer

    @BeforeEach
    fun setUp() {
        countDownTimer = CountDownTimer()
    }

    @Test
    fun `타이머는 1초 단위로 값을 방출한다`() = runTest {
        // given
        val totalDuration = TICK_INTERVAL * 5

        // when
        val actual = countDownTimer.start(totalDuration).toList()

        // then
        val expected = listOf(5000L, 4000L, 3000L, 2000L, 1000L, 0L)
        assertEquals(expected, actual)
    }

    @Test
    fun `타이머는 취소되면 더 이상 값을 방출하지 않는다`() = runTest {
        // given
        val totalDuration = TICK_INTERVAL * 5
        val flow = countDownTimer.start(totalDuration)
        val results = mutableListOf<Long>()

        // when
        val job = launch {
            flow.cancellable().collect {
                results.add(it)
            }
        }

        advanceTimeBy(2500L)
        job.cancel()  // 직접 취소 호출

        // then
        val expected = listOf(5000L, 4000L, 3000L)  // 2.5초 후면 이 값들이 수집될 것으로 예상
        assertEquals(expected, results)
    }
}