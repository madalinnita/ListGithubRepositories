package com.nitaioanmadalin.listgithubrepositories.data.remote

import com.nitaioanmadalin.listgithubrepositories.data.remote.dtos.GithubDto
import retrofit2.http.GET

interface GithubApi {
    @GET("/orgs/square/repos")
   suspend fun getRepositories(): List<GithubDto>

   companion object {
       val BASE_URL = "https://api.github.com/"
   }
}