package com.example.android.moviedb.medialist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.android.moviedb.MainFragmentDirections
import com.example.android.moviedb.R
import com.example.android.moviedb.adapter.MediaListAdapter
import com.example.android.moviedb.databinding.FragmentMediaListBinding
import com.example.android.moviedb.network.MediaType
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MediaListFragment : Fragment() {

    @Inject lateinit var assistedFactory: MediaListViewModelAssistedFactory

    private val viewModel: MediaListViewModel by viewModels {
        MediaListViewModel.Factory(assistedFactory, requireArguments().getSerializable("type") as MediaType)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        val binding: FragmentMediaListBinding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_media_list, container, false)
        binding.lifecycleOwner = this
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