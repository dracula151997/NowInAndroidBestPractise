package com.dracula.nowinandroidbestpractise

import app.cash.turbine.test
import com.dracula.nowinandroidbestpractise.core.common.Result
import com.dracula.nowinandroidbestpractise.core.common.asResult
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class ResultKtTest {
    @Test
    fun result_catches_errors() = runTest {
        flow {
            emit(1)
            emit(2)
            emit(3)
            throw Exception("Test Done")
        }.asResult()
            .test {
                assertEquals(Result.Loading, awaitItem())
                assertEquals(Result.Success(1), awaitItem())
                assertEquals(Result.Success(2), awaitItem())
                assertEquals(Result.Success(3), awaitItem())

                when (val errorResult = awaitItem()) {
                    is Result.Error -> assertEquals(
                        "Test Done",
                        errorResult.e?.message
                    )
                    Result.Loading,
                    is Result.Success -> throw IllegalStateException(
                        "The flow should have emitted an Error Result"
                    )
                }

                awaitComplete()
            }
    }
}