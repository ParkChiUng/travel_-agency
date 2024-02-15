package com.sessac.travel_agency.fragment

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.net.toUri
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
import com.sessac.travel_agency.data.GuideItemFireStore
import com.sessac.travel_agency.databinding.BottomSheetImagePickerBinding
import com.sessac.travel_agency.databinding.FragmentGuideAddBinding
import com.sessac.travel_agency.databinding.FragmentGuideBinding
import com.sessac.travel_agency.databinding.FragmentGuideEditBinding
import com.sessac.travel_agency.viewmodels.GuideViewModel
import kotlinx.coroutines.launch

/**
 *
 */
//tutor pyo
class GuideFragment : Fragment() {
    private lateinit var guideRecyclerView: RecyclerView
    private var guideAdapter: GuideAdapter? = null
    private lateinit var guideBinding: FragmentGuideBinding
    private lateinit var guideAddViewBinding: FragmentGuideAddBinding
    private lateinit var guideDetailViewBinding: FragmentGuideEditBinding
    private lateinit var galleryViewBinding: BottomSheetImagePickerBinding
    private lateinit var commonHandler: CommonHandler

   // private lateinit var imageView: ImageView
    private lateinit var addButton: Button
    private lateinit var guideName: EditText
    private var selectImageUri: Uri? = null

    private val viewModel: GuideViewModel by viewModels()

    /**
     * 레이아웃 바인딩 설정
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        guideBinding = FragmentGuideBinding.inflate(inflater, container, false)
        guideAddViewBinding = FragmentGuideAddBinding.inflate(inflater, container, false)
        guideDetailViewBinding = FragmentGuideEditBinding.inflate(inflater, container, false)
        galleryViewBinding = BottomSheetImagePickerBinding.inflate(inflater, container, false)
        return guideBinding.root
    }

    /**
     * 1. commonHandler.imageCallBack -> 이미지 select Callback 등록
     * 2. viewCreate 후 guide 전체 리스트 호출
     * 3. 각 setup 함수 등록
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        commonHandler = CommonHandler.generateCommonHandler()

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.findAllGuideList()
            }
        }

        setupRecyclerviewAdapter()
        setupFloatingButton()
        setupObserver()
    }

    /**
     * [guide 리스트 observe]
     * viewModel의 findAllGuideList() 함수에서 guideLists 초기화
     *
     * 1. guideLists가 변하면 (가이드 삭제, 등록, 수정 시 ) RecyclerView의 리스트를 다시 생성
     */
    private fun setupObserver() {
        viewModel.guideLists.observe(viewLifecycleOwner) { guides ->
            guides.let {
                if(guides.isNotEmpty()){
//                    guideAdapter?.setGuideList(it)
                    guideAdapter?.setGuideList(it)
                    //tutor pyo
                    guideBinding.emptyView.visibility = View.GONE
                    guideBinding.guideRecyclerview.visibility = View.VISIBLE
                }else{
                    //tutor pyo
                    guideBinding.emptyView.visibility = View.VISIBLE
                    guideBinding.guideRecyclerview.visibility = View.GONE
                }
            }
        }
    }

    /**
     * [플로팅 버튼 이벤트 처리]
     *
     * 1. 플로팅 버튼 ClickListener 등록
     * 2. 플로팅 버튼 클릭 시 가이드 등록 bottomSheet 표출
     */
    private fun setupFloatingButton() {
        guideBinding.fab.setOnClickListener {
            commonHandler.showDialog(guideAddViewBinding.root, requireContext())
            newGuideButtonListener()
        }
    }

    /**
     * [bottomSheet 이벤트 처리]
     *
     * 1. 이미지 뷰 이벤트 처리
     * 2. 등록 버튼 클릭 시 guide 생성
     * 3. selectImageUri -> 갤러리에서 선택한 이미지이다. guide를 insert하고 꼭 null로 초기화 해줘야한다.
     * 4. guide insert 완료 시 이미지, 가이드 이름 레이아웃 초기화 후 bottomSheet dismiss 처리
     */
    private fun newGuideButtonListener() {
        with(guideAddViewBinding) {
            //imageView = guideNewImage
            guideName = guideNewName
            addButton = buttonNewGuide

            guideNewImage.setOnClickListener {
                handleImageClick(guideNewImage)
            }
            addButton.setOnClickListener {
                guideName = guideNewName
                if(isValid()) {
                    viewModel.insertGuide(
//                        GuideItem(
//                            gName = guideName.text.toString(),
//                            gImage = selectImageUri.toString()
//                        )
                        GuideItemFireStore(
                            gName = guideName.text.toString(),
                            gImage = selectImageUri.toString()
                        )
                    )
                    guideNewImage.setImageResource(R.drawable.ic_guide)
                    guideName.setText("")
                    selectImageUri = null
                    commonHandler.dismissDialog(root)
                }
            }
        }
    }

    /**
     * [유효성 체크]
     * 가이드 등록 버튼 클릭 시, 수정 버튼 클릭 시 호출
     *
     * 1. selectImageUri -> 갤러리에서 이미지 선택 시, 가이드 상세 클릭 시 uri 할당한다.
     *                      가이드 등록 시 이미지 선택을 안했을 때만 null
     *
     * 2. guideName -> 가이드 등록 버튼 클릭 시, 수정 버튼 클릭 시 기입한 가이드 이름을 guideName에 할당한다. null이라면 기입 안한 것
     */
    private fun isValid():Boolean{
        var check = true
        if (selectImageUri == null) {
            Toast.makeText(context, "이미지를 선택해주세요.", Toast.LENGTH_SHORT).show()
            check = false
        }
        if (guideName.text.toString() == "") {
            Toast.makeText(context, "가이드 이름을 입력해주세요.", Toast.LENGTH_SHORT).show()
            check = false
        }
        return check
    }


    /**
     * [이미지 클릭 핸들러]
     *
     * @param imageView bottomSheet의 imageView ( 이미지 콜백 후 imageView에 이미지 등록을 위해 사용 )
     *
     * 1. 이미지 클릭 시 갤러리 선택 bottomSheet 표출
     * 2. imageUri 갤러리에서 이미지 선택 시 commonHandler.imageSelect에서 imageUri를 콜백받는다.
     */
    private fun handleImageClick(imageView: ImageView) {
        with(galleryViewBinding) {

            commonHandler.showDialog(root, requireContext())

            // 갤러리에서 가져오기 클릭
            textGallery.setOnClickListener {
                commonHandler.imageSelectAndCallback(requireActivity().activityResultRegistry) { imageUri ->
                    selectImageUri = imageUri
                    imageView.setImageURI(imageUri)
                }

                commonHandler.dismissDialog(root)
            }

            // 닫기 클릭
            textClose.setOnClickListener {
                commonHandler.dismissDialog(root)
            }
        }
    }

    /**
     * [가이드 RecyclerView]
     *
     * 1. 리사이클러 뷰 등록 및 레이아웃 설정
     * 2. 리사이클러 뷰의 가이드 클릭 시 GuideAdapter로 guide정보 콜백
     * 3. 가이드 상세 정보 bottomSheet 표출
     * 4. 가이드 수정, 삭제 이벤트 처리
     */

    private fun setupRecyclerviewAdapter() {
        guideRecyclerView = guideBinding.guideRecyclerview
        with(guideRecyclerView) {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(requireContext(), 2)
        }
        guideAdapter = GuideAdapter { guide ->
            with(guideDetailViewBinding) {
                //imageView = guideDetailedImage
                guideName = guideDetailedName

                Glide.with(guideDetailedImage.context)
                    .load(guide.gImage)
                    .into(guideDetailedImage)

                guideName.setText(guide.gName)

                guideDetailedImage.setOnClickListener {
                    handleImageClick(guideDetailedImage)
                }

                buttonDelGuide.setOnClickListener {
                    viewModel.deleteGuide(guide.guideId)
                    commonHandler.dismissDialog(root)
                }

                buttonEditGuide.setOnClickListener {
                    selectImageUri = guide.gImage.toUri()
                    guideName = guideDetailedName
                    if(isValid()) {
                        viewModel.updateGuide(
//                            GuideItem(
//                                guideId = guide.guideId,
//                                gName = guideName.text.toString(),
//                                gImage = selectImageUri.toString()
//                            )
                            GuideItemFireStore(
                                guideId = guide.guideId,
                                gName = guideName.text.toString(),
                                gImage = selectImageUri.toString()
                            )
                        )
                        guideName.setText("")
                        selectImageUri = null
                        commonHandler.dismissDialog(root)
                    }
                }
                commonHandler.showDialog(root, requireContext())
            }
        }

        guideRecyclerView.adapter = guideAdapter
    }
}