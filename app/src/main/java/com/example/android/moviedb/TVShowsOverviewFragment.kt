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
import com.example.android.moviedb.databinding.FragmentTVShowsOverviewBinding

class TVShowsOverviewFragment : Fragment() {

    private val viewModel: TVShowsOverviewViewModel by lazy {
        ViewModelProvider(this).get(TVShowsOverviewViewModel::class.java)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentTVShowsOverviewBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_t_v_shows_overview,
            container,
            false
        )

        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.tvShowsList.adapter = MediaListAdapter(MediaListAdapter.OnClickListener {
            viewModel.displayMediaDetails(it)
        })
        binding.tvShowsList.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        viewModel.navigateToSelectedTVShow.observe(viewLifecycleOwner, Observer {
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