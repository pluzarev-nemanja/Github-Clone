package com.example.github.userRepo.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
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
import androidx.paging.compose.items
import coil.compose.AsyncImage
import com.example.domain.model.ErrorResponse
import com.example.github.R
import com.example.github.common.action.UserActionUiEvent
import com.example.github.common.components.ErrorScreen
import com.example.github.common.components.LoadingScreen
import com.example.github.common.navigation.route.Screen
import com.example.github.userRepo.model.UserRepoUiModel
import com.example.github.userRepo.uiState.RepositoryUiState
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

@Composable
fun UserRepositoryScreen(
    navController: NavHostController,
    userRepositoryUiState: RepositoryUiState,
    bottomPadding: PaddingValues,
    userAction: (UserActionUiEvent) -> Unit,
) {
    Scaffold(topBar = {
        UserRepositoryTopBar(
            navController = navController,
        )
    }) { paddingValues: PaddingValues ->

        when (userRepositoryUiState) {
            RepositoryUiState.Loading -> LoadingScreen()

            RepositoryUiState.Empty -> {
            }

            is RepositoryUiState.Success ->
                LazyColumWithPaging(
                    userRepositories = userRepositoryUiState.data.collectAsLazyPagingItems(),
                    paddingValues = paddingValues,
                    bottomPadding = bottomPadding,
                    navController = navController,
                    userAction = userAction,
                )

            is RepositoryUiState.Error -> {
                when (userRepositoryUiState) {
                    is RepositoryUiState.Error.Unknown -> ErrorScreen(message = userRepositoryUiState.message)
                    is RepositoryUiState.Error.Connection -> ErrorScreen(message = userRepositoryUiState.message)
                    is RepositoryUiState.Error.Server -> ErrorScreen(message = userRepositoryUiState.message)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserRepositoryTopBar(
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {
    TopAppBar(
        title = {
            Box(modifier = modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text(
                    text = stringResource(R.string.github_mobile),
                    fontSize = MaterialTheme.typography.headlineLarge.fontSize,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                )
            }
        },
        actions = {
            IconButton(onClick = {
                navController.navigate(Screen.Search.route)
            }) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = stringResource(R.string.search_icon),
                )
            }
        },
    )
}

@SuppressLint("MutableCollectionMutableState")
@Composable
fun LazyColumWithPaging(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    userRepositories: LazyPagingItems<UserRepoUiModel>,
    paddingValues: PaddingValues,
    bottomPadding: PaddingValues,
    userAction: (UserActionUiEvent) -> Unit,
) {
    val lazyListState = rememberLazyListState()

    val itemsIndexList = remember { mutableStateListOf<Int>() }
    var firstVisibleItemIndex by remember { mutableIntStateOf(0) }
    var lastVisibleItemIndex by remember { mutableIntStateOf(0) }
    val itemCount by remember {
        mutableIntStateOf(userRepositories.itemCount)
    }
    var isDataInErrorState by remember {
        mutableStateOf(false)
    }

    if (userRepositories.loadState.refresh is LoadState.Error) {
        val shouldShowButton by remember {
            mutableStateOf((userRepositories.loadState.refresh as LoadState.Error).error is ErrorResponse.Network)
        }
        isDataInErrorState = true
        ErrorScreen(
            message = (userRepositories.loadState.refresh as LoadState.Error).error.message,
            shouldShowButton = shouldShowButton,
            onRetryClick = { userAction.invoke(UserActionUiEvent.OnRefreshClick) },
        )
    }

    LaunchedEffect(key1 = userRepositories) {
        snapshotFlow { lazyListState.layoutInfo }
            .map { layoutInfo ->
                val firstVisibleIndex = layoutInfo.visibleItemsInfo.firstOrNull()?.index ?: 0
                val lastVisibleIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
                firstVisibleIndex to lastVisibleIndex
            }
            .distinctUntilChanged()
            .debounce(500L)
            .collect { (first, last) ->
                firstVisibleItemIndex = first
                lastVisibleItemIndex = last

                if (itemCount != lastVisibleItemIndex) {
                    for (i in firstVisibleItemIndex..lastVisibleItemIndex) {
                        if (!itemsIndexList.contains(i) && userRepositories.itemSnapshotList.size > i) {
                            userAction.invoke(
                                UserActionUiEvent.OnUserScrollThroughRepositories(
                                    repositoryName = userRepositories[i]?.repositoryName ?: "",
                                ),
                            )
                            itemsIndexList.add(i)
                            println("Logged item : ${userRepositories[i]}")
                        }
                    }
                }
            }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (userRepositories.loadState.refresh is LoadState.Loading) {
            isDataInErrorState = false
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
            )
        } else {
            LazyColumn(
                modifier =
                    modifier.padding(
                        top = paddingValues.calculateTopPadding(),
                        bottom = bottomPadding.calculateBottomPadding(),
                    ),
                state = lazyListState,
            ) {
                items(
                    count = userRepositories.itemCount,
                ) { index ->

                    userRepositories[index]?.let {
                        RepositoryCard(
                            userRepository = it,
                            navController = navController,
                            userAction = userAction,
                        )
                    }
                }
                item {
                    if (userRepositories.loadState.append is LoadState.Loading) {
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
            LaunchedEffect(key1 = userRepositories) {
                userAction.invoke(
                    UserActionUiEvent.IsRepositoryListEmpty(
                        count = userRepositories.itemCount,
                        isDataInErrorState = isDataInErrorState,
                    ),
                )
            }
        }
    }
}

@Composable
fun RepositoryCard(
    modifier: Modifier = Modifier,
    userRepository: UserRepoUiModel,
    navController: NavHostController,
    userAction: (UserActionUiEvent) -> Unit,
) {
    Card(
        modifier =
            modifier
                .padding(dimensionResource(id = R.dimen.mediumPadding))
                .fillMaxWidth(),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(id = R.dimen.mediumPadding)),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier.size(dimensionResource(id = R.dimen.largeIconSize)),
                contentAlignment = Alignment.BottomEnd,
            ) {
                AsyncImage(
                    modifier =
                        Modifier
                            .clip(CircleShape)
                            .size(dimensionResource(id = R.dimen.largeIconSize)),
                    model = userRepository.userIcon,
                    contentDescription = stringResource(R.string.user_icon),
                )
                Box(
                    modifier =
                        Modifier
                            .size(dimensionResource(id = R.dimen.mediumIconSize))
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.inversePrimary),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_repository),
                        contentDescription = stringResource(R.string.repository_icon),
                        modifier = Modifier.size(dimensionResource(id = R.dimen.smallCornerRadius)),
                    )
                }
            }
            Text(
                modifier =
                    Modifier
                        .padding(start = dimensionResource(id = R.dimen.mediumPadding)),
                text = userRepository.userName,
                overflow = TextOverflow.Ellipsis,
            )
        }
        RepositoryName(
            userName = userRepository.userName,
            userImage = userRepository.userIcon,
            repositoryName = userRepository.repositoryName,
        )

        RepositoryDescription(descriptionText = userRepository.repositoryDescription)
        IssuesRow(modifier = modifier, userRepository = userRepository)
        LabelsLazyRow(modifier = modifier, userRepository = userRepository)

        Button(
            onClick = {
                navController.navigate(Screen.RepositoryDetails.route)
                userAction.invoke(
                    UserActionUiEvent.OnRepositoryClick(
                        owner = userRepository.userName,
                        repository = userRepository.repositoryName,
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

@Composable
private fun LabelsLazyRow(
    modifier: Modifier,
    userRepository: UserRepoUiModel,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.mediumPadding)),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier.padding(start = dimensionResource(id = R.dimen.smallPadding)),
            text = stringResource(R.string.labels_list),
            fontSize = MaterialTheme.typography.bodyMedium.fontSize,
        )

        LazyRow {
            items(
                count = userRepository.labelNames.size,
            ) { index ->
                Text(
                    modifier = Modifier.padding(start = dimensionResource(id = R.dimen.smallPadding)),
                    text = userRepository.labelNames[index],
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                )
                Text(
                    text = stringResource(id = R.string.slash),
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}

@Composable
private fun IssuesRow(
    modifier: Modifier,
    userRepository: UserRepoUiModel,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.mediumPadding)),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier.padding(start = dimensionResource(id = R.dimen.smallPadding)),
            text = stringResource(R.string.open_issues),
            fontSize = MaterialTheme.typography.bodyMedium.fontSize,
        )

        Text(
            modifier = Modifier.padding(start = dimensionResource(id = R.dimen.smallPadding)),
            text = userRepository.issueCount,
            fontSize = MaterialTheme.typography.bodyMedium.fontSize,
        )
    }
}

@Composable
fun RepositoryDescription(
    modifier: Modifier = Modifier,
    descriptionText: String,
) {
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.smallPadding)),
    ) {
        Text(
            modifier =
                Modifier.padding(
                    start =
                        dimensionResource(
                            id = R.dimen.smallPadding,
                        ),
                    bottom =
                        dimensionResource(
                            id = R.dimen.smallPadding,
                        ),
                ),
            text = stringResource(R.string.short_description),
            fontWeight = FontWeight.ExtraBold,
            fontFamily = FontFamily.Monospace,
            fontSize = MaterialTheme.typography.bodyMedium.fontSize,
        )

        Text(
            modifier = Modifier.padding(start = dimensionResource(id = R.dimen.smallPadding)),
            text = descriptionText,
            fontWeight = FontWeight.Light,
            fontFamily = FontFamily.Monospace,
            overflow = TextOverflow.Ellipsis,
            fontSize = MaterialTheme.typography.bodySmall.fontSize,
            maxLines = 4,
        )
    }
}

@Composable
fun RepositoryName(
    modifier: Modifier = Modifier,
    userName: String,
    userImage: String,
    repositoryName: String,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.mediumPadding)),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AsyncImage(
            modifier =
                Modifier
                    .size(dimensionResource(id = R.dimen.smallImageIcon))
                    .clip(CircleShape),
            model = userImage,
            contentDescription = stringResource(id = R.string.user_icon),
        )
        Text(
            modifier = Modifier.padding(start = dimensionResource(id = R.dimen.mediumPadding)),
            text = userName,
            fontWeight = FontWeight.Bold,
            fontSize = MaterialTheme.typography.bodySmall.fontSize,
            fontFamily = FontFamily.Monospace,
        )
        Text(
            text = stringResource(R.string.slash),
            fontFamily = FontFamily.Monospace,
            modifier = Modifier.padding(start = dimensionResource(id = R.dimen.smallPadding)),
        )
        Text(
            modifier =
                Modifier
                    .padding(start = dimensionResource(id = R.dimen.mediumPadding)),
            text = repositoryName,
            fontWeight = FontWeight.Bold,
            fontSize = MaterialTheme.typography.bodySmall.fontSize,
            fontFamily = FontFamily.Monospace,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
        )
    }
}
