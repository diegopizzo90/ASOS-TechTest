package com.diegopizzo.asostechtest.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.diegopizzo.asostechtest.R
import com.diegopizzo.asostechtest.databinding.FragmentDialogBinding
import com.diegopizzo.asostechtest.ui.FilterObject
import com.diegopizzo.asostechtest.ui.MainViewModel
import com.diegopizzo.asostechtest.util.Utils
import com.whiteelephant.monthpicker.MonthPickerDialog
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class FilterDialogFragment : DialogFragment() {

    private var _binding: FragmentDialogBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setFilterValue()
        setClickListeners()
        onMultiButtonSelection()
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        dialog?.setCanceledOnTouchOutside(false)
        dialog?.setCancelable(false)
    }

    private fun setFilterValue() {
        viewModel.filterViewState.apply {
            setFromYearValue(fromYearFilterSelected)
            setToYearValue(toYearFilterSelected)

            binding.multiButtonSorting.setElements(R.array.order_array, sortingSelected.second)

            binding.multiButtonLaunchOutcome.setElements(
                R.array.launch_outcome_array,
                launchOutcomeFilterSelected.second
            )
        }
    }


    private fun onMultiButtonSelection() {
        binding.multiButtonLaunchOutcome.setOnValueChangedListener {
            val item = when (it) {
                0 -> null
                1 -> FilterObject.LaunchOutcome.SUCCESS_LAUNCH
                else -> FilterObject.LaunchOutcome.FAILED_LAUNCH
            }
            viewModel.onLaunchOutcomeFilterSelected(Pair(item, it))
        }
        binding.multiButtonSorting.setOnValueChangedListener {
            val item = when (it) {
                0 -> null
                1 -> FilterObject.Sorting.ASC
                else -> FilterObject.Sorting.DESC
            }
            viewModel.onSortingSelected(Pair(item, it))
        }
    }

    private fun showFromDatePickerDialog() {
        val actualYear = Utils.getActualYear()
        val fromYearSelected = viewModel.filterViewState.fromYearFilterSelected
        val toYearSelected = viewModel.filterViewState.toYearFilterSelected
        val activatedYear: Int = fromYearSelected ?: toYearSelected ?: actualYear
        val maxYear: Int = toYearSelected ?: actualYear + 20
        MonthPickerDialog.Builder(
            requireContext(),
            { _, selectedYear ->
                setFromYearValue(selectedYear)
                viewModel.onFromYearFilterSelected(selectedYear)
            },
            activatedYear,
            0
        ).showYearOnly()
            .setMinYear(actualYear - 20)
            .setMaxYear(maxYear)
            .setTitle(getString(R.string.from_year_selected))
            .build()
            .show()
    }

    private fun showToDatePickerDialog() {
        val actualYear = Utils.getActualYear()
        val fromYearSelected = viewModel.filterViewState.fromYearFilterSelected
        val toYearSelected = viewModel.filterViewState.toYearFilterSelected
        val activatedYear: Int = toYearSelected ?: fromYearSelected ?: actualYear
        val minYear: Int = fromYearSelected ?: actualYear - 20
        MonthPickerDialog.Builder(
            requireContext(),
            { _, selectedYear ->
                setToYearValue(selectedYear)
                viewModel.onToYearFilterSelected(selectedYear)
            },
            activatedYear,
            0
        ).showYearOnly()
            .setMinYear(minYear)
            .setMaxYear(actualYear + 20)
            .setTitle(getString(R.string.to_year_selected))
            .build()
            .show()
    }

    private fun setClickListeners() {
        binding.apply {
            btnFromDatePicker.setOnClickListener {
                showFromDatePickerDialog()
            }
            btnFromDatePickerReset.setOnClickListener {
                setFromYearValue(null)
                viewModel.onFromYearFilterSelected(null)
            }
            btnToDatePicker.setOnClickListener {
                showToDatePickerDialog()
            }
            btnToDatePickerReset.setOnClickListener {
                setToYearValue(null)
                viewModel.onToYearFilterSelected(null)
            }
            btnCancel.setOnClickListener {
                dialog?.cancel()
            }

            btnConfirm.setOnClickListener {
                viewModel.filterLaunches()
                dialog?.cancel()
            }
        }
    }

    private fun setFromYearValue(year: Int?) {
        binding.tvFromYearSelected.text = year?.toString() ?: getString(R.string.any_year)
    }

    private fun setToYearValue(year: Int?) {
        binding.tvToYearSelected.text = year?.toString() ?: getString(R.string.any_year)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}