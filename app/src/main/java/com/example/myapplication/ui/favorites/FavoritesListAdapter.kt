package com.example.myapplication.ui.favorites

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.databinding.CarItemCardBinding
import com.example.myapplication.model.Car
import com.example.myapplication.model.carMileageWithUnitString
import com.example.myapplication.model.carPowerWithUnitString
import com.example.myapplication.model.formatPriceToCurrency
import com.example.myapplication.utils.ShareDialog
import com.example.myapplication.utils.showCustomSnackBarWithUndo
import com.google.android.material.snackbar.Snackbar

class FavoritesListAdapter(
    private val clickListener: (Car) -> Unit,
    private val functionFavorites: (Car) -> Unit,
    private val undoRemovedFavorites: (Car) -> Unit,
) : ListAdapter<Car, FavoritesListAdapter.FavoritesViewHolder>(DiffCallback) {

    class FavoritesViewHolder(
        private var binding: CarItemCardBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(car: Car, functionFavorites: (Car) -> Unit, undoRemovedFavorites: (Car) -> Unit) {
            binding.car = car
            binding.apply {
                carPrice.text = car.formatPriceToCurrency(car.price)
                carPower.text = car.carPowerWithUnitString(car.carPower)
                carYearProduction.text = car.yearStartProduction.toString()
                carItemState.text = car.carMileageWithUnitString(car.mileage)
                shareButtonCarItem.visibility = View.VISIBLE
                favoritesButtonImage.setImageResource(R.drawable.ic_baseline_star_24)
                favoritesButtonImage.setOnClickListener {
                    functionFavorites(car)
                    showCustomSnackBarWithUndo(
                        carItemConstraintLayout,
                        itemView.context.getString(
                            R.string.remove_success_favorites_car,
                            car.brand
                        ),
                        Snackbar.LENGTH_LONG,
                        itemView.context.getString(
                            R.string.undo
                        ),
                        {undoRemovedFavorites(car)}
                    )
                }


                shareButtonCarItem.setOnClickListener {
                    ShareDialog(
                        itemView.context,
                        itemView.context.getString(R.string.check_auto_share_dialog),
                        "Check this new auto!\nBrand:${car.brand}\nModel:${car.model}\nPrice:${car.price}",
                        "Share With"
                    )
                }
            }

            if (car.image != null) {
                val bmp = BitmapFactory.decodeByteArray(car.image, 0, car.image.size)
                binding.carImage.setImageBitmap(
                    Bitmap.createScaledBitmap(
                        bmp,
                        1920,
                        1080,
                        false
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
        if (car.favorite) holder.bind(car, functionFavorites, undoRemovedFavorites)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavoritesViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return FavoritesViewHolder(
            CarItemCardBinding.inflate(layoutInflater, parent, false)
        )
    }
}