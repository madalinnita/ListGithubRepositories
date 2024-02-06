package com.nitaioanmadalin.listgithubrepositories.presentation.repositorieslist

import com.nitaioanmadalin.listgithubrepositories.domain.model.GithubItem

sealed class GithubRepositoryState {
    data class Loading(
        val daoRepositories: List<GithubItem>? = null
    ) : GithubRepositoryState()

    data class Success(val repositories: List<GithubItem>) : GithubRepositoryState()

    data class Error(
        val ex: Throwable,
        val isInternetAvailable: Boolean = true
    ) : GithubRepositoryState()
}