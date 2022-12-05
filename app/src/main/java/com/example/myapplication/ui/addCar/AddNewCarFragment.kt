package com.example.myapplication.ui.addCar

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.SearchView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.myapplication.BaseApplication
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentAddNewCarBinding
import com.example.myapplication.enums.CarAddInputs
import com.example.myapplication.enums.CarColors
import com.example.myapplication.ui.transformIntoDatePicker
import com.example.myapplication.utils.FuelTypeAlertDialog
import com.example.myapplication.utils.carListItemsAlertDialog
import com.example.myapplication.utils.checkForInternet
import com.example.myapplication.utils.showCustomSnackBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.text.NumberFormat
import java.util.*

@Suppress("DEPRECATION")
class AddNewCarFragment : Fragment() {

    private var kw: Int = 0
    private var price: Double = 0.0
    private val REQUEST_CODE = 100

    private lateinit var adapter: ArrayAdapter<*>

    private val mapInputs = mutableMapOf<CarAddInputs, Boolean>()

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
        _binding = FragmentAddNewCarBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("InflateParams")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        swapConstraintIfInternet(requireContext())
        val navBar: BottomNavigationView = requireActivity().findViewById(R.id.nav_view)
        navBar.visibility = View.GONE
        binding.apply {
            buttonAddNewCar.visibility = View.VISIBLE
            buttonAddNewCar.setOnClickListener {
                addNewCar()
            }
        }
        binding.carBrandAddText.setOnClickListener {
            carListItemsAlertDialog(
                requireContext(),
                layoutInflater,
                addNewCarViewModel.getDistinctBrandNames(),
                binding.carBrandAddText,
                binding.carYearAddText
            )
        }
        binding.carYearAddText.transformIntoDatePicker(requireContext(), "yyyy", Date())
        binding.carYearAddText.doOnTextChanged { _, _, _, _ ->
            if (binding.carYearAddText.text?.isEmpty() == true) {
                binding.carModelAddText.isEnabled = false
                resetText(binding.carModelAddText)
            } else binding.carModelAddText.isEnabled = true
        }

        binding.carModelAddText.setOnClickListener {
            carListItemsAlertDialog(
                requireContext(), layoutInflater, addNewCarViewModel.getDistinctModelByBrandAndYear(
                    binding.carBrandAddText.text.toString(),
                    binding.carYearAddText.text.toString()
                ), binding.carModelAddText, null
            )
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
        binding.carColorAddText.addTextChangedListener {
            val drawable = AppCompatResources.getDrawable(requireContext(), R.drawable.circle_shape)
            val wrappedDrawable = drawable?.let { DrawableCompat.wrap(it) }
            wrappedDrawable?.setBounds(0, 0, 70, 70)
            val color =
                CarColors.values().first { it.nameColor == binding.carColorAddText.text.toString() }
            Log.d("color", color.toString())
            wrappedDrawable?.setTint(color.rgbColor)
            binding.carColorAddText.setCompoundDrawables(null, null, wrappedDrawable, null)
        }
        binding.carColorAddText.setOnClickListener {

            val values: Array<CarColors> = CarColors.values()
            val items = arrayOfNulls<CharSequence>(values.size)
            for (i in values.indices) {
                items[i] = values[i].nameColor
            }
            val builder = AlertDialog.Builder(requireContext())
                .create()
            val view = layoutInflater.inflate(R.layout.alert_dialog, null)
            val searchText = view.findViewById<SearchView>(R.id.search_view)
            val listViewBrand = view.findViewById<ListView>(R.id.listView)
            adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_list_item_1,
                items
            )
            listViewBrand.adapter = adapter
            listViewBrand.onItemClickListener =
                AdapterView.OnItemClickListener { _, _, position, _ ->
                    binding.carColorAddText.setText(
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
            builder.setCanceledOnTouchOutside(true)
            builder.setView(view)
            builder.show()

        }
        //TODO 4 Add more information to details card of the car (Color)
        //TODO 5 Fix Price on focus listener that shows error if not changed
        //TODO Change calendar function to show only year options
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
                Mileage = binding.carMileageAddText.text.toString().toDouble(),
                Image = checkIfInsertIsNull(createBitmapFromView(binding.carImage1)),
                Color = binding.carColorAddText.text.toString()
            )
            val action = AddNewCarFragmentDirections
                .actionAddNewCarFragmentToNavigationSell()
            findNavController().navigate(action)
        } else {
            showCustomSnackBar(
                binding.mainConstraintLayoutAddNewCar,
                getString(R.string.error_add_car_toast),
                Snackbar.LENGTH_LONG
            )
        }
    }

    private fun isValidCar(): Boolean {

        mapInputs[CarAddInputs.Brand] =
            addNewCarViewModel.checkBrandInput(binding.carBrandAddText.text.toString())
        mapInputs[CarAddInputs.Year] =
            addNewCarViewModel.checkYearInput(binding.carYearAddText.text.toString().toIntOrNull())
        mapInputs[CarAddInputs.Model] =
            addNewCarViewModel.checkModelInput(binding.carModelAddText.text.toString())
        mapInputs[CarAddInputs.Fuel] =
            addNewCarViewModel.checkFuelInput(binding.carFuelTypeAddText.text.toString())
        mapInputs[CarAddInputs.Power] = addNewCarViewModel.checkPowerInput(kw)
        mapInputs[CarAddInputs.Seats] = addNewCarViewModel.checkSeatsInput(
            binding.carSeatsAddText.text.toString().toIntOrNull()
        )
        mapInputs[CarAddInputs.Price] = addNewCarViewModel.checkPriceInput(price)
        mapInputs[CarAddInputs.Mileage] = addNewCarViewModel.checkMileageInput(
            binding.carMileageAddText.text.toString().toDoubleOrNull()
        )
        mapInputs[CarAddInputs.Colors] =
            addNewCarViewModel.checkColorInput(binding.carColorAddText.text.toString())
        setInputs(mapInputs)
        return !mapInputs.containsValue(false)
    }

    private fun setInputs(map: Map<CarAddInputs, Boolean>) {
        map.forEach { (k, v) ->
            val errorToShow = if (v) null else getString(R.string.error_add_new_car_text)
            when (k) {
                CarAddInputs.Brand -> setErrorShown(binding.carBrandAddLabel, errorToShow)
                CarAddInputs.Year -> setErrorShown(binding.carYearAddLabel, errorToShow)
                CarAddInputs.Model -> setErrorShown(binding.carModelAddLabel, errorToShow)
                CarAddInputs.Fuel -> setErrorShown(binding.carFuelTypeAddLabel, errorToShow)
                CarAddInputs.Power -> setErrorShown(binding.carPowerAddLabel, errorToShow)
                CarAddInputs.Seats -> setErrorShown(binding.carSeatsAddLabel, errorToShow)
                CarAddInputs.Price -> setErrorShown(binding.carPriceAddLabel, errorToShow)
                CarAddInputs.Mileage -> setErrorShown(binding.carMileageAddLabel, errorToShow)
                CarAddInputs.Colors -> setErrorShown(binding.carColorAddLabel, errorToShow)
            }
        }
    }

    private fun setErrorShown(text: TextInputLayout, errorToShow: String?) {
        text.error = errorToShow
        text.isErrorEnabled = errorToShow != null
    }

    private fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE)
    }

    @Deprecated("Deprecated in Java")
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

    private fun swapConstraintIfInternet(context: Context) {
        if (checkForInternet(context)) {
            addNewCarViewModel.refreshDataFromNetwork()
            binding.constraintLayoutAddNewCarWithConnection.visibility = View.VISIBLE
            binding.constraintLayoutAddNewCarWithoutConnection.visibility = View.GONE
        } else {
            binding.constraintLayoutAddNewCarWithConnection.visibility = View.GONE
            binding.constraintLayoutAddNewCarWithoutConnection.visibility = View.VISIBLE
            binding.retryAgainErrorConnection.setOnClickListener {
                swapConstraintIfInternet(context)
            }
        }
    }
}
