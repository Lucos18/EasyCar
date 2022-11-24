package com.example.myapplication.ui.addCar

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.myapplication.BaseApplication
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentAddNewCarBinding
import com.example.myapplication.databinding.FragmentHomeBinding
import com.example.myapplication.model.fuelType
import com.example.myapplication.ui.transformIntoDatePicker
import com.example.myapplication.utils.FuelTypeAlertDialog
import com.example.myapplication.utils.checkForInternet
import com.example.myapplication.utils.showCustomSnackBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import java.text.NumberFormat
import java.util.*


@Suppress("DEPRECATION")
class AddNewCarFragment : Fragment() {

    private var kw: Int = 0
    private var price: Double = 0.0
    private val REQUEST_CODE = 100

    private lateinit var adapter: ArrayAdapter<*>

    private val addNewCarViewModel: AddNewCarViewModel by viewModels {
        AddNewCarViewModelFactory(
            (activity?.application as BaseApplication).database.CarDao()
        )
    }

    private var _binding: FragmentAddNewCarBinding? = null

    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
            addNewCarViewModel.refreshDataFromNetwork()
            _binding = FragmentAddNewCarBinding.inflate(inflater, container, false)
            return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //addNewCarViewModel.addCar("FIAT", "Abarth", 2021, 2022, 5, 8, "diesel", 8.0, R.drawable.ic_baseline_add_24.)
        val navBar: BottomNavigationView = requireActivity().findViewById(R.id.nav_view)
        navBar.visibility = View.GONE
        binding.apply {
            //TODO change when the button will be invisible
            buttonAddNewCar.visibility = View.VISIBLE
            buttonAddNewCar.setOnClickListener {
                addNewCar()
            }
        }
        binding.carBrandAddText.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
                .create()
            val view = layoutInflater.inflate(R.layout.alert_dialog, null)
            val searchText = view.findViewById<SearchView>(R.id.search_view)
            val listViewBrand = view.findViewById<ListView>(R.id.listView)
            adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_list_item_1,
                addNewCarViewModel.getDistinctBrandNames()
            )
            listViewBrand.adapter = adapter
            listViewBrand.onItemClickListener =
                AdapterView.OnItemClickListener { adapterView, view, position, l ->
                    binding.carBrandAddText.setText(
                        listViewBrand.getItemAtPosition(position).toString()
                    )
                    resetText(binding.carYearAddText)
                    binding.carYearAddText.isEnabled = true
                    builder.dismiss()
                }
            listViewBrand.emptyView = view.findViewById(R.id.empty_text_view_search)
            searchText.setOnClickListener {
                searchText.isIconified = false
            }
            searchText.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(p0: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(p0: String?): Boolean {
                    adapter.filter.filter(p0)
                    return true
                }
            })
            builder.setView(view)
            builder.setCanceledOnTouchOutside(false)
            builder.show()
        }
        binding.carYearAddText.transformIntoDatePicker(requireContext(), "yyyy", Date())
        binding.carYearAddText.doOnTextChanged { text, start, before, count ->
            if (binding.carYearAddText.text?.isEmpty() == true) {
                binding.carModelAddText.isEnabled = false
                resetText(binding.carModelAddText)
            } else binding.carModelAddText.isEnabled = true
        }

        binding.carModelAddText.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
                .create()
            val view = layoutInflater.inflate(R.layout.alert_dialog, null)
            val searchText = view.findViewById<SearchView>(R.id.search_view)
            val listViewBrand = view.findViewById<ListView>(R.id.listView)
            adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_list_item_1,
                addNewCarViewModel.getDistinctModelByBrandAndYear(
                    binding.carBrandAddText.text.toString(),
                    binding.carYearAddText.text.toString()
                )
            )
            listViewBrand.adapter = adapter
            listViewBrand.onItemClickListener =
                AdapterView.OnItemClickListener { _, view, position, _ ->
                    binding.carModelAddText.setText(
                        listViewBrand.getItemAtPosition(position).toString()
                    )
                    builder.dismiss()
                }
            listViewBrand.emptyView = view.findViewById(R.id.empty_text_view_search)
            searchText.setOnClickListener {
                searchText.isIconified = false
            }
            searchText.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(p0: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(p0: String?): Boolean {
                    adapter.filter.filter(p0)
                    return true
                }
            })
            builder.setView(view)
            builder.setCanceledOnTouchOutside(false)
            builder.show()
        }

        binding.carPowerAddText.onFocusChangeListener = View.OnFocusChangeListener { _, b ->
            if (!b) {
                val kwText = binding.carPowerAddText.text.toString().toIntOrNull()
                if (kwText != null) {
                    binding.carPowerAddText.setText(
                        getString(
                            R.string.car_power_convert,
                            kwText.toString(),
                            addNewCarViewModel.convertKwToCv(kwText).toString()
                        )
                    )
                    kw = kwText
                }
            }
        }
        binding.carPriceAddText.onFocusChangeListener = View.OnFocusChangeListener { _, b ->
            if (!b) {
                val priceText = binding.carPriceAddText.text.toString().toDoubleOrNull()
                if (priceText != null) {
                    val format: NumberFormat =
                        NumberFormat.getCurrencyInstance(Locale.getDefault())
                    val result: String = format.format(priceText)
                    binding.carPriceAddText.setText(result)
                    price = priceText
                }
            }
        }
        binding.carImage1.setOnClickListener {
            openGalleryForImage()
        }

        binding.carFuelTypeAddText.setOnClickListener {
            FuelTypeAlertDialog(requireContext(), binding.carFuelTypeAddText)
        }
    }

    override fun onDestroyView() {
        val navBar: BottomNavigationView = requireActivity().findViewById(R.id.nav_view)
        navBar.visibility = View.VISIBLE
        super.onDestroyView()
        _binding = null
    }


    private fun addNewCar() {
        if (isValidCar()) {
            addNewCarViewModel.addCar(
                Brand = binding.carBrandAddText.text.toString(),
                YearStartProduction = binding.carYearAddText.text.toString().toInt(),
                YearEndProduction = null,
                Model = binding.carModelAddText.text.toString(),
                FuelType = binding.carFuelTypeAddText.text.toString(),
                Seats = binding.carSeatsAddText.text.toString().toInt(),
                CarPower = kw,
                Price = price,
                Image = checkIfInsertIsNull(createBitmapFromView(binding.carImage1)),
            )
            val action = AddNewCarFragmentDirections
                .actionAddNewCarFragmentToNavigationSell()
            findNavController().navigate(action)
        } else {
            showCustomSnackBar(
                binding.constraintLayoutAddNewCar,
                getString(R.string.error_add_car_toast),
                Snackbar.LENGTH_LONG
            )
        }
    }

    private fun isValidCar(): Boolean {
        return try {
            addNewCarViewModel.checkInputEditTextNewCar(
                binding.carBrandAddText.text.toString(),
                binding.carYearAddText.text.toString().toInt(),
                binding.carModelAddText.text.toString(),
                binding.carFuelTypeAddText.text.toString(),
                kw,
                binding.carSeatsAddText.text.toString().toInt(),
                price
            )
        } catch (e: Exception) {
            showCustomSnackBar(
                binding.constraintLayoutAddNewCar,
                getString(R.string.error_add_car_toast),
                Snackbar.LENGTH_LONG
            )
            false
        }
    }

    private fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
            val selectedImage = data?.data
            if (selectedImage != null) {
                // handle chosen image
                binding.carImage1.setImageURI(data.data)
                binding.carImage1.tag = "is_not_null"
            }
        }
    }

    private fun createBitmapFromView(view: View): Bitmap {
        view.isDrawingCacheEnabled = true
        view.buildDrawingCache()
        return view.drawingCache
    }

    private fun checkIfInsertIsNull(image: Bitmap): Bitmap? {
        return if (binding.carImage1.tag == "is_not_null") {
            image
        } else {
            null
        }
    }

    private fun resetText(binding: TextInputEditText) {
        binding.setText("")
    }
}
