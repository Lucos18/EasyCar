package com.example.myapplication.ui.home

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.databinding.CarItemCardBinding
import com.example.myapplication.model.*
import com.squareup.picasso.Picasso

class HomeListAdapter(
    private val clickListener: (Car) -> Unit
) : ListAdapter<Car, HomeListAdapter.HomeViewHolder>(DiffCallback) {

    class HomeViewHolder(
        private var binding: CarItemCardBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(car: Car) {
            binding.car = car
            binding.carPrice.text = car.formatPriceToCurrency(car.price)
            binding.carPower.text = car.carPowerWithUnitString(car.carPower)
            binding.carYearProduction.text = car.yearStartProduction.toString()
            if (car.image != null) {
                binding.carImage.setImageBitmap(
                    Bitmap.createScaledBitmap(
                        BitmapFactory.decodeByteArray(
                            car.image, 0, car.image.size
                        ), 100, 80, false
                    )
                )
            } else {
                binding.carImage.setImageResource(R.drawable.ic_baseline_directions_car_24)
            }
            binding.executePendingBindings()
        }

    }

    companion object DiffCallback : DiffUtil.ItemCallback<Car>() {
        override fun areItemsTheSame(oldItem: Car, newItem: Car): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Car, newItem: Car): Boolean {
            return oldItem == newItem
        }

    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val car = getItem(position)
        holder.itemView.setOnClickListener {
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