package com.example.domain.model

data class User(
    val name: String?,
    val userImage: String,
    val email: String?,
    val company: String?,
    val biography: String?,
    val followers: Int,
    val following: Int,
    val location: String?,
)
