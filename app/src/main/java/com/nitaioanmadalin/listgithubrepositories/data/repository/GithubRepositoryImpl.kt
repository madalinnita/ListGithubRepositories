package com.nitaioanmadalin.listgithubrepositories.data.repository

import com.nitaioanmadalin.listgithubrepositories.core.utils.network.AppResult
import com.nitaioanmadalin.listgithubrepositories.data.local.GithubDao
import com.nitaioanmadalin.listgithubrepositories.data.remote.GithubApi
import com.nitaioanmadalin.listgithubrepositories.domain.model.GithubItem
import com.nitaioanmadalin.listgithubrepositories.domain.repository.GithubRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GithubRepositoryImpl(
    private val dao: GithubDao,
    private val api: GithubApi
) : GithubRepository {
    override fun getGithubItems(): Flow<AppResult<List<GithubItem>>> = flow {
        emit(AppResult.Loading())
        val daoRepositories = dao.getGithubRepositories().map { it.toGithubRepository() }
        emit(AppResult.Loading(daoRepositories))

        try {
            val remoteRepositories = api.getRepositories()
                .filter { it.name != null && it.description != null }
                .map { it.toGithubEntity() }
            dao.insertGithubRepositories(remoteRepositories)
            val updatedRepositories = dao.getGithubRepositories().map { it.toGithubRepository() }
            emit(AppResult.Success(updatedRepositories))
        } catch (ex: Throwable) {
            emit(AppResult.Error(ex, ex.message))
        }
    }
}