package com.example.data.mapper

import android.annotation.SuppressLint
import android.net.http.HttpException
import com.example.domain.mapper.Mapper
import com.example.domain.model.ErrorResponse
import okio.IOException
import java.net.UnknownHostException

class ThrowableToErrorModelMapper : Mapper<Throwable, ErrorResponse> {

    @SuppressLint("NewApi")
    override suspend fun mappingObjects(input: Throwable): ErrorResponse =
        when (input) {
            is UnknownHostException -> ErrorResponse.Network()
            is HttpException -> ErrorResponse.Host()
            is IOException -> ErrorResponse.Unknown()
            else -> ErrorResponse.Unknown()
        }
}
