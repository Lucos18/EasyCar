package com.example.myapplication.ui.addCar

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.myapplication.BaseApplication
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentAddNewCarBinding
import com.example.myapplication.databinding.FragmentDetailCarBinding
import com.example.myapplication.databinding.FragmentSellBinding
import com.example.myapplication.ui.detailCar.DetailCarViewModel
import com.example.myapplication.ui.detailCar.DetailViewModelFactory

class AddNewCarFragment : Fragment() {

    val addNewCarViewModel: AddNewCarViewModel by activityViewModels {
        DetailViewModelFactory(
            (activity?.application as BaseApplication).database.CarDao()
        )
    }

    private var _binding: FragmentAddNewCarBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_new_car, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.apply {
            //TODO change when the button will be invisible
            buttonAddNewCar.visibility = View.VISIBLE
            buttonAddNewCar.setOnClickListener{
                addNewCar()
            }
        }
    }
    fun addNewCar(){
        if(isValidCar()){

        }
    }
    fun isValidCar(){
        addNewCarViewModel.checkInputEditTextNewCar(
            binding.carBrandAdd.text
        )
    }

}