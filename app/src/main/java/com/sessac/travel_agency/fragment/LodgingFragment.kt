package com.sessac.travel_agency.fragment

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputLayout
import com.sessac.travel_agency.adapter.LodgingAdapter
import com.sessac.travel_agency.R
import com.sessac.travel_agency.common.CommonHandler
import com.sessac.travel_agency.data.LodgingItem
import com.sessac.travel_agency.databinding.BottomSheetImagePickerBinding
import com.sessac.travel_agency.databinding.FragmentLodgingAddBinding
import com.sessac.travel_agency.databinding.FragmentLodgingBinding
import com.sessac.travel_agency.databinding.FragmentLodgingEditBinding
import com.sessac.travel_agency.viewmodels.LodgingViewModel
import kotlinx.coroutines.launch


class LodgingFragment : Fragment() {
    private lateinit var lodgingBinding: FragmentLodgingBinding
    private lateinit var lodgingAddViewBinding: FragmentLodgingAddBinding
    private lateinit var lodgingDetailViewBinding: FragmentLodgingEditBinding
    private lateinit var galleryViewBinding: BottomSheetImagePickerBinding

    private lateinit var lodgingRecyclerView: RecyclerView
    private var lodgingAdapter: LodgingAdapter? = null

    private lateinit var commonHandler: CommonHandler

    private var selectImageUri: Uri? = null
    private lateinit var imageView: ImageView
    private lateinit var addButton: Button
    private lateinit var lodgingName: EditText
    private var location: String = ""
    private lateinit var areaList: Array<String>

    private lateinit var spinner: Spinner

    private val lodgingViewModel: LodgingViewModel by viewModels()


    // Fragment의 레이아웃 생성
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        lodgingBinding = FragmentLodgingBinding.inflate(inflater, container, false)
        lodgingAddViewBinding = FragmentLodgingAddBinding.inflate(inflater, container, false)
        lodgingDetailViewBinding = FragmentLodgingEditBinding.inflate(inflater, container, false)
        galleryViewBinding = BottomSheetImagePickerBinding.inflate(inflater, container, false)
        return lodgingBinding.root
    }

    /**
     * 1. commonHandler.imageCallBack -> 이미지 select Callback 등록
     * 2. viewCreate 후 lodging 전체 리스트 호출
     * 3. 각 setup 함수 등록
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        commonHandler = CommonHandler.generateCommonHandler()
        commonHandler.imageCallback(requireActivity().activityResultRegistry)

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                lodgingViewModel.findAllLodgingList()
            }
        }

        lodgingBinding.areaListSpinner.onItemSelectedListener

        setupAreaSpinnerUI(lodgingBinding)
        setupRecyclerviewAdapter()
        setupFloatingButton()
        setupObserver()
        setupLocationSpinnerHandler()
    }

    /**
     * 지역 선택 스피너 지역 세팅
     * */
    private fun setupAreaSpinnerUI(binding: ViewBinding) {
        areaList = resources.getStringArray(R.array.select_region)
        val areaText: AutoCompleteTextView = when (binding) {
            is FragmentLodgingBinding -> lodgingBinding.areaListSpinner
            is FragmentLodgingEditBinding -> lodgingDetailViewBinding.areaListSpinner
            is FragmentLodgingAddBinding -> lodgingAddViewBinding.areaListSpinner
            else -> return
        }
        commonHandler.spinnerHandler(areaList, areaText, requireContext())
    }



    /**
     * 숙소등급 선택 스피너 등급 세팅
     * */
    private fun setupStarSpinnerUI(binding: ViewBinding) {
        val starList = resources.getStringArray(R.array.select_star)
        val starText: AutoCompleteTextView = when (binding) {
            is FragmentLodgingEditBinding -> lodgingDetailViewBinding.lodgingStarList
            is FragmentLodgingAddBinding -> lodgingAddViewBinding.lodgingStarList
            else -> return
        }
        commonHandler.spinnerHandler(starList, starText, requireContext())
    }


    /**
     * [지역 스피너 버튼 이벤트 처리]
     * 1. 지역 선택 ClickListener 등록
     * 2. 지역 선택 시 viewModel의 findLodgingsByArea() 함수에서 lodgingList 초기화
     */
    private fun setupLocationSpinnerHandler() { // TODO: 분기처리 깔끔하게

        val areaList = resources.getStringArray(R.array.select_region)

        // 어레이 어댑터
        val areaSpinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, areaList)

        // AutoCompleteTextView에 어댑터 세팅
        lodgingBinding.areaListSpinner.setAdapter(areaSpinnerAdapter)
        lodgingAddViewBinding.areaListSpinner.setAdapter(areaSpinnerAdapter)
        lodgingDetailViewBinding.areaListSpinner.setAdapter(areaSpinnerAdapter)

        // AutoCompleteTextView에 아이템 클릭리스너 세팅
        val onItemClickListener = AdapterView.OnItemClickListener { parent, _, position, _ ->
            // 어레이 어댑터에서 선택된 아이템 가져옴
            val selectedArea = areaSpinnerAdapter.getItem(position)
            location = selectedArea.toString()

            when (parent) {
                lodgingBinding.areaListSpinner -> {
                    Log.d("test", "Selected Area: $selectedArea")
                    // ViewModel을 통해 해당 지역에 대한 숙소 데이터 가져오기(lodgingBinding 스피너일때만)
                    lodgingViewModel.findLodgingsByArea(selectedArea.toString())
                }
                lodgingAddViewBinding.areaListSpinner -> {
                    Log.d("test새 아이템 등록", "Selected Area: $selectedArea")
                    // Handle for lodgingAddViewBinding
                }
                lodgingDetailViewBinding.areaListSpinner -> {
                    Log.d("test아이템 디테일", "Selected Area: $selectedArea")
                    // Handle for lodgingDetailViewBinding
                }
            }
        }

        lodgingBinding.areaListSpinner.setOnItemClickListener(onItemClickListener)
        lodgingAddViewBinding.areaListSpinner.setOnItemClickListener(onItemClickListener)
        lodgingDetailViewBinding.areaListSpinner.setOnItemClickListener(onItemClickListener)

        // ViewModel에서 LiveData를 통해 반환된 숙소 데이터를 관찰하고, 변경이 감지되면 UI 업데이트
        lodgingViewModel.lodgingList.observe(viewLifecycleOwner) { lodgings ->
            lodgings?.let {
                // lodgings를 사용하여 RecyclerView 업데이트
                lodgingAdapter?.setLodgingList(it)
            }
        }
    }



    /**
     * [스타 스피너 버튼 이벤트 처리]
     * TODO : 기본 스피너가 아닌 setupLocationSpinnerHandler처럼 ArrayAdapter 이용 AutoCompleteTextView에 어댑터 세팅으로 바꾸기
     *
     * */
    private fun setupStarSpinnerHandler(binding: ViewBinding): Int {
        var star = 5

        val starList = resources.getStringArray(R.array.select_star)
        val starSpinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, starList)

        when (binding) {
            is FragmentLodgingAddBinding -> {
                binding.lodgingStarList.setAdapter(starSpinnerAdapter)
                binding.lodgingStarList.setOnItemClickListener { parent, _, position, _ ->
                    star = parent.getItemAtPosition(position).toString().toInt()
                }
            }
            is FragmentLodgingEditBinding -> {
                binding.lodgingStarList.setAdapter(starSpinnerAdapter)
                binding.lodgingStarList.setOnItemClickListener { parent, _, position, _ ->
                    star = parent.getItemAtPosition(position).toString().toInt()
                }
            }
        }

        return star
    }




    /**
     * [lodging 리스트 observe]
     * viewModel의 findAllLodgingList() 함수에서 lodgingList 초기화
     *
     * 1. lodgingList가 변하면 (숙소 삭제, 등록, 수정 시 ) RecyclerView의 리스트를 다시 생성
     * 2. 스피너 지역 선택시 RecyclerView의 리스트를 다시 생성
     */
    private fun setupObserver() {
        lodgingViewModel.lodgingList.observe(viewLifecycleOwner) { lodgings ->
            lodgings.let {
                lodgingAdapter?.setLodgingList(it) // 여기!! LodgingAdapter에 정의
            }
        }
    }


    /**
     * [플로팅 버튼 이벤트 처리]
     *
     * 1. 플로팅 버튼 ClickListener 등록
     * 2. 플로팅 버튼 클릭 시 숙소 등록 bottomSheet 표출
     * 3. 지역, 숙소 등급 스피너의 리스트 세팅
     */
    private fun setupFloatingButton() {
        lodgingBinding.fab.setOnClickListener {
            commonHandler.showDialog(lodgingAddViewBinding.root, requireContext())
            setupAreaSpinnerUI(lodgingAddViewBinding)
            setupStarSpinnerUI(lodgingAddViewBinding)
            newLodgingButtonListener()
        }
    }



    /**
     * [bottomSheet 이벤트 처리]
     *
     * 1. 이미지 뷰 이벤트 처리
     * 2. 등록 버튼 클릭 시 lodging 생성
     * 3. selectImageUri -> 갤러리에서 선택한 이미지이다. lodging을 insert하고 꼭 null로 초기화 해줘야한다.
     * 4. lodging insert 완료 시 이미지, 숙소명, 지역, 숙소등급 레이아웃 초기화 후 bottomSheet dismiss 처리
     */
    private fun newLodgingButtonListener() {
        with(lodgingAddViewBinding) {
            imageView = lodgingNewImage
            lodgingName = lodgingNewName
            addButton = buttonNewLodging
            imageView.setOnClickListener {
                handleImageClick(imageView)
            }

            addButton.setOnClickListener {
                // 여기에서 setupStarSpinnerHandler 메서드를 사용하여 선택된 등급을 가져옴
                val selectedStar = setupStarSpinnerHandler(lodgingAddViewBinding)
                lodgingViewModel.insertLodging(
                    LodgingItem(
                        area = location,
                        lName = lodgingName.text.toString(),
                        lImage = selectImageUri.toString(),
                        starNum = selectedStar)
                )
                imageView.setImageResource(R.drawable.ic_package)
                lodgingName.setText("")
                selectImageUri = null
                //TODO: 레이팅바 숙소등급, 지역
                commonHandler.dismissDialog(root)
            }
        }
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

            textGallery.setOnClickListener {
                commonHandler.imageSelect { imageUri ->
                    selectImageUri = imageUri
                    imageView.setImageURI(imageUri)
                }
                commonHandler.dismissDialog(root)
            }

            textClose.setOnClickListener {
                commonHandler.dismissDialog(root)
            }
        }
    }


    /**
     * [숙소 RecyclerView]
     *
     * 1. 리사이클러 뷰 등록 및 레이아웃 설정(지역 스피너 리스트 세팅)
     * 2. 리사이클러 뷰의 숙소 클릭 시 LodgingAdapter로 lodging정보 콜백
     * 3. 숙소 상세 정보 bottomSheet 표출
     * 4. 숙소 수정, 삭제 이벤트 처리
     */
    private fun setupRecyclerviewAdapter() {
        setupAreaSpinnerUI(lodgingDetailViewBinding)  // 지역 스피너
        setupStarSpinnerUI(lodgingDetailViewBinding) // 숙소 등급 스피너
        lodgingRecyclerView = lodgingBinding.lodgingRecyclerview
        with(lodgingRecyclerView) {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
        }

        lodgingAdapter = LodgingAdapter { lodging ->
            with(lodgingDetailViewBinding) {
                imageView = lodgingDetailedImage
                lodgingName = lodgingDetailedName

                Glide.with(imageView.context)
                    .load(lodging.lImage)
                    .into(imageView)

                lodgingName.setText(lodging.lName)

                imageView.setOnClickListener {
                    handleImageClick(imageView)
                }

                buttonDelLodging.setOnClickListener {
                    lodgingViewModel.deleteLodging(lodging)
                    commonHandler.dismissDialog(root)
                }

                buttonEditLodging.setOnClickListener {
                    lodgingName = lodgingDetailedName
                    // 여기에서 setupStarSpinnerHandler 메서드를 사용하여 선택된 등급을 가져옴
                    val selectedStar = setupStarSpinnerHandler(lodgingAddViewBinding)
                    lodgingViewModel.updateLodging(
                        LodgingItem(
                            lodgeId = lodging.lodgeId,
                            area = location,  // 등록창의 스피너에서 선택된 지역은 findLodgingsByArea작업 필요없음
                            lName = lodgingName.text.toString(),
                            lImage = if (selectImageUri == null) lodging.lImage else selectImageUri.toString(),
                            starNum = selectedStar
                        )
                    )
                    selectImageUri = null
                    commonHandler.dismissDialog(root)
                }

                commonHandler.showDialog(root, requireContext())
            }
        }

        lodgingRecyclerView.adapter = lodgingAdapter
    }

    // 프래그먼트 화면을 벗어나면 기존 선택된 데이터 제거.
    override fun onPause() {
        super.onPause()

        // 기존에 선택된 지역을 제거하여 화면전환 후 돌아올시 새롭게 보이도록하기위함
        lodgingBinding.areaListSpinner.text = null

    }

}