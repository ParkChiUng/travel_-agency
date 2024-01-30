package com.sessac.travel_agency.fragment

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sessac.travel_agency.R
import com.sessac.travel_agency.adapter.GuideAdapter
import com.sessac.travel_agency.common.CommonHandler
import com.sessac.travel_agency.data.GuideItem
import com.sessac.travel_agency.databinding.FragmentGuideBinding
import com.sessac.travel_agency.viewmodels.GuideViewModel
import kotlinx.coroutines.launch

// GuideFragment 클래스가 OnGuideItemClickListener를 구현하도록 함
class GuideFragment : Fragment() {
    // 리사이클러뷰
    private lateinit var recyclerView: RecyclerView
    private var guideAdapter: GuideAdapter? = null

    private lateinit var binding: FragmentGuideBinding

    private lateinit var commonHandler: CommonHandler

    private lateinit var guideAddView: View
    private lateinit var guideDetailView: View
    private lateinit var selectGalleryView: View

    private lateinit var imageView: ImageView
    private lateinit var addButton: Button
    private lateinit var guideName: EditText

    private var selectImageUri: Uri? = null

    private val viewModel: GuideViewModel by viewModels()

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

        commonHandler = CommonHandler()
        commonHandler.imageCallback(requireActivity().activityResultRegistry)
        guideAddView = layoutInflater.inflate(R.layout.fragment_guide_add, null)
        guideDetailView = layoutInflater.inflate(R.layout.fragment_guide_edit, null)
        selectGalleryView = layoutInflater.inflate(R.layout.bottom_sheet_image_picker, null)

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.findAllGuideList()
            }
        }

        setupRecyclerviewAdapter()  // 리사이클러뷰
        setupFloatingButton()  // 플로팅버튼
        setupObserver()
    }

    private fun setupObserver() {
        viewModel.guideLists.observe(viewLifecycleOwner) { guides ->
            guides.let {
                guideAdapter?.setGuideList(it)
            }
        }
    }

    //플로팅버튼(가이드 등록)
    private fun setupFloatingButton() {
        binding.fab.setOnClickListener {
            commonHandler.showDialog(guideAddView, requireContext())
            newGuideButtonListener()
        }
    }

    private fun newGuideButtonListener() {
        imageView = guideAddView.findViewById(R.id.guide_new_image)
        addButton = guideAddView.findViewById(R.id.button_newGuide)
        guideName = guideAddView.findViewById(R.id.guide_new_name)

        imageView.setOnClickListener {
            handleImageClick(imageView)
        }

        addButton.setOnClickListener {
            viewModel.insertGuide(
                GuideItem(
                    gName = guideName.text.toString(),
                    gImage = selectImageUri.toString()
                )
            )
            selectImageUri = null
            commonHandler.dismissDialog(guideAddView)
        }
    }


    // 이미지 클릭 핸들러
    private fun handleImageClick(imageView: ImageView) {
        val galleryOption: TextView = selectGalleryView.findViewById(R.id.text_gallery)
        val closeOption: TextView = selectGalleryView.findViewById(R.id.text_close)

        commonHandler.showDialog(selectGalleryView, requireContext())

        // 갤러리에서 가져오기 클릭
        galleryOption.setOnClickListener {
            commonHandler.imageSelect { imageUri ->
                selectImageUri = imageUri
                imageView.setImageURI(imageUri)
            }

            commonHandler.dismissDialog(selectGalleryView)
        }

        // 닫기 클릭
        closeOption.setOnClickListener {
            commonHandler.dismissDialog(selectGalleryView)
        }
    }

    private fun setupRecyclerviewAdapter() {
        recyclerView = binding.guideRecyclerview
        recyclerView.setHasFixedSize(true) // 리사이클러뷰의 크기가 변할 일이 없음 명시. 리사이클러뷰 레이아웃 다시 잡을 필요 없이 아이템 자리만 다시 잡기
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        // 아이템 클릭 시 디테일 BottomSheet에 이미지, 이름 저장
        guideAdapter = GuideAdapter { guide ->
            imageView = guideDetailView.findViewById(R.id.guide_detailed_image)
            guideName = guideDetailView.findViewById(R.id.guide_detailed_name)
            val deleteButton: Button = guideDetailView.findViewById(R.id.button_delGuide)
            val updateButton: Button = guideDetailView.findViewById(R.id.button_editGuide)

            Glide.with(imageView.context)
                .load(guide.gImage)
                .into(imageView)

            guideName.setText(guide.gName)

            imageView.setOnClickListener {
                handleImageClick(imageView)
            }

            deleteButton.setOnClickListener {
                viewModel.deleteGuide(guide.guideId)
                commonHandler.dismissDialog(guideDetailView)
            }

            updateButton.setOnClickListener {

                guideName = guideDetailView.findViewById(R.id.guide_detailed_name)

                viewModel.updateGuide(
                    GuideItem(
                        guideId = guide.guideId,
                        gName = guideName.text.toString(),
                        gImage = if (selectImageUri == null) guide.gImage else selectImageUri.toString()
                    )
                )

                selectImageUri = null

                commonHandler.dismissDialog(guideDetailView)
            }

            commonHandler.showDialog(guideDetailView, requireContext())
        }

        recyclerView.adapter = guideAdapter
    }
}