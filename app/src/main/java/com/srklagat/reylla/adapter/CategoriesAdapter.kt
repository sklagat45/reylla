package com.srklagat.reylla.adapter

import android.content.Context
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.RecyclerView
import com.srklagat.reylla.R
import com.srklagat.reylla.listener.CustomItemClickListener
import com.srklagat.reylla.model.CategoriesViews
import kotlinx.android.synthetic.main.activity_intro.*
import kotlinx.android.synthetic.main.row_category_item.view.*


class CategoriesAdapter(
    private val context: Context,
    private val categoryList: List<CategoriesViews>,
    private val customItemClickListener: CustomItemClickListener
) : RecyclerView.Adapter<CategoriesAdapter.CategoryViewHolder>() {

    private var categoryViews: List<CategoriesViews> = categoryList

    fun setCategoryViews(categoryList: List<CategoriesViews>) {
        this.categoryViews = categoryList
    }

    fun getItem(location: Int): Any? = categoryViews.getOrNull(location)

    //will be called by the RecyclerView to obtain a ViewHolder object.
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(
            LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.row_category_item, viewGroup, false)
        )
    }

    //populate the view hierarchy within the ViewHolder object with the data to be displayed

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(categoryViews[position], customItemClickListener, position, context)
    }


    //reference the view elements in the row_category_item.xml file.
    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(
            category: CategoriesViews,
            listener: CustomItemClickListener,
            position: Int,
            context: Context
        ) {
            itemView.img_service_category_image.setImageResource(category.categoryImage)
            itemView.tv_service_category_name.text = category.categoryTitle
            itemView.setOnClickListener { view ->
                listener.onItemClick(view, position)

            }

//            itemView.setOnTouchListener(View.OnTouchListener { v, event -> // TODO Auto-generated method stub
//                val vb = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
//                vb.vibrate(100)
//                false
//            })

        }
    }


    //returns the number of items in the titles array:
    override fun getItemCount(): Int {
        return categoryViews.size
    }
}