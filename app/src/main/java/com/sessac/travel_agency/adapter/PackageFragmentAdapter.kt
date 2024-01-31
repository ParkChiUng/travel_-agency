package com.sessac.travel_agency.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.sessac.travel_agency.fragment.PackageFragment
import com.sessac.travel_agency.fragment.packageTap.EndedPackageFragment
import com.sessac.travel_agency.fragment.packageTap.OngoingPackageFragment
import com.sessac.travel_agency.fragment.packageTap.ScheduledPackageFragment

private const val NUM_PAGES = 3

class PackageFragmentAdapter(fragmentActivity: PackageFragment) :
    FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return NUM_PAGES
    }

    override fun createFragment(position: Int): Fragment {
        when(position) {
            0 -> return EndedPackageFragment()
            1 -> return OngoingPackageFragment()
            2 -> return ScheduledPackageFragment()
            else -> return OngoingPackageFragment()
        }
    }

}