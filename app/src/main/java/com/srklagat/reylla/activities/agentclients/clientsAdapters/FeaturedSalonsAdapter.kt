package com.srklagat.reylla.activities.agentclients.clientsAdapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import butterknife.ButterKnife
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.srklagat.reylla.R
import com.srklagat.reylla.activities.agentclients.clientActivities.GalleryActivity
import com.srklagat.reylla.activities.agentclients.clientActivities.SelectServiceActivity
import com.srklagat.reylla.activities.agentclients.clientActivities.SelectedSalon
import com.srklagat.reylla.model.Company
import com.srklagat.reylla.utils.Constants
import com.srklagat.reylla.utils.Constants.EXTRA_SALON_ID
import com.srklagat.reylla.utils.GlideLoader
import kotlinx.android.synthetic.main.row_featured_saloons_items.view.*

class FeaturedSalonsAdapter(
    private val context: Context,
    private var list: ArrayList<Company>,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    private val currentUser = FirebaseAuth.getInstance().currentUser!!

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ClientViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.row_featured_saloons_items,
                parent,
                false
            )
        )
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if (holder is ClientViewHolder) {

            GlideLoader(context).loadServicePicture(
                model.companyImage,
                holder.itemView.iv_item_salon_image
            )

            val timeSelected: String = holder.itemView.tv_selected_time.text.toString()
            val userID: String = currentUser.uid

            holder.itemView.tv_item_salon_name.text = model.companyName
            holder.itemView.tv_address.text = model.companyAddress

            holder.itemView.tv_view_gallery.setOnClickListener {
                // Launch Product details screen.
                val intent = Intent(context, GalleryActivity::class.java)
                intent.putExtra("salonID", model.id);
                intent.putExtra(Constants.EXTRA_USER_ID, userID)
                intent.putExtra(Constants.EXTRA_SALON_NAME, model.companyName)
                Log.e("service_2","" +  model.id)
                context.startActivity(intent)
            }

            holder.itemView.tv_pick_time.setOnClickListener {
                // Launch Product details screen.
                val intent = Intent(context, SelectServiceActivity::class.java)
                intent.putExtra("salonID", model.id);
                intent.putExtra(Constants.EXTRA_USER_ID, userID)
                Log.e("service_2","" +  model.id)

                context.startActivity(intent)
            }
            holder.itemView.tv_book_us.setOnClickListener {
                // Launch Product details screen.
                val intent = Intent(context, SelectedSalon::class.java)
                intent.putExtra("salonID", model.id);
                intent.putExtra(Constants.EXTRA_TIME_PICKED_ID, timeSelected)
                intent.putExtra(Constants.EXTRA_SALON_ADDRESS, model.companyAddress)
                intent.putExtra(Constants.EXTRA_LONGITUDE, model.longitude)
                intent.putExtra(Constants.EXTRA_LATITUDE, model.latitude)
                Log.e("service_2","" +  model.id)

                context.startActivity(intent)
            }


        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ClientViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        fun bind(company: Company) {

            ButterKnife.bind(this, itemView);
            itemView.tv_item_salon_name.text = company.companyName
            itemView.tv_address.text = company.companyAddress

            Glide.with(context)
                .load(company.companyImage)
                .into(itemView.iv_item_salon_image)


        }
    }
}