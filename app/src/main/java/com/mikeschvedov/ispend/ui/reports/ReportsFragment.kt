package com.mikeschvedov.ispend.ui.reports

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mikeschvedov.ispend.R
import com.mikeschvedov.ispend.databinding.FragmentReportsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReportsFragment : Fragment() {

    private var _binding: FragmentReportsBinding? = null
    private val binding get() = _binding!!

    lateinit var reportsViewModel: ReportsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        /* ViewModel */
        reportsViewModel =
            ViewModelProvider(this).get(ReportsViewModel::class.java)

        /* Binding */
        _binding = FragmentReportsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        /* Adapter */
        val adapter = reportsViewModel.getRecyclerAdapter()
        binding.recyclerviewReports.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerviewReports.adapter = adapter

        setupSpinner()

        return root
    }

    private fun setupSpinner() {
        // Array of months
        val monthsArray = resources.getStringArray(R.array.monthly_reports)
        // Setting up the reports adapter
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, monthsArray)
        // Set Adapter
        val inputLayout = binding.autoCompleteTextViewReports
        inputLayout.setAdapter(arrayAdapter)

        var selectedMonth = ""
        var selectedPosition = -1
        // When we select a category
        inputLayout.onItemClickListener =
            AdapterView.OnItemClickListener { p0, p1, position, p3 ->
                // Store the selected month
                selectedPosition = position
                selectedMonth = monthsArray[position]
            }

        // Create Report
        binding.createReportBtn.setOnClickListener {

            println("selected month: $selectedMonth") /////// ---------- REMOVE AFTER TESTING
            println("selected position/id: $selectedPosition") /////// ---------- REMOVE AFTER TESTING

            reportsViewModel.createReport(selectedPosition)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}