package com.nitaioanmadalin.listgithubrepositories.presentation.repositorieslist

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nitaioanmadalin.listgithubrepositories.core.utils.coroutine.CoroutineDispatchersProvider
import com.nitaioanmadalin.listgithubrepositories.core.utils.network.AppResult
import com.nitaioanmadalin.listgithubrepositories.core.utils.network.ConnectivityUtils
import com.nitaioanmadalin.listgithubrepositories.domain.usecase.GetRepositoriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class GithubViewModel @Inject constructor(
    private val getRepositoriesUseCase: GetRepositoriesUseCase,
    private val connectivityUtils: ConnectivityUtils,
    private val dispatchers: CoroutineDispatchersProvider
): ViewModel() {

    private val _state: MutableStateFlow<GithubRepositoryState> = MutableStateFlow(
        GithubRepositoryState.Loading())
    val state: StateFlow<GithubRepositoryState> = _state

    fun getData(context: Context) {
        if (connectivityUtils.isConnectionAvailable(context)) {
            viewModelScope.launch(dispatchers.io()) {
                // Delay applied in order to see properly the Loading screen for Assessment purposes
                delay(2000)
                getRepositoriesUseCase.getRepos().onEach {
                    withContext(dispatchers.main()) {
                        _state.value = when (it) {
                            is AppResult.Error -> {
                                GithubRepositoryState.Error(it.exception)
                            }

                            is AppResult.Loading -> {
                                GithubRepositoryState.Loading(it.daoData)
                            }

                            is AppResult.Success -> {
                                GithubRepositoryState.Success(it.successData)
                            }
                        }
                    }
                }.launchIn(this)
            }
        } else {
            viewModelScope.launch {
                _state.value = GithubRepositoryState.Error(
                    Throwable("Internet connection is not available"),
                    isInternetAvailable = false
                )
            }
        }
    }

    companion object{
        private const val TAG = "GithubViewModel"
    }
}