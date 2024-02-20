package com.sessac.travel_agency.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.sessac.travel_agency.adapter.PackageFragmentAdapter
import com.sessac.travel_agency.databinding.FragmentOngoingPackageBinding
import com.sessac.travel_agency.databinding.FragmentPackageBinding


class PackageFragment :
    ViewBindingBaseFragment<FragmentPackageBinding>(FragmentPackageBinding::inflate) {

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2
    private lateinit var adapter: PackageFragmentAdapter
    private var tabTitleArray = arrayOf("종료", "진행중", "예정")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = PackageFragmentAdapter(this)
        viewPager = binding.viewPager
        viewPager.adapter = adapter
        tabLayout = binding.tabLayout

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabTitleArray[position]
        }.attach()
    }
}