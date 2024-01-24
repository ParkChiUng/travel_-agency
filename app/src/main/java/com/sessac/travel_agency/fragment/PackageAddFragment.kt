package com.sessac.travel_agency.fragment

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
import com.sessac.travel_agency.databinding.FragmentPackageAddBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit


class PackageAddFragment : Fragment() {

    private lateinit var binding: FragmentPackageAddBinding
    private val TAG = "PackageAddFragment"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)

        binding = FragmentPackageAddBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        spinnerHandler()

        with(binding){
            packageAddDateET.setOnClickListener {
                calendarHandler()
            }
        }
    }

    private fun spinnerHandler(){
        val items = resources.getStringArray(R.array.select_region)

        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, items)

        val autoCompleteTextView: AutoCompleteTextView = binding.areaSpinner
        autoCompleteTextView.setAdapter(adapter)
    }

    private fun calendarHandler(){
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

            addScheduleLayout(day)

            binding.packageAddDateET.setText("$startDateText ~ $endDateText")
        }
    }

    private fun addScheduleLayout(day : Long){
        Log.d(TAG, "addScheduleLayout: $day")

        val recyclerView: RecyclerView = binding.scheduleRV
        recyclerView.setHasFixedSize(true)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = layoutManager

        val buttonText: MutableList<String> = ArrayList()
        buttonText.add("버튼 1")
        buttonText.add("버튼 2")
        buttonText.add("버튼 3")
        buttonText.add("버튼 4")
        buttonText.add("버튼 5")
        buttonText.add("버튼 6")
        buttonText.add("버튼 7")
        buttonText.add("버튼 8")
        buttonText.add("버튼 9")
        val adapter = ScheduleAdapter(buttonText)
        recyclerView.adapter = adapter
    }
}