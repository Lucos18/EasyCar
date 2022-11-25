package com.example.myapplication.ui.favorites

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.databinding.CarItemCardBinding
import com.example.myapplication.model.Car
import com.example.myapplication.model.carPowerWithUnitString
import com.example.myapplication.model.formatPriceToCurrency
import com.example.myapplication.ui.home.HomeListAdapter
import com.example.myapplication.utils.showCustomSnackBar
import com.example.myapplication.utils.showCustomSnackBarWithUndo
import com.google.android.material.snackbar.Snackbar

class FavoritesListAdapter(
    private val clickListener: (Car) -> Unit,
    val context: Context
) : ListAdapter<Car, FavoritesListAdapter.FavoritesViewHolder>(DiffCallback) {

    class FavoritesViewHolder(
        private var binding: CarItemCardBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(car: Car) {
            binding.car = car
            binding.apply {
                carPrice.text = car.formatPriceToCurrency(car.price)
                carPower.text = car.carPowerWithUnitString(car.carPower)
                carYearProduction.text = car.yearStartProduction.toString()
                shareButtonCarItem.visibility = View.VISIBLE
                favoritesButtonImage.setImageResource(R.drawable.ic_baseline_star_24)
                favoritesButtonImage.setOnClickListener {
                    //TODO Remove Car from favorites database
                    showCustomSnackBarWithUndo(
                        carItemConstraintLayout,
                        itemView.context.getString(R.string.remove_success_favorites_car, car.brand),
                        Snackbar.LENGTH_LONG,
                        context.getString(
                            R.string.undo
                        )
                    )
                }
                shareButtonCarItem.setOnClickListener {

                }
            }

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

    override fun onBindViewHolder(holder: FavoritesViewHolder, position: Int) {
        val car = getItem(position)
        holder.itemView.setOnClickListener {
            clickListener(car)
        }
        if (car.favorite) holder.bind(car)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return FavoritesViewHolder(
            CarItemCardBinding.inflate(layoutInflater, parent, false)
        )
    }
}