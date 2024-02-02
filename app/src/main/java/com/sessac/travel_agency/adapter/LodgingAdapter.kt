package com.sessac.travel_agency.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sessac.travel_agency.data.LodgingItem
import com.sessac.travel_agency.databinding.ItemLodgingBinding

/**
 * Lodging RecyclerView의 데이터를 표시하기 위한 어댑터
 */
class LodgingAdapter(val itemOnClick: (LodgingItem) -> Unit) :
    RecyclerView.Adapter<LodgingAdapter.LodgingViewHolder>() {

    private var lodgingList = emptyList<LodgingItem>()

    inner class LodgingViewHolder(val binding: ItemLodgingBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LodgingViewHolder {
        return LodgingViewHolder(
            ItemLodgingBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: LodgingViewHolder, position: Int) {
        val lodging = lodgingList[position]
        with(holder.binding) {
            // Glide를 사용하여 이미지 로드
            Glide.with(lodgingThumbnail.context)
                .load(lodging.lImage)
                .into(lodgingThumbnail)
            lodgingLocation.text = lodging.area
            lodgingName.text = lodging.lName
            lodgingRating.rating = lodging.starNum.toFloat()

            // 카드 클릭시
            root.setOnClickListener {
                itemOnClick(lodging)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setLodgingList(lodging: List<LodgingItem>) {
        lodgingList = lodging
        notifyDataSetChanged()
    }

    override fun getItemCount() = lodgingList.size
}
