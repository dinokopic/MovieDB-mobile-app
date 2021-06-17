package com.example.android.moviedb

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.android.moviedb.databinding.FragmentOverviewBinding
import com.example.android.moviedb.databinding.ListViewItemBinding

class OverviewFragment : Fragment() {

    private val viewModel: OverviewViewModel by lazy {
        ViewModelProvider(this).get(OverviewViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentOverviewBinding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_overview, container, false)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.movieList.adapter = MovieListAdapter()
        return binding.root
    }

}