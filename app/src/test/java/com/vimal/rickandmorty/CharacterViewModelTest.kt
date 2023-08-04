package com.vimal.rickandmorty

import android.app.Application
import android.graphics.Movie
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.vimal.rickandmorty.api.CharacterRepository
import com.vimal.rickandmorty.api.NetworkState
import com.vimal.rickandmorty.model.CharacterDto
import com.vimal.rickandmorty.model.CharacterListResponse
import com.vimal.rickandmorty.model.LocationDto
import com.vimal.rickandmorty.model.OriginDto
import com.vimal.rickandmorty.model.PageInfo
import com.vimal.rickandmorty.viewmodel.CharacterViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class CharacterViewModelTest {

    private val testDispatcher = TestCoroutineDispatcher()
    lateinit var characterViewModel: CharacterViewModel
    lateinit var mainRepository: CharacterRepository
    lateinit var applicationMock: Application

    @get:Rule
    val instantTaskExecutionRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        Dispatchers.setMain(testDispatcher)
        mainRepository = mock(CharacterRepository::class.java)
        characterViewModel = CharacterViewModel(applicationMock, mainRepository)
    }

    @Test
    fun getAllMoviesTest() {
        val origin = OriginDto("name", "url")
        val location = LocationDto("name", "url")
        val character = CharacterDto(
            1, "test", "status", "species",
            "type", "gender", origin, location, "image", emptyList(), "url", "created"
        )

        val resultsList: List<CharacterDto> = listOf(character)
        val characterListResponse = CharacterListResponse(
            info = PageInfo(count = 1, pages = 1, "", ""),
            results = resultsList
        )

        runBlocking {
            Mockito.`when`(mainRepository.getAllCharacter(1))
                .thenReturn(NetworkState.Success(characterListResponse))
            characterViewModel.getAllCharacter(1)
            val result = characterViewModel.characterList.getOrAwaitValue()
//            assertEquals(listOf<CharacterListResponse>(Movie("movie", "", "new")), result)
        }
    }

    @Test
    fun `empty movie list test`() {
        val emptyResultsList: List<CharacterDto> = emptyList()
        val emptyCharacterListResponse = CharacterListResponse(
            info = PageInfo(0, 0, null, null),
            results = emptyResultsList
        )

        runBlocking {
            Mockito.`when`(mainRepository.getAllCharacter(1))
                .thenReturn(NetworkState.Success(emptyCharacterListResponse))
            characterViewModel.getAllCharacter(1)
            val result = characterViewModel.characterList.getOrAwaitValue()
            assertEquals(listOf<Movie>(), result)
        }
    }
}