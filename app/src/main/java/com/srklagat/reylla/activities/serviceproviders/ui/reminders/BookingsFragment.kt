package com.srklagat.reylla.activities.serviceproviders.ui.reminders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.srklagat.reylla.R
import com.srklagat.reylla.activities.agentclients.clientsAdapters.MyOrderAdapter
import com.srklagat.reylla.firebase.FirestoreClass
import com.srklagat.reylla.model.Order
import kotlinx.android.synthetic.main.fragment_bookings.*

class BookingsFragment : Fragment() {

    private lateinit var bookingsViewModel: BookingsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bookingsViewModel =
            ViewModelProvider(this).get(BookingsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_bookings, container, false)


        getMyOrdersList()


        return root
    }
    override fun onResume() {
        super.onResume()

        getMyOrdersList()
    }

    /**
     * A function to get the list of my orders.
     */
    private fun getMyOrdersList() {
        // Show the progress dialog.
//        showProgressDialog(resources.getString(R.string.please_wait))

        FirestoreClass().getMyOrdersList(this@BookingsFragment)
    }

    /**
     * A function to get the success result of the my order list from cloud firestore.
     *
     * @param ordersList List of my orders.
     */
    fun populateBookingListInUI(ordersList: ArrayList<Order>) {

        // Hide the progress dialog.
//        hideProgressDialog()

        if (ordersList.size > 0) {

            rv_my_service_order_items.visibility = View.VISIBLE
            tv_no_orders_found.visibility = View.GONE

            rv_my_service_order_items.layoutManager = LinearLayoutManager(activity)
            rv_my_service_order_items.setHasFixedSize(true)

            val myOrdersAdapter = MyOrderAdapter(requireActivity(), ordersList)
            rv_my_service_order_items.adapter = myOrdersAdapter
        } else {
            rv_my_service_order_items.visibility = View.GONE
            tv_no_orders_found.visibility = View.VISIBLE
        }
    }

}