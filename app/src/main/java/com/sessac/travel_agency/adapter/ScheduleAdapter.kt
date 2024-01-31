package com.sessac.travel_agency.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.sessac.travel_agency.R
import com.sessac.travel_agency.data.ScheduleItem


class ScheduleAdapter(private var day: Int, private val listener: OnScheduleAddItemClickListener) : RecyclerView.Adapter<ScheduleAdapter.ViewHolder>() {

    // 스케쥴 추가 버튼이 클릭되었을 때 이를 처리하기 위한 리스너 인터페이스
    interface OnScheduleAddItemClickListener {
        //fun onScheduleAddItemClicked(selectedData: ScheduleItem?)
        fun onScheduleAddItemClicked()
    }

    // 스케줄 아이템 목록을 담을 리스트
    private val scheduleList = mutableListOf<ScheduleItem>()

    // 스케줄 아이템 추가 메서드
    fun addScheduleItem(schedule: ScheduleItem) {
        scheduleList.add(schedule)
        // 아이템이 추가되었음을 어댑터에 알림
        notifyItemInserted(scheduleList.size - 1) // 추가된 아이템의 위치를 알려줌
    }

    // 스케줄 일수 업데이트 메서드
    fun updateDay(newDay: Int) {
        day = newDay
        // 어댑터를 다시 그리도록 알림
        notifyDataSetChanged()
    }

    // 뷰홀더 생성 메서드
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_schedule, parent, false)
        return ViewHolder(view)
    }

    // 뷰홀더에 데이터 바인딩하는 메서드
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // 현재 위치에 해당하는 스케줄 아이템 가져오기
        val scheduleItem = scheduleList.getOrNull(position)
        // 일차 텍스트 설정
        val text = "${position + 1} 일차"
        holder.scheduleTextView.text = text

        // 확장 버튼 클릭 이벤트 설정
        holder.expand.setOnClickListener {
            // 클릭시 가시성 변경
            val isVisible = holder.detailsText.visibility == View.VISIBLE
            holder.detailsText.visibility = if (isVisible) View.GONE else View.VISIBLE
            holder.theme.visibility = if (isVisible) View.GONE else View.VISIBLE
            holder.lodging.visibility =  if (isVisible) View.GONE else View.VISIBLE
        }

        // ~일차 아이템의 이미지+ 버튼 클릭시 클릭 이벤트 설정
        holder.addBtn.setOnClickListener {
            listener.onScheduleAddItemClicked() // // 스케줄 추가 버튼 클릭 이벤트 호출
            // listener.onScheduleAddItemClicked(null) // (수정)선택된 데이터를 넘겨줘야 함
        }

    }

    // 전체 아이템 수 반환
    override fun getItemCount(): Int {
        return day
    }

    // 뷰홀더 클래스
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val scheduleTextView: TextView = itemView.findViewById(R.id.scheduleLayoutTV)
        val detailsText: TextView = itemView.findViewById(R.id.detailsText)
        val theme: TextView = itemView.findViewById(R.id.schedule_theme)
        val lodging: CardView = itemView.findViewById(R.id.lodgingViews)
        val expand: View = itemView.findViewById(R.id.expandable)
        val addBtn: ImageView = itemView.findViewById(R.id.Btn_addSchedule)
    }
}
