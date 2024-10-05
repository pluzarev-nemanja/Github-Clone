package com.example.github.user.mapper

import com.example.domain.mapper.Mapper
import com.example.domain.model.User
import com.example.github.user.model.UserUiModel

class UserToUserUiModelMapper : Mapper<User, UserUiModel> {
    override suspend fun mappingObjects(input: User): UserUiModel =
        UserUiModel(
            name = input.name ?: "Unknown",
            userImage = input.userImage,
            email = input.email ?: "No email found.",
            company = input.company ?: "No company found.",
            biography = input.biography ?: "No biography found.",
            followers = input.followers.toString().plus(" Followers"),
            following = input.following.toString().plus(" Following"),
            location = input.location ?: "No location found.",
        )
}
