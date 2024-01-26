package com.sessac.travel_agency.fragment

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.datepicker.MaterialDatePicker
import com.sessac.travel_agency.R
import com.sessac.travel_agency.adapter.ScheduleAdapter
import com.sessac.travel_agency.common.CommonHandler
import com.sessac.travel_agency.common.TravelAgencyApplication
import com.sessac.travel_agency.databinding.FragmentPackageAddBinding
import com.sessac.travel_agency.helper.TravelAgencyOpenHelper
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit


class PackageAddFragment : Fragment() {
    private lateinit var dbHelper: TravelAgencyOpenHelper
    private lateinit var db: SQLiteDatabase
    private lateinit var binding: FragmentPackageAddBinding
    private lateinit var regionItem : Array<String>
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ScheduleAdapter
    private var selectButtonPosition : Int = 0
    private lateinit var bottomSheetView: View
    private val commonHandler: CommonHandler = CommonHandler()
    private val TAG = "PackageAddFragment"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)
        binding = FragmentPackageAddBinding.inflate(inflater, container, false)

        val app = TravelAgencyApplication.getTravelApplication()
        dbHelper = app.dbHelper
        db = app.db

        return binding.root
    }

    /**
     * @param commonHandler.spinnerHandler(select Item List, spinner id, context)
     *
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        insertPackageData()

        // 스피너
        regionItem = resources.getStringArray(R.array.select_region)

        commonHandler.spinnerHandler(regionItem, binding.areaSpinner, requireContext())

        with(binding) {
            packageAddDateET.setOnClickListener {
                calendarHandler()
            }
        }


        // 바텀 시트 레이아웃
        bottomSheetView = layoutInflater.inflate(R.layout.fragment_package_add_schedule, null)

        val addButton: Button = bottomSheetView.findViewById(R.id.button_add)

        addButton.setOnClickListener {
            // 바텀시트 등록 버튼 클릭 시
        }
    }

    private fun insertPackageData() {
        val values = ContentValues().apply {
            put("guideInfo", 1)
            put("area", "test")
            put("pName", "test")
            put("pImage", "test")
            put("status", 1)
            put("pStartDate", "test")
            put("pEndDate", "test")
        }
        db.insert(TravelAgencyOpenHelper.TABLE_PACKAGE, null, values)
    }

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

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(requireContext())
        recyclerView = binding.scheduleRV
        recyclerView.setHasFixedSize(false)
        recyclerView.layoutManager = layoutManager

        val itemDecoration = VerticalSpaceItemDecoration(20) // 리사이클러 뷰 간격
        recyclerView.addItemDecoration(itemDecoration)

        adapter = ScheduleAdapter(day)
        recyclerView.adapter = adapter

        adapter.onItemClick = {position ->
            selectButtonPosition = position
            val dialog = BottomSheetDialog(requireContext())
            dialog.setContentView(bottomSheetView)
            dialog.show()
        }
    }

    class VerticalSpaceItemDecoration(private val verticalSpaceHeight: Int) :
        RecyclerView.ItemDecoration() {

        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            outRect.bottom = verticalSpaceHeight
        }
    }
}