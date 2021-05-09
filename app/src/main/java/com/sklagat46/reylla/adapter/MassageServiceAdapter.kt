package com.sklagat46.reylla.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sklagat46.reylla.R
import com.sklagat46.reylla.activities.serviceproviders.MassageActivity
import com.sklagat46.reylla.activities.serviceproviders.ui.details.ServiceDetailsActivity
import com.sklagat46.reylla.model.MassageService
import com.sklagat46.reylla.utils.Constants
import com.sklagat46.reylla.utils.GlideLoader
import kotlinx.android.synthetic.main.row_list_service_item.view.*

class MassageServiceAdapter(
    private val context: Context,
    private var list: ArrayList<MassageService>,
    private val activity: MassageActivity
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MassageServiceAdapter.MassageViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.row_list_service_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if (holder is MassageServiceAdapter.MassageViewHolder) {

            GlideLoader(context).loadServicePicture(
                model.mServiceImageURL,
                holder.itemView.iv_item_service_image
            )

            holder.itemView.tv_item_service_name.text = model.styleName
            holder.itemView.tv_item_duration.text = model.styleDuration

            holder.itemView.tv_item_cost.text = "Ksh" + "$${model.styleCost}"

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

    class MassageViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    }

}