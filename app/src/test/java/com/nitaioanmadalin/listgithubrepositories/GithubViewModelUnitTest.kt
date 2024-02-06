package com.nitaioanmadalin.listgithubrepositories

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nitaioanmadalin.listgithubrepositories.core.utils.coroutine.CoroutineDispatchersProvider
import com.nitaioanmadalin.listgithubrepositories.core.utils.network.AppResult
import com.nitaioanmadalin.listgithubrepositories.core.utils.network.ConnectivityUtils
import com.nitaioanmadalin.listgithubrepositories.data.local.entities.GithubEntity
import com.nitaioanmadalin.listgithubrepositories.domain.model.GithubItem
import com.nitaioanmadalin.listgithubrepositories.domain.usecase.GetRepositoriesUseCase
import com.nitaioanmadalin.listgithubrepositories.presentation.repositorieslist.GithubRepositoryState
import com.nitaioanmadalin.listgithubrepositories.presentation.repositorieslist.GithubViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.rules.TestRule
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.junit.runners.model.Statement

@ExperimentalCoroutinesApi
class GithubViewModelTest {

    @get:Rule
    var rule = InstantTaskExecutorRule()

    private val testDispatcher = UnconfinedTestDispatcher(TestCoroutineScheduler())
    private val dispatchers = object : CoroutineDispatchersProvider {
        override fun io(): CoroutineDispatcher = testDispatcher
        override fun main(): CoroutineDispatcher = testDispatcher
        override fun computation(): CoroutineDispatcher = testDispatcher
    }

    private val scope = TestScope(testDispatcher)

    @MockK(relaxed = true)
    private lateinit var getRepositoriesUseCase: GetRepositoriesUseCase

    @MockK(relaxed = true)
    private lateinit var connectivityUtils: ConnectivityUtils

    @MockK(relaxed = true)
    private lateinit var mockContext: Context

    private lateinit var viewModel: GithubViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun `getData emits Success state when data is successfully retrieved`() =
        scope.runTest {
            val fakeData = listOf(GithubItem(name = "Mock Name", description = "Mock description"))
            coEvery { getRepositoriesUseCase.getRepos() } returns flowOf(AppResult.Success(fakeData))
            every { connectivityUtils.isConnectionAvailable(any()) } returns true

            val viewStateList = mutableListOf<GithubRepositoryState>()
            val job = launch {
                viewModel = getViewModel()
                viewModel.state.toList(viewStateList)
            }

            viewModel.getData(mockContext)

            advanceTimeBy(2500) // To account for the hardcoded delay
            val state = viewStateList.last()
            assertTrue(state is GithubRepositoryState.Success && state.repositories == fakeData)

            job.cancel()
        }

    @Test
    fun `getData emits Error state when an error occurs`() = scope.runTest {
        val exception = Exception("Error")
        coEvery { getRepositoriesUseCase.getRepos() } returns flowOf(AppResult.Error(exception))
        every { connectivityUtils.isConnectionAvailable(any()) } returns true

        val viewStateList = mutableListOf<GithubRepositoryState>()
        val job = launch {
            viewModel = getViewModel()
            viewModel.state.toList(viewStateList)
        }

        viewModel.getData(mockContext)

        advanceTimeBy(2500) // To account for the hardcoded delay
        val state = viewStateList.last()
        assertTrue(state is GithubRepositoryState.Error && state.isInternetAvailable)

        job.cancel()
    }

    @Test
    fun `getData emits Error state when internet is not available`() =
        scope.runTest {
            every { connectivityUtils.isConnectionAvailable(any()) } returns false

            val viewStateList = mutableListOf<GithubRepositoryState>()
            val job = launch {
                viewModel = getViewModel()
                viewModel.state.toList(viewStateList)
            }

            viewModel.getData(mockContext)

            advanceTimeBy(2500) // To account for the hardcoded delay
            val state = viewStateList.last()
            assertTrue(state is GithubRepositoryState.Error && !state.isInternetAvailable)

            job.cancel()
        }

    private fun getViewModel() =
        GithubViewModel(getRepositoriesUseCase, connectivityUtils, dispatchers)

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}