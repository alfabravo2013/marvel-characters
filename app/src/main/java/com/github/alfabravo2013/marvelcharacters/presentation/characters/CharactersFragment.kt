package com.github.alfabravo2013.marvelcharacters.presentation.characters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.github.alfabravo2013.marvelcharacters.R
import com.github.alfabravo2013.marvelcharacters.databinding.FragmentCharactersBinding
import com.github.alfabravo2013.marvelcharacters.presentation.characters.CharactersViewModel.OnEvent
import com.github.alfabravo2013.marvelcharacters.presentation.characters.CharactersViewModel.DIRECTION
import org.koin.androidx.viewmodel.ext.android.viewModel

class CharactersFragment : Fragment() {
    private val viewModel: CharactersViewModel by viewModel()

    private val adapter by lazy {
        CharacterListAdapter(
            onStartReached = { viewModel.getCharactersPage(DIRECTION.PREVIOUS) },
            onEndReached = { viewModel.getCharactersPage(DIRECTION.NEXT) },
            onItemClicked = { id -> navigateToDetail(id) }
        ).apply { setHasStableIds(true) }
    }

    private var _binding: FragmentCharactersBinding? = null
    private val binding: FragmentCharactersBinding get() = _binding!!

    private val observer = Observer<OnEvent> { event ->
        when (event) {
            is OnEvent.ShowLoading -> binding.charactersProgressBar.visibility = View.VISIBLE
            is OnEvent.HideLoading -> binding.charactersProgressBar.visibility = View.GONE
            is OnEvent.ShowError -> showError(event.errorId)
            is OnEvent.CleanList -> adapter.clearList()
            is OnEvent.NextPage -> adapter.addNextPage(event.data)
            is OnEvent.PrevPage -> adapter.addPrevPage(event.data)
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
            viewModel.getFirstPage()
        }

        viewModel.onEvent.observe(viewLifecycleOwner, observer)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.characters_menu, menu)

        val searchItem = menu.findItem(R.id.action_characters_search)
        searchItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                viewModel.updateQueryText("")
                viewModel.getFirstPage()
                return true
            }
        })

        val searchView = searchItem.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.getFirstPage()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return viewModel.updateQueryText(newText)
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_filter_with_images -> {
                item.isChecked = !item.isChecked
                viewModel.onToggleWithImageFilter(item.isChecked)
                viewModel.getFirstPage()
                return true
            }
            R.id.action_filter_with_descriptions -> {
                item.isChecked = !item.isChecked
                viewModel.onToggleWithDescriptionFilter(item.isChecked)
                viewModel.getFirstPage()
                return true
            }
            else -> false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupRecyclerView() {
        val layoutManager = StaggeredGridLayoutManager(
            getSpanCount(),
            StaggeredGridLayoutManager.VERTICAL
        )
        binding.apply {
            characterListRecyclerView.layoutManager = layoutManager
            characterListRecyclerView.adapter = adapter
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
