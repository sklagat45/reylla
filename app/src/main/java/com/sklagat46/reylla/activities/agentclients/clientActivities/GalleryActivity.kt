package com.sklagat46.reylla.activities.agentclients.clientActivities

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.sklagat46.reylla.R
import com.sklagat46.reylla.activities.BaseActivity
import com.sklagat46.reylla.activities.agentclients.clientsAdapters.ClientGalleryAdapter
import com.sklagat46.reylla.firebase.FirestoreClass
import com.sklagat46.reylla.model.GalleryItem
import com.sklagat46.reylla.utils.Util
import kotlinx.android.synthetic.main.activity_gallery.*

class GalleryActivity : BaseActivity() {

    val mFireStore = FirebaseFirestore.getInstance()
    private val currentUser = FirebaseAuth.getInstance().currentUser!!
//    val bundle: Bundle? = intent.extras
//    val provider_id: String? = bundle?.getString()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)

        getSalonsGalleryImagesLists()
        Util.setRecyclerView(this, rv_gallery)

    }

    private fun getSalonsGalleryImagesLists() {
        // Show the progress dialog.
        Toast.makeText(
            this, "please Wait",
            Toast.LENGTH_SHORT
        ).show()

        FirestoreClass().getSalonsGalleryImages(this@GalleryActivity)

    }

    fun successGalleryItemsList(GalleryItem: ArrayList<GalleryItem>) {
        //getting the storage reference


        // Hide the progress dialog.

        if (GalleryItem.size > 0) {

            rv_gallery.visibility = View.VISIBLE
            tv_gallery_no_items.visibility = View.GONE

            rv_gallery.layoutManager = GridLayoutManager(this, 3)
            rv_gallery.setHasFixedSize(true)

            val adapter = ClientGalleryAdapter(this@GalleryActivity, GalleryItem)
            rv_gallery.adapter = adapter

        } else {
            rv_gallery.visibility = View.GONE
            tv_gallery_no_items.visibility = View.VISIBLE
        }
    }

}