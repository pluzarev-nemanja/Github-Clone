package com.example.github

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.example.domain.model.TrackingEvent
import com.example.domain.model.TrackingEventName
import com.example.github.auth.uiState.AuthUiState
import com.example.github.auth.uiState.LoginUiState
import com.example.github.auth.util.Constants.PROMPT
import com.example.github.auth.util.Constants.PROVIDER
import com.example.github.auth.util.Constants.SELECT_ACCOUNT
import com.example.github.auth.viewModel.AuthViewModel
import com.example.github.common.action.UserActionUiEvent
import com.example.github.common.navigation.graph.AppNavigation
import com.example.github.common.navigation.route.Graph
import com.example.github.pullRequest.viewModel.PullRequestViewModel
import com.example.github.topic.viewModel.TopicsViewModel
import com.example.github.ui.theme.GithubTheme
import com.example.github.user.viewModel.UserViewModel
import com.example.github.userRepo.viewModel.UserRepoViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.OAuthCredential
import com.google.firebase.auth.OAuthProvider
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
class MainActivity : ComponentActivity() {
    private val userRepoViewModel: UserRepoViewModel by viewModel()
    private val pullRequestViewModel: PullRequestViewModel by viewModel()
    private val topicsViewModel: TopicsViewModel by viewModel()
    private val userViewModel: UserViewModel by viewModel()
    private val authViewModel: AuthViewModel by viewModel()
    private val mainViewModel: MainViewModel by viewModel()
    private val auth by inject<FirebaseAuth>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        requestNotificationPermission()

        setContent {
            GithubTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    val userRepositoryUIState by userRepoViewModel.repositoryUiState.collectAsStateWithLifecycle()

                    val repositoryDetailsUiState by userRepoViewModel.repositoryDetailsUiState.collectAsStateWithLifecycle()

                    val pullRequestUiState by pullRequestViewModel.pullRequestUiState.collectAsStateWithLifecycle()

                    val pullRequestDetailsUiState by pullRequestViewModel.pullRequestDetailsUiState.collectAsStateWithLifecycle()

                    val userUiState by userViewModel.userUiState.collectAsStateWithLifecycle()

                    val topicsUiState by topicsViewModel.topicsUiState.collectAsStateWithLifecycle()

                    val searchText by topicsViewModel.searchQuery.collectAsStateWithLifecycle()

                    val authUiState by authViewModel.authUiState.collectAsStateWithLifecycle()
                    val loginState by authViewModel.loginUiState.collectAsStateWithLifecycle()
                    val isUserLoggedIn by authViewModel.isUserLoggedIn.collectAsStateWithLifecycle()
                    val tokenState by mainViewModel.messageTokenState.collectAsStateWithLifecycle()
                    val searchedRepositoryUiState by userRepoViewModel.searchedRepositoryUiState.collectAsStateWithLifecycle()
                    val searchRepositoryText by userRepoViewModel.searchQuery.collectAsStateWithLifecycle()
                    val commitUIState by userRepoViewModel.commitUiState.collectAsStateWithLifecycle()

                    var startDestination by remember {
                        mutableStateOf(Graph.AuthNavGraph.route)
                    }

                    LaunchedEffect(key1 = authUiState) {
                        when (authUiState) {
                            is AuthUiState.Success -> {
                                if ((authUiState as AuthUiState.Success).data != null) {
                                    val token = (authUiState as AuthUiState.Success).token
                                    authViewModel.updateIsUserLoggedIn(isLoggedIn = true)

                                    userViewModel.getAuthenticatedUser(token = token)
                                    userRepoViewModel.getAuthRepositories(token = token)
                                } else {
                                    authViewModel.updateIsUserLoggedIn(isLoggedIn = false)
                                    userRepoViewModel.getAllRepositories()
                                    userViewModel.getUser("octocat")
                                }
                            }

                            is AuthUiState.Loading -> {
                            }

                            is AuthUiState.Empty -> {
                            }

                            is AuthUiState.Error -> {
                            }
                        }
                    }

                    startDestination =
                        when (authUiState) {
                            is AuthUiState.Success -> {
                                Graph.MainNavGraph.route
                            }

                            else ->
                                when (loginState) {
                                    is LoginUiState.Completed -> Graph.MainNavGraph.route
                                    else -> Graph.AuthNavGraph.route
                                }
                        }

                    AppNavigation(
                        startDestination = startDestination,
                        navController = rememberNavController(),
                        userRepositoryUiState = userRepositoryUIState,
                        repositoryDetailsUiState = repositoryDetailsUiState,
                        pullRequestUiState = pullRequestUiState,
                        topicsUiState = topicsUiState,
                        searchRepositoryText = searchRepositoryText,
                        pullRequestDetailsUiState = pullRequestDetailsUiState,
                        userUiState = userUiState,
                        loginState = loginState,
                        commitUiState = commitUIState,
                        searchedRepositoryUiState = searchedRepositoryUiState,
                        searchText = searchText,
                        isUserLoggedIn = isUserLoggedIn,
                        userAction = { userAction ->
                            userAction.resolve()
                        },
                    )
                }
            }
        }
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val hasPermission =
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS,
                ) == PackageManager.PERMISSION_GRANTED

            if (!hasPermission) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                    0,
                )
            }
        }
    }

    private fun UserActionUiEvent.resolve() {
        when (this) {
            UserActionUiEvent.OnRefreshClick -> {
                authViewModel.updateIsUserLoggedIn(isLoggedIn = false)
                userRepoViewModel.getAllRepositories()
                pullRequestViewModel.getPullRequests(
                    owner = "mojombo",
                    repository = "god",
                )
                userViewModel.getUser(userName = "octocat")
            }

            is UserActionUiEvent.OnRepositoryClick -> {
                userRepoViewModel.logEvent(
                    trackingEvent = TrackingEvent(
                        trackingEventName = TrackingEventName.REPOSITORY_CLICK.name.lowercase(),
                        data = bundleOf(owner to repository),
                    ),
                )
                userRepoViewModel.getRepositoryCommits(
                    owner = owner,
                    repository = repository,
                )

                userRepoViewModel.getRepositoryDetails(
                    owner = owner,
                    repository = repository,
                )
            }

            is UserActionUiEvent.OnPullRequestClick ->
                pullRequestViewModel.getPullRequestDetails(
                    owner = owner,
                    repository = repository,
                    pullNumber = pullNumber,
                )

            is UserActionUiEvent.IsRepositoryListEmpty ->
                userRepoViewModel.isRepositoryListEmpty(
                    itemCount = count,
                    isDataInErrorState = isDataInErrorState,
                )

            is UserActionUiEvent.UpdateText -> topicsViewModel.updateSearchQuery(text)
            is UserActionUiEvent.OnSearchClick -> {
                userRepoViewModel.logEvent(
                    TrackingEvent(
                        trackingEventName = TrackingEventName.SEARCH_CLICK.name.lowercase(),
                        data = bundleOf("query" to query),
                    ),
                )
                topicsViewModel.searchTopics(query)
            }

            is UserActionUiEvent.IsTopicListEmpty ->
                topicsViewModel.isTopicsListEmpty(
                    count,
                    isDataInErrorState,
                )

            UserActionUiEvent.OnLoginClick -> launchGitHubOAuth()
            UserActionUiEvent.OnLogOutClick -> authViewModel.signOut()
            UserActionUiEvent.OnEnterAsGuestClick -> {
                authViewModel.updateAuthState(AuthUiState.Success(data = null, token = ""))
                authViewModel.updateLoginState(LoginUiState.Completed)
            }

            UserActionUiEvent.OnSignInAgainClick -> {
                authViewModel.updateAuthState(AuthUiState.Empty)
                authViewModel.updateLoginState(LoginUiState.Initial)
            }

            is UserActionUiEvent.OnUserScrollThroughRepositories -> {
                userRepoViewModel.logEvent(
                    trackingEvent =
                    TrackingEvent(
                        trackingEventName = TrackingEventName.REPOSITORY_LIST_SCROLL.name.lowercase(),
                        data = bundleOf("repository" to repositoryName),
                    ),
                )
            }

            is UserActionUiEvent.OnSearchRepositoryClick -> {
                userRepoViewModel.searchRepositories(name = query)
            }

            is UserActionUiEvent.UpdateSearchRepositoryText -> {
                userRepoViewModel.updateSearchQuery(query = text)
            }
        }
    }

    private fun launchGitHubOAuth() {
        authViewModel.signOut()

        val provider = OAuthProvider.newBuilder(PROVIDER)

        provider.addCustomParameter(PROMPT, SELECT_ACCOUNT)

        val pendingResultTask = auth.pendingAuthResult

        authViewModel.updateLoginState(LoginUiState.Loading)

        if (pendingResultTask != null) {
            pendingResultTask
                .addOnSuccessListener { authResult ->
                    authViewModel.updateLoginState(LoginUiState.Completed)

                    val token = (authResult.credential as? OAuthCredential)?.accessToken
                    token?.let {
                        authViewModel.signInWithGithub(it)
                        authViewModel.updateAuthState(AuthUiState.Success(token = it))
                    }
                }
                .addOnFailureListener {
                    authViewModel.updateLoginState(LoginUiState.Canceled)
                    Timber.e("OAuth failed: $it")
                }.addOnCompleteListener {
                    authViewModel.getCurrentUser()
                }
        } else {
            auth.startActivityForSignInWithProvider(this, provider.build())
                .addOnSuccessListener { authResult ->
                    authViewModel.updateLoginState(LoginUiState.Completed)
                    val token = (authResult.credential as? OAuthCredential)?.accessToken
                    token?.let {
                        authViewModel.signInWithGithub(it)
                        authViewModel.updateAuthState(AuthUiState.Success(token = it))
                    }
                }
                .addOnFailureListener {
                    authViewModel.updateLoginState(LoginUiState.Canceled)
                    Timber.e("OAuth failed: $it")
                }.addOnCompleteListener {
                    authViewModel.getCurrentUser()
                }
        }
    }
}
