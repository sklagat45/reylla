package com.srklagat.reylla.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.srklagat.reylla.R
import com.srklagat.reylla.activities.serviceproviders.ui.details.ServiceDetailsActivity
import com.srklagat.reylla.model.TatColorService
import com.srklagat.reylla.utils.Constants
import com.srklagat.reylla.utils.GlideLoader
import kotlinx.android.synthetic.main.row_list_service_item.view.*

class TatColorServiceAdapter(
    private val context: Context,
    private var list: ArrayList<TatColorService>,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return TatColorViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.row_list_service_item,
                parent,
                false
            )
        )
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if (holder is TatColorViewHolder) {


            GlideLoader(context).loadServicePicture(
                model.mserviceImageURL,
                holder.itemView.iv_item_service_image
            )

            //Picasso.get().load("http://i.imgur.com/DvpvklR.png").into(holder.itemView.iv_item_service_image);
            holder.itemView.tv_item_service_name.text = model.styleName
            holder.itemView.tv_item_duration.text = model.styleDuration
            holder.itemView.tv_item_cost.text = model.styleCost

//            holder.itemView.iv_item_delete.setOnClickListener {
//
//                activity.deleteService(model.service_id)
//            }

            holder.itemView.setOnClickListener {
                // Launch Product details screen.
                val intent = Intent(context, ServiceDetailsActivity::class.java)
                intent.putExtra(Constants.EXTRA_SERVICE_ID, model.service_id)
                intent.putExtra(Constants.EXTRA_SERVICE_OWNER_ID, model.providerID)
                context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class TatColorViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        fun bind(serviceItem: TatColorService) {
            itemView.tv_item_service_name.text = serviceItem.styleName
            itemView.tv_item_duration.text = serviceItem.styleDuration
            itemView.tv_item_cost.text = serviceItem.styleCost

            Glide.with(context)
                .load(serviceItem.mserviceImageURL)
                .into(itemView.iv_item_service_image)
        }
    }
}