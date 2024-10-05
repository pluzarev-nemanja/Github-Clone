package com.example.data.mapper

import com.example.data.model.UserResponse
import com.example.domain.mapper.Mapper
import com.example.domain.model.User

class UserResponseToUserMapper : Mapper<UserResponse, User> {
    override suspend fun mappingObjects(input: UserResponse): User =
        User(
            name = input.name,
            userImage = input.avatarUrl,
            email = input.email,
            company = input.company,
            biography = input.biography,
            followers = input.followers,
            following = input.following,
            location = input.location,
        )
}
