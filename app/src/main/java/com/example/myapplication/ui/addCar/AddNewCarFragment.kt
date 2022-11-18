package com.example.myapplication.ui.addCar

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myapplication.R

class AddNewCarFragment : Fragment() {

    companion object {
        fun newInstance() = AddNewCarFragment()
    }

    private lateinit var viewModel: AddNewCarViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_new_car, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AddNewCarViewModel::class.java)
        // TODO: Use the ViewModel
    }

}