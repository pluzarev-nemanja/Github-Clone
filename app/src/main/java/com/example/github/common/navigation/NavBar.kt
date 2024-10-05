package com.example.github.common.navigation

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.github.R
import com.example.github.common.navigation.route.BottomNavItem
import com.example.github.user.uiState.UserUiState

@Composable
fun NavBar(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    userUiState: UserUiState,
    isUserLoggedIn: Boolean,
) {
    val navigationBarItems =
        listOf(
            BottomNavItem.UserRepository,
            BottomNavItem.PullRequest,
            BottomNavItem.SearchTopic,
            BottomNavItem.User,
        )

    var selectedIndex by rememberSaveable {
        mutableIntStateOf(0)
    }

    NavigationBar(
        modifier =
            modifier.clip(
                RoundedCornerShape(
                    topStart = dimensionResource(id = R.dimen.smallCornerRadius),
                    topEnd = dimensionResource(id = R.dimen.smallCornerRadius),
                ),
            ),
    ) {
        navigationBarItems.forEachIndexed { index, bottomNavItem: BottomNavItem ->

            if (bottomNavItem is BottomNavItem.User) {
                NavigationBarItem(
                    selected = selectedIndex == index,
                    onClick = {
                        navController.navigateUp()
                        selectedIndex = index
                        navController.navigate(bottomNavItem.route)
                    },
                    icon = {
                        if (userUiState is UserUiState.Success && isUserLoggedIn) {
                            AsyncImage(
                                modifier =
                                    Modifier
                                        .size(dimensionResource(id = R.dimen.smallImageIcon))
                                        .clip(RoundedCornerShape(dimensionResource(id = R.dimen.mediumIconSize)))
                                        .border(
                                            width = dimensionResource(id = R.dimen.smallBorderWidth),
                                            color = MaterialTheme.colorScheme.inversePrimary,
                                            shape =
                                                RoundedCornerShape(
                                                    dimensionResource(id = R.dimen.mediumIconSize),
                                                ),
                                        ),
                                model = userUiState.data.userImage,
                                contentDescription = bottomNavItem.title,
                            )
                        } else {
                            Icon(
                                painter = painterResource(id = bottomNavItem.icon),
                                contentDescription = bottomNavItem.title,
                                modifier = Modifier.size(dimensionResource(id = R.dimen.smallImageIcon)),
                            )
                        }
                    },
                    label = {
                        Text(
                            text = bottomNavItem.title,
                            fontFamily = FontFamily.Monospace,
                            fontSize = MaterialTheme.typography.bodySmall.fontSize,
                            fontWeight = FontWeight.Bold,
                        )
                    },
                    alwaysShowLabel = false,
                )
            } else {
                NavigationBarItem(
                    selected = selectedIndex == index,
                    onClick = {
                        navController.navigateUp()
                        selectedIndex = index
                        navController.navigate(bottomNavItem.route)
                    },
                    icon = {
                        Icon(
                            painter = painterResource(id = bottomNavItem.icon),
                            contentDescription = bottomNavItem.title,
                            modifier = Modifier.size(dimensionResource(id = R.dimen.smallImageIcon)),
                        )
                    },
                    label = {
                        Text(
                            text = bottomNavItem.title,
                            fontFamily = FontFamily.Monospace,
                            fontSize = MaterialTheme.typography.bodySmall.fontSize,
                            fontWeight = FontWeight.Bold,
                        )
                    },
                    alwaysShowLabel = false,
                )
            }
        }
    }

    DisposableEffect(navController) {
        val listener =
            NavController.OnDestinationChangedListener { _, destination, _ ->
                if (destination.route == navigationBarItems[0].route) {
                    selectedIndex = 0
                }
            }
        navController.addOnDestinationChangedListener(listener)

        onDispose {
            navController.removeOnDestinationChangedListener(listener)
        }
    }
}
