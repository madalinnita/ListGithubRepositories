package com.nitaioanmadalin.listgithubrepositories.domain.usecase

import com.nitaioanmadalin.listgithubrepositories.core.utils.network.AppResult
import com.nitaioanmadalin.listgithubrepositories.domain.model.GithubItem
import com.nitaioanmadalin.listgithubrepositories.domain.repository.GithubRepository
import kotlinx.coroutines.flow.Flow

class GetRepositoriesUseCaseImpl(
    val repository: GithubRepository
): GetRepositoriesUseCase {
    override fun getRepos(): Flow<AppResult<List<GithubItem>>> {
        return repository.getGithubItems()
    }
}