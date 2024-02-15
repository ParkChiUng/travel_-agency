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
import com.sessac.travel_agency.databinding.FragmentOngoingPackageBinding
import com.sessac.travel_agency.viewmodels.PackageViewModel
import kotlinx.coroutines.launch
import java.util.Calendar

/**
 * 패키지(홈)의 종료 페이지
 */
//tutor pyo BaseFragment 로 상속받아 바꾸기
class EndedPackageFragment : Fragment() {

    private lateinit var binding: FragmentOngoingPackageBinding

    private lateinit var packageAdapter: PackageAdapter

    private val viewModel: PackageViewModel by viewModels()
    companion object {
       fun  newInstance() = EndedPackageFragment()
    }
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

        val currentTime = Calendar.getInstance().time

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
        setupObserver()
    }
    private fun setupObserver() {
        viewModel.packageLists.observe(viewLifecycleOwner) { packageItem ->
            packageItem.let {
                if(packageItem.isNotEmpty()){
                    packageAdapter.setPackageList(it)
                    //tutor pyo
                    binding.emptyView.visibility = View.GONE
                    binding.ongoingPackageRecyclerview.visibility = View.VISIBLE
                }else{
                    //tutor pyo
                    binding.emptyView.visibility = View.VISIBLE
                    binding.ongoingPackageRecyclerview.visibility = View.GONE
                }
            }
        }
    }

    private fun setupRecyclerviewAdapter() {
        //tutor pyo
        with(binding.ongoingPackageRecyclerview){
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireActivity())
            packageAdapter = PackageAdapter {packageItem ->
                val bundle = Bundle().apply {
                    putParcelable("packageItem", packageItem)
                }
                findNavController().navigate(R.id.packageFragment_to_packageAddFragment, bundle)
            }
            adapter = packageAdapter
        }
    }
}