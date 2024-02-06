package com.nitaioanmadalin.listgithubrepositories.domain.repository

import com.nitaioanmadalin.listgithubrepositories.core.utils.network.AppResult
import com.nitaioanmadalin.listgithubrepositories.domain.model.GithubItem
import kotlinx.coroutines.flow.Flow

interface GithubRepository {
    fun getGithubItems(): Flow<AppResult<List<GithubItem>>>
}