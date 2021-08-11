package com.github.alfabravo2013.marvelcharacters.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.github.alfabravo2013.marvelcharacters.R
import com.github.alfabravo2013.marvelcharacters.databinding.FragmentCharacterListBinding

class CharacterList : Fragment() {
    val viewModel: CharacterListViewModel by viewModels()

    private var _binding: FragmentCharacterListBinding? = null
    private val binding: FragmentCharacterListBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCharacterListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.navButton.setOnClickListener {
            findNavController().navigate(R.id.action_characterList_to_characterDetails)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
