package com.sessac.travel_agency.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sessac.travel_agency.R

class ScheduleAdapter(private val textViewText: List<String>) : RecyclerView.Adapter<ScheduleAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.schedule_button, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val text = textViewText[position]
        holder.scheduleTextView.text = text
        holder.scheduleTextView.setOnClickListener {
            // 버튼 클릭 시 수행할 작업을 작성합니다.
        }
    }

    override fun getItemCount(): Int {
        return textViewText.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val scheduleTextView: TextView = itemView.findViewById(R.id.scheduleLayoutTV)
    }
}
