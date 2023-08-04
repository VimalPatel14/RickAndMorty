package com.vimal.rickandmorty

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.google.gson.Gson
import com.vimal.rickandmorty.adapter.EpisodeAdapter
import com.vimal.rickandmorty.api.CharacterDetailsRepository
import com.vimal.rickandmorty.api.RetrofitService
import com.vimal.rickandmorty.database.AppDatabase
import com.vimal.rickandmorty.databinding.ActivityCharacterDetailsBinding
import com.vimal.rickandmorty.model.CharacterDto
import com.vimal.rickandmorty.viewmodel.characterdetails.CharacterDetailsViewModel
import com.vimal.rickandmorty.viewmodel.characterdetails.CharacterDetailsViewModelFactory

class CharacterDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCharacterDetailsBinding
    lateinit var viewModel: CharacterDetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCharacterDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val characterJson = intent.getStringExtra("extra_object")
        val character = Gson().fromJson(characterJson, CharacterDto::class.java)
        val adapter = EpisodeAdapter()
        val layoutManager = GridLayoutManager(this, 1)
        binding.recyclerview.layoutManager = layoutManager
        binding.recyclerview.adapter = adapter

        character?.let {
            Glide.with(this).load(character.image)
                .apply(RequestOptions.bitmapTransform(CircleCrop()))
                .placeholder(R.drawable.loading).into(binding.imageview)
            binding.name.text = character.name
            binding.status.text = character.status
            binding.spices.text = character.species
            binding.gender.text = character.gender
        }

        val retrofitService = RetrofitService.getInstance()
        val dao = AppDatabase.getDatabase(application).getEpisodeDao()
        val mainRepository = CharacterDetailsRepository(retrofitService, dao, character.id)

        val viewModelFactory = CharacterDetailsViewModelFactory(application, mainRepository)

        viewModel = ViewModelProvider(
            this,
            viewModelFactory
        ).get(CharacterDetailsViewModel::class.java)

        viewModel.episodeList.observe(this) {
            Log.e("vml", "Episodes: ${it.air_date}")
            it.characterId = character.id
            adapter.setMovies(it)
            viewModel.addEpisode(it)
        }

        viewModel.errorMessage.observe(this) {
            viewModel.allEpisode.observe(this, Observer { list ->
                if (list.isNotEmpty()) {
                    binding.episodetxt.visibility = View.VISIBLE
                    adapter.setMovies(list)
                } else {
                    binding.episodetxt.visibility = View.INVISIBLE
                }
            })
        }
        viewModel.loading.observe(this, Observer {
            binding.episodetxt.visibility = View.INVISIBLE
        })

        viewModel.getCharacter()
    }
}