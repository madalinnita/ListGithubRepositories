package com.nitaioanmadalin.listgithubrepositories.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nitaioanmadalin.listgithubrepositories.data.local.entities.GithubEntity

@Dao
interface GithubDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGithubRepositories(repositories: List<GithubEntity>)

    @Query("SELECT * FROM githubentity")
    suspend fun getGithubRepositories(): List<GithubEntity>
}