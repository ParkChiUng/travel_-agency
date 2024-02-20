package com.sessac.travel_agency.fragment.bottomSheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sessac.travel_agency.R
import com.sessac.travel_agency.adapter.bottomSheet.GuideBSAdapter
import com.sessac.travel_agency.databinding.FragmentGuideAddBinding

//class GuideBSFragment(var adapter: GuideBSAdapter) : BottomSheetDialogFragment() {
class GuideBSFragment() : BottomSheetDialogFragment() {

    private lateinit var guideAddViewBinding: FragmentGuideAddBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        guideAddViewBinding = FragmentGuideAddBinding.inflate(inflater, container, false)

        guideAddViewBinding.buttonNewGuide.setOnClickListener {
            dismiss()
        }

//        btnClose.setOnClickListener {
//            Toast.makeText(mContext, "닫기", Toast.LENGTH_SHORT).show()
//            dismiss()
//        }

        return guideAddViewBinding.root
    }
}