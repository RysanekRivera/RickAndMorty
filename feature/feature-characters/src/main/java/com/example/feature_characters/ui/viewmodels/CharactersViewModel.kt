package com.example.feature_characters.ui.viewmodels

import androidx.lifecycle.viewModelScope
import com.example.feature_characters.data.models.AllCharactersResponse
import com.example.feature_characters.data.models.Character
import com.example.feature_characters.data.models.Info
import com.example.feature_characters.domain.usecases.GetAllCharactersUseCase
import com.example.feature_characters.domain.usecases.GetCharactersByNameUseCase
import com.example.feature_characters.utils.updateUiStateFromNetworkResponse
import com.rysanek.common_ui.state.UiState
import com.rysanek.common_ui.uievents.UiEvent
import com.rysanek.common_ui.viewmodel.BaseViewModel
import com.rysanek.network_utils.NetworkResult
import com.rysanek.network_utils.coroutines.launchWithInternetConnectivity
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.Response

typealias AllCharactersUiState = UiState<AllCharactersResponse>
typealias AllCharactersResult = NetworkResult<Response<AllCharactersResponse>>

data class SearchUiState(
    val query: String = "",
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val nextPage: Int? = null,
    val characters: List<Character> = emptyList(),
    val info: Info? = Info()
) {
    fun hasNextPage() = nextPage != null
    fun markLoading() = copy(isLoading = true)
    fun markIsLoadingMore() = copy(isLoadingMore = true)
    fun updateQuery(newQuery: String) = copy(query = newQuery)
    fun updateFromResponse(response: Response<AllCharactersResponse>): SearchUiState {
        val body = response.body()
        return if (response.isSuccessful && body != null) {
            copy(
                isLoading = false,
                characters = body.characters,
                nextPage = body.extractNextPage(),
                info = body.info
            )
        } else {
            copy(isLoading = false)
        }
    }

    fun updateFromLoadMoreResponse(response: Response<AllCharactersResponse>): SearchUiState {
        val body = response.body()
        return if (response.isSuccessful && body != null) {
            copy(
                isLoading = false,
                characters = characters + body.characters,
                nextPage = body.extractNextPage(),
                info = body.info
            )
        } else {
            copy(isLoading = false)
        }
    }
}

@HiltViewModel
class CharactersViewModel @Inject constructor(
    private val getAllCharactersUseCase: GetAllCharactersUseCase,
    private val getCharactersByNameUseCase: GetCharactersByNameUseCase
): BaseViewModel() {

    private val nextPage = MutableStateFlow<Int?>(1)

    private val networkResultsState = MutableStateFlow<AllCharactersResult>(NetworkResult.Idle)

    private val _getAllCharactersState: MutableStateFlow<AllCharactersUiState> = MutableStateFlow(UiState())
    val getAllCharactersState = _getAllCharactersState.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), _getAllCharactersState.value)

    private val _isLoadingMore = MutableStateFlow(false)
    val isLoadingMore = _isLoadingMore.asStateFlow()

    private val _searchState = MutableStateFlow(SearchUiState())
    val searchState = _searchState.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), _searchState.value)

    private var queryJob: Job? = null

    init {
        collectAndHandleNetworkRequestState()
        getAllCharacters()
    }

    private fun getAllCharacters() = viewModelScope.launchWithInternetConnectivity(networkResultFlow = networkResultsState) {
        getAllCharactersUseCase.fetchData()
    }

    private fun collectAndHandleNetworkRequestState() = viewModelScope.launch {
        networkResultsState.collect { networkResponse ->

            if (networkResponse is NetworkResult.Success && networkResponse.data.isSuccessful && networkResponse.data.body() != null) {
                nextPage.update { networkResponse.data.body()?.extractNextPage() }
            }

            _getAllCharactersState.update {
                it.updateUiStateFromNetworkResponse(networkResponse)
            }
        }
    }

    fun onLoadMore() = viewModelScope.launch{
        try {
            if (nextPage.value != null && !_isLoadingMore.value) {

                _isLoadingMore.value = true

                viewModelScope.launchWithInternetConnectivity(Dispatchers.IO) {
                    val result = getAllCharactersUseCase.fetchData(nextPage.value!!)

                    if (result.isSuccessful && result.body() != null) {

                        val allCharactersResponse = result.body()!!

                        _getAllCharactersState.update { currentState ->
                            currentState.copy(
                                successData = currentState.successData?.copy(
                                    info = allCharactersResponse.info,
                                    characters = (currentState.successData?.characters.orEmpty() + allCharactersResponse.characters).distinctBy { it.id }
                                ) ?: result.body()
                            )
                        }

                        nextPage.update { allCharactersResponse.extractNextPage() }
                    }
                }
            }
        } catch (e: Throwable) {
            events.send(UiEvent.ShowSnackBar("Error Loading More: ${e.message}"))
        } finally {
            _isLoadingMore.value = false
        }
    }

    fun onLoadMoreFromSearch() = viewModelScope.launch {
        try {
            _searchState.update { it.markIsLoadingMore() }
            viewModelScope.launchWithInternetConnectivity(Dispatchers.IO) {
                _searchState.update {
                    it.updateFromLoadMoreResponse(getCharactersByNameUseCase.fetchData(page = it.nextPage, name = it.query))
                }
            }
        } catch (e: Throwable) {
            _searchState.update { it.copy(isLoading = false) }
            events.send(UiEvent.ShowSnackBar("Error Loading More Search Results: ${e.message}"))
        }
    }

    fun onQueryChanged(newValue: String) {
        queryJob?.cancel()
        queryJob = viewModelScope.launch {
            _searchState.update { it.updateQuery(newValue) }

            if (newValue.isEmpty() || newValue.isBlank()) {
                _searchState.update { SearchUiState() }
                return@launch
            }

            // slight delay such that this gets cancelled if the user is still typing
            delay(300)

            _searchState.update { it.markLoading() }

            try {
                viewModelScope.launchWithInternetConnectivity(Dispatchers.IO) {
                    val result = getCharactersByNameUseCase.fetchData(newValue)
                    _searchState.update { it.updateFromResponse(result) }
                }
            } catch (e: Throwable) {
                _searchState.update { it.copy(isLoading = false) }
                events.send(UiEvent.ShowSnackBar("Error Loading Search Results: ${e.message}"))
            }
        }
    }

}