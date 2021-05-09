package com.sklagat46.reylla.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.sklagat46.reylla.R
import com.sklagat46.reylla.listener.CustomItemClickListener
import com.sklagat46.reylla.model.HairCareServiceModel
import kotlinx.android.synthetic.main.row_list_service_item.view.*


class HairCareServiceListAdapter(
    private val context: Context,
    options: FirestoreRecyclerOptions<HairCareServiceModel>,
    private val listener: CustomItemClickListener
) :
    FirestoreRecyclerAdapter<HairCareServiceModel, HairCareServiceListAdapter.HairCareHolder>(
        options
    ) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HairCareHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_list_service_item, parent, false)
        val mViewHolder = HairCareHolder(itemView)
        itemView.setOnClickListener { v -> listener.onItemClick(v, mViewHolder.position) }
        return mViewHolder
    }

    inner class HairCareHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        fun bind(serviceItem: HairCareServiceModel) {
            itemView.tv_item_service_name.text = serviceItem.styleName
            itemView.tv_item_duration.text = serviceItem.styleDuration
            itemView.tv_item_cost.text = serviceItem.styleCost

            Glide.with(context)
                .load(serviceItem.mserviceImageURL)
                .into(itemView.iv_item_service_image)
        }
    }


    override fun onBindViewHolder(
        holder: HairCareHolder,
        position: Int,
        model: HairCareServiceModel
    ) {
        holder.bind(model)
    }
}