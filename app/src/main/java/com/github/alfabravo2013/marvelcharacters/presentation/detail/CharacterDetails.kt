package com.github.alfabravo2013.marvelcharacters.presentation.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.github.alfabravo2013.marvelcharacters.databinding.FragmentCharacterDetailBinding

class CharacterDetails : Fragment() {
    val viewModel: CharacterDetailsViewModel by viewModels()

    private var _binding: FragmentCharacterDetailBinding? = null
    private val binding: FragmentCharacterDetailBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCharacterDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
