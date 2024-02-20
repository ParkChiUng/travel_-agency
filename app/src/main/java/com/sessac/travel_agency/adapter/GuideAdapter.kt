package com.sessac.travel_agency.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sessac.travel_agency.data.GuideItem
import com.sessac.travel_agency.databinding.ItemGuideBinding

class GuideAdapter(val itemOnClick: (GuideItem) -> (Unit)) :
    RecyclerView.Adapter<GuideAdapter.GuideViewHolder>() {

    private var guideList = emptyList<GuideItem>()

    class GuideViewHolder(val binding: ItemGuideBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GuideViewHolder {
        return GuideViewHolder(
            ItemGuideBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: GuideViewHolder, position: Int) {
        val guide = guideList[position]
        with(holder.binding) {
            Glide.with(guideImage.context)
                .load(guide.gImage)
                .into(guideImage)
            guideName.text = guide.gName

            // 카드 클릭시
            root.setOnClickListener {
                itemOnClick(guide)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setGuideList(guide: List<GuideItem>) {
        guideList = guide
        notifyDataSetChanged()
    }

    override fun getItemCount() = guideList.size
}