package com.example.android.moviedb

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.android.moviedb.network.MediaType

class FragmentAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        val type = when (position) {
            1 -> MediaType.TVShow
            else -> MediaType.Movie
        }
        val bundle = Bundle()
        bundle.putSerializable("type", type)
        val fragment = OverviewFragment()
        fragment.arguments = bundle
        return fragment
    }
}