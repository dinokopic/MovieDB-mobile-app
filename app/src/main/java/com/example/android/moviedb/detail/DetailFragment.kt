package com.example.android.moviedb.detail

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.android.moviedb.databinding.FragmentDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DetailFragment : Fragment() {

    @Inject lateinit var assistedFactory: DetailViewModelAssistedFactory

    private val viewModel: DetailViewModel by viewModels {
        DetailViewModel.Factory(assistedFactory,
            DetailFragmentArgs.fromBundle(requireArguments()).selectedMedia)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentDetailBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding.root
    }

}