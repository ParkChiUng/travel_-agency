package com.sessac.travel_agency.fragment.packageTap

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sessac.travel_agency.R
import com.sessac.travel_agency.adapter.PackageAdapter
import com.sessac.travel_agency.data.PackageItem
import com.sessac.travel_agency.databinding.FragmentOngoingPackageBinding
import com.sessac.travel_agency.factory.PackageViewModelFactory
import com.sessac.travel_agency.repository.PackageRepository
import com.sessac.travel_agency.viewmodels.PackageViewModel

/**
 * 패키지(홈)의 예정 페이지
 */
class ScheduledPackageFragment : Fragment() {

    private lateinit var binding: FragmentOngoingPackageBinding

    // 리사이클러뷰
    private lateinit var recyclerView: RecyclerView
    private lateinit var packageList: ArrayList<PackageItem>
    private lateinit var packageAdapter: PackageAdapter

    private val viewModel: PackageViewModel by viewModels {
        PackageViewModelFactory(PackageRepository(requireContext()))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)

        binding = FragmentOngoingPackageBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupRecyclerviewAdapter()

        //플로팅버튼
        val fab: View = binding.fab
        fab.setOnClickListener { view ->
            findNavController().navigate(R.id.packageFragment_to_packageAddFragment)
        }

        return root
    }


    private fun setupRecyclerviewAdapter() {
        packageList = ArrayList()

        recyclerView = binding.ongoingPackageRecyclerview
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(activity)
//        initData()

        packageAdapter = PackageAdapter(viewModel.allPackageList)
        recyclerView.adapter = packageAdapter

        packageAdapter.onItemClick = {
            findNavController().navigate(R.id.packageFragment_to_packageAddFragment)
        }
    }

    // 샘플데이터
//    private fun initData() {
//        val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
//
//        // 가이드 정보 생성 (가이드 이미지는 샘플 데이터로 대체)
//        val guideInfo = GuideItem(1, "가이드이름", R.drawable.ic_guide)
//
//        // 패키지 정보 생성
//        val package1 = PackageItem(
//            packageId = 1,
//            guideInfo = guideInfo,
//            area = "경기도",
//            pName = "새덕후와 함께하는 탐조 가이드",
//            pImage = R.drawable.hotel2,
//            status = 2,
//            pStartDate = dateFormat.parse("2023/02/01")!!,
//            pEndDate = dateFormat.parse("2023/02/02")!!,
//        )
//
//        val package2 = PackageItem(
//            packageId = 2,
//            guideInfo = guideInfo,
//            area = "전라도",
//            pName = "무주 스키장 시즌오프",
//            pImage = R.drawable.hotel3,
//            status = 2,
//            pStartDate = dateFormat.parse("2024/02/03")!!,
//            pEndDate = dateFormat.parse("2024/02/04")!!,
//        )
//
//        packageList.add(package1)
//        packageList.add(package2)
//        packageList.add(package1)
//    }
}