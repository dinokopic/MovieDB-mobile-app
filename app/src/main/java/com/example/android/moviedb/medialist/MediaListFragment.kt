package com.example.android.moviedb.medialist

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
import com.example.android.moviedb.MainFragmentDirections
import com.example.android.moviedb.R
import com.example.android.moviedb.adapter.MediaListAdapter
import com.example.android.moviedb.databinding.FragmentMediaListBinding
import com.example.android.moviedb.network.MediaType

class MediaListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val application = requireActivity().application

        val binding: FragmentMediaListBinding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_media_list, container, false)
        binding.lifecycleOwner = this

        val mediaType = requireArguments().getSerializable("type") as MediaType

        val viewModelFactory = MediaListViewModelFactory(mediaType, application)
        val viewModel = ViewModelProvider(this, viewModelFactory).get(MediaListViewModel::class.java)
        binding.viewModel = viewModel
        binding.movieList.adapter = MediaListAdapter(MediaListAdapter.OnClickListener {
            viewModel.displayMediaDetails(it)
        })
        binding.movieList.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        viewModel.navigateToSelectedMedia.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                this.findNavController().navigate(
                    MainFragmentDirections.actionShowDetails(it)
                )
                viewModel.displayMediaDetailsComplete()
            }
        })

        return binding.root
    }

}