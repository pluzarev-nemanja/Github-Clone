package com.example.github.auth.mapper

import com.example.domain.mapper.Mapper
import com.example.domain.model.AuthUser
import com.example.github.auth.model.AuthUserUiModel

class AuthUserToAuthUserUiModelMapper : Mapper<AuthUser?, AuthUserUiModel?> {
    override suspend fun mappingObjects(input: AuthUser?): AuthUserUiModel? =
        input?.let {
            AuthUserUiModel(
                uid = input.uid,
                displayName = input.displayName ?: "No display name found.",
                email = input.email ?: "No email found.",
            )
        }
}
