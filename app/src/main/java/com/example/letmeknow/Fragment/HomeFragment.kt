package com.example.letmeknow.Fragment


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.example.letmeknow.Activity.CreatePollActivity
import com.example.letmeknow.R
import com.example.letmeknow.adapter.ViewPagerAdapter
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.tabs.TabLayout


class HomeFragment() : Fragment() {

    var tabLayout: TabLayout? = null
    var viewPager: ViewPager? = null
    private var _binding: HomeFragment?=null
    private val binding get() = _binding!!
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_home, container, false)
        val bt=view.findViewById<ExtendedFloatingActionButton>(R.id.extendedFloatingActionButton)
        addFragment(view)

        //moving from homefragment to Create poll Activity
        bt.setOnClickListener {
            val intent = Intent(requireContext(), CreatePollActivity::class.java)
            startActivity(intent)

        }
        return view
    }

    //to add tab layout and two child fragments in parent Home fragment
    private fun addFragment(view: View) {
        tabLayout = view.findViewById<TabLayout>(R.id.tabLayout)
        viewPager = view.findViewById<ViewPager>(R.id.viewPager)
        val adapter = ViewPagerAdapter(childFragmentManager)
        adapter.addFragment(ActiveFragment(), "Active Polls")
        adapter.addFragment(ResultsFragment(), "My Polls")
        viewPager?.setAdapter(adapter)
        tabLayout?.setupWithViewPager(viewPager)
    }

}