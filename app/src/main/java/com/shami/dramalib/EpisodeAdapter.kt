package com.shami.dramalib

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class EpisodeAdapter(
    private val episodes: List<Episode>,
    private val onClick: (Episode) -> Unit
) : RecyclerView.Adapter<EpisodeAdapter.EpisodeViewHolder>() {

    class EpisodeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val card: CardView = view.findViewById(R.id.card_episode)
        val thumbnail: ImageView = view.findViewById(R.id.image_thumbnail)
        val title: TextView = view.findViewById(R.id.text_episode_title)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_episode, parent, false)
        return EpisodeViewHolder(view)
    }

    override fun onBindViewHolder(holder: EpisodeViewHolder, position: Int) {
        val episode = episodes[position]
        holder.title.text = episode.title

        Glide.with(holder.thumbnail.context)
            .load(episode.thumbnailUrl)
            .centerCrop()
            .placeholder(R.drawable.ic_book_foreground)
            .into(holder.thumbnail)

        holder.card.setOnClickListener { onClick(episode) }
    }

    override fun getItemCount(): Int = episodes.size
}
