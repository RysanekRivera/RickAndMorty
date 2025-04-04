package com.example.feature_characters.domain.usecases

import com.example.feature_characters.data.models.AllCharactersResponse
import com.example.feature_characters.data.models.Character
import com.example.feature_characters.data.models.Info
import com.example.feature_characters.domain.repositories.RickAndMortyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import retrofit2.Response

@ExperimentalCoroutinesApi
class GetAllCharactersUseCaseTest {

    private val repository: RickAndMortyRepository = mock()
    private lateinit var useCase: GetAllCharactersUseCase

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        useCase = GetAllCharactersUseCase(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetchData should return successful response`() = runTest {
        val page = 1


        `when`(repository.getAllCharacters(page)).thenReturn(mockSuccessResponse)

        val result = useCase.fetchData(page)

        assertEquals(mockSuccessResponse, result)
        verify(repository).getAllCharacters(page)
    }

    @Test
    fun `fetchData should return error response`() = runTest {
        val page = 1
        val errorResponse = Response.error<AllCharactersResponse>(404, "Not Found".toResponseBody())
        `when`(repository.getAllCharacters(page)).thenReturn(errorResponse)

        val result = useCase.fetchData(page)

        assertEquals(errorResponse.code(), result.code())
        assertFalse(result.isSuccessful)
    }

    private val mockSuccessResponse = Response.success(
        AllCharactersResponse(
            info = Info(
                2,
                next = "2",
                pages = 2,
                prev = null
            ),
            characters = listOf(
                Character(
                    id = 21,
                    name = "Aqua Morty",
                    status = "unknown",
                    species = "Humanoid",
                    type = "Fish Person",
                    gender = "Male",
                    image = "https://rickandmortyapi.com/api/character/avatar/21.jpeg"
                ),
                Character(
                    id = 21,
                    name = "Aqua Rick",
                    status = "unknown",
                    species = "Humanoid",
                    type = "Fish Person",
                    gender = "Male",
                    image = "https://rickandmortyapi.com/api/character/avatar/21.jpeg"
                ),
            )
        )
    )
}
