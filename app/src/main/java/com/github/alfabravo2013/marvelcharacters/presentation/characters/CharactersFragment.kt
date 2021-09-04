package com.github.alfabravo2013.marvelcharacters.presentation.characters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.github.alfabravo2013.marvelcharacters.MainActivity
import com.github.alfabravo2013.marvelcharacters.R
import com.github.alfabravo2013.marvelcharacters.databinding.FragmentCharactersBinding
import com.github.alfabravo2013.marvelcharacters.presentation.characters.CharactersViewModel.OnEvent
import com.github.alfabravo2013.marvelcharacters.presentation.characters.CharactersViewModel.PAGE
import com.github.alfabravo2013.marvelcharacters.presentation.characters.model.CharactersScreenState
import org.koin.androidx.viewmodel.ext.android.viewModel

class CharactersFragment : Fragment() {
    private val viewModel: CharactersViewModel by viewModel()

    private val adapter by lazy {
        CharacterListAdapter(
            onStartReached = { viewModel.getCharactersPage(PAGE.PREVIOUS) },
            onEndReached = { viewModel.getCharactersPage(PAGE.NEXT) },
            onItemClicked = { id -> navigateToDetail(id) }
        ).apply { setHasStableIds(true) }
    }

    private var _binding: FragmentCharactersBinding? = null
    private val binding: FragmentCharactersBinding get() = _binding!!

    private val onEventObserver = Observer<OnEvent> { event ->
        when (event) {
            is OnEvent.ShowLoading -> binding.charactersProgressBar.visibility = View.VISIBLE
            is OnEvent.HideLoading -> binding.charactersProgressBar.visibility = View.GONE
            is OnEvent.ShowError -> showError(event.errorId)
            is OnEvent.CleanList -> adapter.clearList()
            is OnEvent.SubmitPage -> adapter.submitPage(event.data)
        }
    }

    private lateinit var searchView: SearchView

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
        setHasOptionsMenu(true)

        (activity as MainActivity).setTitle(getString(R.string.characters_screen_title))

        binding.charactersRetryButton.setOnClickListener {
            it.visibility = View.GONE
            viewModel.getCharactersPage(PAGE.FIRST)
        }

        viewModel.onEvent.observe(viewLifecycleOwner, onEventObserver)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.characters_menu, menu)
        setupMenuStateObserver(menu)
        setupSearchView(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_filter_images -> {
                viewModel.onToggleWithImageFilter()
                return true
            }
            R.id.action_filter_descriptions -> {
                viewModel.onToggleWithDescriptionFilter()
                return true
            }
            R.id.action_filter_names -> {
                viewModel.onToggleUniqueNamesFilter()
                return true
            }
            else -> false
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getCharactersPage(PAGE.CURRENT)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        searchView.setOnQueryTextListener(null)
    }

    private fun setupMenuStateObserver(menu: Menu) {
        val screenStateObserver = Observer<CharactersScreenState> { state ->
            menu.findItem(R.id.action_filter_images).isChecked = state.hasImageFilterOn
            menu.findItem(R.id.action_filter_descriptions).isChecked = state.hasDescriptionFilterOn
            menu.findItem(R.id.action_filter_names).isChecked = state.uniqueNamesFilterOn
        }

        viewModel.screenState.observe(viewLifecycleOwner, screenStateObserver)
    }

    private fun setupSearchView(menu: Menu) {
        setupSearchItem(menu)
        setupSearchQuery(menu)
    }

    private fun setupSearchItem(menu: Menu) {
        val searchItem = menu.findItem(R.id.action_characters_search)

        searchItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                viewModel.updateQueryText("")
                viewModel.getCharactersPage(PAGE.FIRST)
                return true
            }
        })
    }

    private fun setupSearchQuery(menu: Menu) {
        val searchItem = menu.findItem(R.id.action_characters_search)
        searchView = searchItem.actionView as SearchView
        val queryText = viewModel.screenState.value?.searchQuery ?: ""

        if (queryText.isNotEmpty()) {
            searchItem.expandActionView()
            searchView.setQuery(queryText, false)
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.getCharactersPage(PAGE.FIRST)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return viewModel.updateQueryText(newText)
            }
        })
    }

    private fun setupRecyclerView() {
        val layoutManager = GridLayoutManager(
            requireContext(),
            getSpanCount()
        )

        binding.apply {
            characterListRecyclerView.layoutManager = layoutManager
            characterListRecyclerView.adapter = adapter
            characterListRecyclerView.setHasFixedSize(true)
        }
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
