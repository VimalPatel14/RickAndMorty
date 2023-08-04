package com.vimal.rickandmorty

import com.vimal.rickandmorty.api.ValidationUtil
import com.vimal.rickandmorty.model.CharacterDto
import com.vimal.rickandmorty.model.LocationDto
import com.vimal.rickandmorty.model.OriginDto
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class ValidationUtilTest {

    @Test
    fun validateMovieTest() {
        val origin = OriginDto("name", "url")
        val location = LocationDto("name", "url")
        val character = CharacterDto(
            1, "test", "status", "species",
            "type", "gender", origin, location, "image", emptyList(), "url", "created"
        )
        assertEquals(true, ValidationUtil.validateCharacter(character))
    }

    @Test
    fun validateMovieEmptyTest() {
        val origin = OriginDto("name", "url")
        val location = LocationDto("name", "url")
        val character = CharacterDto(
            1, "", "status", "species",
            "type", "gender", origin, location, "image", emptyList(), "url", "created"
        )
        assertEquals(false, ValidationUtil.validateCharacter(character))
    }

}