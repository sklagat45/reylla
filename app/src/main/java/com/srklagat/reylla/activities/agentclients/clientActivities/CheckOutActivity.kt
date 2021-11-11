package com.srklagat.reylla.activities.agentclients.clientActivities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.srklagat.reylla.R
import com.srklagat.reylla.activities.BaseActivity
import com.srklagat.reylla.activities.agentclients.MainActivity
import com.srklagat.reylla.activities.agentclients.clientsAdapters.CartAdapter
import com.srklagat.reylla.firebase.FirestoreClass
import com.srklagat.reylla.model.Cart
import com.srklagat.reylla.model.Order
import com.srklagat.reylla.utils.Util
import kotlinx.android.synthetic.main.activity_check_out.*
import kotlinx.android.synthetic.main.dialog_progress.*
import java.util.*

class CheckOutActivity : BaseActivity() {

    // A global variable for the cart list.
    private lateinit var mCartItemsList: ArrayList<Cart>

    private var cartAdapter: CartAdapter? = null

    // A global variable for the SubTotal Amount.
    private var mSubTotal: String = "0.0"
    private var mAddress: String = "0.0"
    private var mBookedDate: String = "0.0"
    private var mBookedTime: String = "0.0"


    // A global variable for the Total Amount.
    private var mTotalAmount: String = "0.0"

    // A global variable for Order details.
    private lateinit var mOrderDetails: Order
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_out)

        setupActionBar()

        getCartItemsList()

        btn_place_order.setOnClickListener {

            mCartItemsList

            placeAnOrder()
        }




        Util.setRecyclerView(this, rv_cart_list_items)


    }

    private fun getCartItemsList() {

        FirestoreClass().getCartList(this@CheckOutActivity)
    }


    private fun placeAnOrder() {
        // Show the progress dialog.
        showProgressDialog(resources.getString(R.string.please_wait))

        mBookedDate = checkout_date.text.toString()
        mBookedTime = checkout_time.text.toString()
        mAddress = checkout_place.text.toString()


        mOrderDetails = Order(
            FirestoreClass().getCurrentUserID(),
            mCartItemsList[0].Service_owner_id,
            mCartItemsList,
            mAddress,
            "My order ${System.currentTimeMillis()}",
            mCartItemsList[0].image,
            mSubTotal.toString(),
            "5%",
            mTotalAmount.toString(),
            mBookedDate,
            mBookedTime,
            System.currentTimeMillis()
        )
        FirestoreClass().placeOrder(this@CheckOutActivity, mOrderDetails)

        Toast.makeText(
            this@CheckOutActivity,
            "booking Successful",
            Toast.LENGTH_SHORT
        ).show()

        startActivity(Intent(this@CheckOutActivity, BookedOrderDetails::class.java))
        this.finish()
        hideProgressDialog()

        finish()
    }

    private fun setupActionBar() {
        setSupportActionBar(toolbar_checkout_activity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.back_btn)
        }

        toolbar_checkout_activity.setNavigationOnClickListener { onBackPressed() }
    }

    fun successCartItemsList(cartList: ArrayList<Cart>) {

//        mCartItemsList =cartList
        // Hide Progress dialog.
        if (cartList.isNotEmpty()) {
            mCartItemsList = cartList

            rv_cart_list_items.visibility = View.VISIBLE
            val adapterSalons =
                CartAdapter(this, cartList)
            rv_cart_list_items.adapter = adapterSalons

            var subTotal: Double = 0.0

            for (item in cartList) {

                val price = +item.price.toDouble()

                subTotal += price

            }

            tv_checkout_sub_total.text = "Ksh$subTotal"
            // Here we have kept Shipping Charge is fixed as $10 but in your case it may cary. Also, it depends on the location and total amount.
            tv_checkout_discount_charge.text = "5%"

            if (subTotal > 0) {
                tv_checkout_discount_charge.visibility = View.VISIBLE

                val total = subTotal * 0.95
                tv_checkout_total_amount.text = "Ksh $total"

                mSubTotal = "Ksh$subTotal"

                mTotalAmount = "Ksh $total"

            } else {
                ll_checkout_place_order.visibility = View.GONE
            }


        } else {

            rv_cart_list_items.visibility = View.GONE

        }

    }

    override fun finish() {

        Toast.makeText(
            this@CheckOutActivity,
            "booking Successful",
            Toast.LENGTH_SHORT
        ).show()
        super.finish()
    }
}