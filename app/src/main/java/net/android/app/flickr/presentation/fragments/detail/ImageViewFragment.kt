package net.android.app.flickr.presentation.fragments.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import net.android.app.flickr.databinding.FragmentImageViewBinding

class ImageViewFragment : Fragment() {

    private var _binding: FragmentImageViewBinding? = null
    private val binding
        get() = checkNotNull(_binding)

    private val args: ImageViewFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentImageViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val imageUrl = args.imageUrl

        if ( imageUrl != "null" ) {
            Glide.with(binding.root).load(imageUrl).into(binding.imageContainer)
        } else {
            hideImageContainer()
        }

    }

    private fun hideImageContainer() {
        binding.imageContainer.visibility = View.GONE
        binding.tvImageNotFound.visibility =View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}