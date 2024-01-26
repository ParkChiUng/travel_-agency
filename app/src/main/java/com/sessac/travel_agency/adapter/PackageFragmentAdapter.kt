package com.sessac.travel_agency.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.sessac.travel_agency.fragment.PackageFragment
import com.sessac.travel_agency.fragment.packageTap.EndedPackage
import com.sessac.travel_agency.fragment.packageTap.OngoingPackage
import com.sessac.travel_agency.fragment.packageTap.ScheduledPackage

private const val NUM_PAGES = 3

class PackageFragmentAdapter(fragmentActivity: PackageFragment) :
    FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return NUM_PAGES
    }

    override fun createFragment(position: Int): Fragment {
        when(position) {
            0 -> return EndedPackage()
            1 -> return OngoingPackage()
            2 -> return ScheduledPackage()
            else -> return OngoingPackage()
        }
    }

}