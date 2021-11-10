package com.srklagat.reylla.activities.agentclients.clientActivities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.srklagat.reylla.R
import com.srklagat.reylla.activities.BaseActivity
import com.srklagat.reylla.activities.agentclients.clientsAdapters.CartAdapter
import com.srklagat.reylla.firebase.FirestoreClass
import com.srklagat.reylla.model.Cart
import com.srklagat.reylla.model.SalonService
import com.srklagat.reylla.utils.Util
import kotlinx.android.synthetic.main.activity_cart_list.*

class CartListActivity : BaseActivity() {

    // A global variable for the product list.

    private var cartAdapter: CartAdapter? = null


    // A global variable for the cart list items.
    private lateinit var mCartListItems: ArrayList<Cart>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart_list)

        setupActionBar()

        getCartItemsList()

//        Log.e("service_5","" + salonID)

        btn_checkout.setOnClickListener {
            val intent = Intent(this@CartListActivity, CheckOutActivity::class.java)

            startActivity(intent)
        }

        Util.setRecyclerView(this, rv_cart_items_list)
        // Click event for add hair service button.


    }

    override fun onResume() {
        super.onResume()

        getCartItemsList()
    }



    private fun setupActionBar() {
        setSupportActionBar(toolbar_cart_list_activity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.back_btn)
        }

        toolbar_cart_list_activity.setNavigationOnClickListener { onBackPressed() }
    }



    fun successProductsListFromFireStore(productsList: ArrayList<SalonService>) {

    }

    private fun getCartItemsList() {
        FirestoreClass().getCartList(this@CartListActivity)
    }

    /**
     * A function to notify the user about the item removed from the cart list.
     */
    fun itemRemovedSuccess() {

        hideProgressDialog()

        Toast.makeText(
            this@CartListActivity,
            resources.getString(R.string.msg_item_removed_successfully),
            Toast.LENGTH_SHORT
        ).show()

        getCartItemsList()
    }

    fun successCartItemsList(cartList: ArrayList<Cart>) {

        // Hide Progress dialog.
        if (cartList.isNotEmpty()) {
            rv_cart_items_list.visibility = View.VISIBLE
            tv_no_cart_item_found.visibility = View.GONE
            val adapterSalons =
                CartAdapter(this, cartList)
            rv_cart_items_list.adapter = adapterSalons

            var subTotal: Double = 0.0

            for (item in cartList) {

                     val price = +item.price.toDouble()

                    subTotal += price

            }

            tv_sub_total.text = "Ksh$subTotal"
            // Here we have kept Shipping Charge is fixed as $10 but in your case it may cary. Also, it depends on the location and total amount.
            tv_tax_charge.text = "5%"

            if (subTotal > 0) {
                ll_checkout.visibility = View.VISIBLE

                val total = subTotal * 0.95
                tv_total_amount.text = "Ksh $total"
            } else {
                ll_checkout.visibility = View.GONE
            }

        } else {
            rv_cart_items_list.visibility = View.GONE
            tv_no_cart_item_found.visibility = View.VISIBLE
        }

    }
}