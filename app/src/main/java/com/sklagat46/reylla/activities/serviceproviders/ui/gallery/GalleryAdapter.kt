package com.sklagat46.reylla.activities.serviceproviders.ui.gallery

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sklagat46.reylla.R
import com.sklagat46.reylla.activities.serviceproviders.ui.details.ServiceDetailsActivity
import com.sklagat46.reylla.model.GalleryItem
import com.sklagat46.reylla.utils.Constants
import com.sklagat46.reylla.utils.GlideLoader
import kotlinx.android.synthetic.main.row_gallery_container.view.*

class GalleryAdapter(
    private val context: Context,
    private var list: ArrayList<GalleryItem>,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return GalleryViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.row_gallery_container,
                parent,
                false
            )
        )
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if (holder is GalleryViewHolder) {


            GlideLoader(context).loadServicePicture(
                model.imageUrl,
                holder.itemView.imagePost
            )


//            holder.itemView.iv_item_delete.setOnClickListener {
//
//                activity.deleteService(model.service_id)
//            }

            holder.itemView.setOnClickListener {
                // Launch Product details screen.
                val intent = Intent(context, ServiceDetailsActivity::class.java)
                intent.putExtra(Constants.EXTRA_SERVICE_ID, model.id)
                intent.putExtra(Constants.EXTRA_SERVICE_OWNER_ID, model.imageUrl)
                context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class GalleryViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        fun bind(mGalleryItem: GalleryItem) {

            Glide.with(context)
                .load(mGalleryItem.imageUrl)
                .into(itemView.imagePost)
        }
    }
}