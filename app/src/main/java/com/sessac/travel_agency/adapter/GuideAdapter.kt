package com.sessac.travel_agency.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sessac.travel_agency.R
import com.sessac.travel_agency.data.GuideItem

class GuideAdapter(private val guideList:ArrayList<GuideItem>) : RecyclerView.Adapter<GuideAdapter.GuideHolder>() {

    var onItemClick : ((GuideItem) -> Unit)? = null

    inner class GuideHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView : ImageView = itemView.findViewById(R.id.guide_image)
        val nameTextView : TextView = itemView.findViewById(R.id.guide_name)
    }

    // 뷰홀더와 그에 연결된 뷰를 생성하고 초기화
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GuideHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_guide, parent, false )
        return GuideHolder(view)
    }

    // 데이터세트 크기 가져옴
    override fun getItemCount(): Int {
        return guideList.size
    }

    // 뷰홀더를 데이터와 연결
    override fun onBindViewHolder(holder: GuideHolder, position: Int) {
        val guide = guideList[position]
        holder.imageView.setImageResource(guide.gImage)
        holder.nameTextView.text = guide.gName

        // 카드 클릭시
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(guide)
        }

    }
}