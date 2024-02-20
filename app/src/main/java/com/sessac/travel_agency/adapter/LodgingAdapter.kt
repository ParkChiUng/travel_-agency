package com.sessac.travel_agency.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sessac.travel_agency.data.GuideItem
import com.sessac.travel_agency.data.LodgingItem
import com.sessac.travel_agency.databinding.ItemLodgingBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import reactivecircus.flowbinding.android.view.clicks

/**
 * Lodging RecyclerView의 데이터를 표시하기 위한 어댑터
 */
class LodgingAdapter(
    val itemOnClick: (LodgingItem) -> (Unit),
    private val scope: CoroutineScope
) :
    ListAdapter<LodgingItem, LodgingAdapter.LodgingViewHolder>(diffUtil) {

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<LodgingItem>() {
            override fun areItemsTheSame(oldItem: LodgingItem, newItem: LodgingItem): Boolean {
                return oldItem.lodgeId == newItem.lodgeId
            }

            override fun areContentsTheSame(oldItem: LodgingItem, newItem: LodgingItem): Boolean {
                return oldItem == newItem
            }
        }
    }


    inner class LodgingViewHolder(val binding: ItemLodgingBinding) : RecyclerView.ViewHolder(binding.root){
        fun widgetBinding(lodgingItem: LodgingItem) {
            lodgingItem.let { lodging ->
                with(binding) {
                    // Glide를 사용하여 이미지 로드
                    Glide.with(lodgingThumbnail.context)
                        .load(lodging.lImage)
                        .into(lodgingThumbnail)
                    lodgingLocation.text = lodging.area
                    lodgingName.text = lodging.lName
                    lodgingRating.rating = lodging.starNum.toFloat()

                    // 카드 클릭시
                    binding.root.clicks()
                        .onEach {
                            itemOnClick(lodging)
                        }
                        .launchIn(scope)
                }
            }
        }
    }

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
        holder.widgetBinding(getItem(position))
    }

    fun setLodgingList(lodging: List<LodgingItem>) {
        submitList(lodging)
    }
}
