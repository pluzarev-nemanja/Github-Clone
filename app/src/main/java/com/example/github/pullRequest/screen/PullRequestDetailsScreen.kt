package com.example.github.pullRequest.screen

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.github.R
import com.example.github.common.action.UserActionUiEvent
import com.example.github.common.components.ErrorScreen
import com.example.github.common.components.LoadingScreen
import com.example.github.pullRequest.model.PullRequestDetailsUiModel
import com.example.github.pullRequest.uiState.PullRequestDetailsUiState
import com.example.github.pullRequest.util.PullRequestDetailsProvider

@Composable
fun PullRequestDetailsScreen(
    modifier: Modifier = Modifier,
    pullRequestDetailsUiState: PullRequestDetailsUiState,
    navController: NavHostController,
    bottomPaddingValues: PaddingValues,
    userAction: (UserActionUiEvent) -> Unit,
) {
    Scaffold(
        topBar = {
            PullRequestDetailsTopBar(navController = navController)
        },
    ) { topPadding: PaddingValues ->

        when (pullRequestDetailsUiState) {
            PullRequestDetailsUiState.Loading -> LoadingScreen()
            PullRequestDetailsUiState.Empty -> {}
            is PullRequestDetailsUiState.Success -> {
                PullRequestDetailsColumn(
                    pullRequest = pullRequestDetailsUiState.data,
                    topPaddingValues = topPadding,
                    bottomPaddingValues = bottomPaddingValues,
                )
            }

            is PullRequestDetailsUiState.Error -> {
                when (pullRequestDetailsUiState) {
                    is PullRequestDetailsUiState.Error.Unknown -> ErrorScreen(message = pullRequestDetailsUiState.message)
                    is PullRequestDetailsUiState.Error.Connection ->
                        ErrorScreen(
                            message = pullRequestDetailsUiState.message,
                            shouldShowButton = true,
                            onRetryClick = { userAction.invoke(UserActionUiEvent.OnRefreshClick) },
                        )

                    is PullRequestDetailsUiState.Error.Server -> ErrorScreen(message = pullRequestDetailsUiState.message)
                }
            }
        }
    }
}

@Composable
fun PullRequestDetailsColumn(
    modifier: Modifier = Modifier,
    pullRequest: PullRequestDetailsUiModel,
    topPaddingValues: PaddingValues,
    bottomPaddingValues: PaddingValues,
) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(
                    top = topPaddingValues.calculateTopPadding(),
                    bottom = bottomPaddingValues.calculateBottomPadding(),
                )
                .padding(dimensionResource(id = R.dimen.mediumPadding)),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            AsyncImage(
                modifier =
                    Modifier
                        .clip(CircleShape)
                        .size(dimensionResource(id = R.dimen.extraLargeIcon)),
                model = pullRequest.authorImage,
                contentDescription = stringResource(R.string.user_icon),
            )
            Text(
                text = pullRequest.authorName,
                fontSize = MaterialTheme.typography.headlineMedium.fontSize,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
            )
        }
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.smallPadding)))
        Text(
            text = stringResource(id = R.string.created_pull_request),
            fontSize = MaterialTheme.typography.bodyLarge.fontSize,
            fontWeight = FontWeight.Thin,
            fontFamily = FontFamily.Monospace,
        )
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.smallPadding)))
        Text(
            text = pullRequest.title,
            fontSize = MaterialTheme.typography.bodySmall.fontSize,
            fontFamily = FontFamily.Monospace,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.largePadding)))
        Card(
            modifier =
                Modifier
                    .fillMaxWidth(),
        ) {
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(dimensionResource(id = R.dimen.smallPadding)),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start,
            ) {
                Text(
                    text = stringResource(R.string.short_description),
                    fontSize = MaterialTheme.typography.titleMedium.fontSize,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                )
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.smallPadding)))

                Text(
                    text = pullRequest.description,
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                    fontWeight = FontWeight.Thin,
                    fontFamily = FontFamily.Monospace,
                    maxLines = 3,
                    textAlign = TextAlign.Justify,
                )
            }
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(dimensionResource(id = R.dimen.smallPadding)),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
            ) {
                Text(
                    text = stringResource(R.string.milestone_reached),
                    fontSize = MaterialTheme.typography.titleMedium.fontSize,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                )

                Text(
                    text = pullRequest.milestone,
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                    fontWeight = FontWeight.Thin,
                    fontFamily = FontFamily.Monospace,
                )
            }
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.mediumIconSize)))
            Text(
                modifier = Modifier.padding(dimensionResource(id = R.dimen.smallPadding)),
                text = stringResource(R.string.labels),
                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
            )
            repeat(pullRequest.labels.size) { index ->
                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(start = dimensionResource(id = R.dimen.largeIconSize))
                            .padding(dimensionResource(id = R.dimen.smallPadding))
                            .border(2.dp, MaterialTheme.colorScheme.inversePrimary, CircleShape)
                            .padding(dimensionResource(id = R.dimen.smallPadding)),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = pullRequest.labels[index].labelName,
                        fontSize = MaterialTheme.typography.titleSmall.fontSize,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                    )
                    Icon(
                        modifier =
                            Modifier
                                .padding(start = dimensionResource(id = R.dimen.smallPadding))
                                .size(dimensionResource(id = R.dimen.smallImageIcon)),
                        painter = painterResource(id = R.drawable.ic_label),
                        contentDescription = stringResource(R.string.label_icon),
                    )
                }
            }
            Text(
                modifier = Modifier.padding(dimensionResource(id = R.dimen.smallPadding)),
                text = stringResource(R.string.reviewers),
                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
            )
            repeat(pullRequest.reviewers.size) { index ->
                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(start = dimensionResource(id = R.dimen.largeIconSize))
                            .padding(dimensionResource(id = R.dimen.smallPadding))
                            .border(2.dp, MaterialTheme.colorScheme.inversePrimary, CircleShape)
                            .padding(dimensionResource(id = R.dimen.smallPadding)),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = pullRequest.reviewers[index].reviewerName,
                        fontSize = MaterialTheme.typography.titleSmall.fontSize,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                    )
                    AsyncImage(
                        modifier =
                            Modifier
                                .size(dimensionResource(id = R.dimen.smallImageIcon))
                                .clip(CircleShape),
                        model = pullRequest.reviewers[index].reviewerImage,
                        contentDescription = stringResource(id = R.string.user_icon),
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PullRequestDetailsTopBar(
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {
    TopAppBar(
        title = {
            Box(modifier = modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text(
                    text = stringResource(R.string.pull_request_details),
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

@Preview(showBackground = true)
@Composable
fun PullRequestDetailsPreview(
    @PreviewParameter(PullRequestDetailsProvider::class) pull: PullRequestDetailsUiModel,
) {
    PullRequestDetailsColumn(
        pullRequest = pull,
        topPaddingValues = PaddingValues(),
        bottomPaddingValues = PaddingValues(),
    )
}
