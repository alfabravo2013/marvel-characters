package com.github.alfabravo2013.marvelcharacters.presentation.characters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.github.alfabravo2013.marvelcharacters.databinding.CharactersItemBinding
import com.github.alfabravo2013.marvelcharacters.presentation.characters.model.CharactersItem

class CharacterListAdapter(
    private val onListEnd: () -> Unit,
    private val onItemClicked: (Int) -> Unit
) : RecyclerView.Adapter<CharacterListAdapter.ViewHolder>() {

    private val characters = mutableListOf<CharactersItem>()

    init {
        if (characters.isEmpty()) {
            onListEnd()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CharactersItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(characters[position])
        if (position >= itemCount - 4) {
            onListEnd()
        }
    }

    override fun getItemCount(): Int = characters.size

    fun addList(list: List<CharactersItem>) {
        val startPosition = characters.size
        characters.addAll(list)
        notifyItemRangeInserted(startPosition, list.size)
    }

    inner class ViewHolder(
        private val binding: CharactersItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val id = characters[bindingAdapterPosition].id
                onItemClicked(id)
            }
        }

        fun bind(item: CharactersItem) {
            binding.charactersName.text = item.name
            Glide.with(binding.root.context)
                .load(item.imageUrl)
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.charactersThumbnail)
        }
    }
}
