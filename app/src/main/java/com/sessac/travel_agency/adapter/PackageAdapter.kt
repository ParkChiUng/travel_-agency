package com.sessac.travel_agency.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sessac.travel_agency.common.CommonHandler
import com.sessac.travel_agency.data.LodgingItem
import com.sessac.travel_agency.data.PackageItem
import com.sessac.travel_agency.databinding.ItemPackageBinding
import kotlinx.coroutines.CoroutineScope

//tutor ListAdapter
class PackageAdapter(
    val itemOnClick: (PackageItem) -> (Unit)
    ,private val scope: CoroutineScope
) :
    ListAdapter<PackageItem, PackageAdapter.PackageViewHolder>(diffUtil) {

    private val commonHandler = CommonHandler.generateCommonHandler()

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<PackageItem>() {
            override fun areItemsTheSame(oldItem: PackageItem, newItem: PackageItem): Boolean {
                return oldItem.packageId == newItem.packageId
            }

            override fun areContentsTheSame(oldItem: PackageItem, newItem: PackageItem): Boolean {
                return oldItem == newItem
            }
        }
    }

    inner class PackageViewHolder(val binding: ItemPackageBinding) : RecyclerView.ViewHolder(binding.root){

        fun widgetBinding(packageList: PackageItem) {
            packageList.let {packageItem ->
                with(binding){
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
        }
    }

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
    override fun onBindViewHolder(holder: PackageViewHolder, position: Int) {
        holder.widgetBinding(getItem(position))
    }

    fun setPackageList(packageItem: List<PackageItem>) {
        submitList(packageItem)
    }
}