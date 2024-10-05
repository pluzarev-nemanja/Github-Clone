package com.example.github.common.action

sealed class UserActionUiEvent {
    data class OnRepositoryClick(val owner: String, val repository: String) : UserActionUiEvent()

    data class OnPullRequestClick(val owner: String, val repository: String, val pullNumber: Int) :
        UserActionUiEvent()

    data class UpdateText(val text: String) : UserActionUiEvent()

    data class UpdateSearchRepositoryText(val text: String) : UserActionUiEvent()

    data class OnSearchClick(val query: String) : UserActionUiEvent()

    data class OnSearchRepositoryClick(val query: String) : UserActionUiEvent()

    data class IsTopicListEmpty(val count: Int, val isDataInErrorState: Boolean) :
        UserActionUiEvent()

    data class IsRepositoryListEmpty(val count: Int, val isDataInErrorState: Boolean) :
        UserActionUiEvent()

    data object OnRefreshClick : UserActionUiEvent()

    data object OnLoginClick : UserActionUiEvent()

    data object OnLogOutClick : UserActionUiEvent()

    data object OnSignInAgainClick : UserActionUiEvent()

    data object OnEnterAsGuestClick : UserActionUiEvent()

    data class OnUserScrollThroughRepositories(val repositoryName: String) : UserActionUiEvent()
}
