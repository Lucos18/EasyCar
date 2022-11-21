package com.example.myapplication.ui.addCar

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.myapplication.BaseApplication
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentAddNewCarBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.properties.Delegates


class AddNewCarFragment : Fragment() {
    private var kw by Delegates.notNull<Int>()
    val addNewCarViewModel: AddNewCarViewModel by activityViewModels {
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
        val navBar: BottomNavigationView = requireActivity().findViewById(com.example.myapplication.R.id.nav_view)
        navBar.visibility = View.GONE
        binding.apply {

            //TODO change when the button will be invisible
            buttonAddNewCar.visibility = View.VISIBLE
            buttonAddNewCar.setOnClickListener{
                addNewCar()
            }
            binding.carYearAddText.transformIntoDatePicker(requireContext(), "yyyy", Date())
            binding.carPowerAddText.onFocusChangeListener = View.OnFocusChangeListener { _, b ->
                if (!b) {
                    try{
                        kw = binding.carPowerAddText.text.toString().toInt()
                        binding.carPowerAddText.setText(getString(R.string.car_power_convert, kw.toString(), addNewCarViewModel.convertKwToCv(kw).toString()))
                    }catch (e: Exception){

                    }
                }
            }

        }
    }

    override fun onDestroyView() {
        //TODO change to better function or method
        val navBar: BottomNavigationView = requireActivity().findViewById(com.example.myapplication.R.id.nav_view)
        navBar.visibility = View.VISIBLE
        super.onDestroyView()
        _binding = null
    }


    fun addNewCar(){
        if(isValidCar()){
            showToastAddCar("macchina valida",Toast.LENGTH_LONG)
            addNewCarViewModel.addCar(
                Brand = binding.carBrandAddText.text.toString(),
                YearStartProduction = binding.carYearAddText.text.toString().toInt(),
                YearEndProduction = null,
                Model = binding.carModelAddText.text.toString(),
                FuelType = binding.carFuelTypeAddText.text.toString(),
                Seats = binding.carSeatsAddText.text.toString().toInt(),
                CarPower = kw,
                Price = binding.carPriceAddText.text.toString().toDouble()
            )
        } else {
            showToastAddCar("macchina non valida",Toast.LENGTH_LONG)
        }
    }

    fun isValidCar():Boolean{
        return try {
            addNewCarViewModel.checkInputEditTextNewCar(
                binding.carBrandAddText.text.toString(),
                binding.carYearAddText.text.toString().toInt(),
                binding.carModelAddText.text.toString(),
                binding.carFuelTypeAddText.text.toString(),
                kw,
                binding.carSeatsAddText.text.toString().toInt(),
                binding.carPriceAddText.text.toString().toDouble()
            )
        } catch (e: Exception){
            showToastAddCar("Input inseriti non validi, controlla e riprova.", Toast.LENGTH_LONG)
            false
        }
    }

    private fun showToastAddCar(text:String, length: Int){
        val toast = Toast.makeText(requireContext(), text, length)
        toast.show()
    }
    fun EditText.transformIntoDatePicker(context: Context, format: String, maxDate: Date? = null) {
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
                context, datePickerOnDataSetListener, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH),
            ).run {
                maxDate?.time?.also { datePicker.maxDate = it }
                show()
            }
        }
    }
}
