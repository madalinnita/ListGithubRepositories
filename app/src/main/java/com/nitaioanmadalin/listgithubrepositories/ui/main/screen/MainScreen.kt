package com.nitaioanmadalin.listgithubrepositories.ui.main.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nitaioanmadalin.listgithubrepositories.core.utils.lifecycle.LifecycleHandler
import com.nitaioanmadalin.listgithubrepositories.presentation.repositorieslist.GithubRepositoryState
import com.nitaioanmadalin.listgithubrepositories.presentation.repositorieslist.GithubViewModel
import com.nitaioanmadalin.listgithubrepositories.ui.repositorieslist.ErrorScreen
import com.nitaioanmadalin.listgithubrepositories.ui.repositorieslist.LoadingScreen
import com.nitaioanmadalin.listgithubrepositories.ui.repositorieslist.SuccessScreen

@Composable
fun MainScreen(
    viewModel: GithubViewModel
) {
    val viewState by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LifecycleHandler(
        onResume = { viewModel.getData(context = context) }
    )

    when(val state = viewState) {
        is GithubRepositoryState.Error -> ErrorScreen(
            onRetry = { viewModel.getData(context) },
            isInternetConnectionAvailable = state.isInternetAvailable
        )
        is GithubRepositoryState.Loading -> LoadingScreen()
        is GithubRepositoryState.Success -> SuccessScreen(data = state.repositories)
    }
}