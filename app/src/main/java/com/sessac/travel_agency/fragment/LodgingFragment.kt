package com.sessac.travel_agency.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.sessac.travel_agency.adapter.LodgingAdapter
import com.sessac.travel_agency.R
import com.sessac.travel_agency.common.BottomSheetFragment
import com.sessac.travel_agency.data.LodgingItem
import com.sessac.travel_agency.databinding.FragmentLodgingBinding

class LodgingFragment: Fragment() {

    // 지역 선택
    private lateinit var location: String
    private lateinit var binding: FragmentLodgingBinding

    // 리사이클러뷰
    private lateinit var recyclerView: RecyclerView
    private lateinit var lodgingList: ArrayList<LodgingItem>
    private lateinit var lodgingAdapter: LodgingAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)

        binding = FragmentLodgingBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // 드롭다운 메뉴
        setupLocationSpinner()
        setupLocationSpinnerHandler()

        //리사이클러뷰
        setupRecyclerviewAdapter()

        return root
    }

    // 지역 선택 메뉴 셋업
    private fun setupLocationSpinner() {
        val list = resources.getStringArray(R.array.select_region) // string.xml에 미리 정의해둔 지역
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, list)
        binding.dropLocations.setAdapter(arrayAdapter)

    }

    // 지역 선택 메뉴 사용자 클릭 이벤트 받기
    private fun setupLocationSpinnerHandler() {
        binding.dropLocations.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                // 선택한 지역을 변수에 저장해두기(지역 조건에 맞는 Lodging list만 db에서 받아서 리사이클러뷰에 뿌려줄 용도)
                location = parent.getItemAtPosition(position).toString()

//                when (location) {
//                    "전체" -> setupRecyclerviewAdapter()
//                    else -> null
//                }

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
    }

    private fun setupRecyclerviewAdapter() {
        lodgingList = ArrayList()

        recyclerView = binding.lodgingfragmentRecyclerview
        recyclerView.setHasFixedSize(true) // 리사이클러뷰의 크기가 변할 일이 없음 명시. 리사이클러뷰 레이아웃 다시 잡을 필요 없이 아이템 자리만 다시 잡기
        recyclerView.layoutManager = LinearLayoutManager(activity)
        initData()

        lodgingAdapter = LodgingAdapter(lodgingList)
        recyclerView.adapter = lodgingAdapter


        // 아이템 하나 클릭되면 모달 바텀시트 띄우기 -> Intent 기존데이터 가져가서 바인딩
        lodgingAdapter.onItemClick = {
            //val intent = Intent(context, BottomSheetFragment::class.java)
            //intent.putExtra("lodging", it)
            //startActivity(intent)
            val view: View = layoutInflater.inflate(R.layout.fragment_bottom_sheet, null)
            val dialog = BottomSheetDialog(requireContext())
            dialog.setContentView(view)
            dialog.show()
        }

    }

    // DB data 가져오기(우선 샘플데이터로)
    private fun initData() {
        val hotel1 = LodgingItem(1, "제주도", "그랜드 조선", R.drawable.hotel1, 5)
        val hotel2 = LodgingItem(2, "제주도", "신라 호텔", R.drawable.hotel2, 3)
        val hotel3 = LodgingItem(3, "서울시", "마리나베이샌즈호텔", R.drawable.hotel3, 5)
        val hotel4 = LodgingItem(4, "경상도", "공항 호텔", R.drawable.hotel4, 4)
        val hotel5 = LodgingItem(1, "경상도", "현대 호텔", R.drawable.hotel5, 2)
        val hotel6 = LodgingItem(2, "강원도", "인터컨티넨탈", R.drawable.hotel3, 3)
        val hotel7 = LodgingItem(3, "전라도", "현대민박", R.drawable.hotel4, 1)
        val hotel8 = LodgingItem(4, "충청도", "게하", R.drawable.hotel2, 4)
        lodgingList.add(hotel1)
        lodgingList.add(hotel2)
        lodgingList.add(hotel3)
        lodgingList.add(hotel4)
        lodgingList.add(hotel5)
        lodgingList.add(hotel6)
        lodgingList.add(hotel7)
        lodgingList.add(hotel8)
    }


    // 프래그먼트 화면을 벗어나면 기존 선택된 데이터 제거.
    override fun onPause() {
        super.onPause()

        // 기존에 선택된 지역을 제거하여 화면전환 후 돌아올시 새롭게 보이도록하기위함
        binding.dropLocations.text = null
        
    }

}