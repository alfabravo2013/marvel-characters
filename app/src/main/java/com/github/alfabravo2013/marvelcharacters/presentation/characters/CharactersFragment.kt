package com.github.alfabravo2013.marvelcharacters.presentation.characters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.github.alfabravo2013.marvelcharacters.databinding.FragmentCharactersBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class CharactersFragment : Fragment() {
    private val viewModel: CharactersViewModel by viewModel()
    private val adapter by lazy { CharacterListAdapter() }

    private var _binding: FragmentCharactersBinding? = null
    private val binding: FragmentCharactersBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCharactersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupRecyclerView()

        viewModel.characters.observe(viewLifecycleOwner) { page ->
            adapter.submitData(viewLifecycleOwner.lifecycle, page)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupRecyclerView() {
        val layoutManager = GridLayoutManager(requireContext(), getSpanCount())
        val footerAdapter = CharactersLoadStateAdapter { adapter.retry() }

        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (position == adapter.itemCount && footerAdapter.itemCount > 0) {
                    getSpanCount()
                } else {
                    1
                }
            }
        }

        binding.apply {
            characterListRecyclerView.layoutManager = layoutManager
            characterListRecyclerView.adapter = adapter.withLoadStateFooter(
                footer = footerAdapter
            )
        }
    }

    private fun getSpanCount(): Int {
        val viewportWidthDp = context?.resources?.configuration?.screenWidthDp ?: 320
        return (viewportWidthDp / 200).coerceIn(1..5)
    }
}
