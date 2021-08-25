package com.github.alfabravo2013.marvelcharacters.presentation.characters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.github.alfabravo2013.marvelcharacters.databinding.FragmentCharactersBinding
import com.github.alfabravo2013.marvelcharacters.presentation.characters.CharactersViewModel.OnEvent
import org.koin.androidx.viewmodel.ext.android.viewModel

class CharactersFragment : Fragment() {
    private val viewModel: CharactersViewModel by viewModel()

    private val adapter by lazy {
        CharacterListAdapter(
            { viewModel.getCharactersPage() },
            { id -> navigateToDetail(id) }
        )
    }

    private var _binding: FragmentCharactersBinding? = null
    private val binding: FragmentCharactersBinding get() = _binding!!

    private val observer = Observer<OnEvent> { event ->
        when (event) {
            is OnEvent.ShowLoading -> binding.charactersProgressBar.visibility = View.VISIBLE
            is OnEvent.HideLoading -> binding.charactersProgressBar.visibility = View.GONE
            is OnEvent.ShowError -> showError(event.errorId)
            is OnEvent.SubmitData -> adapter.addList(event.data)
        }
    }

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

        binding.charactersRetryButton.setOnClickListener {
            it.visibility = View.GONE
            viewModel.getCharactersPage()
        }

        viewModel.onEvent.observe(viewLifecycleOwner, observer)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupRecyclerView() {
        val layoutManager = GridLayoutManager(requireContext(), getSpanCount())
        binding.characterListRecyclerView.layoutManager = layoutManager
        binding.characterListRecyclerView.adapter = adapter
    }

    private fun getSpanCount(): Int {
        val viewportWidthDp = context?.resources?.configuration?.screenWidthDp ?: 320
        return (viewportWidthDp / 200).coerceIn(1..5)
    }

    private fun showError(errorId: Int) {
        Toast.makeText(activity, getString(errorId), Toast.LENGTH_SHORT).show()
        binding.charactersRetryButton.visibility = View.VISIBLE
    }

    private fun navigateToDetail(characterId: Int) {
        val action = CharactersFragmentDirections.actionCharactersToDetail(characterId)
        findNavController().navigate(action)
    }
}
