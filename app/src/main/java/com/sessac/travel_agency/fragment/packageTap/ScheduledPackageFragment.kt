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
import com.sessac.travel_agency.data.PackageItem
import com.sessac.travel_agency.databinding.FragmentOngoingPackageBinding
import com.sessac.travel_agency.fragment.ViewBindingBaseFragment
import com.sessac.travel_agency.viewmodels.PackageViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * 패키지(홈)의 예정 페이지
 */
class ScheduledPackageFragment :
    ViewBindingBaseFragment<FragmentOngoingPackageBinding>(FragmentOngoingPackageBinding::inflate) {

    // 리사이클러뷰
    private lateinit var recyclerView: RecyclerView
    private lateinit var packageAdapter: PackageAdapter

    private val viewModel: PackageViewModel by viewModels()

    companion object {
        fun  newInstance() = ScheduledPackageFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentTime = Calendar.getInstance().time

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.getFuturePackages(currentTime)
            }
        }

        //플로팅버튼
        val fab: View = binding.fab
        fab.setOnClickListener {
            findNavController().navigate(R.id.packageFragment_to_packageAddFragment)
        }

        setupRecyclerviewAdapter()
        setupObserver()
    }

    private fun setupObserver() {
        viewModel.packageLists.observe(viewLifecycleOwner) { packageItem ->
            packageItem.let {
                if (packageItem.isNotEmpty()) {
                    packageAdapter.setPackageList(it)
                    binding.emptyView.visibility = View.GONE
                    binding.ongoingPackageRecyclerview.visibility = View.VISIBLE
                } else {
                    binding.emptyView.visibility = View.VISIBLE
                    binding.ongoingPackageRecyclerview.visibility = View.GONE
                }
            }
        }
    }

    private fun setupRecyclerviewAdapter() {
        //tutor pyo
        recyclerView = binding.ongoingPackageRecyclerview
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(activity)

        packageAdapter = PackageAdapter { packageItem ->
            Bundle().apply {
                putParcelable("packageItem", packageItem)
                findNavController().navigate(R.id.packageFragment_to_packageAddFragment, this)
            }
        }
        recyclerView.adapter = packageAdapter
    }
}