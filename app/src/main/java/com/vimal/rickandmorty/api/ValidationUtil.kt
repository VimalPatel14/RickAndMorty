package com.vimal.rickandmorty.api

import com.vimal.rickandmorty.model.CharacterDto

object ValidationUtil {
    fun validateCharacter(character: CharacterDto) : Boolean {
        if (character.name.isNotEmpty() && character.gender.isNotEmpty()) {
            return true
        }
        return false
    }
}