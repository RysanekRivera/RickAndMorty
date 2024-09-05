package io.github.rysanekrivera.home_ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.rysanekrivera.home_api.data.models.GetCharacterByIdResponse
import io.github.rysanekrivera.home_api.repositories.RickAndMortyRepository
import io.github.rysanekrivera.home_api_impl.state.ApiRequestState
import io.github.rysanekrivera.home_api_impl.state.updateValue
import io.github.rysanekrivera.home_ui.uiEvents.UiEvent
import io.github.rysanekrivera.home_ui.uistate.UiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RickAndMortyViewModel @Inject constructor(
    private val repository: RickAndMortyRepository
) : ViewModel() {

    private val _state = MutableStateFlow(UiState())
    val state = _state.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), _state.value)

    private val _uiEvents = MutableSharedFlow<UiEvent>()
    val uiEvents = _uiEvents.asSharedFlow()

    private val currentPage = MutableStateFlow(1)

    init {
        observeEvents()
    }

    private fun observeEvents() {
        viewModelScope.launch {
            getListOfCharacters()
        }
    }

    private suspend fun getListOfCharacters() {
        _state.updateValue { markLoading() }

        runCatching {
            repository.getAllCharacters(page = currentPage.value)
        }.onSuccess { response ->
            _state.updateValue {
                successAllCharactersResponse(response)
            }
            currentPage.updateValue { inc() }
        }.onFailure {
            _state.updateValue {
                errorAllCharactersResponse(error = it)
            }
        }
    }

    fun getCharacterById(id: Int) = viewModelScope.launch {
        _state.updateValue {
            copy(
                isLoading = true,
                characterByIdData = GetCharacterByIdResponse()
            )
        }

        runCatching {
            repository.getCharacterById(id)
        }.onSuccess { response ->
            _state.updateValue {
                successCharactersByIdResponse(response)
            }
        }.onFailure {
            _state.updateValue {
                copy(
                    characterByIdState = ApiRequestState.error(),
                    error = it,
                    isLoading = false
                )
            }
        }
    }

    fun onLoadNextPage() = viewModelScope.launch {
        _state.updateValue { markLoadingMore() }
        runCatching {
            repository.getAllCharacters(page = currentPage.value)
        }.onSuccess { response ->
            _state.updateValue { successLoadingMoreCharacters(response) }
            currentPage.updateValue { inc() }
        }.onFailure {
            _state.updateValue {
                errorCharactersByIdResponse(it)
            }
        }
    }

    fun onNavigateToCharacter(id: Int) = viewModelScope.launch {
        _uiEvents.emit(UiEvent.NavigateToCharacter(id))
    }

}
