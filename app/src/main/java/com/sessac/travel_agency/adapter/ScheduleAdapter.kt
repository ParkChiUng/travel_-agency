package com.sessac.travel_agency.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sessac.travel_agency.data.LodgingItem
import com.sessac.travel_agency.data.ScheduleItem
import com.sessac.travel_agency.databinding.ItemScheduleBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import reactivecircus.flowbinding.android.view.clicks


/**
 * addPageOnclick -> 패키지 추가 페이지일 때 콜백
 * editPageOnClick -> 패키지 수정 페이지일 때 콜백
 */
//tutor pyo rxbinding, flow binding
class ScheduleAdapter(
    val itemOnClick: (Int) -> (Unit),
    private val scope: CoroutineScope
) :
    RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder>() {

    private var schedule: ScheduleItem? = null
    private var lodging: LodgingItem? = null
//    private var scheduleList = emptyList<ScheduleItem>()
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

            with(holder.binding){
                schedule?.let{
                    scheduleTheme.text = it.theme
                    description.text = it.description
                }

                lodging?.let {
                    Glide.with(lodgingImage.context)
                        .load(it.lImage)
                        .into(lodgingImage)

                    lodgingName.text = it.lName
                    lodgingLocation.text = it.area
                    lodgingRating.rating = it.starNum.toFloat()
                }
            }

            /**
             * 스케줄 리스트 클릭
             */
            expandable.clicks()
                .onEach {
                    toggleVisibility()
                }
                .launchIn(scope)

            /**
             * 스케줄 +버튼 클릭
             */
            btnAddSchedule.clicks()
                .onEach {
                    itemOnClick(position)
                }
                .launchIn(scope)

        }
    }

    private fun ItemScheduleBinding.toggleVisibility() {
        val isVisible = description.visibility == View.VISIBLE
        description.visibility = if (isVisible) View.GONE else View.VISIBLE
        scheduleTheme.visibility = if (isVisible) View.GONE else View.VISIBLE
        lodgingViews.visibility = if (isVisible) View.GONE else View.VISIBLE
    }

    /**
     * 스케줄 Add bottomSheet에서 등록 버튼 클릭 시 동작
     */
    @SuppressLint("NotifyDataSetChanged")
    fun setSchedule(scheduleItem: ScheduleItem, lodgingItem: LodgingItem, position: Int) {
        this.schedule = scheduleItem
        this.lodging = lodgingItem

        notifyItemChanged(position)
    }

    /**
     * 입력 받은 스케줄 일수만큼 리사이클러 뷰 생성
     */
    fun scheduleDay(newDay: Int) {
        day = newDay
        notifyDataSetChanged()
    }

    override fun getItemCount() = day
}