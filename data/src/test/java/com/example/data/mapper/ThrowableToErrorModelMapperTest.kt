package com.example.data.mapper

import android.net.http.HttpException
import com.example.domain.model.ErrorResponse
import kotlinx.coroutines.test.runTest
import okio.IOException
import org.junit.Assert.assertEquals
import org.junit.Test
import java.net.UnknownHostException

class ThrowableToErrorModelMapperTest {
    private val throwableToErrorModelMapper = ThrowableToErrorModelMapper()

    @Test
    fun `given UnknownHostException When mapper is called Then actual is equal to expected`() =
        runTest {
            val hostException = UnknownHostException()

            val expected = ErrorResponse.Network()
            val actual = throwableToErrorModelMapper.mappingObjects(hostException)
            assertEquals(expected, actual)
        }

    @Test
    fun `given HttpException When mapper is called Then actual is equal to expected`() =
        runTest {
            val httpException = HttpException(null, null)
            val expected = ErrorResponse.Host()
            val actual = throwableToErrorModelMapper.mappingObjects(httpException)

            assertEquals(expected, actual)
        }

    @Test
    fun `given IOException When mapper is called Then actual is equal to expected`() =
        runTest {
            val ioException = IOException()
            val expected = ErrorResponse.Unknown()
            val actual = throwableToErrorModelMapper.mappingObjects(ioException)
            assertEquals(expected, actual)
        }

    @Test
    fun `given IllegalStateException When mapper is called Then actual is equal to expected`() =
        runTest {
            val illegalStateException = IllegalStateException()
            val expected = ErrorResponse.Unknown()
            val actual = throwableToErrorModelMapper.mappingObjects(illegalStateException)
            assertEquals(expected, actual)
        }
}
