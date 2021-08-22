package com.github.alfabravo2013.marvelcharacters.presentation.characters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.github.alfabravo2013.marvelcharacters.databinding.LoadStateItemBinding

class CharactersLoadStateAdapter(
    private val onRetryClick: () -> Unit
) : LoadStateAdapter<CharactersLoadStateAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): ViewHolder {
        val binding = LoadStateItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ViewHolder(binding).also {
            binding.charactersRetryButton.setOnClickListener {
                onRetryClick()
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    class ViewHolder(
        private val binding: LoadStateItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(state: LoadState) {
            binding.apply {
                if (state is LoadState.Error) {
                    charactersErrorText.text = state.error.localizedMessage
                }
                charactersProgressBar.isVisible = state is LoadState.Loading
                charactersRetryButton.isVisible = state is LoadState.Error
                charactersErrorText.isVisible = state is LoadState.Error
            }
        }
    }
}
