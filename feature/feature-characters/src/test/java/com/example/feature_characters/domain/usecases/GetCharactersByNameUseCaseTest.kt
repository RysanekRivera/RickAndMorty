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
class GetCharactersByNameUseCaseTest {

    private val repository: RickAndMortyRepository = mock()
    private lateinit var useCase: GetCharactersByNameUseCase

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        useCase = GetCharactersByNameUseCase(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetchData should return successful response`() = runTest {
        val name = "Rick"
        val page = 1

        `when`(repository.getCharacterByName(name, page)).thenReturn(mockSuccessResponse)

        val result = useCase.fetchData(name, page)

        assertEquals(mockSuccessResponse, result)
        verify(repository).getCharacterByName(name, page)
    }

    @Test
    fun `fetchData should return error response`() = runTest {
        val name = "Rick"
        val page = 1
        val errorResponse = Response.error<AllCharactersResponse>(404, "Not Found".toResponseBody())
        `when`(repository.getCharacterByName(name, page)).thenReturn(errorResponse)

        val result = useCase.fetchData(name, page)

        assertEquals(errorResponse.code(), result.code())
        assertFalse(result.isSuccessful)
    }

    private val mockSuccessResponse = Response.success(
        AllCharactersResponse(
            info = Info(
                count = 2,
                next = "2",
                pages = 2,
                prev = null
            ),
            characters = listOf(
                Character(
                    id = 1,
                    name = "Rick Sanchez",
                    status = "Alive",
                    species = "Human",
                    type = "",
                    gender = "Male",
                    image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg"
                ),
                Character(
                    id = 2,
                    name = "Rick D. Sanchez",
                    status = "Alive",
                    species = "Human",
                    type = "Scientist",
                    gender = "Male",
                    image = "https://rickandmortyapi.com/api/character/avatar/2.jpeg"
                )
            )
        )
    )
}
