package com.srklagat.reylla.activities.agentclients.clientsAdapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.srklagat.reylla.R
import com.srklagat.reylla.activities.serviceproviders.ui.details.ServiceDetailsActivity
import com.srklagat.reylla.firebase.FirestoreClass
import com.srklagat.reylla.model.Cart
import com.srklagat.reylla.model.SalonService
import com.srklagat.reylla.utils.Constants
import com.srklagat.reylla.utils.GlideLoader
import kotlinx.android.synthetic.main.service_item.view.*

class ServicesAdapter (
    private val context: Context,
    private var list: ArrayList<SalonService>,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ServicesViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.service_item,
                parent,
                false
            )
        )
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if (holder is ServicesAdapter.ServicesViewHolder) {

            GlideLoader(context).loadServicePicture(
                model.mserviceImageURL,
                holder.itemView.iv_item_service_image
            )

            //Picasso.get().load("http://i.imgur.com/DvpvklR.png").into(holder.itemView.iv_item_service_image);
            holder.itemView.tv_item_service_name.text = model.styleName
            holder.itemView.tv_item_duration.text = model.styleDuration
            holder.itemView.tv_item_cost.text = model.styleCost


            holder.itemView.iv_item_add_to_cart.setOnClickListener {
                // Launch Product details screen.

//                fun addToCart() {

                    val addToCart = Cart(
                        FirestoreClass().getCurrentUserID(),
                        model.styleName,
                        model.styleDuration,
                        model.styleCost,
                        model.mserviceImageURL,
                        model.providerID
                    )

                    Toast.makeText(context, " please wait", Toast.LENGTH_LONG).show()

                    FirestoreClass().addCartItems(this@ServicesAdapter, addToCart)

                    holder.itemView.iv_item_add_to_cart.visibility = View.GONE



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

    fun addToCartSuccess() {

        Toast.makeText(context, " Item added to cart", Toast.LENGTH_LONG).show()

    }

    inner class ServicesViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        fun bind(serviceItem: SalonService) {
            itemView.tv_item_service_name.text = serviceItem.styleName
            itemView.tv_item_duration.text = serviceItem.styleDuration
            itemView.tv_item_cost.text = serviceItem.styleCost

            Glide.with(context)
                .load(serviceItem.mserviceImageURL)
                .into(itemView.iv_item_service_image)
        }
    }
}
