package com.srklagat.reylla.activities.agentclients.clientsAdapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.srklagat.reylla.R
import com.srklagat.reylla.model.GalleryItem
import com.srklagat.reylla.utils.GlideLoader
import kotlinx.android.synthetic.main.row_gallery_container.view.*

class ClientGalleryAdapter(
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