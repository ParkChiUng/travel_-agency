package com.sessac.travel_agency.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.sessac.travel_agency.R
import com.sessac.travel_agency.data.PackageItem
import java.text.SimpleDateFormat
import java.util.Locale


//class PackageAdapter (private val packageList:ArrayList<PackageItem>) : RecyclerView.Adapter<PackageAdapter.PackageHolder>() {
class PackageAdapter (private val packageList: MutableLiveData<List<PackageItem>>) : RecyclerView.Adapter<PackageAdapter.PackageHolder>() {

    var onItemClick : ((PackageItem) -> Unit)? = null

    inner class PackageHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView : ImageView = itemView.findViewById(R.id.package_thumbnail)
        val locationTextView : TextView = itemView.findViewById(R.id.package_area)
        val nameTextView : TextView = itemView.findViewById(R.id.package_name)
        val startDateTextView : TextView = itemView.findViewById(R.id.package_startdate)
        val endDateTextView : TextView = itemView.findViewById(R.id.package_enddate)
        val ratingBar : RatingBar = itemView.findViewById(R.id.package_rating)
    }

    // 뷰홀더와 그에 연결된 뷰를 생성하고 초기화
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PackageHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_package, parent, false )
        return PackageHolder(view)
    }

    // 데이터세트 크기 가져옴
    override fun getItemCount(): Int {
        return packageList.value?.size ?: 0
    }

    // 뷰홀더를 데이터와 연결
    override fun onBindViewHolder(holder: PackageAdapter.PackageHolder, position: Int) {
        val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())

        val packageItem = packageList.value?.get(position)
        packageItem?.let {
            holder.imageView.setImageResource(it.pImage)
            holder.locationTextView.text = it.area
            holder.nameTextView.text = it.pName
            holder.startDateTextView.text = "${dateFormat.format(it.pStartDate)}"
            holder.endDateTextView.text = " ~ ${dateFormat.format(it.pEndDate)}"
        }

        // 패키지 아이템에서 star 값 가져오기
        val star = packageItem?.star

        // star가 null인 경우 RatingBar가 안보이도록 GONE으로 설정
        if (star != null) {
            holder.ratingBar.rating = star.toFloat()
            holder.ratingBar.visibility = View.VISIBLE
        } else {
            holder.ratingBar.visibility = View.GONE
        }

        // 카드 클릭시
        holder.itemView.setOnClickListener {
            if (packageItem != null) {
                onItemClick?.invoke(packageItem)
            }
        }
    }

}