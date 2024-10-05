package com.example.domain.mapper

fun interface Mapper<in Input, out Output> {
    suspend fun mappingObjects(input: Input): Output
}
