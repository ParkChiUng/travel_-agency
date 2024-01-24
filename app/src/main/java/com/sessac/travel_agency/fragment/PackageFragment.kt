package com.sessac.travel_agency.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.sessac.travel_agency.R
import com.sessac.travel_agency.databinding.FragmentPackageBinding


class PackageFragment : Fragment() {

    private lateinit var binding: FragmentPackageBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)

        binding = FragmentPackageBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.packageTV.setOnClickListener {

            findNavController().navigate(R.id.packageFragment_to_packageAddFragment)
        }
    }
}