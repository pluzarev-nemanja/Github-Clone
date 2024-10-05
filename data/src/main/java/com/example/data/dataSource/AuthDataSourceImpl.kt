package com.example.data.dataSource

import com.example.domain.dataSource.AuthDataSource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GithubAuthProvider
import kotlinx.coroutines.tasks.await

class AuthDataSourceImpl(
    private val auth: FirebaseAuth,
) : AuthDataSource {
    override suspend fun signInWithGithub(token: String): FirebaseUser? {
        val credential = GithubAuthProvider.getCredential(token)
        val authResult = auth.signInWithCredential(credential).await()
        return authResult.user
    }

    override suspend fun getCurrentUser(): FirebaseUser? = auth.currentUser

    override suspend fun signOut() {
        auth.signOut()
    }
}
