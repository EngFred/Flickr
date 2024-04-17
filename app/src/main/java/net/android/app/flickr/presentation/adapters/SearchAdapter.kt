package net.android.app.flickr.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import net.android.app.flickr.databinding.FlickrImageItemBinding
import net.android.app.flickr.domain.modal.FlickrImage

class SearchAdapter : PagingDataAdapter<FlickrImage, SearchAdapter.ImageViewHolder>(diffCallback = differCallback) {

    private var imageClickListener: ImageClickListener? = null

    fun initImageClickListener( listener: ImageClickListener ) {
        imageClickListener = listener
    }

    inner class ImageViewHolder( private val binding: FlickrImageItemBinding ) : ViewHolder( binding.root ) {
        fun bind( image: FlickrImage ) {
            Glide.with(binding.root).load(image.smallSizeImageUrl).into(binding.root)
            binding.root.setOnClickListener {
                imageClickListener?.onImageClick( image.largerSizeImageUrl ?: image.mediumSizeImageUrl ?: image.smallSizeImageUrl ?: "null")
            }
        }
    }

    companion object {
        val differCallback = object  : DiffUtil.ItemCallback<FlickrImage>() {
            override fun areItemsTheSame(oldItem: FlickrImage, newItem: FlickrImage): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: FlickrImage, newItem: FlickrImage): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = FlickrImageItemBinding.inflate(inflater, parent, false)
        return ImageViewHolder(binding)
    }

    interface ImageClickListener {
        fun onImageClick( imageUrl: String )
    }

}