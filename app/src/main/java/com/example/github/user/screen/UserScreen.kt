package com.example.github.user.screen

import androidx.annotation.DrawableRes
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.github.R
import com.example.github.common.action.UserActionUiEvent
import com.example.github.common.components.ErrorScreen
import com.example.github.common.components.LoadingScreen
import com.example.github.user.model.UserUiModel
import com.example.github.user.uiState.UserUiState
import com.example.github.user.util.UserUiModelPreviewProvider

@Composable
fun UserScreen(
    modifier: Modifier = Modifier,
    userUiState: UserUiState,
    isUserLoggedIn: Boolean,
    navController: NavHostController,
    bottomPadding: PaddingValues,
    userAction: (UserActionUiEvent) -> Unit,
) {
    Scaffold(
        topBar = {
            UserTopBar()
        },
    ) { topPadding ->

        when (userUiState) {
            UserUiState.Loading -> LoadingScreen()
            UserUiState.Empty -> {}
            is UserUiState.Success -> {
                UserScreenContent(
                    userUiModel = userUiState.data,
                    isUserLoggedIn = isUserLoggedIn,
                    topPadding = topPadding,
                    bottomPadding = bottomPadding,
                    userAction = userAction,
                    navController = navController,
                )
            }

            is UserUiState.Error -> {
                when (userUiState) {
                    is UserUiState.Error.Unknown -> ErrorScreen(message = userUiState.message)
                    is UserUiState.Error.Connection ->
                        ErrorScreen(
                            message = userUiState.message,
                            shouldShowButton = true,
                            onRetryClick = { userAction.invoke(UserActionUiEvent.OnRefreshClick) },
                        )

                    is UserUiState.Error.Server -> ErrorScreen(message = userUiState.message)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserTopBar(modifier: Modifier = Modifier) {
    TopAppBar(
        title = {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text(
                    text = stringResource(R.string.profile),
                    fontSize = MaterialTheme.typography.headlineLarge.fontSize,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                )
            }
        },
    )
}

@Composable
fun UserScreenContent(
    modifier: Modifier = Modifier,
    userUiModel: UserUiModel,
    topPadding: PaddingValues,
    bottomPadding: PaddingValues,
    isUserLoggedIn: Boolean,
    navController: NavHostController,
    userAction: (UserActionUiEvent) -> Unit,
) {
    Column(
        modifier =
            modifier
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
                .padding(
                    top = topPadding.calculateTopPadding(),
                    bottom = bottomPadding.calculateBottomPadding(),
                )
                .padding(dimensionResource(id = R.dimen.mediumPadding)),
    ) {
        Card(
            modifier = modifier.clip(RoundedCornerShape(dimensionResource(id = R.dimen.smallPadding))),
        ) {
            Box(
                modifier =
                    modifier
                        .fillMaxWidth()
                        .padding(dimensionResource(id = R.dimen.mediumPadding)),
                contentAlignment = Alignment.Center,
            ) {
                AsyncImage(
                    modifier = Modifier.clip(CircleShape),
                    model = userUiModel.userImage,
                    contentDescription = stringResource(R.string.profile_image),
                )
            }
        }
        UserHeader(userUiModel)
        Biography()
        Text(
            modifier = Modifier.padding(start = dimensionResource(id = R.dimen.smallPadding)),
            text = userUiModel.biography,
            fontSize = MaterialTheme.typography.titleMedium.fontSize,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
        )
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.mediumPadding)))
        Card {
            Text(
                modifier = Modifier.padding(start = dimensionResource(id = R.dimen.smallPadding)),
                text = stringResource(R.string.user_details),
                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.ExtraBold,
            )
            MoreInfoRow(
                text = userUiModel.email,
                icon = R.drawable.ic_email,
            )
            MoreInfoRow(
                text = userUiModel.company,
                icon = R.drawable.ic_company,
            )

            MoreInfoRow(
                text = userUiModel.location,
                icon = R.drawable.ic_location,
            )

            MoreInfoRow(
                text = userUiModel.followers,
                icon = R.drawable.ic_followers,
            )

            MoreInfoRow(
                text = userUiModel.following,
                icon = R.drawable.ic_followers,
            )
        }
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.mediumPadding)))

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                if (isUserLoggedIn) {
                    userAction.invoke(UserActionUiEvent.OnLogOutClick)
                } else {
                    userAction.invoke(UserActionUiEvent.OnSignInAgainClick)
                }
            },
            shape = RectangleShape,
        ) {
            Text(
                text = if (isUserLoggedIn) stringResource(R.string.sign_out) else stringResource(R.string.sign_in),
                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Composable
private fun MoreInfoRow(
    text: String,
    @DrawableRes icon: Int,
) {
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
            painter = painterResource(id = icon),
            contentDescription =
                stringResource(
                    id = R.string.user_icon,
                ),
        )
        Text(
            modifier = Modifier.padding(start = dimensionResource(id = R.dimen.smallPadding)),
            text = text,
            fontSize = MaterialTheme.typography.titleMedium.fontSize,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Composable
private fun Biography() {
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
                    id = R.string.user_icon,
                ),
        )
        Text(
            modifier = Modifier.padding(start = dimensionResource(id = R.dimen.smallPadding)),
            text = stringResource(R.string.biography),
            fontSize = MaterialTheme.typography.titleMedium.fontSize,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Thin,
        )
    }
}

@Composable
private fun UserHeader(userUiModel: UserUiModel) {
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
                    id = R.string.user_icon,
                ),
        )
        Text(
            modifier = Modifier.padding(start = dimensionResource(id = R.dimen.smallPadding)),
            text = stringResource(R.string.user_name),
            fontSize = MaterialTheme.typography.titleMedium.fontSize,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Thin,
        )
        Text(
            modifier = Modifier.padding(start = dimensionResource(id = R.dimen.smallPadding)),
            text = userUiModel.name,
            fontSize = MaterialTheme.typography.titleMedium.fontSize,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun UserScreenPreview(
    @PreviewParameter(UserUiModelPreviewProvider::class) userUiModel: UserUiModel,
) {
    UserScreenContent(
        userUiModel = userUiModel,
        topPadding = PaddingValues(dimensionResource(id = R.dimen.mediumPadding)),
        bottomPadding = PaddingValues(dimensionResource(id = R.dimen.mediumPadding)),
        userAction = {},
        navController = rememberNavController(),
        isUserLoggedIn = false,
    )
}
