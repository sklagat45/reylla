package com.sklagat46.reylla.activities.serviceproviders.ui.gallery

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.sklagat46.reylla.R
import kotlinx.android.synthetic.main.fragment_gallery.*


class GalleryFragment : Fragment() {

//    val GALLERY_REQUEST_CODE:

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar!!.hide()
        return inflater.inflate(R.layout.fragment_gallery, container, false)


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        (activity as AppCompatActivity).supportActionBar!!.hide()

//        setupActionBar()

        // Click event for adding Image button.
        fab_add_images.setOnClickListener {
            val add_image =
                Intent(requireContext(), AddGalleryImagesActivity::class.java)
            requireContext().startActivity(add_image)
        }

    }


//    private fun setupActionBar() {
//        (activity as AppCompatActivity).setSupportActionBar(toolbar_gallery_fragment_activity)
//
//        val actionBar = (activity as AppCompatActivity).supportActionBar
//        if (actionBar != null) {
//            actionBar.setDisplayHomeAsUpEnabled(true)
//            actionBar.setHomeAsUpIndicator(R.drawable.back_btn)
//        }
//
//        toolbar_gallery_fragment_activity.setNavigationOnClickListener {
//            (activity as AppCompatActivity).onBackPressed()
//        }
//    }


}