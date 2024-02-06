package com.nitaioanmadalin.listgithubrepositories.di

import android.app.Application
import androidx.room.Room
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.nitaioanmadalin.listgithubrepositories.core.utils.coroutine.CoroutineDispatchersProvider
import com.nitaioanmadalin.listgithubrepositories.core.utils.coroutine.CoroutineDispatchersProviderImpl
import com.nitaioanmadalin.listgithubrepositories.core.utils.network.ConnectivityUtils
import com.nitaioanmadalin.listgithubrepositories.data.local.GithubDao
import com.nitaioanmadalin.listgithubrepositories.data.local.GithubDatabase
import com.nitaioanmadalin.listgithubrepositories.data.remote.GithubApi
import com.nitaioanmadalin.listgithubrepositories.data.repository.GithubRepositoryImpl
import com.nitaioanmadalin.listgithubrepositories.domain.repository.GithubRepository
import com.nitaioanmadalin.listgithubrepositories.domain.usecase.GetRepositoriesUseCase
import com.nitaioanmadalin.listgithubrepositories.domain.usecase.GetRepositoriesUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GithubModule {

    @Provides
    @Singleton
    fun getGson(): Gson {
        return GsonBuilder().serializeNulls().setLenient().create()
    }

    @Provides
    @Singleton
    fun provideGithubUseCase(
        repository: GithubRepository
    ): GetRepositoriesUseCase {
        return GetRepositoriesUseCaseImpl(repository)
    }

    @Provides
    @Singleton
    fun provideGithubRepository(
        db: GithubDatabase,
        api: GithubApi
    ): GithubRepository {
        return GithubRepositoryImpl(db.dao, api)
    }

    @Provides
    @Singleton
    fun provideGithubDatabase(
        application: Application
    ): GithubDatabase {
        return Room.databaseBuilder(
            application,
            GithubDatabase::class.java,
            "github_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun providesGithubApi(client: OkHttpClient): GithubApi {
        return Retrofit
            .Builder()
            .baseUrl(GithubApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(GithubApi::class.java)
    }

    @Provides
    @Singleton
    fun provideConnectivityUtils(): ConnectivityUtils {
        return ConnectivityUtils()
    }

    @Provides
    fun provideCoroutineDispatchersProvider(): CoroutineDispatchersProvider {
        return CoroutineDispatchersProviderImpl()
    }
}