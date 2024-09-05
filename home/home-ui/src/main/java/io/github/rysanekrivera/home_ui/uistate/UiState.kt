package io.github.rysanekrivera.home_ui.uistate

import io.github.rysanekrivera.home_api.data.models.GetAllCharactersResponse
import io.github.rysanekrivera.home_api.data.models.GetCharacterByIdResponse
import io.github.rysanekrivera.home_api_impl.state.ApiRequestState
import retrofit2.Response

data class UiState(
    val allCharactersState: ApiRequestState = ApiRequestState.idle(),
    val characterByIdState: ApiRequestState = ApiRequestState.idle(),
    val allCharactersData: GetAllCharactersResponse = GetAllCharactersResponse(),
    val characterByIdData: GetCharacterByIdResponse = GetCharacterByIdResponse(),
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val error: Throwable? = null,
) {
    fun markLoading() = copy(isLoading = true)

    fun markLoadingMore() = copy(isLoadingMore = true)

    fun successAllCharactersResponse(response: Response<GetAllCharactersResponse>) = when {

        response.isSuccessful && response.body() != null -> copy(
            allCharactersState = ApiRequestState.success(),
            allCharactersData = response.body()!!,
            isLoading = false
        )

        else -> noContentForAllCharacters()
    }

    fun successCharactersByIdResponse(response: Response<GetCharacterByIdResponse>) = when {

        response.isSuccessful && response.body() != null -> copy(
            characterByIdState = ApiRequestState.success(),
            characterByIdData = response.body()!!,
            isLoading = false
        )

        else -> noContentForSingleCharacter()
    }

    fun successLoadingMoreCharacters(response: Response<GetAllCharactersResponse>) = when {

        response.isSuccessful && response.body() != null -> copy(
            allCharactersState = ApiRequestState.success(),
            allCharactersData = allCharactersData.copy(
                info = response.body()!!.info,
                results = allCharactersData.results + (response.body()!!.results)
            ),
            isLoadingMore = false
        )

        else -> noContentForAllCharacters()
    }


    fun noContentForAllCharacters() = copy(
        allCharactersState = ApiRequestState.successNoContent(),
        isLoading = false,
    )

    fun noContentForSingleCharacter() = copy(
        characterByIdState = ApiRequestState.successNoContent(),
        isLoading = false
    )

    fun errorAllCharactersResponse(error: Throwable?) = copy(
        allCharactersState = ApiRequestState.error(),
        error = error,
        isLoading = false
    )

    fun errorCharactersByIdResponse(error: Throwable?) = copy(
        characterByIdState = ApiRequestState.error(),
        error = error,
        isLoading = false
    )

}