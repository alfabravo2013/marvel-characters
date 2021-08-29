package com.github.alfabravo2013.marvelcharacters.presentation.characters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.github.alfabravo2013.marvelcharacters.databinding.CharactersItemBinding
import com.github.alfabravo2013.marvelcharacters.presentation.characters.model.CharactersItemPage
import com.github.alfabravo2013.marvelcharacters.presentation.characters.model.CharactersItem

class CharacterListAdapter(
    private val onEndReached: () -> Unit,
    private val onStartReached: () -> Unit,
    private val onItemClicked: (Int) -> Unit
) : ListAdapter<CharactersItem, CharacterListAdapter.ViewHolder>(CharacterComparator) {

    private var isFirstPageLoaded = false
    private var isLastPageLoaded = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CharactersItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))

        if (position == 0 && !isFirstPageLoaded) {
            onStartReached.invoke()
        } else if (position >= itemCount - 6 && !isLastPageLoaded) {
            onEndReached.invoke()
        }
    }

    override fun getItemId(position: Int): Long = getItem(position).id.toLong()

    fun addPrevPage(page: CharactersItemPage) {
        updatePageStatus(page)
        submitList(page.characters)
    }

    fun addNextPage(page: CharactersItemPage) {
        updatePageStatus(page)
        submitList(page.characters)
    }

    fun clearList() {
        submitList(null)
        isFirstPageLoaded = false
        isLastPageLoaded = false
    }

    private fun updatePageStatus(page: CharactersItemPage) {
        if (page.prevOffset == null) {
            isFirstPageLoaded = true
        }
        if (page.nextOffset == null) {
            isLastPageLoaded = true
        }
    }

    inner class ViewHolder(
        private val binding: CharactersItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val id = currentList[bindingAdapterPosition].id
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

    object CharacterComparator : DiffUtil.ItemCallback<CharactersItem>() {
        override fun areItemsTheSame(oldItem: CharactersItem, newItem: CharactersItem): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: CharactersItem, newItem: CharactersItem): Boolean =
            oldItem == newItem
    }
}
