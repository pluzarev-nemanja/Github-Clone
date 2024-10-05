package com.example.github.common.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import coil.compose.AsyncImage
import com.example.github.R

@Composable
fun ErrorScreen(
    modifier: Modifier = Modifier,
    message: String?,
    shouldShowButton: Boolean = false,
    onRetryClick: () -> Unit = {},
) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(id = R.dimen.mediumPadding)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            AsyncImage(
                modifier = Modifier.size(dimensionResource(id = R.dimen.largeIconSize)),
                model = R.drawable.ic_github,
                contentDescription = stringResource(R.string.github_icon),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
            )
            Text(
                modifier =
                    Modifier.padding(
                        top =
                            dimensionResource(
                                id = R.dimen.mediumPadding,
                            ),
                    ),
                text = message ?: stringResource(R.string.unknown_error),
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.mediumPadding)))
            AnimatedVisibility(
                visible = shouldShowButton,
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                Button(onClick = onRetryClick) {
                    Text(
                        text = stringResource(R.string.refresh),
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                    )
                }
            }
        }
    }
}
