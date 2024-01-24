package com.sessac.travel_agency.fragment

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.datepicker.MaterialDatePicker
import com.sessac.travel_agency.R
import com.sessac.travel_agency.adapter.ScheduleAdapter
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        spinnerHandler()

        insertPackageData()

        with(binding) {
            packageAddDateET.setOnClickListener {
                calendarHandler()
            }
        }
    }

    //    private fun insertPackageData(guideInfo: GuideItem, area: String, pName: String, pImage: String, status: Int, pStartDate: Date, pEndDate: Date) {
    private fun insertPackageData() {
        val values = ContentValues().apply {
            put("guideInfo", 1)
            put("area", "test")
            put("pName", "test")
            put("pImage", "test")
            put("status", 1)
            put("pStartDate", "test")
            put("pEndDate", "test")

//            put("guideInfo", guideInfo.guideId)
//            put("area", area)
//            put("pName", pName)
//            put("pImage", pImage)
//            put("status", status)
//            put("pStartDate", pStartDate.time) // Date를 long 타입으로 변환하여 저장합니다.
//            put("pEndDate", pEndDate.time) // Date를 long 타입으로 변환하여 저장합니다.
        }
        db.insert(TravelAgencyOpenHelper.TABLE_PACKAGE, null, values)
    }

    private fun spinnerHandler() {
        val items = resources.getStringArray(R.array.select_region)

        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, items)

        val autoCompleteTextView: AutoCompleteTextView = binding.areaSpinner
        autoCompleteTextView.setAdapter(adapter)
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

        val recyclerView: RecyclerView = binding.scheduleRV
        recyclerView.setHasFixedSize(false)

        recyclerView.layoutManager = layoutManager

        val itemDecoration = VerticalSpaceItemDecoration(10) // 10은 원하는 간격입니다.
        recyclerView.addItemDecoration(itemDecoration)

        val adapter = ScheduleAdapter(day)
        recyclerView.adapter = adapter
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