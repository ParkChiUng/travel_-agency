package com.sessac.travel_agency.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.sessac.travel_agency.R
import com.sessac.travel_agency.adapter.GuideAdapter
import com.sessac.travel_agency.data.GuideItem
import com.sessac.travel_agency.databinding.FragmentGuideBinding

// GuideFragment 클래스가 OnGuideItemClickListener를 구현하도록 함
class GuideFragment : Fragment(), GuideAdapter.OnGuideItemClickListener {
    // 리사이클러뷰
    private lateinit var recyclerView: RecyclerView
    private lateinit var guideList: ArrayList<GuideItem>
    private lateinit var guideAdapter: GuideAdapter

    private lateinit var binding: FragmentGuideBinding

    private lateinit var img: ImageView
    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.let { data ->
                handleGalleryResult(data)
            }
        }
    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGuideBinding.inflate(inflater, container, false)

        return binding.root
    }

    // 실제 데이터나 리소스와 관련된 변수들은 생성된 뷰를 기반으로 한 onViewCreated 단계에서 초기화
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerviewAdapter()  // 리사이클러뷰
        setupFloatingButton()  // 플로팅버튼
    }

    //플로팅버튼(가이드 등록)
    private fun setupFloatingButton() {
        val fab: View = binding.fab
        fab.setOnClickListener { view ->
            val view: View = layoutInflater.inflate(R.layout.fragment_guide_add, null)
            val dialog = BottomSheetDialog(requireContext())
            dialog.setContentView(view)
            dialog.show()
        }
    }

    private fun setupRecyclerviewAdapter() {
        guideList = ArrayList()

        recyclerView = binding.guideRecyclerview
        recyclerView.setHasFixedSize(true) // 리사이클러뷰의 크기가 변할 일이 없음 명시. 리사이클러뷰 레이아웃 다시 잡을 필요 없이 아이템 자리만 다시 잡기
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        initData()

        guideAdapter = GuideAdapter(guideList, this)
        recyclerView.adapter = guideAdapter
    }


    // 아이템 하나 클릭되면 모달 바텀시트 띄우기 -> 기존데이터 가져가서 바인딩
    override fun onGuideItemClicked(guide: GuideItem) {
        val view: View = layoutInflater.inflate(R.layout.fragment_guide_edit, null)
        img = view.findViewById(R.id.guide_detailed_image)
        val guideName: EditText = view.findViewById(R.id.guide_detailed_name)

        // 클릭된 가이드 아이템의 데이터 Set
        img.setImageResource(guide.gImage)
        guideName.setText(guide.gName)

        // 이미지뷰 클릭시 이미지 핸들러
        img.setOnClickListener {
            handleImageClick()
        }

        val dialog = BottomSheetDialog(requireContext())
        dialog.setContentView(view)
        dialog.show()
    }

    // 이미지 클릭 핸들러
    private fun handleImageClick() {
        val view: View = layoutInflater.inflate(R.layout.bottom_sheet_image_picker, null)
        val galleryOption: TextView = view.findViewById(R.id.text_gallery)
        val closeOption: TextView = view.findViewById(R.id.text_close)

        val dialog = BottomSheetDialog(requireContext())
        dialog.setContentView(view)
        dialog.show()

        // 갤러리에서 가져오기 클릭
        galleryOption.setOnClickListener {
            openGallery()
            dialog.dismiss()
        }

        // 닫기 클릭
        closeOption.setOnClickListener {
            dialog.dismiss()
        }
    }

    // 갤러리 열기
    private fun openGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryLauncher.launch(galleryIntent)
    }


    private fun handleGalleryResult(data: Intent?) {
        data?.data?.let { uri ->
            // 선택한 이미지를 이미지뷰에 설정
            img.setImageURI(uri)
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