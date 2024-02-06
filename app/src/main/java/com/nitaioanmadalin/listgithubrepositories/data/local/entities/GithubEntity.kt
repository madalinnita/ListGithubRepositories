package com.nitaioanmadalin.listgithubrepositories.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nitaioanmadalin.listgithubrepositories.domain.model.GithubItem

@Entity
data class GithubEntity(
    @PrimaryKey val id: Long? = 0L,
    val name: String?,
    val description: String?
) {
    fun toGithubRepository() = GithubItem(id, name, description)
}