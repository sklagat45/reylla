package com.sklagat46.reylla.activities.serviceproviders.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.sklagat46.reylla.R
import com.sklagat46.reylla.activities.serviceproviders.*
import com.sklagat46.reylla.adapter.CategoriesAdapter
import com.sklagat46.reylla.listener.CustomItemClickListener
import com.sklagat46.reylla.model.CategoriesViews
import com.sklagat46.reylla.utils.CustomGridLayoutManager
import com.sklagat46.reylla.utils.ItemOffsetDecoration
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

    }

    private fun setRvAdapter() {
        val layoutManager = CustomGridLayoutManager(requireContext(), 3)
        layoutManager.setScrollEnabled(true)
        recycleViewCards.layoutManager = layoutManager
        val itemDecoration = ItemOffsetDecoration(requireContext(), R.dimen.item_offset)
        recycleViewCards.addItemDecoration(itemDecoration)
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
                                Intent(requireContext(), NailCareActivity::class.java)
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

    override fun onStart() {
        super.onStart()
        (activity as AppCompatActivity).supportActionBar!!.hide()
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity).supportActionBar!!.show()
    }


    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar!!.hide()
    }

    override fun onPause() {
        super.onPause()
        (activity as AppCompatActivity).supportActionBar!!.hide()

    }


}