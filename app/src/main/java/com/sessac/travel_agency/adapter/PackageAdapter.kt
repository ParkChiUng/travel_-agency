package com.sessac.travel_agency.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sessac.travel_agency.common.CommonHandler
import com.sessac.travel_agency.data.PackageItem
import com.sessac.travel_agency.databinding.ItemPackageBinding


class PackageAdapter (val itemOnClick: (PackageItem) -> (Unit)) :
    RecyclerView.Adapter<PackageAdapter.PackageViewHolder>() {

    private var packageList = emptyList<PackageItem>()
    private val commonHandler = CommonHandler.generateCommonHandler()

    class PackageViewHolder(val binding: ItemPackageBinding) : RecyclerView.ViewHolder(binding.root)

    // 뷰홀더와 그에 연결된 뷰를 생성하고 초기화
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PackageViewHolder {
        return PackageViewHolder(
            ItemPackageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    // 뷰홀더를 데이터와 연결
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: PackageViewHolder, position: Int) {

        val packageItem = packageList[position]

        with(holder.binding){
            Glide.with(packageImage.context)
                .load(packageItem.pImage)
                .into(packageImage)

            packageName.text = packageItem.pName
            packageArea.text = packageItem.area
            packageRating.rating = packageItem.star?: 0F
            packageDate.text = commonHandler.dateHandler(packageItem.pStartDate, packageItem.pEndDate)

            // 카드 클릭시
            root.setOnClickListener {
                itemOnClick(packageItem)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setPackageList(packageItem: List<PackageItem>) {
        packageList = packageItem
        notifyDataSetChanged()
    }

    override fun getItemCount() = packageList.size
}