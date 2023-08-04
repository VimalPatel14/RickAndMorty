package com.vimal.rickandmorty.interfaces

import com.vimal.rickandmorty.model.CharacterDto

interface ItemClickListener {
    fun onItemClick(position: CharacterDto)
}