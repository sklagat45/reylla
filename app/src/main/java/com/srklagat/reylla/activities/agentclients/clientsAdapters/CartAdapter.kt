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
import com.srklagat.reylla.model.Cart
import com.srklagat.reylla.utils.Constants
import com.srklagat.reylla.utils.GlideLoader
import kotlinx.android.synthetic.main.row_cart_items.view.*

class CartAdapter(
    private val context: Context,
    private var list: ArrayList<Cart>,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CartViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.row_cart_items,
                parent,
                false
            )
        )
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if (holder is CartAdapter.CartViewHolder) {

            GlideLoader(context).loadServicePicture(
                model.image,
                holder.itemView.iv_item_service_image
            )

            //Picasso.get().load("http://i.imgur.com/DvpvklR.png").into(holder.itemView.iv_item_service_image);
            holder.itemView.tv_item_service_name.text = model.title
            holder.itemView.tv_item_duration.text = model.duration
            holder.itemView.tv_item_cost.text = model.price


            holder.itemView.iv_item_delete_from_cart.setOnClickListener {

                val intent = Intent(context, ServiceDetailsActivity::class.java)
                intent.putExtra(Constants.EXTRA_SERVICE_ID, model.id)
                intent.putExtra(Constants.EXTRA_SERVICE_OWNER_ID, model.Service_owner_id)
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

    inner class CartViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        fun bind(serviceItem: Cart) {
            itemView.tv_item_service_name.text = serviceItem.title
            itemView.tv_item_duration.text = serviceItem.duration
            itemView.tv_item_cost.text = serviceItem.price


            Glide.with(context)
                .load(serviceItem.image)
                .into(itemView.iv_item_service_image)
        }
    }
}