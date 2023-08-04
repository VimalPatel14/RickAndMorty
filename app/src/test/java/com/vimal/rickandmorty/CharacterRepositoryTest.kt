package com.vimal.rickandmorty

import com.vimal.rickandmorty.api.CharacterRepository
import com.vimal.rickandmorty.api.NetworkState
import com.vimal.rickandmorty.api.RetrofitService
import com.vimal.rickandmorty.database.CharacterDao
import com.vimal.rickandmorty.model.CharacterDto
import com.vimal.rickandmorty.model.CharacterListResponse
import com.vimal.rickandmorty.model.LocationDto
import com.vimal.rickandmorty.model.OriginDto
import com.vimal.rickandmorty.model.PageInfo
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import retrofit2.Response

@RunWith(JUnit4::class)
class CharacterRepositoryTest {

    lateinit var characterRepository: CharacterRepository

    @Mock
    lateinit var apiService: RetrofitService

    @Mock
    lateinit var characterDto: CharacterDao

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        characterRepository = CharacterRepository(apiService, characterDto)
    }

    @Test
    fun `get all character test`() {
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
            val response = Response.success(characterListResponse)
            Mockito.`when`(apiService.getAllCharacter(1))
                .thenReturn(response)
            val result = characterRepository.getAllCharacter(1)
            assertNotNull(result)
        }
    }

    @Test
    fun `not get all character test`() {

        val emptyResultsList: List<CharacterDto> = emptyList()
        val emptyCharacterListResponse = CharacterListResponse(
            info = PageInfo(0, 0, null, null),
            results = emptyResultsList
        )

        runBlocking {

            val response = Response.success(emptyCharacterListResponse)
            Mockito.`when`(apiService.getAllCharacter(1))
                .thenReturn(response)

            val result = characterRepository.getAllCharacter(1)

            assertNotNull(result)
            if (result is NetworkState.Success) {
                assertTrue(result.data.results.isEmpty())
            } else {
                fail("Expected result to be Success")
            }
        }
    }
}