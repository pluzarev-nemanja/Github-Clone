package com.example.github.auth.screen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.github.R
import com.example.github.auth.uiState.LoginUiState
import com.example.github.common.action.UserActionUiEvent
import com.example.github.common.navigation.route.Graph

@Composable
fun AuthScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    userAction: (UserActionUiEvent) -> Unit,
    loginState: LoginUiState,
) {
    val context = LocalContext.current

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(dimensionResource(id = R.dimen.mediumPadding))
                .padding(bottom = dimensionResource(id = R.dimen.mediumPadding)),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        LaunchedEffect(key1 = loginState) {
            if (loginState is LoginUiState.Completed) {
                navController.popBackStack()
                navController.navigate(Graph.MainNavGraph.route)
            }
            if (loginState is LoginUiState.Canceled) {
                Toast.makeText(
                    context,
                    context.getString(R.string.something_went_wrong),
                    Toast.LENGTH_SHORT,
                ).show()
            }
        }

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                userAction.invoke(UserActionUiEvent.OnLoginClick)
            },
            shape = RectangleShape,
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.ic_github),
                    contentDescription = stringResource(R.string.github_logo),
                    modifier = Modifier.size(dimensionResource(id = R.dimen.mediumIconSize)),
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary),
                )
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.mediumPadding)))
                if (loginState is LoginUiState.Loading) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.inversePrimary,
                        modifier = Modifier.size(dimensionResource(id = R.dimen.mediumIconSize)),
                    )
                } else {
                    Text(text = stringResource(R.string.sign_in_with_github))
                }
            }
        }
        OutlinedButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                userAction.invoke(UserActionUiEvent.OnEnterAsGuestClick)
            },
            shape = RectangleShape,
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.ic_person),
                    contentDescription = stringResource(R.string.user_icon),
                    modifier = Modifier.size(dimensionResource(id = R.dimen.mediumIconSize)),
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
                )
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.mediumPadding)))
                Text(text = stringResource(R.string.enter_as_guest))
            }
        }
        Text(
            modifier =
                Modifier.padding(
                    top =
                        dimensionResource(
                            id = R.dimen.mediumPadding,
                        ),
                ),
            text = stringResource(R.string.credentials),
            fontWeight = FontWeight.Light,
            fontFamily = FontFamily.Monospace,
            fontSize = MaterialTheme.typography.bodyMedium.fontSize,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
        )
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.largePadding)))
    }
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        AsyncImage(
            modifier = Modifier.size(dimensionResource(id = R.dimen.extraLargeIcon)),
            model = R.drawable.ic_github,
            contentDescription = stringResource(R.string.github_icon),
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
        )
    }
}
