package com.sessac.travel_agency.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.sessac.travel_agency.R

class ScheduleAdapter(private val day: Int) : RecyclerView.Adapter<ScheduleAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.schedule_button, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val text = "${position + 1} 일차"

        holder.scheduleTextView.text = text

        holder.scheduleItem.setOnClickListener {
        }
    }

    override fun getItemCount(): Int {
        return day
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val scheduleTextView: TextView = itemView.findViewById(R.id.scheduleLayoutTV)
        val scheduleItem: ConstraintLayout = itemView.findViewById(R.id.scheduleItem)
    }
}
