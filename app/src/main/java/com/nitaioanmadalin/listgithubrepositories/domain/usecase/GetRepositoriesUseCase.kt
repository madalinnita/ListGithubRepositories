package com.nitaioanmadalin.listgithubrepositories.domain.usecase

import com.nitaioanmadalin.listgithubrepositories.core.utils.network.AppResult
import com.nitaioanmadalin.listgithubrepositories.domain.model.GithubItem
import kotlinx.coroutines.flow.Flow

interface GetRepositoriesUseCase {
    fun getRepos(): Flow<AppResult<List<GithubItem>>>
}