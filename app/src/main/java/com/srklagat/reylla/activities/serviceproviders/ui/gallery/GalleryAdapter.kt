package com.srklagat.reylla.activities.serviceproviders.ui.gallery

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Magnifier
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.srklagat.reylla.R
import com.srklagat.reylla.R2.layout.row_gallery_container
import com.srklagat.reylla.activities.serviceproviders.ui.details.ServiceDetailsActivity
import com.srklagat.reylla.model.GalleryItem
import com.srklagat.reylla.utils.Constants
import com.srklagat.reylla.utils.GlideLoader
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


    @RequiresApi(Build.VERSION_CODES.P)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]
        val magnifier = Magnifier(holder.itemView.imagePost
        )

        if (holder is GalleryViewHolder) {


            GlideLoader(context).loadServicePicture(
                model.imageUrl,
                holder.itemView.imagePost
            )


//            holder.itemView.iv_item_delete.setOnClickListener {
//
//                activity.deleteService(model.service_id)
//            }

//            holder.itemView.setOnClickListener {
//                // Launch Product details screen.
//
//                val intent = Intent(context, ServiceDetailsActivity::class.java)
//                intent.putExtra(Constants.EXTRA_SERVICE_ID, model.id)
//                intent.putExtra(Constants.EXTRA_SERVICE_OWNER_ID, model.imageUrl)
//                context.startActivity(intent)
//            }

            holder.itemView.setOnTouchListener { v, event ->
                when (event.actionMasked) {
                    MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                        val viewPosition = IntArray(2)
                        v.getLocationOnScreen(viewPosition)
                        magnifier.show(event.rawX - viewPosition[0], event.rawY - viewPosition[1])
                    }
                    MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                        magnifier.dismiss()
                    }
                }
                true
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