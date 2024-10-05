package com.example.github.topic.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.github.R
import com.example.github.common.action.UserActionUiEvent
import com.example.github.common.components.EmptyScreen
import com.example.github.common.components.ErrorScreen
import com.example.github.common.components.InitialSearchScreen
import com.example.github.common.components.LoadingScreen
import com.example.github.topic.model.TopicUiModel
import com.example.github.topic.uiState.TopicsUiState

@Composable
fun SearchTopicScreen(
    modifier: Modifier = Modifier,
    bottomPadding: PaddingValues,
    topicsUiState: TopicsUiState,
    searchText: String,
    userAction: (UserActionUiEvent) -> Unit,
) {
    Scaffold(
        topBar = {
            TopicTopBar(
                searchText = searchText,
                onUserAction = userAction,
            )
        },
    ) { padding: PaddingValues ->

        when (topicsUiState) {
            TopicsUiState.Initial -> InitialSearchScreen()
            TopicsUiState.Empty -> EmptyScreen()
            TopicsUiState.Loading -> LoadingScreen()
            is TopicsUiState.Success ->
                TopicsLazyColumn(
                    topics = topicsUiState.data.collectAsLazyPagingItems(),
                    topPaddingValues = padding,
                    bottomPadding = bottomPadding,
                    onUserAction = userAction,
                    searchText = searchText,
                )

            is TopicsUiState.Error -> {
                when (topicsUiState) {
                    is TopicsUiState.Error.Unknown -> {}
                    is TopicsUiState.Error.Connection -> {}
                    is TopicsUiState.Error.Server -> {}
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopicTopBar(
    modifier: Modifier = Modifier,
    searchText: String,
    onUserAction: (UserActionUiEvent) -> Unit,
) {
    TopAppBar(
        title = {
            OutlinedTextField(
                value = searchText,
                onValueChange = {
                    onUserAction.invoke(UserActionUiEvent.UpdateText(text = it))
                },
                placeholder = { Text(stringResource(id = R.string.search)) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions =
                    KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Search,
                    ),
                keyboardActions =
                    KeyboardActions(
                        onSearch = {
                            onUserAction.invoke(UserActionUiEvent.OnSearchClick(query = searchText))
                        },
                    ),
            )
        },
        actions = {
            IconButton(onClick = {
                onUserAction.invoke(UserActionUiEvent.UpdateText(text = ""))
            }) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(R.string.clear_search),
                )
            }
        },
    )
}

@Composable
fun TopicsLazyColumn(
    modifier: Modifier = Modifier,
    topPaddingValues: PaddingValues,
    bottomPadding: PaddingValues,
    searchText: String,
    topics: LazyPagingItems<TopicUiModel>,
    onUserAction: (UserActionUiEvent) -> Unit,
) {
    var isDataInErrorState by remember {
        mutableStateOf(false)
    }
    if (topics.loadState.refresh is LoadState.Error) {
        isDataInErrorState = true
        ErrorScreen(
            message = (topics.loadState.refresh as LoadState.Error).error.message,
            shouldShowButton = false,
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (topics.loadState.refresh is LoadState.Loading) {
            isDataInErrorState = false
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
            )
        } else {
            LazyColumn(
                modifier =
                    modifier
                        .fillMaxSize()
                        .padding(
                            top = topPaddingValues.calculateTopPadding(),
                            bottom = bottomPadding.calculateBottomPadding(),
                        )
                        .padding(dimensionResource(id = R.dimen.mediumPadding)),
            ) {
                items(
                    count = topics.itemCount,
                ) { index: Int ->

                    topics[index]?.let {
                        TopicCard(
                            topic = it,
                        )
                    }
                }
                item {
                    if (topics.loadState.append is LoadState.Loading) {
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
            LaunchedEffect(key1 = topics) {
                onUserAction.invoke(
                    UserActionUiEvent.IsTopicListEmpty(
                        count = topics.itemCount,
                        isDataInErrorState = isDataInErrorState,
                    ),
                )
            }
        }
    }
}

@Composable
fun TopicCard(
    modifier: Modifier = Modifier,
    topic: TopicUiModel,
) {
    Card(
        modifier =
            modifier
                .padding(dimensionResource(id = R.dimen.mediumPadding))
                .fillMaxWidth(),
    ) {
        TopicHeader(topic)
        ShortDescription()
        Text(
            modifier = Modifier.padding(start = dimensionResource(id = R.dimen.mediumPadding)),
            text = topic.shortDescription,
            fontWeight = FontWeight.Thin,
            fontSize = MaterialTheme.typography.bodySmall.fontSize,
            fontFamily = FontFamily.Monospace,
        )
        CreatedBy(topic)
        CreatedAt(topic)
    }
}

@Composable
private fun CreatedAt(topic: TopicUiModel) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.mediumPadding)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
    ) {
        Icon(
            modifier = Modifier.size(dimensionResource(id = R.dimen.mediumIconSize)),
            painter = painterResource(id = R.drawable.ic_time),
            contentDescription =
                stringResource(
                    id = R.string.related_topics,
                ),
        )
        Text(
            modifier = Modifier.padding(start = dimensionResource(id = R.dimen.mediumPadding)),
            text = topic.createdAt,
            fontWeight = FontWeight.Thin,
            fontSize = MaterialTheme.typography.bodyLarge.fontSize,
            fontFamily = FontFamily.Monospace,
        )
    }
}

@Composable
private fun CreatedBy(topic: TopicUiModel) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.mediumPadding)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
    ) {
        Icon(
            modifier = Modifier.size(dimensionResource(id = R.dimen.mediumIconSize)),
            painter = painterResource(id = R.drawable.ic_person),
            contentDescription =
                stringResource(
                    id = R.string.related_topics,
                ),
        )
        Text(
            modifier = Modifier.padding(start = dimensionResource(id = R.dimen.mediumPadding)),
            text = topic.createdBy,
            fontWeight = FontWeight.Thin,
            fontSize = MaterialTheme.typography.bodyLarge.fontSize,
            fontFamily = FontFamily.Monospace,
        )
    }
}

@Composable
private fun ShortDescription() {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.mediumPadding)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
    ) {
        Icon(
            modifier = Modifier.size(dimensionResource(id = R.dimen.mediumIconSize)),
            painter = painterResource(id = R.drawable.ic_description),
            contentDescription =
                stringResource(
                    id = R.string.related_topics,
                ),
        )
        Text(
            modifier = Modifier.padding(start = dimensionResource(id = R.dimen.mediumPadding)),
            text = stringResource(id = R.string.short_description),
            fontWeight = FontWeight.Thin,
            fontSize = MaterialTheme.typography.bodySmall.fontSize,
            fontFamily = FontFamily.Monospace,
        )
    }
}

@Composable
private fun TopicHeader(topic: TopicUiModel) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.smallPadding)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
    ) {
        Icon(
            modifier = Modifier.size(dimensionResource(id = R.dimen.smallImageIcon)),
            painter = painterResource(id = R.drawable.ic_topic),
            contentDescription =
                stringResource(
                    id = R.string.related_topics,
                ),
        )
        Text(
            modifier = Modifier.padding(start = dimensionResource(id = R.dimen.mediumPadding)),
            text = topic.name,
            fontWeight = FontWeight.Bold,
            fontSize = MaterialTheme.typography.bodyLarge.fontSize,
            fontFamily = FontFamily.Monospace,
        )
    }
}
