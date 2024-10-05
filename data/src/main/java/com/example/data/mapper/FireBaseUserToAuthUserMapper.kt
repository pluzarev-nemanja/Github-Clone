package com.example.data.mapper

import com.example.domain.mapper.Mapper
import com.example.domain.model.AuthUser
import com.google.firebase.auth.FirebaseUser

class FireBaseUserToAuthUserMapper : Mapper<FirebaseUser?, AuthUser?> {
    override suspend fun mappingObjects(input: FirebaseUser?): AuthUser? =
        input?.let {
            AuthUser(
                uid = input.uid,
                displayName = input.displayName,
                email = input.email,
            )
        }
}
