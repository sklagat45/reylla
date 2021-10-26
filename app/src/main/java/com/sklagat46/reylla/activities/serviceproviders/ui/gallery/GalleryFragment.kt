package com.sklagat46.reylla.activities.serviceproviders.ui.gallery

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.sklagat46.reylla.R
import com.sklagat46.reylla.firebase.FirestoreClass
import com.sklagat46.reylla.model.GalleryItem
import kotlinx.android.synthetic.main.fragment_gallery.*

class GalleryFragment : Fragment() {

    //    val GALLERY_REQUEST_CODE:
    private val currentUser = FirebaseAuth.getInstance().currentUser!!

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

        getGalleryImagesLists()
    }


    private fun getGalleryImagesLists() {
        // Show the progress dialog.
        Toast.makeText(
            activity, "please Wait",
            Toast.LENGTH_SHORT
        ).show()

        FirestoreClass().getGalleryImages(this@GalleryFragment)

    }

    fun successGalleryItemsList(GalleryItem: ArrayList<GalleryItem>) {
        //getting the storage reference


        // Hide the progress dialog.

        if (GalleryItem.size > 0) {

            gallery_recyclerView.visibility = View.VISIBLE
            tv_gallery_no_items.visibility = View.GONE

            gallery_recyclerView.layoutManager = GridLayoutManager(activity, 3)
            gallery_recyclerView.setHasFixedSize(true)

            val adapter = GalleryAdapter(requireActivity(), GalleryItem)
            gallery_recyclerView.adapter = adapter

        } else {
            gallery_recyclerView.visibility = View.GONE
            tv_gallery_no_items.visibility = View.VISIBLE
        }
    }


}