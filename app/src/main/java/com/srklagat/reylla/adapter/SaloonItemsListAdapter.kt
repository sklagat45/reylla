package com.srklagat.reylla.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.srklagat.reylla.R
import com.srklagat.reylla.model.Company
import com.srklagat.reylla.utils.GlideLoader
import kotlinx.android.synthetic.main.row_existing_saloon.view.*

class SaloonItemsListAdapter (
        private val context: Context,
        private var list: ArrayList<Company>
        ): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

                // A global variable for OnClickListener interface.
                private var onClickListener: OnClickListener? = null

                /**
                 * Inflates the item views which is designed in xml layout file
                 *
                 * create a new
                 * {@link ViewHolder} and initializes some private fields to be used by RecyclerView.
                 */
                override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
                        return SaloonViewHolder(
                                LayoutInflater.from(context).inflate(
                                        R.layout.row_existing_saloon,
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
                @SuppressLint("SetTextI18n")
                override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                        val model = list[position]

                        if (holder is SaloonViewHolder) {

                                GlideLoader(context).loadServicePicture(
                                        model.companyImage,
                                        holder.itemView.img_saloon_image
                                )
                                holder.itemView.tv_saloon_name.text = model.companyName
                                holder.itemView.tv_location.text = "$${model.companyAddress}"

                                holder.itemView.setOnClickListener {
                                        if (onClickListener != null) {
                                                onClickListener!!.onClick(position, model)
                                        }
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
                 * A function for OnClickListener where the Interface is the expected parameter and assigned to the global variable.
                 *
                 * @param onClickListener
                 */
                fun setOnClickListener(onClickListener: OnClickListener) {
                        this.onClickListener = onClickListener
                }

                /**
                 * An interface for onclick items.
                 */
                interface OnClickListener {

                        fun onClick(position: Int, company: Company)
                }

                /**
                 * A ViewHolder describes an item view and metadata about its place within the RecyclerView.
                 */
                class SaloonViewHolder(view: View) : RecyclerView.ViewHolder(view)


}