package com.example.github.userRepo.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.List
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.github.R
import com.example.github.common.action.UserActionUiEvent
import com.example.github.common.components.ErrorScreen
import com.example.github.common.components.LoadingScreen
import com.example.github.core.util.ShadesOfGreenGenerator.GetShadeOfGreen
import com.example.github.userRepo.model.RepositoryDetailsUiModel
import com.example.github.userRepo.uiState.CommitUiState
import com.example.github.userRepo.uiState.RepositoryDetailsUiState

@Composable
fun RepositoryDetailsScreen(
    modifier: Modifier = Modifier,
    repositoryDetailsUiState: RepositoryDetailsUiState,
    paddingValues: PaddingValues,
    commitUiState: CommitUiState,
    navController: NavHostController,
    userAction: (UserActionUiEvent) -> Unit,
) {
    Scaffold(
        topBar = {
            RepositoryDetailsTopBar(
                navController = navController,
            )
        },
    ) { topPadding ->

        when (repositoryDetailsUiState) {
            RepositoryDetailsUiState.Loading -> LoadingScreen()

            RepositoryDetailsUiState.Empty -> {}

            is RepositoryDetailsUiState.Success ->
                RepositoryDetailsContent(
                    repositoryDetails = repositoryDetailsUiState.data,
                    commitUiState = commitUiState,
                    topPadding = topPadding,
                    bottomPadding = paddingValues,
                )

            is RepositoryDetailsUiState.Error -> {
                when (repositoryDetailsUiState) {
                    is RepositoryDetailsUiState.Error.Unknown -> ErrorScreen(message = repositoryDetailsUiState.message)
                    is RepositoryDetailsUiState.Error.Connection ->
                        ErrorScreen(
                            message = repositoryDetailsUiState.message,
                            shouldShowButton = true,
                            onRetryClick = {
                                userAction.invoke(UserActionUiEvent.OnRefreshClick)
                            },
                        )

                    is RepositoryDetailsUiState.Error.Server -> ErrorScreen(message = repositoryDetailsUiState.message)
                }
            }
        }
    }
}

@Composable
fun RepositoryDetailsContent(
    modifier: Modifier = Modifier,
    repositoryDetails: RepositoryDetailsUiModel,
    commitUiState: CommitUiState,
    topPadding: PaddingValues,
    bottomPadding: PaddingValues,
) {
    LazyColumn(
        modifier =
            modifier
                .fillMaxSize()
                .padding(
                    top = topPadding.calculateTopPadding(),
                    bottom = bottomPadding.calculateBottomPadding(),
                )
                .padding(dimensionResource(id = R.dimen.mediumScreenPadding)),
    ) {
        item {
            RepositoryDetailsSection(repositoryDetails = repositoryDetails)
            TopicsList(
                topics = repositoryDetails.topics,
            )
        }
        item {
            when (commitUiState) {
                CommitUiState.Loading ->
                    Row(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        CircularProgressIndicator()
                    }

                CommitUiState.Empty -> {}
                is CommitUiState.Success -> {
                    val commitsList by commitUiState.data.collectAsStateWithLifecycle(initialValue = emptyList())

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.Start,
                    ) {
                        Text(
                            text = stringResource(R.string.past_30_days_commits),
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                        )
                        LazyHorizontalGrid(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .height(dimensionResource(id = R.dimen.extraLargeIcon))
                                    .clip(RoundedCornerShape(dimensionResource(id = R.dimen.smallCornerShape)))
                                    .background(MaterialTheme.colorScheme.secondaryContainer),
                            rows = GridCells.Fixed(4),
                            horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.gridCellSpace)),
                            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.gridCellSpace)),
                        ) {
                            items(commitsList.size) { index ->
                                val shadeOfGreen =
                                    GetShadeOfGreen(commitsList[index])
                                Box(
                                    modifier =
                                        Modifier
                                            .padding(dimensionResource(id = R.dimen.smallPadding))
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(dimensionResource(id = R.dimen.smallCornerShape)))
                                            .size(dimensionResource(id = R.dimen.mediumBoxSize))
                                            .background(shadeOfGreen),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    Text(
                                        fontSize = MaterialTheme.typography.bodySmall.fontSize,
                                        text = commitsList[index].toString(),
                                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),
                                    )
                                }
                            }
                        }
                    }
                }

                is CommitUiState.Error -> {
                    when (commitUiState) {
                        is CommitUiState.Error.Server ->
                            ErrorCommitBox(
                                message = commitUiState.message ?: "",
                            )
                        is CommitUiState.Error.Connection ->
                            ErrorCommitBox(
                                message = commitUiState.message ?: "",
                            )
                        is CommitUiState.Error.Unknown ->
                            ErrorCommitBox(
                                message = commitUiState.message ?: "",
                            )
                    }
                }
            }
        }
    }
}

@Composable
private fun RepositoryDetailsSection(repositoryDetails: RepositoryDetailsUiModel) {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.smallPadding)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AsyncImage(
            modifier =
                Modifier
                    .clip(CircleShape)
                    .size(dimensionResource(id = R.dimen.largeImage)),
            model = repositoryDetails.userIcon,
            contentDescription = stringResource(R.string.user_icon),
        )
        UserNameDetails(userName = repositoryDetails.userName)
        Card {
            RepositoryName(
                repositoryName = repositoryDetails.repositoryName,
            )

            Description()
            Text(
                modifier =
                    Modifier
                        .padding(
                            start = dimensionResource(id = R.dimen.smallPadding),
                            top =
                                dimensionResource(
                                    id = R.dimen.mediumPadding,
                                ),
                        ),
                text = repositoryDetails.repositoryDescription,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )

            StatusCards(
                icon = R.drawable.ic_fork,
                number = repositoryDetails.repositoryForks,
            )
            StatusCards(icon = R.drawable.ic_watchers, number = repositoryDetails.watchers)
            StatusCards(
                icon = R.drawable.ic_followers,
                number = repositoryDetails.userFollowers,
            )
        }
    }
}

@Composable
fun ErrorCommitBox(
    modifier: Modifier = Modifier,
    message: String,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            fontSize = MaterialTheme.typography.bodyMedium.fontSize,
            text = message,
            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),
        )
    }
}

@Composable
fun TopicsList(
    modifier: Modifier = Modifier,
    topics: List<String>,
) {
    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.mediumPadding)))
    Card(
        modifier =
            modifier
                .fillMaxWidth(),
    ) {
        Text(
            modifier = Modifier.padding(dimensionResource(id = R.dimen.smallPadding)),
            text = stringResource(R.string.related_topics),
            fontWeight = FontWeight.ExtraBold,
            fontFamily = FontFamily.Monospace,
            fontSize = MaterialTheme.typography.titleMedium.fontSize,
        )

        LazyRow(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            items(topics) { topic: String ->
                Text(
                    modifier = Modifier.padding(dimensionResource(id = R.dimen.smallPadding)),
                    text = topic,
                    fontWeight = FontWeight.Light,
                    fontFamily = FontFamily.Monospace,
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                    color = MaterialTheme.colorScheme.primary,
                )
                Text(
                    text = stringResource(R.string.slash),
                    fontFamily = FontFamily.Monospace,
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                    modifier = Modifier.padding(start = dimensionResource(id = R.dimen.smallPadding)),
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        }
    }
    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.mediumPadding)))
}

@Composable
fun RepositoryName(
    modifier: Modifier = Modifier,
    repositoryName: String,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.smallPadding)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_repository),
            contentDescription = stringResource(R.string.repository_icon),
            modifier = Modifier.size(dimensionResource(id = R.dimen.mediumIconSize)),
        )

        Text(
            modifier = Modifier.padding(start = dimensionResource(id = R.dimen.smallPadding)),
            text = stringResource(R.string.repository),
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace,
            fontSize = MaterialTheme.typography.bodyMedium.fontSize,
            color = MaterialTheme.colorScheme.primary,
        )

        Text(
            text = stringResource(R.string.slash),
            fontFamily = FontFamily.Monospace,
            fontSize = MaterialTheme.typography.bodyMedium.fontSize,
            modifier = Modifier.padding(start = dimensionResource(id = R.dimen.smallPadding)),
            color = MaterialTheme.colorScheme.primary,
        )
        Text(
            modifier =
                Modifier
                    .padding(start = dimensionResource(id = R.dimen.smallPadding)),
            text = repositoryName,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace,
            fontSize = MaterialTheme.typography.bodyMedium.fontSize,
            color = MaterialTheme.colorScheme.primary,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
        )
    }
}

@Composable
fun UserNameDetails(
    modifier: Modifier = Modifier,
    userName: String,
) {
    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.mediumPadding)))
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        Text(
            modifier = Modifier.padding(start = dimensionResource(id = R.dimen.mediumPadding)),
            text = userName,
            fontWeight = FontWeight.ExtraBold,
            fontSize = MaterialTheme.typography.headlineSmall.fontSize,
            fontFamily = FontFamily.Monospace,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.mediumPadding)))
}

@Composable
fun Description(modifier: Modifier = Modifier) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.smallPadding)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
    ) {
        Icon(
            modifier = Modifier.size(dimensionResource(id = R.dimen.mediumIconSize)),
            imageVector = Icons.Filled.List,
            contentDescription = stringResource(R.string.description_icon),
        )
        Text(
            modifier = Modifier.padding(start = dimensionResource(id = R.dimen.smallPadding)),
            text = stringResource(R.string.repository_description),
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace,
            fontSize = MaterialTheme.typography.bodyMedium.fontSize,
        )
    }
}

@Composable
fun StatusCards(
    modifier: Modifier = Modifier,
    icon: Int,
    number: String,
) {
    Box(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.smallPadding))
                .background(MaterialTheme.colorScheme.inversePrimary),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
        ) {
            Icon(
                modifier =
                    Modifier
                        .padding(dimensionResource(id = R.dimen.smallPadding))
                        .size(dimensionResource(id = R.dimen.mediumIconSize)),
                painter = painterResource(id = icon),
                contentDescription = stringResource(R.string.watchers_icon),
            )
            Text(
                modifier =
                    Modifier.padding(
                        start = dimensionResource(id = R.dimen.smallPadding),
                        top =
                            dimensionResource(
                                id = R.dimen.mediumPadding,
                            ),
                    ),
                text = number,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepositoryDetailsTopBar(
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {
    TopAppBar(
        title = {
            Box(modifier = modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text(
                    text = stringResource(R.string.repository_details),
                    fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = FontFamily.Monospace,
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = {
                navController.navigateUp()
            }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.back_icon),
                )
            }
        },
    )
}
