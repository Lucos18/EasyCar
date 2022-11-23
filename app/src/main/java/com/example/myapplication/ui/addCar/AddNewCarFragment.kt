package com.example.myapplication.ui.addCar

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.HorizontalScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.databinding.adapters.TextViewBindingAdapter.setText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.myapplication.BaseApplication
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentAddNewCarBinding
import com.example.myapplication.model.fuelType
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.textfield.TextInputEditText
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*


class AddNewCarFragment : Fragment() {
    private var kw: Int = 0
    private var price: Double = 0.0
    private var selectedImageGallery: Uri? = null
    private val REQUEST_CODE = 100

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
    ): View? {
        _binding = FragmentAddNewCarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addNewCarViewModel.refreshDataFromNetwork()
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
            val listItems = addNewCarViewModel.getDistinctBrandNames()
            val items = arrayOfNulls<CharSequence>(listItems.size)
            for (i in listItems.indices) {
                items[i] = listItems[i]
            }
            val builder: AlertDialog.Builder = AlertDialog.Builder(context)
            builder.setTitle("choose brand")
            builder.setSingleChoiceItems(
                items,
                addNewCarViewModel.checkedItemBrand
            ) { _: DialogInterface, which ->
                addNewCarViewModel.checkedItemBrand = which
            }
            builder.setItems(items) { _: DialogInterface, which ->
                addNewCarViewModel.checkedItemBrand = which
            }
            builder.setPositiveButton("Ok") { _: DialogInterface, _ ->
                binding.carBrandAddText.setText(items[addNewCarViewModel.checkedItemBrand].toString())
                binding.carYearAddText.isEnabled = true
            }
            builder.setNegativeButton("Cancel") { _: DialogInterface, _ ->
                binding.carBrandAddText.setText("")
            }
            builder.show()
        }

        binding.carYearAddText.transformIntoDatePicker(requireContext(), "yyyy", Date())
        binding.carYearAddText.doOnTextChanged { text, start, before, count ->
            if (binding.carYearAddText.text?.isEmpty() == true)
            {
                binding.carModelAddText.isEnabled = false
                binding.carModelAddText.setText("")
            } else binding.carModelAddText.isEnabled = true
        }
/*
        binding.carModelAddText.setOnClickListener {
            val listItems = addNewCarViewModel.getDistinctModelByBrandAndYear(
                binding.carBrandAddText.text.toString(),
                binding.carYearAddText.text.toString()
            )
            val items = arrayOfNulls<CharSequence>(listItems.size)
            for (i in listItems.indices) {
                items[i] = listItems[i]
            }
            val builder: AlertDialog.Builder = AlertDialog.Builder(context)
            builder.setTitle("choose brand")
            builder.setSingleChoiceItems(
                items,
                addNewCarViewModel.checkedItemBrand
            ) { _: DialogInterface, which ->
                addNewCarViewModel.checkedItemBrand = which
            }
            builder.setItems(items) { _: DialogInterface, which ->
                addNewCarViewModel.checkedItemBrand = which
            }
            builder.setPositiveButton("Ok") { _: DialogInterface, _ ->
                binding.carBrandAddText.setText(items[addNewCarViewModel.checkedItemBrand].toString())
                binding.carYearAddText.isEnabled = true
            }
            builder.setNegativeButton("Cancel") { _: DialogInterface, _ ->
                binding.carBrandAddText.setText("")
            }
            builder.show()
        }

 */

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
        /*
        binding.carImage2.setOnClickListener {
            openGalleryForImage()
            selectedImage = null
            carImage2.setImageURI(selectedImage)
        }

         */

        binding.carFuelTypeAddText.setOnClickListener {
            val values: Array<fuelType> = fuelType.values()
            val items = arrayOfNulls<CharSequence>(values.size)
            for (i in values.indices) {
                items[i] = values[i].toString()
            }
            val builder: AlertDialog.Builder = AlertDialog.Builder(context)
            builder.setTitle("Choose fuel type")
            builder.setSingleChoiceItems(items, 0) { dialogInterface: DialogInterface, which ->
                binding.carFuelTypeAddText.setText(items[which].toString())
            }
            builder.setItems(items) { _: DialogInterface, which ->
                binding.carFuelTypeAddText.setText(items[which].toString())
            }
            builder.setPositiveButton("OK") { _: DialogInterface, _ ->

            }
            builder.setNegativeButton("Cancel") { _: DialogInterface, _ ->
                binding.carFuelTypeAddText.setText("")
            }
            builder.show()
        }


    }

    override fun onDestroyView() {
        //TODO change to better function or method
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
            showToastAddCar(getString(R.string.error_add_car_toast), Toast.LENGTH_LONG)
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
            showToastAddCar(getString(R.string.error_add_car_toast), Toast.LENGTH_SHORT)
            false
        }
    }

    private fun showToastAddCar(text: String, length: Int) {
        val toast = Toast.makeText(requireContext(), text, length)
        toast.show()
    }

    private fun EditText.transformIntoDatePicker(
        context: Context,
        format: String,
        maxDate: Date? = null
    ) {
        //val maxYearCar = addNewCarViewModel.getDistinctMaxYearCarByBrand(binding.carBrandAddText.text.toString())
        isFocusableInTouchMode = false
        isClickable = true
        isFocusable = false
        val myCalendar = Calendar.getInstance()
        val datePickerOnDataSetListener =
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, _ ->
                myCalendar.set(Calendar.YEAR, year)
                myCalendar.set(Calendar.MONTH, monthOfYear)
                val sdf = SimpleDateFormat(format, Locale.ITALY)
                setText(sdf.format(myCalendar.time))
            }

        setOnClickListener {
            DatePickerDialog(
                context, datePickerOnDataSetListener,
                myCalendar
                    .get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH),
            ).run {
                maxDate?.time?.also { datePicker.maxDate = it }
                show()
            }
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

    private fun createSingleChoiceAlertDialog(
        title: String,
        positiveButtonText: String,
        negativeButtonText: String,
        listItems: List<String>,
        bindingText: TextInputEditText,
        enableBinding: TextInputEditText?
    ) {

    }
}
