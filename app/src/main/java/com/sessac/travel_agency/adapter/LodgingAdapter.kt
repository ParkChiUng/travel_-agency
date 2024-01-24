package com.sessac.travel_agency.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sessac.travel_agency.R
import com.sessac.travel_agency.data.LodgingItem

class LodgingAdapter(private val lodgingList:ArrayList<LodgingItem>) : RecyclerView.Adapter<LodgingAdapter.LodgingHolder>() {

    //var onItemClick : ((LodgingItem) -> Unit)? = null

    inner class LodgingHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView : ImageView = itemView.findViewById(R.id.lodging_thumbnail)
        val locationTextView : TextView = itemView.findViewById(R.id.lodging_location)
        val nameTextView : TextView = itemView.findViewById(R.id.lodging_name)
        val rateView : RatingBar = itemView.findViewById(R.id.lodging_rating)
    }

    // 뷰홀더와 그에 연결된 뷰를 생성하고 초기화
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LodgingHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_lodging, parent, false )
        return LodgingHolder(view)
    }

    // 데이터세트 크기 가져옴
    override fun getItemCount(): Int {
        return lodgingList.size
    }

    // 뷰홀더를 데이터와 연결
    override fun onBindViewHolder(holder: LodgingHolder, position: Int) {
        val lodging = lodgingList[position]
        holder.imageView.setImageResource(lodging.lImage)
        holder.locationTextView.text = lodging.area
        holder.nameTextView.text = lodging.lName
        holder.rateView.rating = lodging.starNum.toFloat()

        // 카드 클릭시
//        holder.itemView.setOnClickListener {
//            onItemClick?.invoke(lodging)
//        }

    }
}