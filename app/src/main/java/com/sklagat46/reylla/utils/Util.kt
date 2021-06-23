package com.sklagat46.reylla.utils

import android.content.Context
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

object Util {

    fun setRecyclerView(
        context: Context?,
        recyclerView: RecyclerView
    ) {
        val mLayoutManager =
            LinearLayoutManager(
                context,
                LinearLayoutManager.VERTICAL, false
            )
        recyclerView.layoutManager = mLayoutManager
        recyclerView.itemAnimator = DefaultItemAnimator()
        /* recyclerView.addItemDecoration(
             DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
         )*/
    }

}