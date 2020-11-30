package com.gabriel_codarcea.core.data.helpers

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.gabriel_codarcea.core.data.model.Breed
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

internal fun getBreedTest1() = Breed(
    2,
    "id1",
    "cat1",
    "Aggresive",
    "ES",
    "Angry ugly cat",
    "https://nourl1.com",
    "Spain",
    "https://noimage1.com"
)

internal fun getBreedTest2() = Breed(
    4,
    "id2",
    "cat2",
    "Calm",
    "FR",
    "Calm pretty cat",
    "https://nourl2.com",
    "France",
    "https://noimage2.com"
)

fun <T> LiveData<T>.getOrAwaitValue(
    time: Long = 3,
    timeUnit: TimeUnit = TimeUnit.SECONDS
): T {
    var data: T? = null
    val latch = CountDownLatch(1)
    val observer = object : Observer<T> {
        override fun onChanged(o: T?) {
            data = o
            latch.countDown()
            this@getOrAwaitValue.removeObserver(this)
        }
    }

    this.observeForever(observer)

    // Don't wait indefinitely if the LiveData is not set.
    if (!latch.await(time, timeUnit)) {
        throw TimeoutException("LiveData value was never set.")
    }

    @Suppress("UNCHECKED_CAST")
    return data as T
}

