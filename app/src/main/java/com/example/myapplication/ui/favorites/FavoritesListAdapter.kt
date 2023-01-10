package com.example.myapplication.ui.favorites

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import co.ankurg.expressview.ExpressView
import co.ankurg.expressview.OnCheckListener
import com.example.myapplication.R
import com.example.myapplication.databinding.CarItemCardBinding
import com.example.myapplication.model.*
import com.example.myapplication.utils.ShareDialog
import com.example.myapplication.utils.createBitmapFromCarImage
import com.example.myapplication.utils.setAndGetUriByBrandParsingListOfLogoAndImageView
import com.example.myapplication.utils.showCustomSnackBarWithUndo
import com.example.myapplication.workers.CarReminderWorker
import com.example.myapplication.workers.FORMAT_IMAGE_PNG
import com.example.myapplication.workers.IMAGE_NAME
import com.google.android.material.snackbar.Snackbar
import java.io.File

class FavoritesListAdapter(
    private val clickListener: (Car) -> Unit,
    private val functionFavorites: (Car) -> Unit,
    private val undoRemovedFavorites: (Car) -> Unit,
    private val listLogo: LiveData<List<CarLogo>>,
) : ListAdapter<Car, FavoritesListAdapter.FavoritesViewHolder>(DiffCallback) {

    class FavoritesViewHolder(
        private var binding: CarItemCardBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            car: Car,
            functionFavorites: (Car) -> Unit,
            undoRemovedFavorites: (Car) -> Unit,
            listLogo: LiveData<List<CarLogo>>
        ) {
            binding.car = car
            if (car.model.length >= 17){
                val modelReplaced = car.model.replaceRange(18 until car.model.length, "...")
                binding.carModel.text = modelReplaced
            } else binding.carModel.text = car.model
            binding.apply {
                carPrice.text = car.formatPriceToCurrency(car.price)
                carPower.text = car.carPowerWithUnitString(car.carPower)
                carYearProduction.text = car.yearStartProduction.toString()
                carItemState.text = car.carMileageWithUnitString(car.mileage)
                shareButtonCarItem.visibility = View.VISIBLE
                binding.favoritesButton?.isChecked = car.favorite
                binding.favoritesButton?.animationStartDelay = 0L
                binding.favoritesButton?.burstAnimationDuration = 300L
                binding.favoritesButton?.setOnCheckListener(object : OnCheckListener {
                    override fun onChecked(view: ExpressView?) {
                        //Nothing to do
                    }
                    override fun onUnChecked(view: ExpressView?) {
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
                })
                shareButtonCarItem.setOnClickListener {
                    var file: File? = null
                    if (car.image != null)
                    {
                        file = File(createBitmapFromCarImage(car.image,car.id), "$IMAGE_NAME${car.id}$FORMAT_IMAGE_PNG")
                    }
                    ShareDialog(
                        itemView.context,
                        itemView.context.getString(R.string.check_auto_share_dialog),
                        "Check this new auto!\nBrand:${car.brand}\nModel:${car.model}\nPrice:${car.price}",
                        "Share With",
                        file
                    )
                }
                setAndGetUriByBrandParsingListOfLogoAndImageView(
                    listLogo.value,
                    car.brand,
                    binding.carItemLogo
                )
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

    override fun onBindViewHolder(holder: FavoritesListAdapter.FavoritesViewHolder, position: Int) {
        val car = getItem(position)
        holder.itemView.setOnClickListener {
            clickListener(car)
        }
        if (car.favorite) holder.bind(car, functionFavorites, undoRemovedFavorites, listLogo)
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