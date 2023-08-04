package com.vimal.rickandmorty.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vimal.rickandmorty.R
import com.vimal.rickandmorty.model.Episode

class EpisodeAdapter() :
    RecyclerView.Adapter<EpisodeAdapter.ViewHolder>() {

    var episodeList = mutableListOf<Episode>()

    fun setMovies(episods: Episode) {
        episodeList.add(episods)
        notifyDataSetChanged()
    }

    fun setMovies(movies: List<Episode>) {
        episodeList.addAll(movies)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_episode, parent, false)
        return ViewHolder(inflater)
    }

    override fun getItemCount(): Int {
        return episodeList.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val episode: TextView = itemView.findViewById(R.id.episode)
        val name: TextView = itemView.findViewById(R.id.name)
        val airdate: TextView = itemView.findViewById(R.id.airdate)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.episode.text = episodeList[position].episode
        holder.name.text = episodeList[position].name
        holder.airdate.text = episodeList[position].air_date
    }
}