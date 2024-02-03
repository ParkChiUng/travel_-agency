package com.sessac.travel_agency.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sessac.travel_agency.data.ScheduleItem
import com.sessac.travel_agency.databinding.ItemScheduleBinding


class ScheduleAdapter(
    val addOnClick: (String, Int) -> Unit,
    val editOnClick: (ScheduleItem, String, Int) -> (Unit),
) :
    RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder>() {

    private lateinit var dayText: String
    private var schedule: ScheduleItem? = null
    private var isEditMode: Boolean = false
    private var scheduleList = emptyList<ScheduleItem>()
    private var day = 0

    class ScheduleViewHolder(val binding: ItemScheduleBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        return ScheduleViewHolder(
            ItemScheduleBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
        with(holder.binding) {
            scheduleLayoutTV.text = "${position + 1} 일차"

            if (isEditMode) {
                val schedule = scheduleList[position]

                lodgingName.text = schedule.lodgingName
                scheduleTheme.text = schedule.theme
                description.text = schedule.description

                expandable.setOnClickListener {
                    toggleVisibility()
                    editOnClick(schedule, "item", position)
                }
                btnAddSchedule.setOnClickListener {
                    editOnClick(schedule, "button", position)
                }
            } else {
                
                with(holder.binding){
                    if(schedule != null){
                        lodgingName.text = schedule?.lodgingName
                        scheduleTheme.text = schedule?.theme
                        description.text = schedule?.description
                    }
                }

                expandable.setOnClickListener {
                    toggleVisibility()
                    addOnClick("item", position)
                }
                btnAddSchedule.setOnClickListener {
                    addOnClick("button", position)
                }
            }
        }
    }

    private fun ItemScheduleBinding.toggleVisibility() {
        val isVisible = description.visibility == View.VISIBLE
        description.visibility = if (isVisible) View.GONE else View.VISIBLE
        scheduleTheme.visibility = if (isVisible) View.GONE else View.VISIBLE
        lodgingViews.visibility = if (isVisible) View.GONE else View.VISIBLE
    }

    fun setEditMode(isEditMode: Boolean) {
        this.isEditMode = isEditMode
    }

    fun setScheduleList(scheduleItem: List<ScheduleItem>) {
        scheduleList = scheduleItem
        notifyDataSetChanged()
    }

    fun setSchedule(scheduleItem: ScheduleItem, position: Int) {
        this.schedule = scheduleItem
        notifyItemChanged(position)
    }

    fun updateDay(newDay: Int) {
        day = newDay
        notifyDataSetChanged()
    }

    override fun getItemCount() = if (scheduleList.isEmpty()) day else scheduleList.size
}