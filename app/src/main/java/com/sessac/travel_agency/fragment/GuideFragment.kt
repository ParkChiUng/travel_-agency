package com.sessac.travel_agency.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sessac.travel_agency.databinding.FragmentGuideBinding

class GuideFragment : Fragment() {
    private lateinit var binding: FragmentGuideBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)

        binding = FragmentGuideBinding.inflate(inflater, container, false)

        return binding.root

    }
}