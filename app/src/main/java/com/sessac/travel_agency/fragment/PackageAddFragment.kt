package com.sessac.travel_agency.fragment

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.datepicker.MaterialDatePicker
import com.sessac.travel_agency.R
import com.sessac.travel_agency.adapter.PackageAdapter
import com.sessac.travel_agency.adapter.ScheduleAdapter
import com.sessac.travel_agency.common.CommonHandler
import com.sessac.travel_agency.data.GuideScheduleItem
import com.sessac.travel_agency.data.LodgingItem
import com.sessac.travel_agency.data.PackageItem
import com.sessac.travel_agency.data.ScheduleItem
import com.sessac.travel_agency.databinding.BottomSheetImagePickerBinding
import com.sessac.travel_agency.databinding.FragmentPackageAddBinding
import com.sessac.travel_agency.databinding.FragmentPackageScheduleAddBinding
import com.sessac.travel_agency.viewmodels.PackageViewModel
import kotlinx.coroutines.launch
import java.util.Date

//tutor pyo 주석
class PackageAddFragment : Fragment(){
    private lateinit var packageBinding: FragmentPackageAddBinding
    private lateinit var scheduleBinding: FragmentPackageScheduleAddBinding
    private lateinit var galleryViewBinding: BottomSheetImagePickerBinding
    private lateinit var bottomNav: BottomNavigationView

    private lateinit var packageAdapter: PackageAdapter
    private lateinit var scheduleAdapter: ScheduleAdapter
    private lateinit var guideSpinnerAdapter: ArrayAdapter<String>
    private lateinit var lodgingSpinnerAdapter: ArrayAdapter<String>

    private lateinit var packageRecyclerView: RecyclerView
    private var commonHandler = CommonHandler

    private var scheduleList = mutableListOf<ScheduleItem>()

    private lateinit var scheduleItem: ScheduleItem
    private lateinit var lodgingItem: LodgingItem

    private var guideId: Int = 0
    private var guideName: String = ""
    private var packageId: Int = 0

    private var lodgingId: Int = 0
    private var schedulePosition: Int = 0

    private var selectImageUri: Uri? = null
    private var startDate: Date = Date()
    private var endDate: Date = Date()
    private var calculatorDate: Int = 0

    private var parcelablePackageItem: PackageItem? = null

    private val TAG = "PackageAddFragment"

    private val viewModel: PackageViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        packageBinding = FragmentPackageAddBinding.inflate(inflater, container, false)
        scheduleBinding = FragmentPackageScheduleAddBinding.inflate(inflater, container, false)
        galleryViewBinding = BottomSheetImagePickerBinding.inflate(inflater, container, false)

        return packageBinding.root
    }
    @Suppress("deprecation")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        commonHandler = CommonHandler.generateCommonHandler()

        parcelablePackageItem = if(Build.VERSION.SDK_INT > Build.VERSION_CODES.TIRAMISU){
            arguments?.getParcelable("packageItem", PackageItem::class.java)
        }else{
            arguments?.getParcelable("packageItem")!!
        }

        initScheduleRecyclerView()
        setAreaSpinner()
        removeBottomNav()
        setupObserver()
        clickEventHandler()

        parcelablePackageItem?.let {
            Log.d(TAG, "onViewCreated: ${it.pImage}")
            initData(it)
        }
    }

    /**
     *  parcelablePackageItem 이 null이 아니면 수정 페이지
     *  각 화면에 가져온 값들 저장
     */
    private fun initData(packageItem: PackageItem) {
//        scheduleAdapter.setEditMode(true)

        with(packageBinding) {
            Glide.with(packageAddImage.context)
                .load(packageItem.pImage)
                .into(packageAddImage)

            packageId = packageItem.packageId
            guideId = packageItem.guideId
            selectImageUri = packageItem.pImage.toUri()
            startDate = packageItem.pStartDate
            endDate = packageItem.pEndDate

            packageAddNameET.setText(packageItem.pName)
            areaSpinner.setText(packageItem.area)
            guideSpinner.setText(packageItem.guideName)
            packageAddDateET.setText(commonHandler.dateHandler(startDate, endDate))
            textRegister.text = getString(R.string.alert_title_update)
            buttonAddPackage.text = getString(R.string.alert_title_update)

            calculatorDate = commonHandler.dayCalculator(startDate, endDate)
            scheduleAdapter.scheduleDay(calculatorDate)

            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.findSchedule(packageId)
            }
        }
    }

    private fun clickEventHandler() {
        with(packageBinding) {

            /**
             * 날짜 editText 클릭 시
             */
            packageAddDateET.setOnClickListener {
                calendarHandler()
            }

            // Toolbar의 Navigation Icon 클릭 시 뒤로 가기 동작 설정
            toolbar.setNavigationOnClickListener {
                commonHandler.alertDialog(requireContext(), "warning") {
                    findNavController().popBackStack()  // yes클릭시 뒤로 화면전환 됨
                }
            }

            /**
             * imageView 클릭 시 동작
             */
            packageAddImage.setOnClickListener {
                imageClickHandler(packageAddImage)
            }

            /**
             *  parcelablePackageItem null이면 수정, 아니면 추가
             *  parcelablePackageItem은 packageTap Fragment들에서 Parcelble로 받는다.
             */
            if (parcelablePackageItem == null) {
                buttonAddPackage.setOnClickListener {
                    if (isValid()) {
                        viewModel.insertPackage(
                            PackageItem(
                                guideName = guideName,
                                guideId = guideId,
                                area = areaSpinner.text.toString(),
                                pName = packageAddNameET.text.toString(),
                                pImage = selectImageUri.toString(),
                                pStartDate = startDate,
                                pEndDate = endDate
                            )
                        )
                    }
                }
            } else {
                buttonAddPackage.setOnClickListener {
                    if (isValid()) {
                        viewModel.updatePackage(
                            PackageItem(
                                packageId = packageId,
                                guideName = guideSpinner.text.toString(),
                                guideId = guideId,
                                area = areaSpinner.text.toString(),
                                pName = packageAddNameET.text.toString(),
                                pImage = selectImageUri.toString(),
                                pStartDate = startDate,
                                pEndDate = endDate,
                            )
                        )
                        viewModel.updateGuideSchedule(
                            GuideScheduleItem(
                                guideId = guideId,
                                packageId = packageId,
                            )
                        )
                        findNavController().popBackStack()
                    }
                }
            }

            /**
             * 가이드 스피너 클릭 시
             * 만약 날짜를 선택 안하면 Toast 메시지 출력
             */
            guideSpinner.setOnClickListener {
                if (startDate.before(Date())) {
                    Toast.makeText(context, "날짜를 선택해주세요.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    /**
     * [유효성 체크]
     * 패키지 등록, 수정 버튼 클릭 시 호출
     */
    private fun isValid(): Boolean {
        var check = true
        with(packageBinding) {

            Log.d(TAG, "isValid1: ${scheduleList.size}")
            Log.d(TAG, "isValid2: $calculatorDate")

            if (selectImageUri == null) {
                commonHandler.commonToast("이미지를 선택해주세요.")
                check = false
            }
            if (packageAddNameET.text.toString() == "") {
                Toast.makeText(context, "패키지 이름을 입력해주세요.", Toast.LENGTH_SHORT).show()
                check = false
            }
            if (areaSpinner.text.isBlank()) {
                Toast.makeText(context, "지역을 선택해주세요.", Toast.LENGTH_SHORT).show()
                check = false
            }
            if (guideName.isBlank()) {
                Toast.makeText(context, "가이드를 선택해주세요.", Toast.LENGTH_SHORT).show()
                check = false
            }

            if(scheduleList.size != calculatorDate){
                Toast.makeText(context, "일정을 추가해주세요.", Toast.LENGTH_SHORT).show()
                check = false
            }
//            if (startDate.before(Date())){
//                Toast.makeText(context, "올바른 날짜를 선택해주세요.", Toast.LENGTH_SHORT).show()
//                check = false
//            }
        }
        return check
    }

    private fun isValidBottomSheet(): Boolean {
        var check = true
        with(scheduleBinding) {
            if (themeSpinner.text.toString() == "") {
                Toast.makeText(context, "테마를 선택해주세요.", Toast.LENGTH_SHORT).show()
                check = false
            }
            if (lodgingSpinner.text.isBlank()) {
                Toast.makeText(context, "숙소를 선택해주세요.", Toast.LENGTH_SHORT).show()
                check = false
            }
            if (scheduleDescription.text.toString() == "") {
                Toast.makeText(context, "설명을 작성해주세요.", Toast.LENGTH_SHORT).show()
                check = false
            }
        }
        return check
    }

    /**
     * [package 리스트 observe]
     * viewModel의 findPackage() 함수에서 packageLists 초기화
     *
     * 1. packageLists 변하면 (패키지 삭제, 등록, 수정 시 ) RecyclerView의 리스트를 다시 생성
     */
    private fun setupObserver() {

        viewModel.packageLists.observe(viewLifecycleOwner) { packageList ->
            packageList.let {
                packageAdapter.setPackageList(it)
            }
        }

        /**
         * 날짜 선택 시 동작
         * 0. 날짜 선택하면 해당 날짜에 스케줄이 없는 가이드만 룸에서 가져와서 라이브 데이터에 넣어줌
         * 1. 가이드 없으면 Toast 메세지 띄우고 나머지 코드 실행하지 않고 observe 종료
         * 2. 가이드 있으면 가이드 리스트 spinner에 추가
         * 3. 클릭한 가이드 이름, db에 저장되어 있는 id 저장
         */
        viewModel.guideLists.observe(viewLifecycleOwner) { guideList ->
            with(packageBinding) {
                if (guideList.isEmpty()) {
                    guideSpinner.setText("해당 날짜에 가능한 가이드가 없습니다.")
                    Toast.makeText(context, "해당 날짜에 가능한 가이드가 없습니다.", Toast.LENGTH_SHORT).show()
                    return@observe
                } else
                    guideSpinner.setText("")

                commonHandler.spinnerHandler(
                    guideList.map {
                        it.gName
                    }.toTypedArray(),
                    guideSpinner
                )

                guideSpinnerAdapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_dropdown_item_1line,
                    guideList.map {
                        it.gName
                    }.toTypedArray(),
                )

                guideSpinner.setOnItemClickListener { _, _, position, _ ->
                    guideName = guideList[position].gName
                    guideId = guideList[position].guideId
                }
            }
        }

        /**
         * 스케줄 addButton 클릭 시 동작
         * 0. 선택한 지역으로 숙소 정보 가져옴
         * 1. 숙소 없으면 Toast 메세지 띄우고 나머지 코드 실행하지 않고 observe 종료
         * 2. 숙소 있으면 숙소 리스트 spinner에 추가
         * 3. 클릭한 숙소 이름, db에 저장되어 있는 id 저장
         */
        viewModel.lodgingList.observe(viewLifecycleOwner) { lodgingList ->
            with(scheduleBinding) {
                if (lodgingList.isEmpty()) {
                    lodgingSpinner.setText("해당 지역의 숙소가 없습니다.")
                    Toast.makeText(context, "숙소 정보가 없습니다.", Toast.LENGTH_SHORT).show()
                    return@observe
                } else
                    lodgingSpinner.setText("")

                commonHandler.spinnerHandler(
                    lodgingList.map { it.lName }.toTypedArray(),
                    lodgingSpinner
                )

                lodgingSpinnerAdapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_dropdown_item_1line,
                    lodgingList.map { it.lName }.toTypedArray()
                )

                lodgingSpinner.setOnItemClickListener { _, _, position, _ ->
                    lodgingId = lodgingList[position].lodgeId
                }
            }
        }

        /**
         * 룸에 패키지 추가되면 동작
         * 패키지가 추가되면 packageId를 리턴하고 라이브 데이터 packageId를 변경한다.
         * insert된 패키지 id와 스피너에서 선택한 가이드의 id로 스케줄 저장
         */
        viewModel.packageId.observe(viewLifecycleOwner) { packageId ->
            viewModel.insertGuideSchedule(
                GuideScheduleItem(
                    guideId = guideId,
                    packageId = packageId.toInt(),
                )
            )

            for (scheduleItem in scheduleList) {
                scheduleItem.packageId = packageId.toInt()
                viewModel.insertSchedule(scheduleItem)
            }

            findNavController().popBackStack()
        }

        viewModel.scheduleList.observe(viewLifecycleOwner) { findScheduleList ->
            for (it in findScheduleList) {
                scheduleList.add(it)
                Log.d(TAG, "setupObserver: ${it.description}")
                viewLifecycleOwner.lifecycleScope.launch {
                    lodgingItem = viewModel.findLodgingReturn(it.lodgingId)
                    scheduleAdapter.setSchedule(it, lodgingItem, it.day)
                }
            }
        }

        viewModel.lodgingItem.observe(viewLifecycleOwner) { lodgingItem->
            scheduleAdapter.setSchedule(scheduleItem, lodgingItem, schedulePosition)
        }
    }

    /**
     * 이미지 클릭 시 동작
     * 0. 갤러리에서 가져오기, 취소 팝업 표출
     * 1. 갤러리에서 가져오기 클릭 시 갤러리 화면 표출
     * 2. 갤러리에서 선택한 이미지의 Uri를 콜백 받음
     * 3. 콜백 받은 imageUri 저장하고 setImageURI로 선택한 이미지 보여지게 함.
     */
    private fun imageClickHandler(imageView: ImageView) {
        with(galleryViewBinding) {

            commonHandler.showBottomSheet(root, requireContext())

            // 갤러리에서 가져오기 클릭
            textGallery.setOnClickListener {

                commonHandler.imageSelectAndCallback(requireActivity().activityResultRegistry) { imageUri ->
                    selectImageUri = imageUri
                    imageView.setImageURI(imageUri)
                }

                commonHandler.dismissBottomSheet(root)
            }

            // 닫기 클릭
            textClose.setOnClickListener {
                commonHandler.dismissBottomSheet(root)
            }
        }
    }

    /**
     * 0. dateRangePicker는 시작 날짜와 종료 날짜를 선택하는 함수
     * 1. 시작 날짜와 종료 날짜를 EditText에 표출
     * 2. dayCalculator 함수로 시작 날짜와 종료 날짜를 일수로 변경 후 scheduleAdapter로 전달
     * 3. 해당 날짜에 겹치는 가이드가 있는지 조회
     * 4. 조회한 리스트 라이브 데이터인 guideLists에 저장
     */
    private fun calendarHandler() {
        val dateRangePicker = MaterialDatePicker.Builder.dateRangePicker()
            .setTitleText("기간 선택")
            .setTheme(R.style.CustomCalendar)
            .build()

        dateRangePicker.show(childFragmentManager, dateRangePicker.toString())

        dateRangePicker.addOnPositiveButtonClickListener { selectDate ->
            startDate = Date(selectDate.first ?: 0)
            endDate = Date(selectDate.second ?: 0)

            packageBinding.packageAddDateET.setText(commonHandler.dateHandler(startDate, endDate))

            calculatorDate = commonHandler.dayCalculator(startDate, endDate)

            scheduleAdapter.scheduleDay(calculatorDate)

            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.findGuideSchedule(startDate, endDate)
            }
        }
    }

    private fun removeBottomNav() {
        // BottomNavigationView 초기화(바텀 네비 뷰 전환 클릭 이벤트 감지하기 위함)
        bottomNav =
            requireActivity().findViewById(R.id.bottomNav)  // 프래그먼트가 속한 액티비티requireActivity()에서 BottomNavigationView를 찾아 bottomNav 변수에 할당

        // BottomNavigationView를 숨김
        bottomNav.visibility = View.GONE
    }

    private fun setAreaSpinner() {
        commonHandler.spinnerHandler(
            resources.getStringArray(R.array.select_region),
            packageBinding.areaSpinner
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        bottomNav.visibility = View.VISIBLE
    }

    private fun initScheduleRecyclerView() {
        //tutor pyo
        packageRecyclerView = packageBinding.scheduleRV
        packageRecyclerView.setHasFixedSize(true)
        packageRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        scheduleAdapter = ScheduleAdapter({ position->

            /**
             * 스케줄 리스트의 +버튼 클릭 시 몇 번째 item인지 position 콜백
             */
            schedulePosition = position
            scheduleBottomSheetHandler()
        }, viewLifecycleOwner.lifecycleScope)

        packageRecyclerView.adapter = scheduleAdapter
    }

    private fun scheduleBottomSheetHandler() {
        if(packageBinding.areaSpinner.text.isBlank()){
            Toast.makeText(context, "해당 지역에는 숙소 정보가 없습니다.", Toast.LENGTH_SHORT).show()
        }else{
            with(scheduleBinding) {
                commonHandler.showBottomSheet(root, requireContext())

                viewLifecycleOwner.lifecycleScope.launch {
                    viewModel.findLodgingList(packageBinding.areaSpinner.text.toString())
                }

                // 테마
                commonHandler.spinnerHandler(
                    resources.getStringArray(R.array.select_theme),
                    themeSpinner
                )

                buttonAdd.setOnClickListener {
                    if(isValidBottomSheet()){
                        viewLifecycleOwner.lifecycleScope.launch {
                            viewModel.findLodging(lodgingId)
                        }

                        scheduleItem = ScheduleItem(
                            packageId = 0,
                            lodgingId = lodgingId,
                            theme = themeSpinner.text.toString(),
                            description = scheduleDescription.text.toString(),
                            day = schedulePosition
                        )

                        scheduleList.add(scheduleItem)

                        themeSpinner.setText("")
                        scheduleDescription.setText("")
                        commonHandler.dismissBottomSheet(root)
                    }
                }
            }
        }
    }
}
