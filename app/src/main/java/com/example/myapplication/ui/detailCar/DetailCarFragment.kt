package com.example.myapplication.ui.detailCar

import android.app.AlertDialog
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.myapplication.BaseApplication
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentDetailCarBinding
import com.example.myapplication.model.Car
import com.example.myapplication.model.carPowerWithUnitString
import com.example.myapplication.model.formatPriceToCurrency
import com.example.myapplication.model.fuelType
import com.example.myapplication.ui.transformIntoDatePicker
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.*

class DetailCarFragment : Fragment() {
    private val detailCarArgs: DetailCarFragmentArgs by navArgs()
    private lateinit var car: Car
    private val detailCarViewModel: DetailCarViewModel by viewModels {
        DetailViewModelFactory(
            (activity?.application as BaseApplication).database.CarDao()
        )
    }

    private var _binding: FragmentDetailCarBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailCarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewSwitcher()
        val navBar: BottomNavigationView =
            requireActivity().findViewById(R.id.nav_view)
        navBar.visibility = View.GONE
        val id = detailCarArgs.carId
        if (id > 0) {
            detailCarViewModel.getCarById(id).observe(this.viewLifecycleOwner) { carSelected ->
                car = carSelected
                bindCar(car)
            }
        }
        if (detailCarArgs.isUserCreated) {
            binding.deleteCarFab.visibility = View.VISIBLE
            binding.editCarFab.visibility = View.VISIBLE
            binding.deleteCarFab.setOnClickListener {
                val builder = AlertDialog.Builder(requireContext())
                //set title for alert dialog
                builder.setTitle("Delete Car")
                //set message for alert dialog
                builder.setMessage("Are you sure do you want to delete this car from database?")
                builder.setIcon(android.R.drawable.ic_dialog_alert)

                //performing positive action
                builder.setPositiveButton("Yes") { _, _ ->
                    detailCarViewModel.deleteCarById(detailCarArgs.carId)
                    val action = DetailCarFragmentDirections
                        .actionDetailCarFragmentToNavigationSell()
                    findNavController().navigate(action)
                }

                //performing negative action
                builder.setNegativeButton("No") { _, _ ->

                }
                // Create the AlertDialog
                val alertDialog: AlertDialog = builder.create()
                // Set other dialog properties
                alertDialog.setCancelable(false)
                alertDialog.show()
            }

        }
    }

    override fun onDestroyView() {
        val navBar: BottomNavigationView =
            requireActivity().findViewById(R.id.nav_view)
        navBar.visibility = View.VISIBLE
        super.onDestroyView()
        _binding = null
    }

    private fun bindCar(car: Car) {
        binding.apply {
            binding.carYearProductionEditText.transformIntoDatePicker(
                requireContext(),
                "yyyy",
                Date()
            )
            editCarFab.setOnClickListener {
                switchBetweenEditAndSave()
                setEditTextBinding()
                deleteCarFab.visibility = View.GONE
                editCarFab.visibility = View.GONE
                saveCarFab.visibility = View.VISIBLE
            }
            saveCarFab.setOnClickListener {
                if (checkInput()) {
                    switchBetweenEditAndSave()
                    deleteCarFab.visibility = View.VISIBLE
                    editCarFab.visibility = View.VISIBLE
                    saveCarFab.visibility = View.GONE
                    editCarFab.setImageResource(R.drawable.ic_baseline_edit_24)
                    detailCarViewModel.updateCar(
                        car,
                        power = carPowerEditText.text.toString(),
                        seats = carSeatsEditText.text.toString(),
                        fuelType = carFuelTypeEditText.text.toString(),
                        year = carYearProductionEditText.text.toString(),
                        price = carPriceEditText.text.toString(),
                    )
                } else {
                    showCustomSnackBar(
                        getString(R.string.error_validation_edit),
                        com.google.android.material.snackbar.Snackbar.LENGTH_LONG
                    )
                }
            }
            carBrandDetail.text = car.brand
            carModelDetail.text = car.model
            carPriceDetail.text = car.formatPriceToCurrency(car.price)
            carPowerDetail.text = car.carPowerWithUnitString(car.carPower)

            carFuelTypeDetail.text = getString(R.string.car_fuel_type_detail_string, car.fuelType)
            carSeatsTypeDetail.text =
                getString(R.string.car_seats_detail_string, car.seats.toString())
            carYearProductionDetail.text =
                getString(R.string.car_year_detail_string, car.yearStartProduction.toString())
            carNotesDetailLabel.text = getString(R.string.car_notes_detail_string_label)
            if (car.image != null) {
                binding.carImageDetail.setImageBitmap(
                    Bitmap.createScaledBitmap(
                        BitmapFactory.decodeByteArray(
                            car.image, 0, car.image.size
                        ), 600, 250, false
                    )
                )
            } else {
                binding.carImageDetail.setImageResource(R.drawable.ic_baseline_directions_car_24)
            }
        }
    }

    private fun setupViewSwitcher() {
        val inAnim = AnimationUtils.loadAnimation(requireContext(), android.R.anim.slide_in_left)
        binding.apply {
            yearProductionSwitcher.inAnimation = inAnim
            fuelTypeSwitcher.inAnimation = inAnim
            seatsSwitcher.inAnimation = inAnim
            powerSwitcher.inAnimation = inAnim
        }


        val out = AnimationUtils.loadAnimation(requireContext(), android.R.anim.slide_out_right)
        binding.apply {
            yearProductionSwitcher.outAnimation = out
            fuelTypeSwitcher.outAnimation = out
            seatsSwitcher.outAnimation = out
            powerSwitcher.outAnimation = out
        }

    }

    fun setEditTextBinding() {
        binding.apply {
            carYearProductionEditText.setText(car.yearStartProduction.toString())
            carPowerEditText.setText(car.carPower.toString())
            carSeatsEditText.setText(car.seats.toString())
            carFuelTypeEditText.setText(car.fuelType)
            carPriceEditText.setText(car.price.toString())
        }
    }

    fun switchBetweenEditAndSave() {
        binding.apply {
            powerSwitcher.showNext()
            seatsSwitcher.showNext()
            yearProductionSwitcher.showNext()
            fuelTypeSwitcher.showNext()
            priceSwitcher.showNext()
        }
    }

    fun showCustomSnackBar(text: String, length: Int) {
        val snackbar = com.google.android.material.snackbar.Snackbar
            .make(
                binding.coordinatorDetailCar,
                text,
                length
            )
        snackbar.show()
    }

    fun checkInput():Boolean{
        return detailCarViewModel.checkInputEditTextNewCar(
            power = binding.carPowerEditText.text.toString().toInt(),
            seats = binding.carSeatsEditText.text.toString().toInt(),
            fuelType = binding.carFuelTypeEditText.text.toString(),
            year = binding.carYearProductionEditText.text.toString().toInt(),
            price = binding.carPriceEditText.text.toString().toDouble()
        )
    }
}