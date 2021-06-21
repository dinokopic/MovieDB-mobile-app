package com.example.android.moviedb.detail

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.android.moviedb.databinding.FragmentDetailBinding


class DetailFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val application = requireActivity().application
        val binding = FragmentDetailBinding.inflate(inflater)
        binding.lifecycleOwner = this

        val media = DetailFragmentArgs.fromBundle(requireArguments()).selectedMedia
        val viewModelFactory = DetailViewModelFactory(media, application)
        binding.viewModel = ViewModelProvider(
            this, viewModelFactory).get(DetailViewModel::class.java)
        return binding.root
    }
}