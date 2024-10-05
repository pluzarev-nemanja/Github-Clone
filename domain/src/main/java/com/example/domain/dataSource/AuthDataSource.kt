package com.example.domain.dataSource

import com.google.firebase.auth.FirebaseUser

interface AuthDataSource {
    suspend fun signInWithGithub(token: String): FirebaseUser?

    suspend fun getCurrentUser(): FirebaseUser?

    suspend fun signOut()
}
