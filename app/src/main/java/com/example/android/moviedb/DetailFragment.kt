package com.example.android.moviedb

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.android.moviedb.databinding.FragmentDetailBinding


class DetailFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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