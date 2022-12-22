package com.example.myapplication.ui.search

import android.os.Bundle
import android.view.KeyEvent
import android.view.KeyEvent.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.myapplication.BaseApplication
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentSearchBinding
import com.example.myapplication.enums.CarFiltersSearch
import com.example.myapplication.model.Car
import com.example.myapplication.utils.carListItemsAlertDialog
import com.example.myapplication.utils.showCustomSnackBar
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class SearchFragment : Fragment() {
    val searchViewModel: SearchViewModel by activityViewModels {
        SearchViewModelFactory(
            (activity?.application as BaseApplication).database.CarDao()
        )
    }
    private var _binding: FragmentSearchBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        searchViewModel.refreshDataFromNetwork()
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val observer = Observer<List<Car>> {
            if (searchViewModel.filteredList != null) {
                binding.searchCarsButton.text = getString(
                    R.string.button_result_text,
                    searchViewModel.filteredList?.size.toString()
                )
            } else {
                binding.searchCarsButton.text = getString(
                    R.string.button_result_text,
                    searchViewModel.allCars.value?.size.toString()
                )
                searchViewModel.filteredList = searchViewModel.allCars.value?.toList()
            }
        }
        searchViewModel.allCars.observe(viewLifecycleOwner, observer)

        binding.startingPriceSearchText.doOnTextChanged { _, _, _, _ ->
            if (binding.startingPriceSearchText.text.toString().isNotEmpty()) {
                searchViewModel.onStartingPriceChange(
                    binding.startingPriceSearchText.text.toString().toDouble()
                )
            }
        }
        binding.startingPriceSearch.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            onKey(v,keyCode,event, binding.startingPriceSearch, binding.endingPriceSearch)
        })
        binding.endingPriceSearchText.doOnTextChanged { _, _, _, _ ->
            if (binding.endingPriceSearchText.text.toString().isNotEmpty()) {
                searchViewModel.onEndingPriceChange(
                    binding.endingPriceSearchText.text.toString().toDouble()
                )
            } else searchViewModel.onEndingPriceChange(
                0.0
            )
        }
        binding.carSearchBrandText.setOnClickListener {
            carListItemsAlertDialog(
                requireContext(),
                layoutInflater,
                searchViewModel.getDistinctBrandNames(),
                binding.carSearchBrandText,
                binding.carSearchModelText
            )
        }
        binding.carSearchBrandText.doOnTextChanged { _, _, _, _ ->
            if (binding.carSearchBrandText.text.toString().isNotEmpty()) {
                binding.carSearchModelText.isEnabled = true
                searchViewModel.onBrandChange(binding.carSearchBrandText.text.toString())
            } else
            {
                binding.carSearchModelText.isEnabled = false
                searchViewModel.mapFilters[CarFiltersSearch.BRAND] = false
                searchViewModel.filterListOfCars()
            }
        }
        binding.carSearchModelText.setOnClickListener {
            carListItemsAlertDialog(
                requireContext(),
                layoutInflater,
                searchViewModel.getDistinctModelByBrand(binding.carSearchBrandText.text.toString()),
                binding.carSearchModelText,
                null
            )
        }
        binding.carSearchModelText.doOnTextChanged { _, _, _, _ ->
            if (binding.carSearchModelText.text.toString().isNotEmpty()) {
                searchViewModel.onModelChange(binding.carSearchModelText.text.toString())
            }
        }
        binding.buttonGroupVehicleState.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    binding.newCarButton.id -> {
                        searchViewModel.onStateCarCheckChange(
                            binding.newCarButton.text.toString(),
                            true
                        )
                    }
                    binding.usedCarButton.id -> {
                        searchViewModel.onStateCarCheckChange(
                            binding.usedCarButton.text.toString(),
                            true
                        )
                    }
                }
            } else when (checkedId) {
                binding.usedCarButton.id -> {
                    searchViewModel.onStateCarCheckChange(
                        binding.usedCarButton.text.toString(),
                        false
                    )
                }
                binding.newCarButton.id -> {
                    searchViewModel.onStateCarCheckChange(
                        binding.newCarButton.text.toString(),
                        false
                    )
                }
            }
        }
        binding.buttonGroupVehiclePowerType.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                binding.KwFilter.id -> searchViewModel.multiplierPower = 1.00
                binding.CVFilter.id -> searchViewModel.multiplierPower = 1.36
            }
            searchViewModel.filterListOfCars()
        }
        binding.carSearchPowerStartingText.doOnTextChanged { _, _, _, _ ->
            if (binding.carSearchPowerStartingText.text.toString().isNotEmpty()) {
                searchViewModel.onStartingPowerChange(
                    binding.carSearchPowerStartingText.text.toString().toDouble()
                )
            } else searchViewModel.mapFilters[CarFiltersSearch.MIN_POWER] = false
        }
        binding.carSearchPowerEndingText.doOnTextChanged { _, _, _, _ ->
            if (binding.carSearchPowerEndingText.text.toString().isNotEmpty()) {
                searchViewModel.onEndingPowerChange(
                    binding.carSearchPowerEndingText.text.toString().toDouble()
                )
            } else searchViewModel.onEndingPowerChange(
                0.0
            )
        }
        binding.searchCarsButton.setOnClickListener {
            val minPower = binding.carSearchPowerStartingText.text.toString().toDoubleOrNull()
            val maxPower = binding.carSearchPowerEndingText.text.toString().toDoubleOrNull()
            val minPrice = binding.startingPriceSearchText.text.toString().toDoubleOrNull()
            val maxPrice = binding.endingPriceSearchText.text.toString().toDoubleOrNull()
            if (searchViewModel.checkMaxIsNotMinorOfMin(minPower, maxPower)) {
                if (searchViewModel.checkMaxIsNotMinorOfMin(minPrice, maxPrice)) {
                    val action = SearchFragmentDirections
                        .actionNavigationSearchToSearchResults()
                    findNavController().navigate(action)
                } else {
                    binding.endingPriceSearchText.error =
                        getString(R.string.error_max_minor_than_minimum)
                    showErrorSnackBar()
                }
            } else {
                binding.carSearchPowerEndingText.error =
                    getString(R.string.error_max_minor_than_minimum)
                showErrorSnackBar()
            }
        }
        binding.dieselFilter.setOnCheckedChangeListener { _, isChecked ->
            searchViewModel.onCheckDieselFilter(isChecked)
        }
        binding.electricFilter.setOnCheckedChangeListener { _, isChecked ->
            searchViewModel.onCheckElectricFilter(isChecked)
        }
        binding.petrolFilter.setOnCheckedChangeListener { _, isChecked ->
            searchViewModel.onCheckPetrolFilter(isChecked)
        }
        binding.gasFilter.setOnCheckedChangeListener { _, isChecked ->
            searchViewModel.onCheckGasFilter(isChecked)
        }
        binding.checkboxAtLeastOnePhoto.setOnClickListener {
            searchViewModel.onCheckBoxChange(binding.checkboxAtLeastOnePhoto.isChecked)
        }
        searchViewModel.allCars.observe(this.viewLifecycleOwner) { }
        searchViewModel.currentNumberOfResults.observe(this.viewLifecycleOwner) {
            if (searchViewModel.currentNumberOfResults.value != 0) {
                binding.searchCarsButton.text =
                    getString(R.string.button_result_text, it.toString())
            } else {
                binding.searchCarsButton.text =
                    getString(
                        R.string.button_result_text,
                        searchViewModel.filteredList?.size.toString()
                    )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun showErrorSnackBar() {
        showCustomSnackBar(
            constraintLayout = binding.mainConstraintSearchCars,
            getString(R.string.error_check_inputs),
            Snackbar.LENGTH_LONG
        )
    }
    fun onKey(v: View?, keyCode: Int, event: KeyEvent, text1: TextInputLayout, text2: TextInputLayout): Boolean {
        // If the event is a key-down event on the "enter" button
        if (event.action == ACTION_DOWN &&
            keyCode == KEYCODE_ENTER
        ) {
            // Perform action on Enter key press
            text1.clearFocus()
            text2.requestFocus()
            return true
        }
        return false
    }
}