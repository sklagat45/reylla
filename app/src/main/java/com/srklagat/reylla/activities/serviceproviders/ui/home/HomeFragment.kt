package com.srklagat.reylla.activities.serviceproviders.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.srklagat.reylla.R
import com.srklagat.reylla.activities.serviceproviders.*
import com.srklagat.reylla.adapter.CategoriesAdapter
import com.srklagat.reylla.adapter.SaloonItemsListAdapter
import com.srklagat.reylla.firebase.FirestoreClass
import com.srklagat.reylla.listener.CustomItemClickListener
import com.srklagat.reylla.model.CategoriesViews
import com.srklagat.reylla.model.Company
import com.srklagat.reylla.utils.CustomGridLayoutManager
import com.srklagat.reylla.utils.ItemOffsetDecoration
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : Fragment() {

    private var categoriesAdapter: CategoriesAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar!!.hide()
        return inflater.inflate(R.layout.fragment_home, container, false)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as AppCompatActivity).supportActionBar!!.hide()

        setRvAdapter()
        setUpAdapter()
        getGetExistingSalonList()

    }

//    private fun setSaloonRVAdapter() {
//        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
//        rv_other_saloons.layoutManager = layoutManager
//
//    }

    private fun setRvAdapter() {
        val layoutManager = CustomGridLayoutManager(requireContext(), 3)
        layoutManager.setScrollEnabled(true)
        recycleViewCards.layoutManager = layoutManager
        val itemDecoration = ItemOffsetDecoration(requireContext(), R.dimen.item_offset)
        recycleViewCards.addItemDecoration(itemDecoration)
    }

    private fun getGetExistingSalonList() {
        // Show the progress dialog.
        Toast.makeText(
            activity,"please Wait",
            Toast.LENGTH_SHORT
        ).show()

        FirestoreClass().getSalonItemsList(this@HomeFragment)

    }
    private fun setUpAdapter() {
        categoriesAdapter = CategoriesAdapter(requireContext(), emptyList(),
            object : CustomItemClickListener {
                override fun onItemClick(view: View?, position: Int) {
                    val category = categoriesAdapter?.getItem(position) as? CategoriesViews
                    when (category?.categoryTitle) {
                        "Nails" -> {
                            val nailCareIntent =
                                Intent(requireContext(), NailCareActivity::class.java)
                            requireContext().startActivity(nailCareIntent)

                        }
                        "Hair Care" -> {
                            val hairCareIntent =
                                Intent(requireContext(), HairCareActivity::class.java)
                            requireContext().startActivity(hairCareIntent)

                        }
                        "Massage" -> {
                            val massageCareIntent =
                                Intent(requireContext(), MassageActivity::class.java)
                            requireContext().startActivity(massageCareIntent)

                        }
                        "Makeups" -> {
                            val makeupsCareIntent =
                                Intent(requireContext(), MakeUPActivity::class.java)
                            requireContext().startActivity(makeupsCareIntent)

                        }
                        "color and Tattoo" -> {
                            val tattooCareIntent =
                                Intent(requireContext(), TatooAndColorActivity::class.java)
                            requireContext().startActivity(tattooCareIntent)

                        }
                        "Bridal" -> {
                            val bridalCareIntent =
                                Intent(requireContext(), BridalActivity::class.java)
                            requireContext().startActivity(bridalCareIntent)

                        }
                    }


                }
            })

        recycleViewCards.adapter = categoriesAdapter

        categoriesAdapter?.setCategoryViews(setCategoriesList())
        categoriesAdapter?.notifyDataSetChanged()

    }

    private fun setCategoriesList(): List<CategoriesViews> {
        val arrayList = ArrayList<CategoriesViews>()
        arrayList.add(
            CategoriesViews(
                "Nails",
                R.drawable.nail_care,
                1
            )
        )

        arrayList.add(
            CategoriesViews(
                "Hair Care",
                R.drawable.haircare_1,
                2
            )
        )


        arrayList.add(
            CategoriesViews(
                "Massage",
                R.drawable.massage_img,
                3
            )
        )
        arrayList.add(
            CategoriesViews(
                "Makeups",
                R.drawable.makeup_img1,
                4
            )
        )

        arrayList.add(
            CategoriesViews(
                "color and Tattoo",
                R.drawable.angel_tattoo,
                5
            )
        )


        arrayList.add(
            CategoriesViews(
                "Bridal",
                R.drawable.bridal_img,
                6
            )
        )

        return arrayList
    }



    /**
     * A function to get the success result of the dashboard items from cloud firestore.
     *
     * @param salonItemsList
     */
    fun successSaloonItemsList(salonItemsList: ArrayList<Company>) {

        // Hide the progress dialog.

        if (salonItemsList.size > 0) {

            rv_other_saloons.visibility = View.VISIBLE
            tv_no_saloon_exist.visibility = View.GONE

            rv_other_saloons.layoutManager = GridLayoutManager(activity, 3,)
            rv_other_saloons.setHasFixedSize(true)

            val adapter = SaloonItemsListAdapter(requireActivity(), salonItemsList)
            rv_other_saloons.adapter = adapter

            adapter.setOnClickListener(object :
                SaloonItemsListAdapter.OnClickListener {
                override fun onClick(position: Int, company: Company) {

//                    val intent = Intent(context, DetailsActivity::class.java)
//                    intent.putExtra(Constants.EXTRA_PRODUCT_ID, company.id)
//                    intent.putExtra(Constants.EXTRA_PRODUCT_OWNER_ID, company.user_id)
//                    startActivity(intent)
                }
            })
            // END
        } else {
            rv_other_saloons.visibility = View.GONE
            tv_no_saloon_exist.visibility = View.VISIBLE
        }
    }


}