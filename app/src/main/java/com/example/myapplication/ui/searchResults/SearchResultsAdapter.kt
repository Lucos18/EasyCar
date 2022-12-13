package com.example.myapplication.ui.searchResults

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.databinding.CarItemCardBinding
import com.example.myapplication.databinding.CarItemCardSearchResultBinding
import com.example.myapplication.model.*
import com.example.myapplication.ui.home.HomeListAdapter
import com.example.myapplication.utils.ShareDialog
import com.example.myapplication.utils.createBitmapFromCarImage
import com.example.myapplication.utils.setAndGetUriByBrandParsingListOfLogoAndImageView
import com.example.myapplication.workers.FORMAT_IMAGE_PNG
import com.example.myapplication.workers.IMAGE_NAME
import java.io.File

class SearchResultsAdapter(
    private val clickListener: (Car) -> Unit,
    private val functionFavorites: (Car) -> Unit,
    private val listLogo: LiveData<List<CarLogo>>,
) : ListAdapter<Car, SearchResultsAdapter.SearchResultsViewHolder>(DiffCallback) {

    class SearchResultsViewHolder(
        private var binding: CarItemCardSearchResultBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(car: Car, functionFavorites: (Car) -> Unit, listLogo: LiveData<List<CarLogo>>) {
            binding.car = car
            if (car.model.length >= 21){
                val modelReplaced = car.model.replaceRange(22 until car.model.length, "...")
                binding.carModel.text = modelReplaced
            } else binding.carModel.text = car.model

            binding.carPrice.text = car.formatPriceToCurrency(car.price)
            binding.carPower.text = car.carPowerWithUnitString(car.carPower)
            binding.carYearProduction.text = car.yearStartProduction.toString()
            binding.carItemState.text = car.carMileageWithUnitString(car.mileage)
            binding.favoritesButtonImage.setImageResource(getImageResource(car.favorite))
            binding.favoritesButtonImage.setOnClickListener {
                functionFavorites(car)
                binding.favoritesButtonImage.setImageResource(getImageResource(car.favorite))
            }
            binding.shareButtonCarItem.setOnClickListener {
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
            setAndGetUriByBrandParsingListOfLogoAndImageView(
                listLogo.value,
                car.brand,
                binding.carItemLogo
            )
            binding.executePendingBindings()
        }
        fun getImageResource(isFavorite: Boolean): Int{
            return if (isFavorite) R.drawable.ic_baseline_star_24
            else R.drawable.ic_baseline_star_border_24dp
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

    override fun onBindViewHolder(holder: SearchResultsViewHolder, position: Int) {
        val car = getItem(position)
        holder.itemView.setOnClickListener {
            clickListener(car)
        }
        holder.bind(car, functionFavorites, listLogo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultsViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return SearchResultsViewHolder(
            CarItemCardSearchResultBinding.inflate(layoutInflater, parent, false),
        )
    }
}
