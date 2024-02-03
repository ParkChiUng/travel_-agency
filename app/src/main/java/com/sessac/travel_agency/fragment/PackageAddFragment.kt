package com.sessac.travel_agency.fragment

import android.annotation.SuppressLint
import android.net.Uri
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
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.datepicker.MaterialDatePicker
import com.sessac.travel_agency.R
import com.sessac.travel_agency.adapter.PackageAdapter
import com.sessac.travel_agency.adapter.ScheduleAdapter
import com.sessac.travel_agency.common.CommonHandler
import com.sessac.travel_agency.data.PackageItem
import com.sessac.travel_agency.data.ScheduleItem
import com.sessac.travel_agency.databinding.BottomSheetImagePickerBinding
import com.sessac.travel_agency.databinding.FragmentPackageAddBinding
import com.sessac.travel_agency.databinding.FragmentPackageScheduleAddBinding
import com.sessac.travel_agency.viewmodels.PackageViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit


class PackageAddFragment : Fragment(), ScheduleAdapter.OnScheduleAddItemClickListener {
    private lateinit var packageBinding: FragmentPackageAddBinding
    private lateinit var scheduleBinding: FragmentPackageScheduleAddBinding
    private lateinit var bottomNav: BottomNavigationView
    private lateinit var galleryViewBinding: BottomSheetImagePickerBinding
    private lateinit var regionItem: Array<String>
    private lateinit var scheduleList: ArrayList<ScheduleItem>
    private lateinit var scheduleAdapter: ScheduleAdapter
    private var packageAdapter: PackageAdapter? = null
    private lateinit var dialog: BottomSheetDialog
    private lateinit var packageRecyclerView: RecyclerView
    private lateinit var commonHandler: CommonHandler
    private var selectGuideId: Int = 0
    private val TAG = "PackageAddFragment"
    private var selectImageUri: Uri? = null
    private var startDate: Date = Date()
    private var endDate: Date = Date()
    private lateinit var guideSpinnerAdapter: ArrayAdapter<String>
    private var selectedGuide: String = ""
    private var packageItem: PackageItem? = null
    private var packageId: Int? = null

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        commonHandler = CommonHandler.generateCommonHandler()
        commonHandler.imageCallback(requireActivity().activityResultRegistry)

        initRecyclerView()

        packageItem = arguments?.getParcelable("packageItem")

        packageItem?.let {
            packageId = it.packageId
            initData(it)
        }

        setAreaSpinner()
        removeBottomNav()
        setupObserver()
        clickEventHandler()
    }

    @SuppressLint("SetTextI18n")
    private fun initData(packageItem: PackageItem){
        with(packageBinding) {
            areaSpinner.setText(packageItem.area)
            packageAddNameET.setText(packageItem.pName)
            guideSpinner.setText(packageItem.guideName)

            Glide.with(packageAddImage.context)
                .load(packageItem.pImage)
                .into(packageAddImage)

            selectImageUri = packageItem.pImage.toUri()

            startDate = packageItem.pStartDate
            endDate = packageItem.pEndDate

            val startDate = commonHandler.dateHandler(startDate)
            val endDate = commonHandler.dateHandler(endDate)

            packageAddDateET.setText("$startDate ~ $endDate")
            textRegister.text = "수정"
            buttonAddPackage.text = "수정"

            val diffInMillies = Math.abs(packageItem.pStartDate.time - packageItem.pEndDate.time)
            val day = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS)

            addScheduleLayout(day.toInt())
        }
    }

    private fun clickEventHandler(){
        with(packageBinding) {
            packageAddDateET.setOnClickListener {
                calendarHandler()
            }

            // Toolbar의 Navigation Icon 클릭 시 뒤로 가기 동작 설정
            toolbar.setNavigationOnClickListener {
                commonHandler.alertDialog(requireContext(), "warning") {
                    findNavController().popBackStack()  // yes클릭시 뒤로 화면전환 됨
                }
            }

            packageAddImage.setOnClickListener {
                handleImageClick(packageAddImage)
            }

            if(packageItem == null){
                buttonAddPackage.setOnClickListener {
                    if(isValid()){
                        viewModel.insertPackage(
                            PackageItem(
                                guideName = selectedGuide,
                                area = areaSpinner.text.toString(),
                                pName = packageAddNameET.text.toString(),
                                pImage = selectImageUri.toString(),
                                pStartDate = startDate,
                                pEndDate = endDate,
                            )
                        )
                        findNavController().popBackStack()
                    }
                }
            }else{
                buttonAddPackage.setOnClickListener {
                    if(isValid()){
                        viewModel.updatePackage(
                            PackageItem(
//                                guideName = selectedGuide?: guideSpinner.text.toString(),
                                packageId = packageId!!,
                                guideName = guideSpinner.text.toString(),
                                area = areaSpinner.text.toString(),
                                pName = packageAddNameET.text.toString(),
                                pImage = selectImageUri.toString(),
                                pStartDate = startDate,
                                pEndDate = endDate,
                            )
                        )
                        findNavController().popBackStack()
                    }
                }
            }
            guideSpinner.setOnClickListener{
                if(startDate.before(Date())){
                    Toast.makeText(context, "날짜를 선택해주세요.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    /**
     * [유효성 체크]
     * 패키지 등록 버튼 클릭 시 호출
     */
    private fun isValid():Boolean{
        var check = true
        with(packageBinding) {
            if (selectImageUri == null) {
                Toast.makeText(context, "이미지를 선택해주세요.", Toast.LENGTH_SHORT).show()
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
            if (selectedGuide.isBlank()) {
                Toast.makeText(context, "가이드를 선택해주세요.", Toast.LENGTH_SHORT).show()
                check = false
            }
//            if (startDate.before(Date())){
//                Toast.makeText(context, "올바른 날짜를 선택해주세요.", Toast.LENGTH_SHORT).show()
//                check = false
//            }
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
                packageAdapter?.setPackageList(it)
            }
        }

        viewModel.guideLists.observe(viewLifecycleOwner) { guideList ->
            guideList.let {
                val guideItem: List<String> = guideList.map{ it.gName }
                val guideItemArray: Array<String> = guideItem.toTypedArray()

                guideSpinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, guideItemArray)

                commonHandler.spinnerHandler(guideItemArray, packageBinding.guideSpinner, requireContext())

                packageBinding.guideSpinner.setOnItemClickListener { parent, _, position, _ ->
                    selectedGuide = guideSpinnerAdapter.getItem(position).toString()
                }
            }
        }
    }

    private fun handleImageClick(imageView: ImageView) {
        with(galleryViewBinding) {

            commonHandler.showDialog(root, requireContext())

            // 갤러리에서 가져오기 클릭
            textGallery.setOnClickListener {
                commonHandler.imageSelect { imageUri ->
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

    private fun removeBottomNav(){
        // BottomNavigationView 초기화(바텀 네비 뷰 전환 클릭 이벤트 감지하기 위함)
        bottomNav = requireActivity().findViewById(R.id.bottomNav)  // 프래그먼트가 속한 액티비티requireActivity()에서 BottomNavigationView를 찾아 bottomNav 변수에 할당

        // BottomNavigationView를 숨김
        bottomNav.visibility = View.GONE
    }

    private fun setAreaSpinner(){
        // 지역선택 스피너
        regionItem = resources.getStringArray(R.array.select_region)
        commonHandler.spinnerHandler(regionItem, packageBinding.areaSpinner, requireContext())
    }

    override fun onDestroy() {
        super.onDestroy()
        bottomNav.visibility = View.VISIBLE
    }

    private fun initRecyclerView() {
        packageRecyclerView = packageBinding.scheduleRV
        packageRecyclerView.setHasFixedSize(true)
        packageRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        scheduleAdapter = ScheduleAdapter(0, this)
        packageRecyclerView.adapter = scheduleAdapter
    }


    // ~일차의 + 버튼
    override fun onScheduleAddItemClicked() {
        // 스케쥴 바텀 시트 레이아웃, 뷰
        with(scheduleBinding){
            commonHandler.showDialog(root, requireContext())

            // 스피너에 리스트 추가
            commonHandler.spinnerHandler(resources.getStringArray(R.array.select_lodging), lodgingSpinner, requireContext())
            commonHandler.spinnerHandler(resources.getStringArray(R.array.select_theme), themeSpinner, requireContext())

            // 스케쥴 바텀시트의 등록 버튼 눌렀을때
            buttonAdd.setOnClickListener {
                handleAddButtonClick() // 닫기전 선택된 데이터가 데이터클래스에 담겨 insert 되게 추가
                commonHandler.dismissDialog(root)
            }
        }
    }

    // 스케쥴을 어댑터에 추가->어댑터 및 리사이클러뷰를 업데이트하여 마지막으로 추가된 스케쥴의 detail을 갱신하는 함수
    private fun addScheduleToAdapter(schedule: ScheduleItem) {
        scheduleAdapter.addScheduleItem(schedule)  // 새로운 스케쥴 아이템을 어댑터에 추가
        scheduleAdapter.notifyDataSetChanged()  // 어댑터에 데이터가 변경되었음을 알리는 함수, 데이터 변경 후에 이 함수를 호출하여 어댑터에게 데이터를 다시 그리도록 함

        // 생성된 스케쥴의 detail을 업데이트
        val lastItemPosition = scheduleAdapter.itemCount - 1  // 어댑터의 현재 아이템 개수에서 1을 뺀 값으로, 마지막으로 추가된 스케쥴의 위치를 가져옴
        if (lastItemPosition >= 0) {
            val lastViewHolder =
                packageRecyclerView.findViewHolderForAdapterPosition(lastItemPosition) as? ScheduleAdapter.ViewHolder  // 해당 위치의 뷰홀더를 가져옴. 만약 해당 위치의 뷰홀더가 없다면 null을 반환
            lastViewHolder?.theme?.text = schedule.theme // 뷰홀더의 theme에 schedule.theme 넣기
            lastViewHolder?.detailsText?.text = schedule.detail // 뷰홀더의 detailsText에 schedule.detail 넣기
            //숙소는 id니까... 숙소객체에서 id 통해 숙소명 집어넣어야함
        }
    }


    // 바텀시트 안의 등록버튼 누를 시
    private fun handleAddButtonClick() {
        // 스케쥴 생성
        val newSchedule = createScheduleItemFromBottomSheet()

        // 프래그먼트에서 어댑터에게 스케줄 추가 및 갱신 알림
        addScheduleToAdapter(newSchedule)
    }


    // Btn_addSchedule을 눌렀을 때의 스케쥴 데이터 생성
    private fun createScheduleItemFromBottomSheet(): ScheduleItem {

        // 스피너에서 선택된 특정 text 가져오기
        val selectedTheme = scheduleBinding.themeSpinnerTIL.editText?.text.toString()
        val selectedLodging = scheduleBinding.lodgingSpinnerTIL.editText?.text.toString()
        val description = scheduleBinding.scheduleDescription.text.toString()

        // 바인딩한거 전달. 샘플
        return ScheduleItem(
            scheduleId = 1,
            lodgingInfo = 1, // 숙소이름이 아닌 Id..
            theme = selectedTheme,
            detail = description,
            day = 1
        )
    }

    @SuppressLint("SetTextI18n")
    private fun calendarHandler() {
        val dateRangePicker = MaterialDatePicker.Builder.dateRangePicker()
        dateRangePicker.setTitleText("기간 선택")
        dateRangePicker.setTheme(R.style.CustomCalendar)

        val picker = dateRangePicker.build()
        picker.show(childFragmentManager, picker.toString())
        picker.addOnPositiveButtonClickListener {
            val calendar = Calendar.getInstance()
            val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())

            calendar.timeInMillis = it.first ?: 0
            startDate = calendar.time

            calendar.timeInMillis = (it.second ?: 0) + TimeUnit.DAYS.toMillis(1)
            endDate = calendar.time

            calendar.timeInMillis = it.first ?: 0
            val startDateText = dateFormat.format(calendar.time)

            calendar.timeInMillis = it.second ?: 0
            val endDateText = dateFormat.format(calendar.time)

            val diffInMillies = Math.abs(endDate.time - startDate.time)
            val day = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS)

            addScheduleLayout(day.toInt())

            packageBinding.packageAddDateET.setText("$startDateText ~ $endDateText")

            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.findGuideSchedule(startDate, endDate)
            }
        }
    }

    private fun addScheduleLayout(day: Int) {
        scheduleAdapter.updateDay(day)
    }
}
