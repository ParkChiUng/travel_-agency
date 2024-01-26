package com.sessac.travel_agency.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.sessac.travel_agency.R
import com.sessac.travel_agency.adapter.GuideAdapter
import com.sessac.travel_agency.data.GuideItem
import com.sessac.travel_agency.databinding.FragmentGuideBinding

class GuideFragment : Fragment() {
    // 리사이클러뷰
    private lateinit var recyclerView: RecyclerView
    private lateinit var guideList: ArrayList<GuideItem>
    private lateinit var guideAdapter: GuideAdapter

    private lateinit var binding: FragmentGuideBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)

        binding = FragmentGuideBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //리사이클러뷰
        setupRecyclerviewAdapter()

        //플로팅버튼
        val fab: View = binding.fab
        fab.setOnClickListener { view ->
            val view: View = layoutInflater.inflate(R.layout.fragment_guide_add, null)
            val dialog = BottomSheetDialog(requireContext())
            dialog.setContentView(view)
            dialog.show()
        }

        return root
    }



    private fun setupRecyclerviewAdapter() {
        guideList = ArrayList()

        recyclerView = binding.guideRecyclerview
        recyclerView.setHasFixedSize(true) // 리사이클러뷰의 크기가 변할 일이 없음 명시. 리사이클러뷰 레이아웃 다시 잡을 필요 없이 아이템 자리만 다시 잡기
        recyclerView.layoutManager = GridLayoutManager(activity,2)
        initData()

        guideAdapter = GuideAdapter(guideList)
        recyclerView.adapter = guideAdapter


        // 아이템 하나 클릭되면 모달 바텀시트 띄우기 -> Intent 기존데이터 가져가서 바인딩
        guideAdapter.onItemClick = {
            val view: View = layoutInflater.inflate(R.layout.fragment_guide_edit, null)
            val dialog = BottomSheetDialog(requireContext())
            dialog.setContentView(view)
            dialog.show()
        }

    }

    // DB data 가져오기(우선 샘플데이터로)
    private fun initData() {
        val guide1 = GuideItem(1, "김철수", R.drawable.hotel3)
        val guide2 = GuideItem(2, "데이빗", R.drawable.hotel4)
        val guide3 = GuideItem(3, "조안나", R.drawable.hotel5)
        guideList.add(guide1)
        guideList.add(guide2)
        guideList.add(guide3)
        guideList.add(guide1)
        guideList.add(guide3)
        guideList.add(guide2)
        guideList.add(guide1)
        guideList.add(guide2)
        guideList.add(guide3)
    }


}