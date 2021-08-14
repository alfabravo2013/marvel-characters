package com.github.alfabravo2013.marvelcharacters.presentation.characterlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.github.alfabravo2013.marvelcharacters.databinding.FragmentCharacterListBinding
import com.github.alfabravo2013.marvelcharacters.presentation.characterlist.CharacterListViewModel.OnEvent
import org.koin.androidx.viewmodel.ext.android.viewModel

class CharacterListFragment : Fragment() {
    private var _binding: FragmentCharacterListBinding? = null
    private val binding: FragmentCharacterListBinding get() = _binding!!

    private val viewModel: CharacterListViewModel by viewModel()
    private lateinit var adapter: CharacterListAdapter

    private val eventObserver = Observer<OnEvent> { event ->
        when (event) {
            is OnEvent.CharactersLoaded -> adapter.setData(event.characters)
            is OnEvent.ShowLoading -> { binding.characterListProgressBar.visibility = View.VISIBLE }
            is OnEvent.HideLoading -> { binding.characterListProgressBar.visibility = View.GONE }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCharacterListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = CharacterListAdapter { getMoreCharacters() }
        binding.characterListRecyclerView.adapter = adapter

        viewModel.onEvent.observe(viewLifecycleOwner, eventObserver)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getMoreCharacters() {
        viewModel.getMoreCharacters()
    }
}
