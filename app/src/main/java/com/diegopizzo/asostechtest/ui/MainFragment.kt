package com.diegopizzo.asostechtest.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.diegopizzo.asostechtest.R
import com.diegopizzo.asostechtest.databinding.FragmentMainBinding
import com.diegopizzo.asostechtest.ui.base.FragmentViewBinding
import com.diegopizzo.network.base.isTrue
import com.diegopizzo.network.model.CompanyInfoDataModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class MainFragment : FragmentViewBinding<FragmentMainBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentMainBinding
        get() = FragmentMainBinding::inflate

    private val viewModel: MainViewModel by sharedViewModel()

    private var mainAdapter: MainAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.viewStates.observe(viewLifecycleOwner, viewStateObserver)
        setRecyclerView()
        viewModel.getSpaceXInformation()
    }

    private val viewStateObserver = Observer<MainViewState> { viewState ->
        if (viewState.isLoading.isTrue()) {
            binding.progressBar.visibility = View.VISIBLE
            loadingCompanyInfo()
        } else {
            binding.progressBar.visibility = View.GONE
        }

        viewState.spaceXInformation?.apply {
            launches?.let { launches -> mainAdapter?.addLaunches(launches) }
            companyInfo?.let { info -> setCompanyInfo(info) }
        }

        if (viewState.isLaunchesNotAvailable.isTrue()) {
            binding.tvEmptyLaunches.visibility = View.VISIBLE
        } else {
            binding.tvEmptyLaunches.visibility = View.GONE
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
        }
    }
}