package com.nitaioanmadalin.listgithubrepositories.data.remote.dtos

import com.nitaioanmadalin.listgithubrepositories.data.local.entities.GithubEntity

data class GithubDto(
    val id: Long? = 0L,
    val name: String?,
    val description: String?
) {
    fun toGithubEntity(): GithubEntity = GithubEntity(id, name, description)
}