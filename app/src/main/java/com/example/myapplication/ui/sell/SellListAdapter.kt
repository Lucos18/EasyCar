package com.example.myapplication.ui.sell

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.CarItemCardBinding
import com.example.myapplication.model.Car
import com.example.myapplication.ui.home.HomeListAdapter

class SellListAdapter(
    private val clickListener: (Car) -> Unit
) : ListAdapter<Car, SellListAdapter.SellViewHolder>(DiffCallback) {

    class SellViewHolder(
        private var binding: CarItemCardBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(car: Car) {
            binding.car = car
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
    override fun onBindViewHolder(holder: SellViewHolder, position: Int) {
        val car = getItem(position)
        holder.itemView.setOnClickListener{
            clickListener(car)
        }
        holder.bind(car)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SellViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return SellViewHolder(
            CarItemCardBinding.inflate(layoutInflater, parent, false)
        )
    }
}