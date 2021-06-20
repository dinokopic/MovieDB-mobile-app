package com.example.android.moviedb

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.android.moviedb.databinding.FragmentOverviewBinding
import com.example.android.moviedb.network.MediaType

class OverviewFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val application = requireActivity().application

        val binding: FragmentOverviewBinding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_overview, container, false)
        binding.lifecycleOwner = this

        val mediaType = requireArguments().getSerializable("type") as MediaType

        val viewModelFactory = OverviewViewModelFactory(mediaType, application)
        val viewModel = ViewModelProvider(this, viewModelFactory).get(OverviewViewModel::class.java)
        binding.viewModel = viewModel
        binding.movieList.adapter = MediaListAdapter(MediaListAdapter.OnClickListener {
            viewModel.displayMediaDetails(it)
        })
        binding.movieList.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        viewModel.navigateToSelectedMedia.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                this.findNavController().navigate(
                    MainFragmentDirections.actionShowDetails(it))
                viewModel.displayMediaDetailsComplete()
            }
        })

        return binding.root
    }

}