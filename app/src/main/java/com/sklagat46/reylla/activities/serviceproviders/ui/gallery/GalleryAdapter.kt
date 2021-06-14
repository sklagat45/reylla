package com.sklagat46.reylla.activities.serviceproviders.ui.gallery

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sklagat46.reylla.R
import com.sklagat46.reylla.activities.serviceproviders.ui.details.ServiceDetailsActivity
import com.sklagat46.reylla.model.GalleryItem
import com.sklagat46.reylla.utils.Constants
import com.sklagat46.reylla.utils.GlideLoader
import kotlinx.android.synthetic.main.row_gallery_container.view.*
import java.io.FileWriter

class GalleryAdapter(
    private val context: Context,
    private var list: List<GalleryItem>
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            RecyclerView.ViewHolder {
        return GalleryViewHolder(
            LayoutInflater.from(context).inflate(R.layout.row_gallery_container, parent, false)
        )
    }


    class GalleryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
//        val iv_gallery = view.findViewById(R.id.iv_gallery_items)

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = list[position]
        if (holder is GalleryViewHolder) {

            GlideLoader(context).loadServicePicture(
                item.imageUrl,
                holder.itemView.imagePost
            )
            holder.itemView.setOnClickListener {
                // Launch Product details screen.
                val intent = Intent(context, ServiceDetailsActivity::class.java)
                intent.putExtra(Constants.EXTRA_SERVICE_ID, item.id)
                intent.putExtra(Constants.EXTRA_SERVICE_OWNER_ID, item.userId)
                context.startActivity(intent)
            }
        }
    }

    // Function to Write to a File
    fun writeToFile(FirebaseImageName: String) {
        try {
            var writeData = FileWriter("doneGalleryImages.txt", true)
            writeData.write(FirebaseImageName + "\n")
            writeData.close()
        } catch (ex: Exception) {
            print("Could not save the image name.")
        }
    }


    override fun getItemCount(): Int {
        return list.size
    }
}