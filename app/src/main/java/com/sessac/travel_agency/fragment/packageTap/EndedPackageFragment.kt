package com.sessac.travel_agency.fragment.packageTap

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sessac.travel_agency.R
import com.sessac.travel_agency.adapter.PackageAdapter
import com.sessac.travel_agency.common.CommonHandler
import com.sessac.travel_agency.data.PackageItem
import com.sessac.travel_agency.databinding.FragmentOngoingPackageBinding
import com.sessac.travel_agency.repository.PackageRepository
import com.sessac.travel_agency.viewmodels.PackageViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * 패키지(홈)의 종료 페이지
 */
class EndedPackageFragment : Fragment() {

    private lateinit var binding: FragmentOngoingPackageBinding

    // 리사이클러뷰
    private lateinit var recyclerView: RecyclerView
    private lateinit var packageAdapter: PackageAdapter

    private val viewModel: PackageViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)

        binding = FragmentOngoingPackageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        commonHandler = CommonHandler.generateCommonHandler()
//        commonHandler.imageCallback(requireActivity().activityResultRegistry)

        val currentTime = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
        val formattedTime = dateFormat.format(currentTime)

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.getPastPackages(currentTime)
            }
        }

        //플로팅버튼
        val fab: View = binding.fab
        fab.setOnClickListener {
            findNavController().navigate(R.id.packageFragment_to_packageAddFragment)
        }

        setupRecyclerviewAdapter()
//        setupFloatingButton()
        setupObserver()
    }
    private fun setupObserver() {
        viewModel.packageLists.observe(viewLifecycleOwner) { packageItem ->
            packageItem.let {
                packageAdapter.setPackageList(it)
            }
        }
    }

    private fun setupRecyclerviewAdapter() {

        recyclerView = binding.ongoingPackageRecyclerview
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(activity)

        packageAdapter = PackageAdapter {packageItem ->
            val bundle = Bundle().apply {
                putParcelable("packageItem", packageItem)
            }
            findNavController().navigate(R.id.packageFragment_to_packageAddFragment, bundle)
        }
        recyclerView.adapter = packageAdapter
    }
}