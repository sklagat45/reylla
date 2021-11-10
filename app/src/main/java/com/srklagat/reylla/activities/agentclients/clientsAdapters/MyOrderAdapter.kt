package com.srklagat.reylla.activities.agentclients.clientsAdapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.srklagat.reylla.R
import com.srklagat.reylla.activities.agentclients.clientActivities.CartListActivity
import com.srklagat.reylla.firebase.FirestoreClass
import com.srklagat.reylla.model.Order
import com.srklagat.reylla.utils.GlideLoader
import kotlinx.android.synthetic.main.row_cart_items.view.*

class MyOrderAdapter(
    private val context: Context,
    private var list: ArrayList<Order>,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return OrderViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.row_cart_items,
                parent,
                false
            )
        )
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if (holder is OrderViewHolder) {

            GlideLoader(context).loadServicePicture(
                model.image,
                holder.itemView.iv_item_service_image
            )

            //Picasso.get().load("http://i.imgur.com/DvpvklR.png").into(holder.itemView.iv_item_service_image);
            holder.itemView.tv_item_service_name.text = model.title
            holder.itemView.tv_item_duration.text = model.address
            holder.itemView.tv_item_cost.text = model.sub_total_amount


            holder.itemView.iv_item_delete_from_cart.setOnClickListener {

                FirestoreClass().removeItemFromCart(context, model.id)

                if (context is CartListActivity) {
                    context.showProgressDialog(context.resources.getString(R.string.please_wait))
                }

            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun addToCartSuccess() {

        Toast.makeText(context, " Item added to cart", Toast.LENGTH_LONG).show()

    }

    inner class OrderViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        fun bind(serviceItem: Order) {
            itemView.tv_item_service_name.text = serviceItem.title
            itemView.tv_item_duration.text = serviceItem.address
            itemView.tv_item_cost.text = serviceItem.sub_total_amount


            Glide.with(context)
                .load(serviceItem.image)
                .into(itemView.iv_item_service_image)
        }
    }
}