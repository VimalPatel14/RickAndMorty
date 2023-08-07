package com.vimal.rickandmorty

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.vimal.rickandmorty.adapter.CharacterAdapter
import com.vimal.rickandmorty.api.CharacterRepository
import com.vimal.rickandmorty.api.RetrofitService
import com.vimal.rickandmorty.database.AppDatabase
import com.vimal.rickandmorty.databinding.ActivityMainBinding
import com.vimal.rickandmorty.interfaces.ItemClickListener
import com.vimal.rickandmorty.model.CharacterDto
import com.vimal.rickandmorty.viewmodel.CharacterViewModel
import com.vimal.rickandmorty.viewmodel.CharacterViewModelFactory

class MainActivity : AppCompatActivity(), ItemClickListener {

    lateinit var viewModel: CharacterViewModel
    private lateinit var binding: ActivityMainBinding

    var isOffline = false
    private var currentPage = 1
    private var isLoading = false
    lateinit var adapter: CharacterAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = CharacterAdapter(this@MainActivity, this@MainActivity)

        val retrofitService = RetrofitService.getInstance()
        val dao = AppDatabase.getDatabase(application).getCharacterDao()
        val characterRepository = CharacterRepository(retrofitService, dao)

        val layoutManager = GridLayoutManager(this, 2)
        binding.recyclerview.layoutManager = layoutManager
        binding.recyclerview.adapter = adapter

        binding.recyclerview.addOnScrollListener(recyclerViewOnScrollListener)

        val viewModelFactory = CharacterViewModelFactory(application, characterRepository)

        viewModel = ViewModelProvider(
            this,
            viewModelFactory
        ).get(CharacterViewModel::class.java)


        viewModel.characterList.observe(this) {
            binding.progressDialog.visibility = View.GONE
            adapter.setCharacter(it.results)
            isOffline = false
            isLoading = false
            for (i in 0 until it.results.size) {
                val notes = CharacterDto(
                    id = it.results[i].id,
                    name = it.results[i].name,
                    status = it.results[i].status,
                    species = it.results[i].species,
                    type = it.results[i].type,
                    gender = it.results[i].gender,
                    origin = it.results[i].origin,
                    location = it.results[i].location,
                    image = it.results[i].image,
                    episode = it.results[i].episode,
                    url = it.results[i].url,
                    created = it.results[i].created
                )
                viewModel.addNote(notes)
            }


        }
        viewModel.errorMessage.observe(this) {
            viewModel.allNotes.observe(this, Observer { list ->
                isLoading = false
                if (list.isNotEmpty()) {
                    isOffline = true
                    adapter.setCharacter(list)
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        "We are having some issue. No Offline Data", Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }
        viewModel.loading.observe(this, Observer {
            if (it) {
                binding.progressDialog.visibility = View.VISIBLE
            } else {
                binding.progressDialog.visibility = View.GONE
            }
        })
        viewModel.getAllCharacter(currentPage)
    }

    override fun onItemClick(position: CharacterDto) {
        val characterJson = Gson().toJson(position)
        val intent = Intent(this, CharacterDetailsActivity::class.java)
        intent.putExtra("extra_object", characterJson)
        startActivity(intent)
    }

    private val recyclerViewOnScrollListener =
        object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager
                if (layoutManager is LinearLayoutManager) {
                    val visibleItemCount = layoutManager.childCount
                    val totalItemCount = layoutManager.itemCount
                    val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                    if (!isLoading && (visibleItemCount + firstVisibleItemPosition) >=
                        totalItemCount && firstVisibleItemPosition >= 0
                    ) {
                        //We are at the bottom and not currently loading data
                        loadNextPage()
                    }
                }
            }
        }

    private fun loadNextPage() {
        if (!isLoading) {
            adapter.showLoadingCharacter()
            isLoading = true
            currentPage++
            viewModel.getAllCharacter(currentPage)
        }
    }

    private fun setSpanCount(count: Int) {
        val layoutManager = GridLayoutManager(this, count)
        binding.recyclerview.layoutManager = layoutManager
        adapter.notifyDataSetChanged()
    }
}