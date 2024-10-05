package com.example.github.userRepo.screen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.navigation.NavHostController
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.github.R
import com.example.github.common.action.UserActionUiEvent
import com.example.github.common.components.EmptyScreen
import com.example.github.common.components.InitialSearchScreen
import com.example.github.common.components.LoadingScreen
import com.example.github.userRepo.uiState.SearchedRepositoryUiState

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    bottomPadding: PaddingValues,
    userAction: (UserActionUiEvent) -> Unit,
    navController: NavHostController,
    searchRepositoryText: String,
    searchedRepositoryUiState: SearchedRepositoryUiState,
) {
    Scaffold(
        topBar = {
            SearchTopBar(
                onUserAction = userAction,
                searchText = searchRepositoryText,
            )
        },
    ) { padding ->
        when (searchedRepositoryUiState) {
            is SearchedRepositoryUiState.Success ->
                LazyColumWithPaging(
                    userRepositories = searchedRepositoryUiState.data.collectAsLazyPagingItems(),
                    paddingValues = padding,
                    bottomPadding = bottomPadding,
                    navController = navController,
                    userAction = userAction,
                )

            SearchedRepositoryUiState.Loading -> LoadingScreen()
            SearchedRepositoryUiState.Empty -> EmptyScreen()
            SearchedRepositoryUiState.Initial -> InitialSearchScreen()
            is SearchedRepositoryUiState.Error -> {
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTopBar(
    modifier: Modifier = Modifier,
    onUserAction: (UserActionUiEvent) -> Unit,
    searchText: String,
) {
    TopAppBar(
        title = {
            OutlinedTextField(
                value = searchText,
                onValueChange = {
                    onUserAction.invoke(UserActionUiEvent.UpdateSearchRepositoryText(text = it))
                },
                placeholder = { Text(text = stringResource(R.string.search_repository)) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions =
                    KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Search,
                    ),
                keyboardActions =
                    KeyboardActions(
                        onSearch = {
                            onUserAction.invoke(UserActionUiEvent.OnSearchRepositoryClick(query = searchText))
                        },
                    ),
            )
        },
        actions = {
            IconButton(onClick = {
                onUserAction.invoke(UserActionUiEvent.UpdateSearchRepositoryText(text = ""))
            }) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(R.string.clear_search),
                )
            }
        },
    )
}
