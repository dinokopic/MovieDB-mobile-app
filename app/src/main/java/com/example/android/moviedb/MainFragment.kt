package com.example.android.moviedb

import android.os.Bundle
import android.os.CountDownTimer
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.android.moviedb.adapter.FragmentAdapter
import com.example.android.moviedb.adapter.MediaListAdapter
import com.example.android.moviedb.databinding.FragmentMainBinding
import com.example.android.moviedb.network.MediaType
import com.example.android.moviedb.search.SearchViewModel
import com.google.android.material.tabs.TabLayoutMediator

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private val viewModel: SearchViewModel by lazy {
        ViewModelProvider(this).get(SearchViewModel::class.java)
    }
    private var mediaType = MediaType.Movie
    private var query: String = ""

    private val timer = object: CountDownTimer(1000, 1000) {
        override fun onTick(p0: Long) {

        }
        override fun onFinish() {
            viewModel.showSearchResults()
            val mediaType = when (binding.viewPager.currentItem) {
                1 -> MediaType.TVShow
                else -> MediaType.Movie
            }
            viewModel.search(query, mediaType)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main,
            container,false)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        query = viewModel.searchQuery.value ?: ""

        binding.searchList.adapter = MediaListAdapter(MediaListAdapter.OnClickListener {
            viewModel.displayMediaDetails(it)
        })
        binding.searchList.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        viewModel.navigateToSelectedMedia.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                viewModel.setQueryValue(query)
                this.findNavController().navigate(
                    MainFragmentDirections.actionShowDetails(it))
                viewModel.displayMediaDetailsComplete()
            }
        })

        val fragmentAdapter = FragmentAdapter(this)
        binding.viewPager.adapter = fragmentAdapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                1 -> getString(R.string.tv_shows)
                else -> getString(R.string.movies)
            }
        }.attach()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView
        searchView.queryHint = getString(R.string.query_hint)
        if (!viewModel.searchQuery.value.isNullOrEmpty()) {
            searchItem.expandActionView()
            searchView.setQuery(viewModel.searchQuery.value, false)
            viewModel.setQueryValue(null)
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (!viewModel.searchQuery.value.isNullOrEmpty()) {
                    return true
                }
                newText?.apply {
                    query = newText
                    timer.cancel()
                    if (query.length > 3) {
                        timer.start()
                    } else {
                        viewModel.removeMedia()
                        viewModel.hideSearchResults()
                    }
                }
                return true
            }

        })
    }

    override fun onDestroy() {
        super.onDestroy()
        if (query.isNotEmpty()) {
            viewModel.setQueryValue(query)
        }
    }

}