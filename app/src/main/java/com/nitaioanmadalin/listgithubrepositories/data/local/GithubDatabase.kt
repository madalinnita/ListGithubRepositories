package com.nitaioanmadalin.listgithubrepositories.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nitaioanmadalin.listgithubrepositories.data.local.entities.GithubEntity

@Database(
    entities = [GithubEntity::class],
    version = 1
)
abstract class GithubDatabase: RoomDatabase() {
    abstract val dao: GithubDao
}