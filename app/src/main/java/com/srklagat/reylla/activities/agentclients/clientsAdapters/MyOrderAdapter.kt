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
import com.srklagat.reylla.activities.agentclients.clientActivities.CartListActivity
import com.srklagat.reylla.activities.serviceproviders.ui.reminders.MyOrderDetails
import com.srklagat.reylla.firebase.FirestoreClass
import com.srklagat.reylla.model.Order
import com.srklagat.reylla.utils.Constants
import com.srklagat.reylla.utils.GlideLoader
import kotlinx.android.synthetic.main.row_cart_items.view.*

class MyOrderAdapter(
    private val context: Context,
    private var list: ArrayList<Order>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    /**
     * Inflates the item views which is designed in xml layout file
     *
     * create a new
     * {@link ViewHolder} and initializes some private fields to be used by RecyclerView.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.row_order_item_list,
                parent,
                false
            )
        )
    }

    /**
     * Binds each item in the ArrayList to a view
     *
     * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
     * an item.
     *
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if (holder is MyViewHolder) {

            GlideLoader(context).loadServicePicture(
                model.image,
                holder.itemView.iv_item_service_image
            )

            holder.itemView.tv_item_service_name.text = model.title
            holder.itemView.tv_item_cost.text = "$${model.total_amount}"

//            holder.itemView.ib_delete_product.visibility = View.GONE

            holder.itemView.setOnClickListener {
                val intent = Intent(context, MyOrderDetails::class.java)
                intent.putExtra(Constants.EXTRA_MY_ORDER_DETAILS, model)
                context.startActivity(intent)
            }
        }
    }

    /**
     * Gets the number of items in the list
     */
    override fun getItemCount(): Int {
        return list.size
    }

    /**
     * A ViewHolder describes an item view and metadata about its place within the RecyclerView.
     */
    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
}
// END