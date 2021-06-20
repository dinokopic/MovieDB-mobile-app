package com.example.android.moviedb

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.android.moviedb.databinding.FragmentMainBinding
import com.example.android.moviedb.network.MediaType
import com.google.android.material.tabs.TabLayoutMediator

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private lateinit var viewModel: SearchViewModel
    private var mediaType = MediaType.Movie
    private var query: String = ""

    private val timer = object: CountDownTimer(1000, 1000) {
        override fun onTick(p0: Long) {
            Log.i("OK", "OK")
        }
        override fun onFinish() {
            viewModel.showSearchResults()
            val currItem = when (binding.viewPager.currentItem) {
                1 -> "TV Shows"
                else -> "Movies"
            }
            viewModel.search(query)
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
        val application = requireActivity().application

        val viewModelFactory = SearchViewModelFactory(mediaType, application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(SearchViewModel::class.java)
        binding.viewModel = viewModel

        query = viewModel.searchQuery.value.toString()

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
                1 -> "TV Shows"
                else -> "Movies"
            }
        }.attach()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)
        val searchItem = menu.findItem(R.id.action_search)
        val searchView: SearchView = searchItem.actionView as SearchView
        searchView.setIconifiedByDefault(true)
        if (!viewModel.searchQuery.value.isNullOrEmpty()) {
            searchItem.expandActionView()
            searchView.clearFocus()
            searchView.setQuery(viewModel.searchQuery.value, true)
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