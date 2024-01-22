package com.sessac.travel_agency.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sessac.travel_agency.databinding.FragmentLodgingBinding

class LodgingFragment: Fragment() {

    private lateinit var binding: FragmentLodgingBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)

        binding = FragmentLodgingBinding.inflate(inflater, container, false)

        return binding.root

    }
}