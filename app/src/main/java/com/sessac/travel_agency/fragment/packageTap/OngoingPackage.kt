package com.sessac.travel_agency.fragment.packageTap

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sessac.travel_agency.R
import com.sessac.travel_agency.adapter.PackageAdapter
import com.sessac.travel_agency.common.TravelAgencyApplication
import com.sessac.travel_agency.data.PackageItem
import com.sessac.travel_agency.database.PackageDao
import com.sessac.travel_agency.databinding.FragmentOngoingPackageBinding
import com.sessac.travel_agency.factory.PackageViewModelFactory
import com.sessac.travel_agency.repository.PackageRepository
import com.sessac.travel_agency.viewmodels.PackageViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * 패키지(홈)의 진행중 페이지
 */
class OngoingPackage : Fragment() {

    private lateinit var binding: FragmentOngoingPackageBinding

    // 리사이클러뷰
    private lateinit var recyclerView: RecyclerView
    private lateinit var packageList: ArrayList<PackageItem>
    private lateinit var packageAdapter: PackageAdapter
    private lateinit var packageDao: PackageDao // DAO 추가

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

        val app = TravelAgencyApplication.getTravelApplication()
        packageDao = app.database.packageDao()

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

        CoroutineScope(Dispatchers.IO).launch{
            initData()
        }

        packageAdapter = PackageAdapter(viewModel.allPackageList)
        recyclerView.adapter = packageAdapter

        Log.d("TAG", "setupRecyclerviewAdapter: ${viewModel.getAllPackage()}")

        packageAdapter.onItemClick = {
            findNavController().navigate(R.id.packageFragment_to_packageAddFragment)


        }
    }


    // 플로팅버튼의 등록페이지로 이동된상태에서 바텀네비 다른 메뉴 전환 후 다시 패키지로 돌아올때 메인이 보이도록
//    override fun onPause() {
//        super.onPause()
//
//        binding.fab = null
//
//    }


    // 샘플데이터
    private suspend fun initData() {
        val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())

        // 가이드 정보 생성 (가이드 이미지는 샘플 데이터로 대체)
//        val guideInfo = GuideItem(1, "가이드이름", R.drawable.ic_guide)
        val guideInfo = 1

        // 패키지 정보 생성
//        val package1 = PackageItem(
//            packageId = 1,
//            guideInfo = guideInfo,
//            area = "경기도",
//            pName = "1박2일 아산온천",
//            pImage = R.drawable.hotel1,
//            status = 1,
//            pStartDate = dateFormat.parse("2024/01/01")!!,
//            pEndDate = dateFormat.parse("2024/01/03")!!,
//        )
//
//        val package2 = PackageItem(
//            packageId = 2,
//            guideInfo = guideInfo,
//            area = "제주도",
//            pName = "2박3일 다이빙투어",
//            pImage = R.drawable.hotel2,
//            status = 1,
//            pStartDate = dateFormat.parse("2024/01/02")!!,
//            pEndDate = dateFormat.parse("2024/01/04")!!,
//        )

        val package1 = PackageItem(
            packageId = 1,
            guideInfo = guideInfo,
            area = "경기도",
            pName = "1박2일 아산온천",
            pImage = R.drawable.hotel1,
            status = 1,
            pStartDate = dateFormat.parse("2024/01/01")!!,
            pEndDate = dateFormat.parse("2024/01/03")!!,
        )

        val package2 = PackageItem(
            packageId = 2,
            guideInfo = guideInfo,
            area = "제주도",
            pName = "2박3일 다이빙투어",
            pImage = R.drawable.hotel2,
            status = 1,
            pStartDate = dateFormat.parse("2024/01/02")!!,
            pEndDate = dateFormat.parse("2024/01/04")!!,
        )

        packageList.add(package1)
        packageList.add(package2)

        packageDao.insert(packageList)
    }

}