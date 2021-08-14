package com.github.alfabravo2013.marvelcharacters.presentation.characterlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.alfabravo2013.marvelcharacters.databinding.CharacterListItemBinding
import com.github.alfabravo2013.marvelcharacters.presentation.characterlist.model.CharacterListItem

class CharacterListAdapter(
    private val onEndOfList: () -> Unit
) : RecyclerView.Adapter<CharacterListAdapter.ViewHolder>() {
    private val characterList: MutableList<CharacterListItem> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CharacterListItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(characterList[position])
        if (position == characterList.lastIndex) {
            onEndOfList.invoke()
        }
    }

    override fun getItemCount(): Int = characterList.size

    fun setData(data: List<CharacterListItem>) {
        val prevLastIndex = characterList.lastIndex
        characterList.addAll(data)
        notifyItemRangeInserted(prevLastIndex + 1, data.size)
    }

    class ViewHolder(
        private val binding: CharacterListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CharacterListItem) {
            binding.itemHeroName.text = item.name
            Glide.with(binding.root.context)
                .load(item.imageUrl)
                .into(binding.itemHeroThumbnail)
        }
    }
}
