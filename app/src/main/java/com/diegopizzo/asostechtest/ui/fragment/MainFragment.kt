package com.diegopizzo.asostechtest.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.diegopizzo.asostechtest.R
import com.diegopizzo.asostechtest.databinding.FragmentMainBinding
import com.diegopizzo.asostechtest.ui.FilterDialogViewState
import com.diegopizzo.asostechtest.ui.MainAdapter
import com.diegopizzo.asostechtest.ui.MainViewModel
import com.diegopizzo.asostechtest.ui.MainViewState
import com.diegopizzo.asostechtest.ui.base.FragmentViewBinding
import com.diegopizzo.network.base.isTrue
import com.diegopizzo.network.model.CompanyInfoDataModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.core.parameter.parametersOf

class MainFragment : FragmentViewBinding<FragmentMainBinding>(), MainAdapter.AdapterEvent {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentMainBinding
        get() = FragmentMainBinding::inflate

    private val viewModel: MainViewModel by sharedViewModel {
        parametersOf(findNavController())
    }

    private var mainAdapter: MainAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.viewStates.observe(viewLifecycleOwner, viewStateObserver)
        viewModel.filterViewStates.observe(viewLifecycleOwner, filterViewStateObserver)
        setRecyclerView()
        onEmptyDataClickListener()
        viewModel.getSpaceXInformation()
    }

    private val viewStateObserver = Observer<MainViewState> { viewState ->
        viewState.apply {
            if (isLoading.isTrue()) {
                binding.progressBar.visibility = View.VISIBLE
                loadingCompanyInfo()
            } else {
                binding.progressBar.visibility = View.GONE
            }

            spaceXInformation?.apply {
                launches?.let { launches ->
                    mainAdapter?.submitList(launches, viewModel.filterViewState.queryFilter)
                }
                companyInfo?.let { info -> setCompanyInfo(info) }
            }

            if (isDataNotAvailable) {
                binding.tvEmptyData.visibility = View.VISIBLE
            } else {
                binding.tvEmptyData.visibility = View.GONE
            }

            if (isLaunchesNotAvailable) {
                binding.tvEmptyLaunches.visibility = View.VISIBLE
            } else {
                binding.tvEmptyLaunches.visibility = View.GONE
            }
        }
    }

    private val filterViewStateObserver = Observer<FilterDialogViewState> { filterViewState ->
        filterViewState.apply {
            if (isFilterPerforming) {
                viewModel.viewState.spaceXInformation?.launches?.let {
                    mainAdapter?.submitList(it, queryFilter)
                }
            }
        }
    }

    private fun setCompanyInfo(companyInfo: CompanyInfoDataModel) {
        val notAvailable = getString(R.string.not_available)
        companyInfo.apply {
            setCompanyInfo(
                companyName ?: notAvailable,
                founder ?: notAvailable,
                founded ?: notAvailable,
                employees ?: notAvailable,
                launchSites ?: notAvailable,
                valuation ?: notAvailable
            )
        }
    }

    private fun setCompanyInfo(
        companyName: String,
        founder: String,
        founded: String,
        employees: String,
        launchSites: String,
        valuation: String
    ) {
        binding.tvCompanyInfo.text = getString(
            R.string.company_info,
            companyName,
            founder,
            founded,
            employees,
            launchSites,
            valuation
        )
    }

    private fun loadingCompanyInfo() {
        val notAvailable = getString(R.string.not_available)
        setCompanyInfo(
            notAvailable,
            notAvailable,
            notAvailable,
            notAvailable,
            notAvailable,
            notAvailable
        )
    }

    private fun setRecyclerView() {
        mainAdapter = MainAdapter(requireContext()) { article ->
            viewModel.openArticle(article)
        }
        binding.recyclerView.apply {
            adapter = mainAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
        mainAdapter?.onAdapterEvent = this
    }

    private fun onEmptyDataClickListener() {
        binding.tvEmptyData.setOnClickListener {
            viewModel.retry()
        }
    }

    override fun onEmptyListFiltered() {
        binding.tvEmptyLaunches.visibility = View.VISIBLE
    }

    override fun onSuccessListFiltered() {
        binding.tvEmptyLaunches.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
        binding.tvEmptyLaunches.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mainAdapter?.onAdapterEvent = null
    }
}