package com.github.alfabravo2013.marvelcharacters.presentation.characters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.github.alfabravo2013.marvelcharacters.databinding.CharactersItemBinding
import com.github.alfabravo2013.marvelcharacters.presentation.characters.model.CharactersItem

class CharacterListAdapter :
    PagingDataAdapter<CharactersItem, CharacterListAdapter.ViewHolder>(CharacterComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CharactersItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position) ?: return
        holder.bind(item)
    }

    class ViewHolder(
        private val binding: CharactersItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CharactersItem) {
            binding.charactersName.text = item.name
            Glide.with(binding.root.context)
                .load(item.imageUrl)
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.charactersThumbnail)
        }
    }

    class CharacterComparator : DiffUtil.ItemCallback<CharactersItem>() {
        override fun areItemsTheSame(oldItem: CharactersItem, newItem: CharactersItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CharactersItem, newItem: CharactersItem): Boolean {
            return oldItem == newItem
        }
    }
}
