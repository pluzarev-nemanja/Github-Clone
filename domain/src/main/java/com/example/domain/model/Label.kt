package com.example.domain.model

data class Label(
    val color: String,
    val default: Boolean,
    val description: String,
    val id: Long,
    val name: String,
    val nodeId: String,
    val url: String,
)
