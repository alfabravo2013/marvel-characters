package com.github.alfabravo2013.marvelcharacters.presentation.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.github.alfabravo2013.marvelcharacters.MainActivity
import com.github.alfabravo2013.marvelcharacters.R
import com.github.alfabravo2013.marvelcharacters.databinding.FragmentDetailBinding
import com.github.alfabravo2013.marvelcharacters.presentation.characters.model.Detail
import com.github.alfabravo2013.marvelcharacters.presentation.detail.DetailViewModel.OnEvent
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailFragment : Fragment() {
    private var _binding: FragmentDetailBinding? = null
    private val binding: FragmentDetailBinding get() = _binding!!

    private val viewModel: DetailViewModel by viewModel()

    private val observer = Observer<OnEvent> { event ->
        when (event) {
            is OnEvent.ShowLoading -> binding.detailProgressBar.visibility = View.VISIBLE
            is OnEvent.HideLoading -> binding.detailProgressBar.visibility = View.GONE
            is OnEvent.Error -> Toast.makeText(
                activity,
                getString(event.errorId),
                Toast.LENGTH_LONG).show()
            is OnEvent.ShowDetail -> bindElements(event.detail)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (activity as MainActivity).setTitle(getString(R.string.detail_screen_title))

        viewModel.onEvent.observe(viewLifecycleOwner, observer)

        val characterId = DetailFragmentArgs.fromBundle(requireArguments()).characterId
        viewModel.getDetail(characterId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun bindElements(detail: Detail) {
        with(binding) {
            detailName.text = detail.name
            detailDescription.text = detail.description
            Glide.with(binding.root.context)
                .load(detail.imageUrl)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(detailImage)
        }
    }
}
