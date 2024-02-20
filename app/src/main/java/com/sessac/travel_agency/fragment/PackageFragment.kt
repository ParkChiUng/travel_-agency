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
import com.sessac.travel_agency.databinding.FragmentPackageBinding


class PackageFragment : Fragment() {
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2
    private lateinit var adapter: PackageFragmentAdapter
    private var tabTitleArray = arrayOf("종료", "진행중", "예정")
    private lateinit var binding: FragmentPackageBinding  // 리소스가 누수되고 있음. onDestroyed되어도 계속 잡고있음. 추상클래스 적용

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)

        binding = FragmentPackageBinding.inflate(inflater, container, false)

        return binding.root
    }

    // 화면 전환하고 돌아와도 언제나 진행탭을 기본으로 보이도록
//    override fun onResume() {
//        super.onResume()
//
//
//        // 현재 탭을 선택된 상태에서 선택 해제
//        tabLayout.getTabAt(TAB_DEFAULT_INDEX)?.select()
//
//    }
//
//    companion object {
//        private const val TAB_DEFAULT_INDEX = 1
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = PackageFragmentAdapter(this)
        viewPager = binding.viewPager
        viewPager.adapter = adapter
        tabLayout = binding.tabLayout

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabTitleArray[position]
        }.attach()


//        binding.packageTV.setOnClickListener {
//
//            findNavController().navigate(R.id.packageFragment_to_packageAddFragment)
//        }
    }

    // 뒤로가기 할때 리사이클러뷰도 해당 탭페이지에 맞게 보이도록
//    override fun onPause() {
//        super.onPause()
//        // 현재 탭을 선택된 상태에서 선택 해제
//        tabLayout.getTabAt(TAB_DEFAULT_INDEX)?.select()
//
//    }


}