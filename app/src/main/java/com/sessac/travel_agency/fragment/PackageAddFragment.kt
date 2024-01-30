package com.sessac.travel_agency.fragment

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.datepicker.MaterialDatePicker
import com.sessac.travel_agency.R
import com.sessac.travel_agency.adapter.GuideAdapter
import com.sessac.travel_agency.adapter.ScheduleAdapter
import com.sessac.travel_agency.common.CommonHandler
import com.sessac.travel_agency.common.TravelAgencyApplication
import com.sessac.travel_agency.data.GuideItem
import com.sessac.travel_agency.data.ScheduleItem
import com.sessac.travel_agency.databinding.FragmentPackageAddBinding
import com.sessac.travel_agency.databinding.FragmentPackageScheduleAddBinding
import com.sessac.travel_agency.helper.TravelAgencyOpenHelper
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit
import kotlin.math.log


class PackageAddFragment : Fragment(), ScheduleAdapter.OnScheduleAddItemClickListener {
    private lateinit var dbHelper: TravelAgencyOpenHelper
    private lateinit var db: SQLiteDatabase
    private lateinit var binding: FragmentPackageAddBinding
    private lateinit var scheduleBinding: FragmentPackageScheduleAddBinding
    private lateinit var regionItem: Array<String>
    private lateinit var scheduleList: ArrayList<ScheduleItem>
    private lateinit var scheduleAdapter: ScheduleAdapter
    private lateinit var dialog: BottomSheetDialog
    private lateinit var recyclerView: RecyclerView
    private lateinit var commonHandler: CommonHandler
    private val TAG = "PackageAddFragment"


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPackageAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // DB 초기화
//        val app = TravelAgencyApplication.getTravelApplication()
//        dbHelper = app.dbHelper
//        db = app.db

        // insertPackageData()

        // 지역선택 스피너
        commonHandler = CommonHandler()
        regionItem = resources.getStringArray(R.array.select_region)
        commonHandler.spinnerHandler(regionItem, binding.areaSpinner, requireContext())


        // 달력 선택 핸들러
        with(binding) {
            packageAddDateET.setOnClickListener {
                calendarHandler()
            }
        }

        // RecyclerView 초기화 및 어댑터 설정
        initRecyclerView()
    }


    private fun initRecyclerView() {
        recyclerView = binding.scheduleRV
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        scheduleAdapter = ScheduleAdapter(0, this)
        recyclerView.adapter = scheduleAdapter
    }


    // ~일차의 + 버튼
    override fun onScheduleAddItemClicked() {
        // 스케쥴 바텀 시트 레이아웃, 뷰
        //val view: View = layoutInflater.inflate(R.layout.fragment_package_add_schedule, null)
        val view: View = layoutInflater.inflate(R.layout.fragment_package_schedule_add, null)

        val addButton: Button = view.findViewById(R.id.button_add)
        val theme: AutoCompleteTextView = view.findViewById(R.id.themeSpinner)
        val lodging: AutoCompleteTextView = view.findViewById(R.id.lodgingSpinner)

        dialog = BottomSheetDialog(requireContext(), R.style.AppBottomSheetDialogTheme) // 키보드가 레이아웃 가리지 않게 & 모서리 둥글게
        dialog.setContentView(view)
        dialog.show()

        // 드롭다운 리스트 세팅(숙소는 나중에 룸DB에 존재하는 숙소리스트가 뜨도록 수정하기)
        val lodgingList = resources.getStringArray(R.array.select_lodging)
        val themeList = resources.getStringArray(R.array.select_theme)

        // 스피너에 리스트 추가
        commonHandler.spinnerHandler(lodgingList, lodging, requireContext())
        commonHandler.spinnerHandler(themeList, theme, requireContext())

        // 스케쥴 바텀시트의 등록 버튼 눌렀을때
        addButton.setOnClickListener {
            handleAddButtonClick() // 닫기전 선택된 데이터가 데이터클래스에 담겨 insert 되게 추가
            dialog.dismiss() // 닫기
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
                recyclerView.findViewHolderForAdapterPosition(lastItemPosition) as? ScheduleAdapter.ViewHolder  // 해당 위치의 뷰홀더를 가져옴. 만약 해당 위치의 뷰홀더가 없다면 null을 반환
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

        scheduleBinding = FragmentPackageScheduleAddBinding.inflate(layoutInflater, null, false)
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


//    private fun insertPackageData() {
//        val values = ContentValues().apply {
//            put("guideInfo", 1)
//            put("area", "test")
//            put("pName", "test")
//            put("pImage", "test")
//            put("status", 1)
//            put("pStartDate", "test")
//            put("pEndDate", "test")
//        }
//        db.insert(TravelAgencyOpenHelper.TABLE_PACKAGE, null, values)
//    }


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
            val startDate = calendar.time

            calendar.timeInMillis = (it.second ?: 0) + TimeUnit.DAYS.toMillis(1)
            val endDate = calendar.time

            calendar.timeInMillis = it.first ?: 0
            val startDateText = dateFormat.format(calendar.time)

            calendar.timeInMillis = it.second ?: 0
            val endDateText = dateFormat.format(calendar.time)

            val diffInMillies = Math.abs(endDate.time - startDate.time)
            val day = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS)

            addScheduleLayout(day.toInt())

            binding.packageAddDateET.setText("$startDateText ~ $endDateText")
        }
    }

    private fun addScheduleLayout(day: Int) {
        Log.d(TAG, "addScheduleLayout: $day")

        scheduleAdapter.updateDay(day)
    }
}
