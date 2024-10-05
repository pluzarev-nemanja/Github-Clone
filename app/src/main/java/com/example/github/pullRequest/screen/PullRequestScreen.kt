package com.example.github.pullRequest.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.example.domain.model.ErrorResponse
import com.example.github.R
import com.example.github.common.action.UserActionUiEvent
import com.example.github.common.components.ErrorScreen
import com.example.github.common.components.LoadingScreen
import com.example.github.common.navigation.route.Screen
import com.example.github.pullRequest.model.PullRequestUiModel
import com.example.github.pullRequest.uiState.PullRequestUiState

@Composable
fun PullRequestScreen(
    modifier: Modifier = Modifier,
    pullRequestUiState: PullRequestUiState,
    bottomPaddingValues: PaddingValues,
    navController: NavHostController,
    userAction: (UserActionUiEvent) -> Unit,
) {
    Scaffold(
        topBar = {
            PullRequestTopBar()
        },
    ) { topPadding: PaddingValues ->

        when (pullRequestUiState) {
            PullRequestUiState.Loading -> LoadingScreen()
            PullRequestUiState.Empty -> {}
            is PullRequestUiState.Success -> {
                PullRequestColumn(
                    pullRequests = pullRequestUiState.data.collectAsLazyPagingItems(),
                    paddingValues = topPadding,
                    bottomPaddingValues = bottomPaddingValues,
                    navController = navController,
                    onUserAction = userAction,
                )
            }

            is PullRequestUiState.Error -> {
                when (pullRequestUiState) {
                    is PullRequestUiState.Error.Unknown -> {}
                    is PullRequestUiState.Error.Connection -> {}
                }
            }
        }
    }
}

@Composable
fun PullRequestColumn(
    modifier: Modifier = Modifier,
    pullRequests: LazyPagingItems<PullRequestUiModel>,
    paddingValues: PaddingValues,
    bottomPaddingValues: PaddingValues,
    navController: NavHostController,
    onUserAction: (UserActionUiEvent) -> Unit,
) {
    if (pullRequests.loadState.refresh is LoadState.Error) {
        val shouldShowButton by remember {
            mutableStateOf((pullRequests.loadState.refresh as LoadState.Error).error is ErrorResponse.Network)
        }
        ErrorScreen(
            message = (pullRequests.loadState.refresh as LoadState.Error).error.message,
            shouldShowButton = shouldShowButton,
            onRetryClick = { onUserAction.invoke(UserActionUiEvent.OnRefreshClick) },
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (pullRequests.loadState.refresh is LoadState.Loading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
            )
        } else {
            LazyColumn(
                modifier =
                    modifier.padding(
                        top = paddingValues.calculateTopPadding(),
                        bottom = bottomPaddingValues.calculateBottomPadding(),
                    ),
            ) {
                items(
                    count = pullRequests.itemCount,
                    key = { pullRequests.peek(it)?.id ?: 0 },
                ) { index: Int ->
                    pullRequests[index]?.let {
                        PullRequestCard(
                            pullRequest = it,
                            navController = navController,
                            onUserAction = onUserAction,
                        )
                    }
                }
                item {
                    if (pullRequests.loadState.append is LoadState.Loading) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PullRequestCard(
    modifier: Modifier = Modifier,
    pullRequest: PullRequestUiModel,
    navController: NavHostController,
    onUserAction: (UserActionUiEvent) -> Unit,
) {
    Card(
        modifier =
            modifier
                .padding(dimensionResource(id = R.dimen.smallPadding))
                .fillMaxWidth(),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(id = R.dimen.smallPadding)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
        ) {
            AsyncImage(
                modifier =
                    Modifier
                        .clip(CircleShape)
                        .size(dimensionResource(id = R.dimen.largeIconSize)),
                model = pullRequest.userImage,
                contentDescription = stringResource(id = R.string.user_icon),
            )
            Text(
                modifier =
                    Modifier
                        .padding(start = dimensionResource(id = R.dimen.mediumPadding)),
                text = pullRequest.userName,
                fontWeight = FontWeight.ExtraBold,
                fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                fontFamily = FontFamily.Monospace,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                modifier =
                    Modifier
                        .padding(start = dimensionResource(id = R.dimen.mediumPadding)),
                text = stringResource(R.string.created_pull_request),
                fontWeight = FontWeight.Light,
                fontSize = MaterialTheme.typography.bodySmall.fontSize,
                fontFamily = FontFamily.Monospace,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(start = dimensionResource(id = R.dimen.largeIconSize))
                    .padding(dimensionResource(id = R.dimen.smallPadding)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
        ) {
            Icon(
                modifier = Modifier.size(dimensionResource(id = R.dimen.smallImageIcon)),
                painter = painterResource(id = R.drawable.ic_pull_request),
                contentDescription =
                    stringResource(
                        id = R.string.pull_requests,
                    ),
            )
            Text(
                modifier =
                    Modifier
                        .padding(start = dimensionResource(id = R.dimen.mediumPadding)),
                text = pullRequest.title,
                fontWeight = FontWeight.SemiBold,
                fontSize = MaterialTheme.typography.bodySmall.fontSize,
                fontFamily = FontFamily.Monospace,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
        repeat(pullRequest.labels.size) { index ->
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(start = dimensionResource(id = R.dimen.largeIconSize))
                        .padding(dimensionResource(id = R.dimen.smallPadding))
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.inversePrimary),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
            ) {
                Text(text = pullRequest.labels[index].labelName)
            }
        }
        repeat(pullRequest.reviewers.size) { index ->
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(start = dimensionResource(id = R.dimen.largeIconSize))
                        .padding(dimensionResource(id = R.dimen.smallPadding))
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.inversePrimary),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
            ) {
                Text(text = pullRequest.reviewers[index].reviewerName)
            }
        }
        Button(
            onClick = {
                navController.navigate(Screen.PullRequestDetails.route)
                onUserAction.invoke(
                    UserActionUiEvent.OnPullRequestClick(
                        owner = pullRequest.ownerName,
                        repository = pullRequest.repositoryName,
                        pullNumber = pullRequest.id,
                    ),
                )
            },
            modifier =
                Modifier
                    .padding(dimensionResource(id = R.dimen.mediumPadding))
                    .fillMaxWidth(),
        ) {
            Text(text = stringResource(R.string.see_details))
            Icon(
                modifier =
                    Modifier
                        .padding(start = dimensionResource(id = R.dimen.smallPadding))
                        .size(dimensionResource(id = R.dimen.smallImageIcon)),
                painter = painterResource(id = R.drawable.ic_details),
                contentDescription =
                    stringResource(
                        R.string.details_icon,
                    ),
                tint = MaterialTheme.colorScheme.onPrimary,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PullRequestTopBar(modifier: Modifier = Modifier) {
    TopAppBar(
        title = {
            Box(modifier = modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text(
                    text = stringResource(R.string.pull_requests),
                    fontSize = MaterialTheme.typography.headlineMedium.fontSize,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = FontFamily.Monospace,
                )
            }
        },
    )
}
