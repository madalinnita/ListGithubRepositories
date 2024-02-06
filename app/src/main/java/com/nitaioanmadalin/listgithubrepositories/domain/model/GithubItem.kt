package com.nitaioanmadalin.listgithubrepositories.domain.model

import com.nitaioanmadalin.listgithubrepositories.data.local.entities.GithubEntity

data class GithubItem(
    val id: Long? = 0L,
    val name: String?,
    val description: String?
)