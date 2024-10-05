package com.example.github.user.util

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.github.user.model.UserUiModel

class UserUiModelPreviewProvider : PreviewParameterProvider<UserUiModel> {
    override val values =
        sequenceOf(
            UserUiModel(
                name = "Octocat",
                userImage = "https://avatars.githubusercontent.com/u/583231?v=4",
                email = "octocat@gmail.com",
                company = "Github",
                biography = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. A enim mattis arcu volutpat facilisis eu at ipsum. ",
                following = "123 following",
                followers = "22 followers",
                location = "San Francisco",
            ),
        )
}
