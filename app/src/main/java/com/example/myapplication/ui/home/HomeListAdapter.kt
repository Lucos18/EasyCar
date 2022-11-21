package com.example.myapplication.ui.home

import android.provider.Settings.Global.getString
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.databinding.CarItemCardBinding
import com.example.myapplication.model.Car
import java.text.NumberFormat
import java.util.*

class HomeListAdapter(
    private val clickListener: (Car) -> Unit
) : ListAdapter<Car, HomeListAdapter.HomeViewHolder>(DiffCallback) {

    class HomeViewHolder(
        private var binding: CarItemCardBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(car: Car) {
            val format: NumberFormat = NumberFormat.getCurrencyInstance(Locale.getDefault())
            val result: String = format.format(car.price)
            binding.car = car
            binding.carPrice.text = result
            binding.carPower.text = car.carPower.toString() + " kW"
            binding.carYearProduction.text = car.yearStartProduction.toString()
            binding.executePendingBindings()
        }

    }
    companion object DiffCallback: DiffUtil.ItemCallback<Car>() {
        override fun areItemsTheSame(oldItem: Car, newItem: Car): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Car, newItem: Car): Boolean {
            return oldItem == newItem
        }

    }
    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val car = getItem(position)
        holder.itemView.setOnClickListener{
            clickListener(car)
        }
        holder.bind(car)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return HomeViewHolder(
            CarItemCardBinding.inflate(layoutInflater, parent, false)
        )
    }
}